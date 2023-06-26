package com.cloud.mapper;

import com.cloud.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-21
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 独占锁
     * @param userId
     * @return
     */
    User selectByUserIdForUpdate(@Param("userId") String userId);
}
