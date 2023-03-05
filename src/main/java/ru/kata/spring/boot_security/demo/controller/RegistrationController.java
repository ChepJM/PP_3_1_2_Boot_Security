package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegistrationController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @GetMapping("/addAdmin")
    public String addAdmin() {
        String username = "admin";
        String secondName = "adminName";
        String password = "12345";
        int age = 14;
        String email = "admin@mail.ru";
        List<Role> roleList = new ArrayList<>();
        roleList.add(new Role("ROLE_ADMIN"));
        roleList.add(new Role("ROLE_USER"));
        User user = new User(
                username,
                secondName,
                password,
                age,
                email,
                roleList
        );
        userService.saveUser(user);

        return "redirect:/";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            List<String> messages = new ArrayList<>();
            for (ObjectError error : errors) {
                messages.add(error.getDefaultMessage());
            }
            model.addAttribute("messages", messages);
            return "registration";
        }
        userService.saveUser(user);
        return "redirect:/";
    }

}
