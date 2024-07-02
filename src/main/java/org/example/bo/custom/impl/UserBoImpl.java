package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.bo.custom.UserBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.UserDaoImpl;
import org.example.dto.Customer;
import org.example.dto.User;
import org.example.entity.UserEntity;
import org.example.util.DaoType;

public class UserBoImpl implements UserBo {

    private final UserDaoImpl userDao= DaoFactory.getInstance().getDao(DaoType.USER);

    public boolean save(User user){
        return userDao.save(new ObjectMapper().convertValue(user, UserEntity.class));
    }
    public String generateId(){
        if (userDao.getLastId()==null){
            return "U0001";
        }
        int num = Integer.parseInt(userDao.getLastId().split("U")[1]);
        num++;
        return String.format("U%04d",num);
    }
    public User getUser(String id){
        UserEntity userEntity = userDao.search(id);
        return new ObjectMapper().convertValue(userEntity,User.class);
    }
    public boolean delete(String id){
        return userDao.deleteById(id);
    }
    public ObservableList<User> getAll(){
        ObservableList<UserEntity> userEntities = userDao.findAll();
        ObservableList<User> userList = FXCollections.observableArrayList();
        userEntities.forEach(userEntity -> userList.add(new ObjectMapper().convertValue(userEntity,User.class)));
        return userList;
    }
    public boolean update(User user){
        return userDao.update(new ObjectMapper().convertValue(user, UserEntity.class));
    }
    public ObservableList<String> getUserId(){
        ObservableList<String> list=userDao.gettAllId();
        return list;
    }

}
