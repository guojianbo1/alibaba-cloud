package com.cloud.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 余额变更流水表
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ry_user_balance_flow")
public class UserBalanceFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 类型 1.扣减，2.增加
     */
    private Integer type;

    /**
     * 状态 1.准备，2.完成，3.取消
     */
    private Integer status;

    /**
     * 变更前金额
     */
    private BigDecimal preBalance;

    /**
     * 变更后金额
     */
    private BigDecimal afterBalance;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    public static final String ID = "id";

    public static final String ORDER_ID = "order_id";

    public static final String USER_ID = "user_id";

    public static final String AMOUNT = "amount";

    public static final String TYPE = "type";

    public static final String STATUS = "status";

    public static final String PRE_BALANCE = "pre_balance";

    public static final String AFTER_BALANCE = "after_balance";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Getter
    @AllArgsConstructor
    public enum TYPE_ENUM {
        /**
         * 类型枚举
         */
        SUB(1, "扣减"),
        ADD(2, "增加");
        private int type;
        private String text;
    }

    @Getter
    @AllArgsConstructor
    public enum STATUS_ENUM {
        /**
         * 状态枚举
         */
        PRE(1, "准备"),
        OK(2, "完成"),
        CANCEL(3, "取消");
        private int status;
        private String text;
    }

}
