package sec.project.config;

import sec.project.repository.SiteuserRepository;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
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
    
    //private Map<String, String> accountDetails;

    @PostConstruct
    public void init() {
        // this data would typically be retrieved from a database
        /*
        this.accountDetails = new TreeMap<>();
        this.accountDetails.put("ted", "$2a$06$rtacOjuBuSlhnqMO2GKxW.Bs8J6KI0kYjw/gtF0bfErYgFyNTZRDm");
        // added, create account mike with password "president"
        this.accountDetails.put("mike", "$2a$10$nKOFU.4/iK9CqDIlBkmMm.WZxy2XKdUSlImsG8iKsAP57GMcXwLTS");
        */
        
        
        // Add username admin with password "1234" and with admin role true
        Siteuser user = new Siteuser("admin", passwordEncoder.encode("1234"), true);
        siteuserRepository.save(user);
        
        Siteuser user2 = new Siteuser("tom", passwordEncoder.encode("qwer"), false);
        siteuserRepository.save(user2);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    /*    if (!this.accountDetails.containsKey(username)) {
            throw new UsernameNotFoundException("No such user: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                username,
                this.accountDetails.get(username),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }*/
    
    
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
        // Username is not locked
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
