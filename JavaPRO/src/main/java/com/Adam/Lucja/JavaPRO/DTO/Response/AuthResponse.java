package com.Adam.Lucja.JavaPRO.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthResponse {

    private Long id;
    private String accessToken;
    private String login;
    private String email;
    private List<String > roles;

}
