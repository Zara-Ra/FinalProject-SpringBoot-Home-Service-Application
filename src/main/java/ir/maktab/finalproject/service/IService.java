package ir.maktab.finalproject.service;

import java.util.Optional;

public interface IService<T>{
    T add(T service);
    void delete(T service);
    Optional<T> findByName(String name);
}
