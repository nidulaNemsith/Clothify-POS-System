package org.example.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.dao.custom.ProductDao;
import org.example.dto.OrderHasItem;
import org.example.entity.ProductEntity;
import org.example.entity.SupplierEntity;
import org.example.entity.UserEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ProductDaoImpl implements ProductDao {
    @Override
    public ProductEntity search(String s) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM product WHERE id=:id");
        query.setParameter("id",s);
        ProductEntity productEntity = (ProductEntity) query.uniqueResult();
        session.close();
        return productEntity;
    }

    @Override
    public boolean save(ProductEntity productEntity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.persist(productEntity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(ProductEntity productEntity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("UPDATE product " +
                "SET name =:name,category =:category,size =:size,qty =:qty,price=:price,supplierID=:supplierID WHERE id =:id");
        query.setParameter("name",productEntity.getName());
        query.setParameter("category",productEntity.getCategory());
        query.setParameter("size",productEntity.getSize());
        query.setParameter("qty",productEntity.getQty());
        query.setParameter("price",productEntity.getPrice());
        query.setParameter("supplierID",productEntity.getSupplierID());
        query.setParameter("id",productEntity.getProductId());

        int i = query.executeUpdate();
        session.getTransaction().commit();
        session.close();

        return i>0;
    }

    public boolean increaseQtyOfProduct(String id, int qty) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("UPDATE product SET qty=qty + :qty WHERE id=:id");
        query.setParameter("qty",qty);
        query.setParameter("id",id);
        int i = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return i>0;
    }
    public boolean decreaseQtyOfProduct(String id, int qty) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("UPDATE product SET qty=qty - :qty WHERE id=:id");
        query.setParameter("qty",qty);
        query.setParameter("id",id);
        int i = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return i>0;
    }
    public boolean increaseQty(ObservableList<OrderHasItem>productIdList){
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("UPDATE product SET qty=qty+:qty WHERE id=:id");

        productIdList.forEach(orderHasItem -> {
            query.setParameter("qty",orderHasItem.getQty());
            query.setParameter("id",orderHasItem.getProductId());
            query.executeUpdate();
        });
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public boolean decreaseQty(ObservableList<OrderHasItem>productIdList){
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("UPDATE product SET qty=qty-:qty WHERE id=:id");

        productIdList.forEach(orderHasItem -> {
            query.setParameter("qty",orderHasItem.getQty());
            query.setParameter("id",orderHasItem.getProductId());
            query.executeUpdate();
        });
        session.getTransaction().commit();
        session.close();
        return true;
    }


    @Override
    public boolean delete(ProductEntity productEntity) {
        return false;
    }

    @Override
    public ObservableList<ProductEntity> findAll() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        List<ProductEntity> list = session.createQuery("FROM product").list();
        session.close();

        ObservableList<ProductEntity> productEntityList = FXCollections.observableArrayList();

        list.forEach(productEntity -> {
            productEntityList.add(productEntity);
        });
        return productEntityList;
    }

    @Override
    public boolean deleteById(String id) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("DELETE FROM product WHERE id=:id");
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
        Query query = session.createQuery("SELECT id FROM product ORDER BY id DESC LIMIT 1");
        return (String) query.uniqueResult();
    }
    public ObservableList<String>gettAllId(){
        Session session=HibernateUtil.getSession();
        session.getTransaction().begin();
        List <String> list = session.createQuery("SELECT id FROM product").list();
        session.close();
        ObservableList<String> idList=FXCollections.observableArrayList();
        list.forEach(s -> {
            idList.add(s);
        });
        return idList;
    }
}
