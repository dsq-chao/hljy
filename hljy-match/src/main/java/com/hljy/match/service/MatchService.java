package com.hljy.match.service;

import com.hljy.match.vo.MatchVO;

import java.util.List;

/**
 * 匹配服务接口
 */
public interface MatchService {
    
    /**
     * 获取推荐用户列表
     */
    List<MatchVO> getRecommendUsers(Long userId);
    
    /**
     * 喜欢用户
     */
    void likeUser(Long userId, Long targetUserId);
    
    /**
     * 不喜欢用户
     */
    void dislikeUser(Long userId, Long targetUserId);
    
    /**
     * 获取匹配成功列表
     */
    List<MatchVO> getMatches(Long userId);
}
