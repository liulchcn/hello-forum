package com.hello.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.model.user.dtos.LoginDTO;
import com.hello.model.user.dtos.UserRegisterDTO;
import com.hello.model.user.pojos.User;
import com.hello.model.user.vos.UserLoginVO;
import com.hello.model.user.vos.UserRegisterVO;
import org.bouncycastle.openssl.PasswordException;

public interface UserService extends IService<User> {

    UserLoginVO login(LoginDTO loginDTO) throws PasswordException;

    UserRegisterVO register(UserRegisterDTO registerDTO);
}
