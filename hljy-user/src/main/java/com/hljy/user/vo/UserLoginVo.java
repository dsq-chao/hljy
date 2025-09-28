package com.hljy.user.vo;
import lombok.Data;

@Data
public class UserLoginVo {
    private String token;
    private Long userId;
    private String username;
}
