package com.hljy.user.dto;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * 用户信息更新DTO
 */
@Data
public class UserUpdateDTO {
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long id;
    
    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Size(min = 1, max = 20, message = "昵称长度必须在1-20个字符之间")
    private String nickname;
    
    /**
     * 性别：1-男，2-女
     */
    @NotNull(message = "性别不能为空")
    @Min(value = 1, message = "性别只能是1或2")
    @Max(value = 2, message = "性别只能是1或2")
    private Integer gender;
    
    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    @Min(value = 18, message = "年龄不能小于18岁")
    @Max(value = 100, message = "年龄不能大于100岁")
    private Integer age;
    
    /**
     * 个人简介
     */
    @Size(max = 500, message = "个人简介不能超过500个字符")
    private String bio;
    
    /**
     * 兴趣标签，JSON字符串
     */
    @Size(max = 500, message = "兴趣标签不能超过500个字符")
    private String interests;
    
    /**
     * 所在城市
     */
    @Size(max = 50, message = "城市名称不能超过50个字符")
    private String city;
}

