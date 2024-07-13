package org.example.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.dao.custom.OrderDao;
import org.example.entity.OrderEntity;
import org.example.entity.ProductEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class OrderDaoImpl implements OrderDao {
    @Override
    public OrderEntity search(String s) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM orders WHERE id=:id");
        query.setParameter("id",s);
        OrderEntity orderEntity = (OrderEntity) query.uniqueResult();
        session.close();
        return orderEntity;
    }

    @Override
    public boolean save(OrderEntity orderEntity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.persist(orderEntity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(OrderEntity orderEntity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.update(orderEntity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(OrderEntity orderEntity) {
        return false;
    }

    @Override
    public ObservableList<OrderEntity> findAll() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        List<OrderEntity> list = session.createQuery("FROM orders").list();
        session.close();

        ObservableList<OrderEntity> orderEntityList = FXCollections.observableArrayList();

        list.forEach( orderEntity-> {
            orderEntityList.add(orderEntity);
        });
        return orderEntityList;
    }
    @Override
    public boolean deleteById(String id) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("DELETE FROM orders WHERE id=:id");
        query.setParameter("id",id);
        int i = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return i>0;

    }
    @Override
    public String getLastId() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("SELECT id FROM orders ORDER BY id DESC LIMIT 1");
        return (String) query.uniqueResult();
    }
    public ObservableList<String>gettAllId(){
        Session session=HibernateUtil.getSession();
        session.getTransaction().begin();
        List <String> list = session.createQuery("SELECT id FROM order").list();
        session.close();
        ObservableList<String> idList=FXCollections.observableArrayList();
        list.forEach(s -> {
            idList.add(s);
        });
        return idList;
    }
}
