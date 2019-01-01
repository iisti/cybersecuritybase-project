package sec.project.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author iisti
 */
@Entity
public class Siteuser extends AbstractPersistable<Long> {

    @Column(unique = true)
    private String username;
    private String password;
    private static int userID = 0;
    @Column(unique = false)
    private int loginAttempts;
    private boolean isAdmin;

    public Siteuser() {
        this.username = "username" + Siteuser.userID++;
        // default password is president
        this.password = "$2a$10$nKOFU.4/iK9CqDIlBkmMm.WZxy2XKdUSlImsG8iKsAP57GMcXwLTS";
        this.loginAttempts = 0;
        this.isAdmin = false;
    }
    
    public Siteuser(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.loginAttempts = 0;
        Siteuser.userID++; 
        this.isAdmin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoginAttempts() {
        return this.loginAttempts;
    }
    
    public void zeroLoginAttempts() {
        this.loginAttempts = 0;
    }
    
    public void addLoginAttempts() {

    }
    
    public boolean isLocked() {
        if (this.loginAttempts > 3) {
            return true;
        }
        return false;
    }
    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }
    
    public boolean getIsAdmin() {
        return this.isAdmin;
    }
}
