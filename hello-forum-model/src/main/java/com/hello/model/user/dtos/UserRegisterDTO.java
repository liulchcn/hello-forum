package com.hello.model.user.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterDTO implements Serializable {

    private String username;

    private String password;

    private String nickName;

    private String phone;
}
