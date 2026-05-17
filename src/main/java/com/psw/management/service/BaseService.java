package com.psw.management.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {
    T create(T entity);
    T update(T entity);
    void delete(Long id);
    Optional<T> getById(Long id);
    List<T> getAll();
}
