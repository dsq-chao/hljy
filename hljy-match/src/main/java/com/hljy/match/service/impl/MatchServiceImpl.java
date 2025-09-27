package com.hljy.match.service.impl;

import com.hljy.match.service.MatchService;
import com.hljy.match.vo.MatchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 匹配服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    
    @Override
    public List<MatchVO> getRecommendUsers(Long userId) {
        // TODO: 实现推荐算法
        // 1. 根据用户兴趣标签匹配
        // 2. 根据地理位置匹配
        // 3. 根据活跃度匹配
        // 4. 排除已匹配和已拒绝的用户
        
        List<MatchVO> recommendUsers = new ArrayList<>();
        
        // 模拟数据
        MatchVO user1 = new MatchVO();
        user1.setUserId(2L);
        user1.setUsername("user2");
        user1.setNickname("小美");
        user1.setGender(2);
        user1.setAge(25);
        user1.setAvatar("https://example.com/avatar2.jpg");
        user1.setBio("喜欢旅行和摄影");
        user1.setCity("北京");
        user1.setScore(85.5);
        recommendUsers.add(user1);
        
        MatchVO user2 = new MatchVO();
        user2.setUserId(3L);
        user2.setUsername("user3");
        user2.setNickname("小雨");
        user2.setGender(2);
        user2.setAge(23);
        user2.setAvatar("https://example.com/avatar3.jpg");
        user2.setBio("热爱生活，喜欢美食");
        user2.setCity("上海");
        user2.setScore(78.0);
        recommendUsers.add(user2);
        
        return recommendUsers;
    }
    
    @Override
    public void likeUser(Long userId, Long targetUserId) {
        // TODO: 实现喜欢逻辑
        // 1. 检查是否已经匹配
        // 2. 创建匹配记录
        // 3. 检查是否互相喜欢，如果是则创建聊天会话
        // 4. 发送通知
        
        log.info("用户{}喜欢用户{}", userId, targetUserId);
    }
    
    @Override
    public void dislikeUser(Long userId, Long targetUserId) {
        // TODO: 实现不喜欢逻辑
        // 1. 创建拒绝记录
        // 2. 更新推荐算法权重
        
        log.info("用户{}不喜欢用户{}", userId, targetUserId);
    }
    
    @Override
    public List<MatchVO> getMatches(Long userId) {
        // TODO: 实现获取匹配列表
        // 1. 查询互相喜欢的用户
        // 2. 返回匹配成功列表
        
        List<MatchVO> matches = new ArrayList<>();
        
        // 模拟数据
        MatchVO match = new MatchVO();
        match.setUserId(2L);
        match.setUsername("user2");
        match.setNickname("小美");
        match.setGender(2);
        match.setAge(25);
        match.setAvatar("https://example.com/avatar2.jpg");
        match.setBio("喜欢旅行和摄影");
        match.setCity("北京");
        match.setScore(85.5);
        matches.add(match);
        
        return matches;
    }
}
