package sec.project.config;

/**
 *
 * @author iisti
 */
import java.io.IOException;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sec.project.domain.Siteuser;
import sec.project.repository.SiteuserRepository;
 
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
 
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
        @Autowired
        private SiteuserRepository siteuserRepository;
        
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
        //set our response to OK status
        response.setStatus(HttpServletResponse.SC_OK);
        
        String username = authentication.getName();
        Siteuser user = siteuserRepository.findByUsername(username);
        /*if (user.isLocked()) {
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/locked");
        }*/
        response.sendRedirect("/form");
        
        // Set failed login attempts counter to zero
        user.zeroLoginAttempts();
        
        logger.info("AuthenticationSuccess: " + username + ", failed login attempts " + user.getLoginAttempts());
        /*
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(auth.getAuthority())){
            	admin = true;
            }
        }*/
    }  
}