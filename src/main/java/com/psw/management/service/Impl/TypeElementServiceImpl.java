package com.psw.management.service.Impl;

import com.psw.management.entity.TypeElement;
import com.psw.management.repository.TypeElementRepository;
import com.psw.management.service.TypeElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TypeElementServiceImpl implements TypeElementService {
    @Autowired
    private TypeElementRepository typeElementRepository;

    @Override
    public TypeElement create(TypeElement entity) {
        return typeElementRepository.save(entity);
    }

    @Override
    public TypeElement update(TypeElement entity) {
        return typeElementRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        typeElementRepository.deleteById(id);
    }

    @Override
    public Optional<TypeElement> getById(Long id) {
        return typeElementRepository.findById(id);
    }

    @Override
    public List<TypeElement> getAll() {
        return typeElementRepository.findAll();
    }
}
