package com.Adam.Lucja.JavaPRO.Security;

import com.Adam.Lucja.JavaPRO.Entity.Login;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

@Getter
public class LoginPrincipal implements UserDetails {

    private Login login;

    private Collection<? extends GrantedAuthority> authorities;

    private LoginPrincipal(Login login) {
        this.login = login;
    }

    public static LoginPrincipal create(Login szukanyStudent) {
        return new LoginPrincipal(szukanyStudent);
    }

    @Override
    public String getUsername() {
        return login.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return Objects.equals(login.getUsername(), that.login.getUsername());
    }

}
