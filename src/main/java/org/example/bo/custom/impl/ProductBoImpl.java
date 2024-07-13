package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.bo.custom.ProductBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.ProductDaoImpl;
import org.example.dto.Product;

import org.example.entity.ProductEntity;
import org.example.util.DaoType;

public class ProductBoImpl implements ProductBo{
    private ProductDaoImpl productDao= DaoFactory.getInstance().getDao(DaoType.PRODUCT);


    public boolean save(Product product){
        return productDao.save(new ObjectMapper().convertValue(product, ProductEntity.class));
    }
    public String generateId(){
        if (productDao.getLastId()==null){
            return "P0001";
        }
        int num = Integer.parseInt(productDao.getLastId().split("P")[1]);
        num++;
        return String.format("P%04d",num);
    }
    public Product getProduct(String id){
        ProductEntity productEntity = productDao.search(id);
        return new ObjectMapper().convertValue(productEntity,Product.class);
    }
    public boolean delete(String id){
        return productDao.deleteById(id);
    }
    public ObservableList<Product> getAll(){
        ObservableList<ProductEntity> productEntities = productDao.findAll();
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        productEntities.forEach(productEntity -> {
            productObservableList.add(new ObjectMapper().convertValue(productEntity,Product.class));
        });
        return productObservableList;
    }
    public boolean update(Product product){
        return productDao.update(new ObjectMapper().convertValue(product, ProductEntity.class));
    }
    public ObservableList<String> getProductId(){
        ObservableList<String> list=productDao.gettAllId();
        return list;
    }
    public boolean increaseQtyById(String id,int qty){
       return productDao.increaseQtyOfProduct(id,qty);
    }
    public boolean decreaseQtyById(String id,int qty){
        return productDao.decreaseQtyOfProduct(id,qty);
    }


}
