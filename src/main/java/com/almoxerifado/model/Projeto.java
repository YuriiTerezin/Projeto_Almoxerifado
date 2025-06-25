package com.almoxerifado.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String ConsumoMaterial;

    @ManyToMany
    @JoinTable(
        name = "projeto_materiais",
        joinColumns = @JoinColumn(name = "projeto_id"),
        inverseJoinColumns = @JoinColumn(name = "material_id"))
    private List<Material> materiais;

    @ElementCollection
    @CollectionTable(name = "projeto_material_quantidade", joinColumns = @JoinColumn(name = "projeto_id"))
    @MapKeyColumn(name = "material_id")
    @Column(name = "quantidade")
    private java.util.Map<Long, Integer> quantidades = new java.util.HashMap<>();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getConsumoMaterial() { return ConsumoMaterial; }
    public void setConsumoMaterial(String ConsumoMaterial) { this.ConsumoMaterial = ConsumoMaterial; }

    public List<Material> getMateriais() { return materiais; }
    public void setMateriais(List<Material> materiais) { this.materiais = materiais; }

    public java.util.Map<Long, Integer> getQuantidades() { return quantidades; }
    public void setQuantidades(java.util.Map<Long, Integer> quantidades) { this.quantidades = quantidades; }
}