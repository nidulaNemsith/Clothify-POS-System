package org.example.dao.custom;

import org.example.dao.CrudDao;
import org.example.entity.CustomerEntity;
import org.example.entity.UserEntity;

public interface UserDao extends CrudDao<UserEntity,String> {
}
