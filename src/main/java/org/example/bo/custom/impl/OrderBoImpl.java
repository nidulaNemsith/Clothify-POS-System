package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.bo.custom.OrderBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.OrderDaoImpl;
import org.example.dto.Order;
import org.example.entity.OrderEntity;
import org.example.util.DaoType;

public class OrderBoImpl implements OrderBo {
    private OrderDaoImpl orderDao= DaoFactory.getInstance().getDao(DaoType.ORDER);

    public boolean save(Order order){
        return orderDao.save(new ObjectMapper().convertValue(order, OrderEntity.class));
    }
    public String generateId(){
        if (orderDao.getLastId()==null){
            return "OR0001";
        }
        int num = Integer.parseInt(orderDao.getLastId().split("OR")[1]);
        num++;
        return String.format("OR%04d",num);
    }
    public boolean delete(String id){
        return orderDao.deleteById(id);
    }
    public ObservableList<Order> getAll(){
        ObservableList<OrderEntity> orderEntities = orderDao.findAll();
        ObservableList<Order> orderObservableList = FXCollections.observableArrayList();
        orderEntities.forEach(orderEntity -> {
            orderObservableList.add(new ObjectMapper().convertValue(orderEntity,Order.class));
        });
        return orderObservableList;
    }
    public boolean update(Order order){
        return orderDao.update(new ObjectMapper().convertValue(order, OrderEntity.class));
    }
    public ObservableList<String> getOrderId(){
        ObservableList<String> list=orderDao.gettAllId();
        return list;
    }
    public Order getOrder(String id) {
        OrderEntity orderEntity = orderDao.search(id);
        return new ObjectMapper().convertValue(orderEntity,Order.class);
    }
}
