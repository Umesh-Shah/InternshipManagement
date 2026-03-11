package ca.uwindsor.ims.security;

import ca.uwindsor.ims.repository.LoginRepository;
import ca.uwindsor.ims.repository.VbctLoginRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ImsUserDetailsService implements UserDetailsService {

    private final LoginRepository loginRepository;
    private final VbctLoginRepository vbctLoginRepository;

    public ImsUserDetailsService(LoginRepository loginRepository, VbctLoginRepository vbctLoginRepository) {
        this.loginRepository = loginRepository;
        this.vbctLoginRepository = vbctLoginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loginRepository.findByUsername(username)
                .map(ImsUserDetails::fromLogin)
                .or(() -> vbctLoginRepository.findByLoginName(username)
                        .map(ImsUserDetails::fromVbctLogin))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
