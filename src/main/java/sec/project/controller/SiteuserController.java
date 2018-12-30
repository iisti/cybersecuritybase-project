/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Siteuser;
import sec.project.repository.SiteuserRepository;

/**
 *
 * @author Admin
 */
@Controller
public class SiteuserController {
    
    @Autowired
    private SiteuserRepository siteuserRepository;

    @RequestMapping(value = "/create-user", method = RequestMethod.GET)
    public String loadForm() {
        return "create-user";
    }

    @RequestMapping(value = "/create-user", method = RequestMethod.POST)
    public String submitForm(@RequestParam String username, @RequestParam String password) {
        siteuserRepository.save(new Siteuser(username, password));
        return "users";
    }
    
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getRegistered(Model model) {
        model.addAttribute("users", siteuserRepository.findAll());
        return "users";
    }
}
