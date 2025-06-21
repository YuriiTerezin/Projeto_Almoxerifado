package com.almoxerifado.controller;

import com.almoxerifado.model.Material;
import com.almoxerifado.model.Projeto;
import com.almoxerifado.service.MaterialService;
import com.almoxerifado.service.ProjetoService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public String listarProjetos(Model model) {
        model.addAttribute("projetos", projetoService.listar());
        model.addAttribute("materiais", materialService.listar());
        model.addAttribute("projeto", new Projeto());
        return "projetos";
    }

    @PostMapping
    public String adicionarProjeto(@ModelAttribute Projeto projeto) {
        projetoService.salvar(projeto);
        return "redirect:/projetos";
    }

    @GetMapping("/exportar")
    public void exportarProjetos(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=projetos.pdf");

        List<Projeto> projetos = projetoService.listar();
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("Projetos e Materiais"));
        PdfPTable table = new PdfPTable(2);
        table.addCell("Projeto");
        table.addCell("Materiais");

        for (Projeto p : projetos) {
            table.addCell(p.getNome());
            table.addCell(p.getMateriais().toString());
        }

        document.add(table);
        document.close();
    }
}