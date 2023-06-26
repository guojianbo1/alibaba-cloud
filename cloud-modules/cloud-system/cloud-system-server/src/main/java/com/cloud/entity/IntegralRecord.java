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
 * 积分记录表
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-26
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
  @TableName("ry_integral_record")
public class IntegralRecord implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 主键id
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 积分
     */
      private BigDecimal amount;

      /**
     * 订单id
     */
      private Long orderId;

      /**
     * 用户id
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


      public static final String ID = "id";

      public static final String AMOUNT = "amount";

      public static final String ORDER_ID = "order_id";

      public static final String USER_ID = "user_id";

      public static final String CREATE_TIME = "create_time";

      public static final String UPDATE_TIME = "update_time";

  }
