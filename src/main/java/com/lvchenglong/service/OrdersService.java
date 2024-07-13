package com.lvchenglong.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.common.Constant;
import com.lvchenglong.common.config.AliPayConfig;
import com.lvchenglong.common.enums.LogsModuleEnum;
import com.lvchenglong.common.enums.OrderStatusEnum;
import com.lvchenglong.common.enums.OrderTypeEnum;
import com.lvchenglong.common.system.AsyncTaskFactory;
import com.lvchenglong.entity.Goods;
import com.lvchenglong.entity.Orders;
import com.lvchenglong.entity.User;
import com.lvchenglong.exception.CustomException;
import com.lvchenglong.mapper.OrdersMapper;
import com.lvchenglong.utils.SaUtils;
import com.revinate.guava.util.concurrent.RateLimiter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class OrdersService implements InitializingBean {
    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private GoodsService goodsService;

    @Resource
    AliPayConfig aliPayConfig;

    private static final ConcurrentHashMap<Integer, Object> ordersMap = new ConcurrentHashMap<>();

    private RateLimiter limiter;

    /**
     * 下单（针对团购、普通下单）
     * 不处理秒杀是因为秒杀有单独的数量，需要单独校验，需要做单独的高并发处理
     * @param orders
     */
    @Transactional  // 同时更新商品和订单等多个数据表
    public Orders add(Orders orders) {
        // 获取当前登录用户
        User loginUser = SaUtils.getLoginUser();
        /**
         * 订单锁
         * 采取分段锁，锁是业务编号   比如用户ID  比如订单编号
         */
        Object lock = ordersMap.computeIfAbsent(loginUser.getId(), k -> new Object());
        // 加锁，保证一个人不能并发下单，同一时间只能下单一次
        synchronized (lock){
            Integer goodsId = orders.getGoodsId();
            Goods goods = goodsService.selectById(goodsId);
            if(ObjectUtil.isNull(goods)){
                throw new CustomException("未找到商品");
            }
            int store = goods.getStore() - orders.getNum();
            if(store < 0){
                throw new CustomException("商品库存不足");
            }
            // 团购订单  查询拼团信息
            Integer groupOrderId = orders.getGroupOrderId();  // 待拼团的订单ID
            Orders groupOrder = null;
            if(ObjectUtil.isNotNull(groupOrderId)){
                groupOrder = this.selectById(groupOrderId);
                if(ObjectUtil.isNull(groupOrder) || !OrderStatusEnum.IN_GROUP.name().equals(groupOrder.getStatus())){
                    throw new CustomException("拼团已失效");
                }
                if(!orders.getGoodsId().equals(groupOrder.getGoodsId())){
                    throw new CustomException("拼团信息错误");
                }
            }
            goods.setStore(store);
            goodsService.updateById(goods);  // 更新商品库存

            orders.setGoodsName(goods.getName());
            orders.setGoodsImg(goods.getImg());
            if(OrderTypeEnum.COMMON.name().equals(orders.getType())){
                // 普通订单
                orders.setGoodsPrice(goods.getOriginPrice());
                orders.setTotal(goods.getOriginPrice().multiply(BigDecimal.valueOf(orders.getNum())));
            }else if (OrderTypeEnum.GROUP.name().equals(orders.getType())){
                // 团购订单
                orders.setGoodsPrice(goods.getGroupPrice());
                orders.setTotal(goods.getGroupPrice().multiply(BigDecimal.valueOf(orders.getNum())));
            }
            // 设置订单状态为待支付
            orders.setStatus(OrderStatusEnum.NOT_PAY.value);
            // 订单的唯一标识
            String orderNo = IdUtil.getSnowflakeNextIdStr();
            orders.setOrderNo(orderNo);
            orders.setCreateTime(DateUtil.now());
            orders.setUserId(loginUser.getId());
            // 将订单数据插入数据库
            ordersMapper.insert(orders);

            // 把当前的订单ID作为另一个拼团订单的`group_order_id`
            Integer orderId = orders.getId();
            if(ObjectUtil.isNotNull(orderId)){
                if (groupOrder != null) {
                    groupOrder.setGroupOrderId(orderId);
                }
                this.updateById(groupOrder);  // 更新另一个拼团订单的信息
            }
        }
        // 记录日志
        AsyncTaskFactory.recordLog(LogsModuleEnum.ORDER.value, "创建订单成功，订单编号：【" +orders.getOrderNo() + "】", loginUser.getId());

        // 解锁（释放）
        ordersMap.remove(loginUser.getId());

        return orders;
    }

    public Orders addFlashOrder(Orders orders) {
        // 设置堵塞，限制秒杀请求
        limiter.acquire();
        // 获取当前登录用户
        User loginUser = SaUtils.getLoginUser();
        /**
         * 订单锁
         * 采取分段锁，锁是业务编号   比如用户ID  比如订单编号
         */
        Object lock = ordersMap.computeIfAbsent(loginUser.getId(), k -> new Object());
        // 加锁，保证一个人不能并发下单，同一时间只能下单一次
        synchronized (lock){
            Integer goodsId = orders.getGoodsId();
            Goods goods = goodsService.selectById(goodsId);
            if(ObjectUtil.isNull(goods)){
                throw new CustomException("未找到商品");
            }
            // 秒杀额度校验
            int flashStore = goods.getFlashNum() - orders.getNum();
            if(flashStore < 0){
                throw new CustomException("秒杀商品已抢完");
            }
            // 更新秒杀额度
            goods.setFlashNum(flashStore);
            int store = goods.getStore() - orders.getNum();
            if(store < 0){
                throw new CustomException("商品库存不足");
            }

            goods.setStore(store);
            goodsService.updateById(goods);  // 更新商品库存

            orders.setGoodsName(goods.getName());
            orders.setGoodsImg(goods.getImg());
            // 秒杀订单
            orders.setGoodsPrice(goods.getFlashPrice());
            orders.setTotal(goods.getFlashPrice().multiply(BigDecimal.valueOf(orders.getNum())));
            // 设置订单状态为待支付
            orders.setStatus(OrderStatusEnum.NOT_PAY.value);
            // 订单的唯一标识
            String orderNo = IdUtil.getSnowflakeNextIdStr();
            orders.setOrderNo(orderNo);
            orders.setCreateTime(DateUtil.now());
            orders.setUserId(loginUser.getId());
            // 将订单数据插入数据库
            ordersMapper.insert(orders);
        }
        // 记录日志
        AsyncTaskFactory.recordLog(LogsModuleEnum.ORDER.value, "创建订单成功，订单编号：【" +orders.getOrderNo() + "】", loginUser.getId());
        // 解锁（释放）
        ordersMap.remove(loginUser.getId());

        return orders;
    }

    public void delete(Integer id) {
        ordersMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.delete(id);
        }
    }

    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    public Orders selectById(Integer id) {
        Orders orders = ordersMapper.selectById(id);
        return orders;
    }

    public List<Orders> selectAll(Orders orders) {
        List<Orders> list = ordersMapper.selectAll(orders);
        return list;
    }


    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        limiter =RateLimiter.create(Constant.FLASH_LIMIT_NUM);
    }

    public Orders selectByOrderNo(String orderNo) {
        return ordersMapper.selectByOrderNo(orderNo);
    }

    public List<Orders> selectByStatus(String status) {
        return ordersMapper.selectByStatus(status);
    }

    /**
     * 取消团购
     * 发起退款
     * 更改状态为取消
     */
    @Transactional
    public void closeGroupOrder(Orders orders) {
        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(Constant.ALIPAY_GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), Constant.ALIPAY_FORMAT, Constant.ALIPAY_CHARSET, aliPayConfig.getAlipayPublicKey(), Constant.ALIPAY_SIGN_TYPE);

        // 2. 创建 Request并设置Request参数
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", orders.getOrderNo());  // 我们自己生成的订单编号（不能重复）
        bizContent.set("refund_amount", orders.getTotal()); // 订单的总金额
        bizContent.set("trade_no", orders.getPayNo()); // 支付宝支付订单号
        bizContent.set("out_request_no", IdUtil.fastSimpleUUID());   // 随机数
        request.setBizContent(bizContent.toString());
        try {
            // 调用退款接口
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("订单号【{}】退款成功", orders.getOrderNo());
                // 取消订单
                this.cancel(orders);
            }else {
                log.info("订单号【{}】退款失败", orders.getOrderNo());
            }
        } catch (AlipayApiException e) {
            log.error("退款失败", e);
        }
    }

    /**
     * 取消订单
     * 释放库存
     */
    @Transactional  // 涉及到两个表的操作，保证同一事务中同时成功或者失败
    public void cancel(Orders orders) {
        // 更改待支付为取消
        orders.setStatus(OrderStatusEnum.CANCEL.name());
        Integer goodsId = orders.getGoodsId();
        Goods goods = goodsService.selectById(goodsId);
        if(ObjectUtil.isNotNull(goods)){
            goods.setStore(goods.getStore() + orders.getNum());
            // 更新商品库存
            goodsService.updateById(goods);
        }
        // 更新订单状态
        this.updateById(orders);
    }
}
