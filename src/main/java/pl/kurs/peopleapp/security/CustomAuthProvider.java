package pl.kurs.peopleapp.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder encoder;
    private final LoginAttemptService loginAttemptService;

    public CustomAuthProvider(UserDetailsService uds, PasswordEncoder encoder, LoginAttemptService las) {
        this.userDetailsService = uds;
        this.encoder = encoder;
        this.loginAttemptService = las;
    }

    @Override
    public Authentication authenticate(Authentication auth)  {
        String username = auth.getName();
        String password = auth.getCredentials().toString();

        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException("Account is temporarily locked due to failed login attempts.");
        }

        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (!encoder.matches(password, user.getPassword())) {
            loginAttemptService.loginFailed(username);
            throw new BadCredentialsException("Wrong password");
        }

        loginAttemptService.loginSucceeded(username);
        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.equals(aClass);
    }
}
