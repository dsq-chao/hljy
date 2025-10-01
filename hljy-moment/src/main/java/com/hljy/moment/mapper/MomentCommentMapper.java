package com.hljy.moment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hljy.moment.entity.MomentComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 动态评论Mapper接口
 */
@Mapper
public interface MomentCommentMapper extends BaseMapper<MomentComment> {

    /**
     * 分页查询动态评论（包含用户信息）
     */
    IPage<MomentComment> selectCommentPageWithUser(Page<MomentComment> page, @Param("momentId") Long momentId);

    /**
     * 查询动态的所有评论（树形结构）
     */
    List<MomentComment> selectCommentTreeByMomentId(@Param("momentId") Long momentId);
}
