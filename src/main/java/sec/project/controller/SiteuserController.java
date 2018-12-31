package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Siteuser;
import sec.project.repository.SiteuserRepository;

/**
 *
 * @author iisti
 */
@Controller
public class SiteuserController {
    
    @Autowired
    private SiteuserRepository siteuserRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getUsers(Model model) {
        model.addAttribute("users", siteuserRepository.findAll());
        return "users";
    }
    
    @RequestMapping(value = "/locked", method = RequestMethod.GET)
    public String locked() {
        return "locked";
    }
        
    @RequestMapping(value = "/create-user", method = RequestMethod.GET)
    public String loadForm() {
        return "create-user";
    }
        
    @RequestMapping(value = "/create-user", method = RequestMethod.POST)
    public String submitForm(@RequestParam String username, @RequestParam String psw, @RequestParam(value="admin", required = false )boolean admin) {
        siteuserRepository.save(new Siteuser(username,  passwordEncoder.encode(psw), admin));
        return "created";
    }
    
    @RequestMapping(value = "/created", method = RequestMethod.GET)
    public String createdUser() {
        return "created";
    }
}
