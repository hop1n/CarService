package org.example.Service;

public interface Service <T> {
    void add(T obj);
    T getById(int id);
    void remove(int id);
    void sort();
}
