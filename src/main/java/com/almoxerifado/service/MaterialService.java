package com.almoxerifado.service;

import com.almoxerifado.model.Material;
import com.almoxerifado.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    public List<Material> listar() {
        return repository.findAll();
    }

    public Material salvar(Material material) {
        return repository.save(material);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Material buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }
}