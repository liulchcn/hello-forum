package com.hello.model.user.vos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegisterVO implements Serializable {

    private String username;

    private String nickName;

    private String phone;

}
