package sec.project.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import sec.project.domain.Siteuser;
import sec.project.repository.SiteuserRepository;

/**
 *
 * @author iisti
 */
@Component
public class AuthenticationEventListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private SiteuserRepository siteuserRepository;
    
    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {

        String username = (String) event.getAuthentication().getPrincipal();

        Siteuser user = siteuserRepository.findByUsername(username);
        if (user != null) {
            user.addLoginAttempts();
            siteuserRepository.save(user);
            logger.info("AuthenticationFailed, username: " + username + ", failed login attempts: " + user.getLoginAttempts());
        }
        
    }
    /*
    public void authenticationSuccess(AuthenticationSuccessEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        Siteuser user = siteuserRepository.findByUsername(username);
        //System.out.println(siteuserRepository.findByUsername(username).getLoginAttempts());
        if (user.getLoginAttempts() > 1) {
            System.out.println("failed");
        }
    }*/
}
