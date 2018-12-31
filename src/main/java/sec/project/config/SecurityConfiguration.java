package sec.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // no real security at the moment
        
        // _csrf parameter
        // OWASP Top 10 - 2013
        // A8 – Cross-Site Request Forgery (CSRF)
        //http.csrf().disable();

        // A2:2017-Broken Authentication
        // A7:2017-Cross-Site Scripting (XSS)
        //http.headers().frameOptions().sameOrigin();
        
        http.authorizeRequests()
                // A5:2017-Broken Access Control
                .antMatchers("/users").hasAnyAuthority("ADMIN", "USER")
                // A3:2017-Sensitive Data Exposure
                .antMatchers("/registered").permitAll()
                .anyRequest().authenticated();
        http.formLogin().successHandler(customizeAuthenticationSuccessHandler)
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
