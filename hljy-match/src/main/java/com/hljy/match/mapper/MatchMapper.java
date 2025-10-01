package com.hljy.match.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hljy.match.entity.Match;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 匹配记录Mapper接口
 */
@Mapper
public interface MatchMapper extends BaseMapper<Match> {
    
    /**
     * 查询用户已匹配的用户ID列表
     */
    List<Long> selectMatchedUserIds(@Param("userId") Long userId);
    
    /**
     * 查询用户已拒绝的用户ID列表
     */
    List<Long> selectRejectedUserIds(@Param("userId") Long userId);
    
    /**
     * 查询用户已喜欢的用户ID列表
     */
    List<Long> selectLikedUserIds(@Param("userId") Long userId);
    
    /**
     * 检查两个用户是否互相喜欢
     */
    int checkMutualLike(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    
    /**
     * 查询匹配成功的用户ID列表
     */
    List<Long> selectMutualMatchUserIds(@Param("userId") Long userId);
}
