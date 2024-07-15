package com.lvchenglong.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.entity.Goods;
import com.lvchenglong.mapper.GoodsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class GoodsService {
    @Resource
    private GoodsMapper goodsMapper;


    public void add(Goods goods) {
        goods.setDate(DateUtil.today());
        goodsMapper.insert(goods);
    }


    public void delete(Integer id) {
        goodsMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.delete(id);
        }
    }

    public void updateById(Goods goods) {
        goodsMapper.updateById(goods);
    }

    public Goods selectById(Integer id) {
        Goods goods = goodsMapper.selectById(id);
        return goods;
    }

    public List<Goods> selectAll(Goods goods) {
        List<Goods> list = goodsMapper.selectAll(goods);
        return list;
    }


    public PageInfo<Goods> selectPage(Goods goods, Integer pageNum, Integer pageSize) throws ParseException {
        PageHelper.startPage(pageNum,pageSize);
        List<Goods> list = goodsMapper.selectAll(goods);
        extracted(list);
        return PageInfo.of(list);
    }


    public List<Goods> selectFlash(Goods goods) throws ParseException {
        List<Goods> list = goodsMapper.selectAll(goods);
        extracted(list);
        return list.size() > 2 ? list.subList(0,2) : list;
    }

    private static void extracted(List<Goods> list) throws ParseException {
        for (Goods dbGoods : list) {
            // 计算出该秒杀商品剩余的时间
            if (dbGoods.getHasFlash() && ObjectUtil.isNotEmpty(dbGoods.getFlashTime())) {
                long now = System.currentTimeMillis();
                Date end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbGoods.getFlashTime());
                long gap = (end.getTime() - now) / 1000;
                dbGoods.setMaxTime(gap);
            }
        }
    }
}
