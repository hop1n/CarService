package org.example.service;

public interface Service <T> {
    void add(T obj);
    T getById(int id);
    void remove(int id);
}
