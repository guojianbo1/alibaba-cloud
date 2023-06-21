package com.cloud.domain.response;

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
   private String userId;
   private String userName;
   private Integer age;
   private List<String> menuList;
}
