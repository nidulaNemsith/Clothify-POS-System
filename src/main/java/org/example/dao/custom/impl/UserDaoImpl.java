package org.example.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.dao.custom.CustomerDao;
import org.example.dao.custom.UserDao;
import org.example.entity.CustomerEntity;
import org.example.entity.SupplierEntity;
import org.example.entity.UserEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public UserEntity search(String s) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM user WHERE id=:id");
        query.setParameter("id",s);
        UserEntity userEntity = (UserEntity) query.uniqueResult();
        session.close();
        return userEntity;
    }

    @Override
    public boolean save(UserEntity userEntity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.persist(userEntity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(UserEntity userEntity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("UPDATE user " +
                "SET name =:name,address =:address,email =:email,phoneNumber =:phoneNumber WHERE id =:id");
        query.setParameter("name",userEntity.getName());
        query.setParameter("address",userEntity.getAddress());
        query.setParameter("email",userEntity.getEmail());
        query.setParameter("phoneNumber",userEntity.getAddress());
        query.setParameter("id",userEntity.getId());

        int i = query.executeUpdate();
        session.getTransaction().commit();
        session.close();

        return i>0;
    }

    @Override
    public boolean delete(UserEntity userEntity) {
        return false;
    }

    @Override
    public ObservableList<UserEntity> findAll() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        List<UserEntity> list = session.createQuery("FROM user").list();
        session.close();

        ObservableList<UserEntity> userEntityList = FXCollections.observableArrayList();

        list.forEach(userEntity -> {
            userEntityList.add(userEntity);
        });
        return userEntityList;
    }

    @Override
    public boolean deleteById(String id) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("DELETE FROM user WHERE id=:id");
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
        Query query = session.createQuery("SELECT id FROM user ORDER BY id DESC LIMIT 1");
        return (String) query.uniqueResult();
    }
}
