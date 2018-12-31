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
            // A10:2017-Insufficient Logging & Monitoring
            // Uncomment this line for logging failed loging attempts
            //logger.info("AuthenticationFailed, username: " + username + ", failed login attempts: " + user.getLoginAttempts());
        }
        else if (user == null) {
            // Uncomment this line for logging failed loging attempts
            //logger.info("AuthenticationFailed, username does not exist: " + username);
        }
        
    }
}
