package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.bo.custom.OrderDetailBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.OrderDetailDaoImpl;
import org.example.dto.OrderHasItem;
import org.example.entity.OrderHasItemEntity;
import org.example.util.DaoType;
import java.util.List;

public class OrderDetailBoImpl implements OrderDetailBo {
    private OrderDetailDaoImpl orderDetailDao= DaoFactory.getInstance().getDao(DaoType.ORDER_DETAIL);

    public String generateId(){
        if (orderDetailDao.getLastId()==null){
            return "ORI0001";
        }
        int num = Integer.parseInt(orderDetailDao.getLastId().split("ORI")[1]);
        num++;
        return String.format("ORI%04d",num);
    }
    public boolean save(OrderHasItem orderHasItem){
        return orderDetailDao.save(new ObjectMapper().convertValue(orderHasItem, OrderHasItemEntity.class));
    }
    public boolean update(OrderHasItem orderHasItem){
        return orderDetailDao.update(new ObjectMapper().convertValue(orderHasItem, OrderHasItemEntity.class));
    }
    public double finalTotal(String orderId){
        double total = 0;
        List<Double> priceList = orderDetailDao.orderPrice(orderId);
        for (int i = 0; i < priceList.size(); i++) {
            total+=priceList.get(i);
        }
        return total;
    }
    public int finalQty(String orderId){
        int qty=0;
        List<Integer> qtyList = orderDetailDao.totalQty(orderId);
        for (int i = 0; i < qtyList.size(); i++) {
            qty+=qtyList.get(i);
        }
        return qty;
    }
    public ObservableList<OrderHasItem> getAll(){
        ObservableList<OrderHasItemEntity> orderHasItemEntities = orderDetailDao.findAll();
        ObservableList<OrderHasItem> observableArrayList = FXCollections.observableArrayList();
        orderHasItemEntities.forEach(orderHasItemEntity -> {
            observableArrayList.add(new ObjectMapper().convertValue(orderHasItemEntity,OrderHasItem.class));
        });
        return observableArrayList;
    }

    public ObservableList<OrderHasItem> getAllByOrderId(String orderId){
        ObservableList<OrderHasItemEntity> allByOrderId = orderDetailDao.getAllByOrderId(orderId);
        ObservableList<OrderHasItem> observableArrayList = FXCollections.observableArrayList();
        allByOrderId.forEach(orderHasItemEntity -> {
            observableArrayList.add(new ObjectMapper().convertValue(orderHasItemEntity, OrderHasItem.class));
        });
        return observableArrayList;
    }

    public ObservableList<String>getProductIds(String orderId){
        return orderDetailDao.getProductIdsByOrderId(orderId);
    }

    public OrderHasItem getOrderItem(String orderId,String productId){
        OrderHasItemEntity item = orderDetailDao.getItem(orderId, productId);
        return new ObjectMapper().convertValue(item,OrderHasItem.class);
    }

    public boolean isProductInCart(String orderId,String productId){
        for (OrderHasItemEntity orderHasItemEntity : orderDetailDao.getAllByOrderId(orderId)) {
            if (orderHasItemEntity.getProductId().equals(productId)) return true;
        }
        return false;
    }

    public String getOrderItemId(String orderId,String productId){
        return orderDetailDao.getOrderItemIdByProductId(orderId, productId);
    }

    public boolean deleteItem(String orderId,String productId){
        return orderDetailDao.deleteOrderItem(orderId, productId);
    }

    public boolean delete(String orderId){
        return orderDetailDao.deleteById(orderId);
    }


}
