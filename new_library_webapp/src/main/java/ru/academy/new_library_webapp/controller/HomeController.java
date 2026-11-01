package ru.academy.new_library_webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // шаблон index.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // шаблон login.html
    }
}
