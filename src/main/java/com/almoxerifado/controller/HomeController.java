package com.almoxerifado.controller;

import com.almoxerifado.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("materiais", materialService.listar());
        return "index";
    }
}