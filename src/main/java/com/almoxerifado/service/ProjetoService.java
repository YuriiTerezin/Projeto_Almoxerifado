package com.almoxerifado.service;

import com.almoxerifado.model.Projeto;
import com.almoxerifado.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository repository;

    public List<Projeto> listar() {
        return repository.findAll();
    }

    public Projeto salvar(Projeto projeto) {
        return repository.save(projeto);
    }
}