package org.example.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.dao.DaoFactory;
import org.example.dao.custom.OrderDetailDao;
import org.example.entity.OrderEntity;
import org.example.entity.OrderHasItemEntity;
import org.example.entity.UserEntity;
import org.example.util.DaoType;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class OrderDetailDaoImpl implements OrderDetailDao {


    @Override
    public OrderHasItemEntity search(String s) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM order_has_item WHERE id=:id");
        query.setParameter("id",s);
        OrderHasItemEntity orderHasItemEntity = (OrderHasItemEntity) query.uniqueResult();
        session.close();
        return orderHasItemEntity;
    }

    @Override
    public boolean save(OrderHasItemEntity orderHasItemEntity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.persist(orderHasItemEntity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(OrderHasItemEntity orderHasItemEntity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.update(orderHasItemEntity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(OrderHasItemEntity orderHasItemEntity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.delete(orderHasItemEntity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public ObservableList<OrderHasItemEntity> findAll() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        List<OrderHasItemEntity> list = session.createQuery("FROM order_has_item").list();
        session.close();

        ObservableList<OrderHasItemEntity> orderHasItemEntityList = FXCollections.observableArrayList();

        list.forEach(orderHasItemEntity -> {
            orderHasItemEntityList.add(orderHasItemEntity);
        });
        return orderHasItemEntityList;
    }

    public OrderHasItemEntity getItem(String orderId,String productId){
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        return (OrderHasItemEntity) session.createQuery("FROM order_has_item WHERE orderId=:orderId AND productId=:productId")
                .setParameter("orderId",orderId)
                .setParameter("productId",productId)
                .uniqueResult();
    }

    public ObservableList<OrderHasItemEntity> getAllByOrderId(String orderId){
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM order_has_item WHERE orderId=:id");
        query.setParameter("id",orderId);
        List <OrderHasItemEntity> list = query.list();
        session.close();

        ObservableList<OrderHasItemEntity> orderHasItemEntities = FXCollections.observableArrayList();

        list.forEach(orderHasItemEntity -> {
            orderHasItemEntities.add(orderHasItemEntity);
        });

        return  orderHasItemEntities;
    }

    public ObservableList<String>getProductIdsByOrderId(String orderId){
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("SELECT productId FROM order_has_item WHERE orderId=:id");
        query.setParameter("id",orderId);
        List <String>list = query.list();
        session.close();
        ObservableList<String> productIds = FXCollections.observableArrayList();
        list.forEach(s -> {
            productIds.add(s);
        });
        return productIds;
    }

    public List<Double>orderPrice(String orderId){
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("SELECT amount FROM order_has_item WHERE orderId=:id");
        query.setParameter("id",orderId);
        List <Double>list = query.list();
        session.close();
        return list;
    }
    public List<Integer>totalQty(String orderId){
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("SELECT qty FROM order_has_item WHERE orderId=:id");
        query.setParameter("id",orderId);
        List<Integer>list = query.list();
        session.close();
        return list;
    }

    public String getOrderItemIdByProductId(String orderId,String productId){
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery(" SELECT id FROM order_has_item WHERE orderId=:orderId AND productId=:productId");
        query.setParameter("orderId",orderId);
        query.setParameter("productId",productId);
        return (String) query
                .uniqueResult();
    }

    public boolean deleteOrderItem(String orderId,String productId){
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("DELETE FROM order_has_item WHERE orderId=:orderId AND productId=:productId");
        query.setParameter("orderId",orderId);
        query.setParameter("productId",productId);
        int i = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return i>0;
    }

    @Override
    public boolean deleteById(String orderId) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("DELETE FROM order_has_item WHERE orderId=:orderId");
        query.setParameter("orderId",orderId);
        int i = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return i>0;
    }

    @Override
    public String getLastId() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("SELECT id FROM order_has_item ORDER BY id DESC LIMIT 1");
        return (String) query.uniqueResult();
    }


}
