package com.hljy.moment.vo;

import lombok.Data;

/**
 * 动态话题VO
 */
@Data
public class MomentTopicVO {

    /**
     * 话题ID
     */
    private Long id;

    /**
     * 话题名称
     */
    private String name;

    /**
     * 话题描述
     */
    private String description;

    /**
     * 使用次数
     */
    private Integer usageCount;
}
