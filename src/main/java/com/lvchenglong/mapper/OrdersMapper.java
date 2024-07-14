package com.lvchenglong.mapper;

import com.lvchenglong.entity.Orders;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrdersMapper {
    void insert(Orders orders);

    void deleteById(Integer id);

    void updateById(Orders orders);

    List<Orders> selectAll(Orders orders);

    Orders selectById(Integer id);

    @Select("select * from orders where order_no = #{orderNo}")
    Orders selectByOrderNo(String orderNo);

    @Select("select * from orders where status = #{status}")
    List<Orders> selectByStatus(String status);

    List<Orders> selectAllGroup(Orders orders);
}
