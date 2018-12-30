package sec.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import sec.project.domain.Siteuser;
import sec.project.repository.SiteuserRepository;

/**
 *
 * @author iisti
 */
@Component
public class AuthenticationEventListener {

    @Autowired
    private SiteuserRepository siteuserRepository;
    
    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {

        String username = (String) event.getAuthentication().getPrincipal();

        // update the failed login count for the user
        // ...
        Siteuser user = siteuserRepository.findByUsername(username);
        if (user != null) {
            user.addLoginAttempts();
            siteuserRepository.save(user);
            System.out.println(siteuserRepository.findByUsername(username).getLoginAttempts());
        }
        
    }
}
