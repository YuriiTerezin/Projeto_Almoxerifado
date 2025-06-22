package com.almoxerifado.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "index"; // Vai procurar por src/main/resources/templates/index.html
    }
}