/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.AbstractPersistable;
import sec.project.repository.SiteuserRepository;

/**
 *
 * @author iisti
 */
@Entity
public class Siteuser extends AbstractPersistable<Long> {

    //@Autowired PasswordEncoder passwordEncoder;
    //@Autowired private SiteuserRepository siteuserRepository;
    
    @Column(unique = true)
    private String username;
    private String password;
    private static int userID = 0;
    @Column(unique = false)
    private int loginAttempts;
    private boolean isAdmin;


    //int reposize = (int) siteuserRepository.count();
    
    public Siteuser() {
        this.username = "username" + Siteuser.userID++;
        // president
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

    //@OneToMany(mappedBy = "user")
    /*
    private List<FileObject> fileObjects;

    public List<FileObject> getFileObjects() {
        return fileObjects;
    }

    public void setFileObjects(List<FileObject> fileObjects) {
        this.fileObjects = fileObjects;
    }*/

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
        this.loginAttempts++;
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
    
    /*
    public boolean isAdmin() {
        return this.isAdmin;
    }*/
}
