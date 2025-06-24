package com.almoxerifado.controller;

import com.almoxerifado.model.Material;
import com.almoxerifado.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/materiais")
public class MaterialController {

    private final MaterialService service;

    @Autowired
    public MaterialController(MaterialService service) {
        this.service = service;
    }

    @GetMapping
    public String listarMateriais(Model model) {
        model.addAttribute("materiais", service.listar());
        model.addAttribute("material", new Material());
        return "materiais";
    }

    @PostMapping
    public String adicionarMaterial(@ModelAttribute("material") Material material) {
        service.salvar(material);
        return "redirect:/materiais";
    }

    @GetMapping("/editar/{id}")
    public String editarMaterial(@PathVariable Long id, Model model) {
        Material material = service.buscarPorId(id);
        model.addAttribute("material", material);
        model.addAttribute("materiais", service.listar());
        return "materiais";
    }

    @GetMapping("/remover/{id}")
    public String removerMaterial(@PathVariable Long id) {
        service.deletar(id);
        return "redirect:/materiais";
    }
}