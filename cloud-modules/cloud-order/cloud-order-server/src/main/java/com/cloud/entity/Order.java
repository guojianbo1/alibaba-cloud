package com.cloud.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ry_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单所属用户id
     */
    private String userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    public static final String ORDER_ID = "order_id";

    public static final String ORDER_AMOUNT = "order_amount";

    public static final String USER_ID = "user_id";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

}
