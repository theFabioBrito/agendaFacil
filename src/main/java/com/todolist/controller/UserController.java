package com.todolist.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.todolist.model.User;
import com.todolist.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (user.getPassword() == null || !user.getPassword().matches("\\d{4}")) {
            model.addAttribute("error", "A senha deve conter 4 dígitos numéricos.");
            return "register";
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Nome de usuário já existente. Tente outro.");
            return "register";
        }

        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpSession session) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(user.getPassword())) {
            session.setAttribute("user", optionalUser.get());
            return "redirect:/";
        }

        model.addAttribute("error", "Usuário ou senha inválidos.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
