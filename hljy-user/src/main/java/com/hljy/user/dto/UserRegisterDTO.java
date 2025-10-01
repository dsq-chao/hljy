package com.hljy.user.dto;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * 用户注册DTO
 */
@Data
public class UserRegisterDTO {
    
    /**
     * 用户名，3-20个字符，只能包含字母、数字和下划线
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "用户名只能包含字母、数字和下划线，长度3-20个字符")
    private String username;
    
    /**
     * 密码，6-20个字符
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;
    
    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
    
    /**
     * 手机号，格式：1[3-9]\\d{9}
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    /**
     * 邮箱，可选
     */
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 昵称，1-20个字符
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
     * 年龄，18-100
     */
    @NotNull(message = "年龄不能为空")
    @Min(value = 18, message = "年龄不能小于18岁")
    @Max(value = 100, message = "年龄不能大于100岁")
    private Integer age;
    
    /**
     * 验证码，4-6个字符
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 4, max = 6, message = "验证码长度必须在4-6个字符之间")
    private String captcha;
}