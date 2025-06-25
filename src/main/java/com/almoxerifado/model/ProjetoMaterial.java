package com.almoxerifado.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "projeto_materiais")
public class ProjetoMaterial implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material;

    private Integer quantidade;

    public ProjetoMaterial() {}

    public ProjetoMaterial(Projeto projeto, Material material, Integer quantidade) {
        this.projeto = projeto;
        this.material = material;
        this.quantidade = quantidade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Projeto getProjeto() { return projeto; }
    public void setProjeto(Projeto projeto) { this.projeto = projeto; }

    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjetoMaterial)) return false;
        ProjetoMaterial that = (ProjetoMaterial) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProjetoMaterial{" +
                "id=" + id +
                ", material=" + (material != null ? material.getDescricao() : null) +
                ", quantidade=" + quantidade +
                '}';
    }
}
