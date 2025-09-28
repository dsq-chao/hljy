package com.hljy.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hljy.user.dto.UserLoginDTO;
import com.hljy.user.entity.User;
import com.hljy.user.vo.UserLoginVo;
import com.hljy.user.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     * @param loginDTO 登录参数
     */
    UserLoginVo login(UserLoginDTO loginDTO);
}
