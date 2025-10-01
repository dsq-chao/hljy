package com.hljy.moment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 添加评论DTO
 */
@Data
public class MomentCommentDTO {

    /**
     * 动态ID
     */
    @NotNull(message = "动态ID不能为空")
    private Long momentId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 200, message = "评论内容不能超过200个字符")
    private String content;

    /**
     * 父评论ID（回复评论时使用）
     */
    private Long parentId;
}
