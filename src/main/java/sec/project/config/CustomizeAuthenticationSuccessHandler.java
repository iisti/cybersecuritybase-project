/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.config;

/**
 *
 * @author Admin
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
        
        
        //String username = (String) event.getAuthentication().getPrincipal();
        String username = authentication.getName();
        //System.out.println(username);
        Siteuser user = siteuserRepository.findByUsername(username);
        //System.out.println(siteuserRepository.findByUsername(username).getLoginAttempts());
        if (user.getLoginAttempts() > 1) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/locked");
        }
        
        
        
        
        boolean admin = false;
        
        logger.info("AT onAuthenticationSuccess(...) function!");
        /*
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(auth.getAuthority())){
            	admin = true;
            }
        }
        
        if(admin){
        	response.sendRedirect("/admin");
        }else{
        	response.sendRedirect("/user");
        }*/
    }  
}