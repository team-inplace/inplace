package my.inplace.infra.security;

import lombok.RequiredArgsConstructor;
import my.inplace.domain.search.PasswordEncoderAdaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomPasswordEncoder implements PasswordEncoderAdaptor {
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
