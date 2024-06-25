package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.bo.custom.SupplierBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.SupplierDaoImpl;
import org.example.dto.Supplier;
import org.example.entity.SupplierEntity;
import org.example.util.DaoType;

public class SupplierBoImpl implements SupplierBo {
    private SupplierDaoImpl supplierDao= DaoFactory.getInstance().getDao(DaoType.SUPPLIER);

    public boolean save(Supplier supplier){
        return supplierDao.save(new ObjectMapper().convertValue(supplier, SupplierEntity.class));
    }
    public String generateId(){
        if (supplierDao.getLastId()==null){
            return "S0001";
        }
        int num = Integer.parseInt(supplierDao.getLastId().split("S")[1]);
        num++;
        return String.format("S%04d",num);
    }
    public Supplier getSupplier(String id){
        SupplierEntity supplierEntity = supplierDao.search(id);
        return new ObjectMapper().convertValue(supplierEntity,Supplier.class);
    }
    public boolean delete(String id){
        return supplierDao.deleteById(id);
    }
    public ObservableList<Supplier> getAll(){
        ObservableList<SupplierEntity> supplierEntities = supplierDao.findAll();
        ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
        supplierEntities.forEach(supplierEntity -> {
            supplierList.add(new ObjectMapper().convertValue(supplierEntity,Supplier.class));
        });
        return supplierList;
    }
    public boolean update(Supplier supplier){
        return supplierDao.update(new ObjectMapper().convertValue(supplier, SupplierEntity.class));
    }
}
