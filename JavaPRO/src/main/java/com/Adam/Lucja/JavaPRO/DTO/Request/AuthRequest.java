package com.Adam.Lucja.JavaPRO.DTO.Request;


import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class AuthRequest {

    @NotNull
    private String loginOrEmail;

    @NotNull
    private String password;

}
