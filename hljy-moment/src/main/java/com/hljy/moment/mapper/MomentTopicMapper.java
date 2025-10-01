package com.hljy.moment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hljy.moment.entity.MomentTopic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 动态话题Mapper接口
 */
@Mapper
public interface MomentTopicMapper extends BaseMapper<MomentTopic> {

    /**
     * 查询热门话题
     */
    List<MomentTopic> selectHotTopics(@Param("limit") Integer limit);
}
