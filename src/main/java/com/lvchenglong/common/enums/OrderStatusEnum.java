package com.lvchenglong.common.enums;

public enum OrderStatusEnum {
    CANCEL("已取消"),
    NOT_PAY("待支付"),
    IN_GROUP("拼团中"),
    NOT_SEND("待发货"),
    NOT_ACCEPT("待收货"),
    DONE("已完成"),
    REFUND_DONE("已退款"),
    COMMENT_DONE("已评价");

    public String value;

    OrderStatusEnum(String value) {
        this.value = value;
    }
}
