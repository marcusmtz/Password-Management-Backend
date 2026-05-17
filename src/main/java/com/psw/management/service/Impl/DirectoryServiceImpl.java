package com.psw.management.service.Impl;

import com.psw.management.entity.Directory;
import com.psw.management.repository.DirectoryRepository;
import com.psw.management.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DirectoryServiceImpl implements DirectoryService {
    @Autowired
    private DirectoryRepository directoryRepository;
    @Override
    public Directory create(Directory entity) {
        return directoryRepository.save(entity);
    }

    @Override
    public Directory update(Directory entity) {
        return directoryRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        directoryRepository.deleteById(id);
    }

    @Override
    public Optional<Directory> getById(Long id) {
        return directoryRepository.findById(id);
    }

    @Override
    public List<Directory> getAll() {
        return directoryRepository.findAll();
    }

    @Override
    public List<Directory> getByUserId(Long userId) {
        return directoryRepository.findByUserId(userId);
    }
}
