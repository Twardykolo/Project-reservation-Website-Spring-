package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.Entity.Login;
import com.Adam.Lucja.JavaPRO.Repository.LoginRepository;
import com.Adam.Lucja.JavaPRO.Security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Locale;

@Service
public class LoginDetailsService implements UserDetailsService {

    @Autowired
    private LoginRepository loginRepository;

    /**
     * Metoda wykorzystywana przy weryfikacji użytkownika przez zabezpieczenia (Spring Security).
     * Metoda stara się odszukać odpowiedni {@link Login} przy pomocy {@link LoginRepository}
     * na podstawie nazwy użytkownika podanej jako parametr. Jeśli login został odnaleziony,
     * zwracany jest wynik metody build klasy {@link UserDetailsImpl}, używającej loginu jako parametru.
     * W przeciwnym razie wyrzucony zostaje wyjątek mówiący o tym, że nie odnaleziono takiego użytkownika.
     * Do wyrzucania wyjątku użyto funkcji lambda.
     * @param username Nazwa szukanego użytkownika, typ {@link String}
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException
     */
    @Transactional //adnotacja zmieniająca tryb komunikacji z bazą danych, dzięki czemu można wybierać duże dane (np. pliki z bazy)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Login login = loginRepository.findByUsername(username.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new ExpressionException("User Not Found"));
        return UserDetailsImpl.build(login);
    }
}
