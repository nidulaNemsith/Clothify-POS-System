package org.example.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.dao.custom.SupplierDao;
import org.example.entity.CustomerEntity;
import org.example.entity.SupplierEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class SupplierDaoImpl implements SupplierDao {
    @Override
    public SupplierEntity search(String s) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM supplier WHERE id=:id");
        query.setParameter("id",s);
        SupplierEntity supplierEntity = (SupplierEntity) query.uniqueResult();
        session.close();
        return supplierEntity;
    }

    @Override
    public boolean save(SupplierEntity supplierEntity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.persist(supplierEntity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(SupplierEntity supplierEntity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("UPDATE supplier " +
                "SET name =:name,address =:address,email =:email,phoneNumber =:phoneNumber WHERE id =:id");
        query.setParameter("name",supplierEntity.getName());
        query.setParameter("address",supplierEntity.getAddress());
        query.setParameter("email",supplierEntity.getEmail());
        query.setParameter("phoneNumber",supplierEntity.getPhoneNumber());
        query.setParameter("id",supplierEntity.getSupplierId());

        int i = query.executeUpdate();
        session.getTransaction().commit();
        session.close();

        return i>0;
    }

    @Override
    public boolean delete(SupplierEntity supplierEntity) {
        return false;
    }

    @Override
    public ObservableList<SupplierEntity> findAll() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        List<SupplierEntity> list = session.createQuery("FROM supplier").list();
        session.close();

        ObservableList<SupplierEntity> supplierEntityList = FXCollections.observableArrayList();

        list.forEach(supplierEntity -> {
            supplierEntityList.add(supplierEntity);
        });
        return supplierEntityList;
    }

    @Override
    public boolean deleteById(String id) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("DELETE FROM supplier WHERE id=:id");
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
        Query query = session.createQuery("SELECT id FROM supplier ORDER BY id DESC LIMIT 1");
        return (String) query.uniqueResult();
    }
}
