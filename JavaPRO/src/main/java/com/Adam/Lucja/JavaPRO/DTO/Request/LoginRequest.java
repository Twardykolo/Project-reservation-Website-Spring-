package com.Adam.Lucja.JavaPRO.DTO.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String email;


    public LoginRequest(StudentRequest studentRequest){
        this.username = studentRequest.getNrAlbum();
        this.email=studentRequest.getEmail();
        this.password=studentRequest.getPassword();
    }
}
