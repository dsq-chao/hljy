package com.hljy.moment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 发布动态DTO
 */
@Data
public class MomentPublishDTO {

    /**
     * 动态内容
     */
    @NotBlank(message = "动态内容不能为空")
    @Size(max = 500, message = "动态内容不能超过500个字符")
    private String content;

    /**
     * 图片URL列表
     */
    private List<String> images;

    /**
     * 话题标签
     */
    private List<String> topics;
}
