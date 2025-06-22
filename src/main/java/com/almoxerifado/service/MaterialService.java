package com.almoxerifado.service;

import com.almoxerifado.model.Material;
import com.almoxerifado.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> listar() {
        return materialRepository.findAll();
    }

    public Material salvar(Material material) {
        return materialRepository.save(material);
    }

    public void deletar(Long id) {
        materialRepository.deleteById(id);
    }

    public Material buscarPorId(Long id) {
        return materialRepository.findById(id).orElse(null);
    }
}