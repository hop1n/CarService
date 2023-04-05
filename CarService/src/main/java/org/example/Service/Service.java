package org.example.Service;

public interface Service <T> {
    void add(T obj);
    void remove(int id);
    void sort();
    T getById(int id);
}
