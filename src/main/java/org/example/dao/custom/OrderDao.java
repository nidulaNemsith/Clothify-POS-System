package org.example.dao.custom;

import org.example.dao.CrudDao;
import org.example.dto.Order;
import org.example.entity.OrderEntity;

public interface OrderDao extends CrudDao<OrderEntity,String> {
}
