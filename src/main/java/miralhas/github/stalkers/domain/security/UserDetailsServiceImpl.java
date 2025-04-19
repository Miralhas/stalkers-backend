package miralhas.github.stalkers.domain.security;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
                .map(SecurityUser::new)
                .orElseThrow(() -> {
                    String message = "User with email address of '%s' could not be found".formatted(email);
                    return new UsernameNotFoundException(message);
                });
    }
    
}
