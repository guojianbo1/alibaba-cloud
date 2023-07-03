package com.cloud.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户详情DTO
 *
 * @author guojianbo
 * @date 2023/6/19 11:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailDTO {
   @ApiModelProperty(value = "用户id")
   private String userId;
   @ApiModelProperty(value = "用户名称")
   private String userName;
   @ApiModelProperty(value = "用户年龄")
   private Integer age;
   @ApiModelProperty(value = "菜单列表")
   private List<String> menuList;
}
