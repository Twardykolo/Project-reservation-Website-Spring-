package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Request.LoginRequest;
import com.Adam.Lucja.JavaPRO.Entity.Login;
import com.Adam.Lucja.JavaPRO.Entity.Login2Role;
import com.Adam.Lucja.JavaPRO.Entity.Role;
import com.Adam.Lucja.JavaPRO.Repository.Login2RoleRepository;
import com.Adam.Lucja.JavaPRO.Repository.LoginRepository;
import com.Adam.Lucja.JavaPRO.Repository.RoleRepository;
import com.Adam.Lucja.JavaPRO.Util.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Login2RoleRepository login2RoleRepository;

    /**
     * Metoda tworząca nowy {@link Login} w bazie danych, a następnie przypisuje mu rolę użytkownika.
     * Nowy login zostaje utworzony na podstawie danych przekazanych w obiekcie pomocniczym {@link LoginRequest}, przy
     * czym hasło zostaje zaszyfrowane przy użyciu {@link PasswordEncoder} (Spring Security) używając BCrypt.
     * Tak utworzony login zostaje zapisany w bazie danych za pomocą {@link LoginRepository}. Następnie na podstawie
     * wartości ROLE_USER przechowywanej w {@link ERole}, przy użyciu {@link RoleRepository} odszukany zostaje w bazie
     * danych obiekt klasy {@link Role} odpowiadający roli użytkownika. Utworzone zostaje powiązanie pomiędzy utworzonym
     * wcześniej loginem, a rolą użytkownika przez stworzenie nowego obiektu wiążącego {@link Login2Role} i zapisaniu
     * go w bazie danych przy użyciu {@link Login2RoleRepository}. W wyniku funkcji zwracana jest reprezentacja
     * zapisanego w bazie danych loginu.
     * @param loginRequest Obiekt pomocniczy przechowujący dane potrzebne do utworzenia nowego {@link Login}u
     * @return {@link Login}
     */
    public Login createLogin(LoginRequest loginRequest){
        Login login = Login.builder()
                .username(loginRequest.getUsername())
                .password(passwordEncoder.encode(loginRequest.getPassword()))
                .email(loginRequest.getEmail()).build();
        Login savedLogin = loginRepository.save(login);
        Role roleUser = roleRepository.findByName(ERole.ROLE_USER).get();
        Login2Role login2Role = Login2Role.builder()
                .login(savedLogin)
                .role(roleUser).build();
        login2RoleRepository.save(login2Role);
        return savedLogin;
    }
}
