package com.Adam.Lucja.JavaPRO.Config;

import com.Adam.Lucja.JavaPRO.Entity.Student;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

@Getter
public class LoginPrincipal implements UserDetails {

    private Student login;

    private Collection<? extends GrantedAuthority> authorities;

    private LoginPrincipal(Student login) {
        this.login = login;
    }

    public static LoginPrincipal create(Student login) {
        return new LoginPrincipal(login);
    }

    @Override
    public String getUsername() {
        return login.getNrAlbum();
    }

    @Override
    public String getPassword() {
        return login.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginPrincipal that = (LoginPrincipal) o;
        return Objects.equals(login.getNrAlbum(), that.login.getNrAlbum());
    }

}
