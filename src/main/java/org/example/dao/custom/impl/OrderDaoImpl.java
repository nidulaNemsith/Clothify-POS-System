package org.example.dao.custom.impl;

import javafx.collections.ObservableList;
import org.example.dao.custom.OrderDao;
import org.example.entity.OrderEntity;

public class OrderDaoImpl implements OrderDao {
    @Override
    public OrderEntity search(String s) {
        return null;
    }

    @Override
    public boolean save(OrderEntity orderEntity) {
        return false;
    }

    @Override
    public boolean update(OrderEntity orderEntity) {
        return false;
    }

    @Override
    public boolean delete(OrderEntity orderEntity) {
        return false;
    }

    @Override
    public ObservableList<OrderEntity> findAll() {
        return null;
    }

    @Override
    public boolean deleteById(String s) {
        return false;
    }

    @Override
    public String getLastId() {
        return null;
    }
}
