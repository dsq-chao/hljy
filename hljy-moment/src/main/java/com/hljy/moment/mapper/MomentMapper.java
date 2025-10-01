package com.hljy.moment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hljy.moment.entity.Moment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 动态Mapper接口
 */
@Mapper
public interface MomentMapper extends BaseMapper<Moment> {

    /**
     * 分页查询动态列表（包含用户信息）
     */
    IPage<Moment> selectMomentPageWithUser(Page<Moment> page, @Param("userId") Long userId);

    /**
     * 查询用户是否点赞了动态
     */
    int checkUserLikedMoment(@Param("momentId") Long momentId, @Param("userId") Long userId);

    /**
     * 查询动态的点赞用户列表
     */
    List<Long> selectMomentLikedUserIds(@Param("momentId") Long momentId);
}
