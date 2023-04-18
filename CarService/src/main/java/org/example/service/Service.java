package org.example.service;

public interface Service<T> {
    T add(T obj);

    T getById(Long id);

    boolean remove(Long id);
}
