package com.hello.service.user.controller.v1;

import com.hello.model.user.dtos.UserRegisterDTO;
import com.hello.model.user.pojos.User;
import com.hello.model.user.vos.UserRegisterVO;
import com.hello.service.user.service.UserService;
import com.hello.model.user.vos.UserLoginVO;
import com.hello.model.user.dtos.LoginDTO;
import com.hello.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.PasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping ("/login")

    public Result<UserLoginVO> login(@ModelAttribute LoginDTO loginDTO) throws PasswordException {
        log.info("{}",loginDTO);
        UserLoginVO userVO = userService.login(loginDTO);
        return Result.success(userVO);
    }
    @PostMapping ("/register")
    public Result<UserRegisterVO> register(@ModelAttribute UserRegisterDTO registerDTO) throws PasswordException {
        log.info("{}",registerDTO);
        UserRegisterVO registerVO = userService.register(registerDTO);

        return Result.success(registerVO);
    }

}
