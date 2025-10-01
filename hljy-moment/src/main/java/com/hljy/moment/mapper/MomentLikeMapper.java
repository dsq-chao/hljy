package com.hljy.moment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hljy.moment.entity.MomentLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动态点赞Mapper接口
 */
@Mapper
public interface MomentLikeMapper extends BaseMapper<MomentLike> {
}
