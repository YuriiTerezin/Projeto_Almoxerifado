package com.almoxerifado.controller;

import com.almoxerifado.model.Projeto;
import com.almoxerifado.model.Material;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public String listarProjetos(Model model) {
        List<Projeto> projetos = projetoService.listar();
        // DTOs para exibição
        class MaterialDTO {
            public String descricao;
            public Integer quantidade;
            public String valorTotalFormatado;
            public MaterialDTO(String descricao, Integer quantidade, String valorTotalFormatado) {
                this.descricao = descricao;
                this.quantidade = quantidade;
                this.valorTotalFormatado = valorTotalFormatado;
            }
        }
        class ProjetoDTO {
            public Long id;
            public String nome;
            public List<MaterialDTO> materiais;
            public Integer totalItens;
            public String valorTotalFormatado;
            public ProjetoDTO(Long id, String nome, List<MaterialDTO> materiais, Integer totalItens, String valorTotalFormatado) {
                this.id = id;
                this.nome = nome;
                this.materiais = materiais;
                this.totalItens = totalItens;
                this.valorTotalFormatado = valorTotalFormatado;
            }
        }
        List<ProjetoDTO> projetosDTO = new java.util.ArrayList<>();
        for (Projeto projeto : projetos) {
            List<MaterialDTO> materiaisDTO = new java.util.ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;
            int somaItens = 0;
            if (projeto.getMateriais() != null) {
                for (Material mat : projeto.getMateriais()) {
                    Integer qtd = projeto.getQuantidades().get(mat.getId());
                    if (mat.getPrecoUnitario() != null && qtd != null) {
                        BigDecimal valorTotal = mat.getPrecoUnitario().multiply(BigDecimal.valueOf(qtd));
                        String valorTotalFormatado = "R$ " + String.format("%,.2f", valorTotal).replace('.', ',');
                        materiaisDTO.add(new MaterialDTO(mat.getDescricao(), qtd, valorTotalFormatado));
                        total = total.add(valorTotal);
                        somaItens += qtd;
                    }
                }
            }
            String valorTotalProjetoFormatado = "R$ " + String.format("%,.2f", total).replace('.', ',');
            projetosDTO.add(new ProjetoDTO(
                projeto.getId(),
                projeto.getNome(),
                materiaisDTO,
                somaItens,
                valorTotalProjetoFormatado
            ));
        }
        model.addAttribute("projetos", projetosDTO);
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

    @PostMapping("/salvar")
    public String salvarProjeto(
            @ModelAttribute Projeto projeto,
            @RequestParam(value = "materiaisSelecionados", required = false) List<Long> materiaisSelecionados,
            @RequestParam Map<String, String> params,
            Model model
    ) {
        List<com.almoxerifado.model.Material> materiais = new java.util.ArrayList<>();
        java.util.Map<Long, Integer> quantidades = new java.util.HashMap<>();
        if (materiaisSelecionados != null && !materiaisSelecionados.isEmpty()) {
            for (Long matId : materiaisSelecionados) {
                com.almoxerifado.model.Material mat = materialService.buscarPorId(matId);
                if (mat != null) {
                    materiais.add(mat);
                    String quantidadeStr = params.get("quantidade_" + matId);
                    int quantidade = 1;
                    try {
                        quantidade = Integer.parseInt(quantidadeStr);
                    } catch (Exception ignored) {}
                    quantidades.put(matId, quantidade);
                }
            }
            projeto.setMateriais(materiais);
            projeto.setQuantidades(quantidades);
            projetoService.salvar(projeto);
            return "redirect:/projetos";
        } else {
            model.addAttribute("erro", "Selecione pelo menos um material para o projeto.");
            model.addAttribute("projeto", projeto);
            model.addAttribute("materiais", materialService.listar());
            return "projeto_form";
        }
    }

    @GetMapping("/novo")
    public String novoProjeto(Model model) {
        model.addAttribute("projeto", new Projeto());
        model.addAttribute("materiais", materialService.listar());
        return "projeto_form";
    }

    @PostMapping("/remover/{id}")
    public String removerProjeto(@PathVariable Long id) {
        projetoService.deletar(id);
        return "redirect:/projetos";
    }
}