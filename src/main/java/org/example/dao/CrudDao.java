package org.example.dao;

import javafx.collections.ObservableList;

public interface CrudDao <T,S> extends SuperDao{

    T search(S s);
    boolean save(T t);
    boolean update(T t);
    boolean delete(T t);
    ObservableList<T>findAll();
    boolean deleteById(S s);
    String getLastId();






}
