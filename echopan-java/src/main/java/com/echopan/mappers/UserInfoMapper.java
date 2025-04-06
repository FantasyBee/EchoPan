package com.echopan.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 用户信息 数据库操作接口
 */
public interface UserInfoMapper<T, P> extends BaseMapper<T, P> {

    /**
     * 根据UserId更新
     */
    Integer updateByUserId(@Param("bean") T t, @Param("userId") String userId);


    /**
     * 根据UserId删除
     */
    Integer deleteByUserId(@Param("userId") String userId);


    /**
     * 根据UserId获取对象
     */
    T selectByUserId(@Param("userId") String userId);


    /**
     * 根据Email更新
     */
    Integer updateByEmail(@Param("bean") T t, @Param("email") String email);


    /**
     * 根据Email删除
     */
    Integer deleteByEmail(@Param("email") String email);


    /**
     * 根据Email获取对象
     */
    T selectByEmail(@Param("email") String email);


    /**
     * 根据NickName更新
     */
    Integer updateByNickName(@Param("bean") T t, @Param("nickName") String nickName);


    /**
     * 根据NickName删除
     */
    Integer deleteByNickName(@Param("nickName") String nickName);


    /**
     * 根据NickName获取对象
     */
    T selectByNickName(@Param("nickName") String nickName);


    /**
     * 根据QqOpenId更新
     */
    Integer updateByQqOpenId(@Param("bean") T t, @Param("qqOpenId") String qqOpenId);


    /**
     * 根据QqOpenId删除
     */
    Integer deleteByQqOpenId(@Param("qqOpenId") String qqOpenId);


    /**
     * 根据QqOpenId获取对象
     */
    T selectByQqOpenId(@Param("qqOpenId") String qqOpenId);


    Integer updateUserSpace(@Param("userId") String userId, @Param("useSpace") Long useSpace, @Param("totalSpace") Long totalSpace);
}
