package sec.project.config;

import sec.project.repository.SiteuserRepository;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sec.project.domain.Siteuser;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired SiteuserRepository siteuserRepository;
    @Autowired PasswordEncoder passwordEncoder;
    
    @PostConstruct
    public void init() {
        // Add username admin with password "1234" and with admin role true
        Siteuser user = new Siteuser("admin", passwordEncoder.encode("1234"), true);
        siteuserRepository.save(user);
        
        Siteuser user2 = new Siteuser("tom", passwordEncoder.encode("qwer"), false);
        siteuserRepository.save(user2);
        
        Siteuser user3 = new Siteuser("mike", passwordEncoder.encode("qwertyuiop"), false);
        siteuserRepository.save(user3);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Siteuser siteuser = siteuserRepository.findByUsername(username);
        if (siteuser == null) {
            throw new UsernameNotFoundException("No such user: " + username);
        }

        // If account is locked, send info that account is locked
        // https://docs.spring.io/spring-security/site/docs/4.2.8.RELEASE/apidocs/org/springframework/security/core/userdetails/UserDetails.html
        if (siteuser.isLocked()) {
            return new org.springframework.security.core.userdetails.User(
                siteuser.getUsername(),
                siteuser.getPassword(),
                true,
                true,
                true,
                false, // Account is locked
                Arrays.asList(new SimpleGrantedAuthority("USER")));
        }
        // Username is admin
        if (siteuser.getIsAdmin()) {
            return new org.springframework.security.core.userdetails.User(
                siteuser.getUsername(),
                siteuser.getPassword(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
        }        
        // Username is not locked and not admin
        return new org.springframework.security.core.userdetails.User(
                siteuser.getUsername(),
                siteuser.getPassword(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}
