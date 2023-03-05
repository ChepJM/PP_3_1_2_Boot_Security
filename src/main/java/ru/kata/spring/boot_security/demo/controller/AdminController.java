package ru.kata.spring.boot_security.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping
    public String index(Authentication auth, Model model) throws JsonProcessingException {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("currentUser", userService.getUserByEmail(getPrincipalEmail(auth)));
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin-page";
    }

    public String getPrincipalEmail(Authentication auth) {
        String principalString = auth.getPrincipal().toString();
        String email = principalString.substring(
                principalString.indexOf("email") + 7,
                principalString.indexOf("roleList") - 3
        );
        return email;
    }

    @PostMapping
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping(value = "/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @PutMapping
    public String editUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

}
