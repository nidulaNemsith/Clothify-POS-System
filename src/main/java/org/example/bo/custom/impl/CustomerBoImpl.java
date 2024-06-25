package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.bo.custom.CustomerBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.CustomerDaoImpl;
import org.example.dto.Customer;
import org.example.entity.CustomerEntity;
import org.example.util.DaoType;

public class CustomerBoImpl implements CustomerBo{

    private CustomerDaoImpl customerDao=DaoFactory.getInstance().getDao(DaoType.CUSTOMER);

    public boolean save(Customer customer){
        return customerDao.save(new ObjectMapper().convertValue(customer, CustomerEntity.class));
    }
    public String generateId(){
        if (customerDao.getLastId()==null){
            return "C0001";
        }
        int num = Integer.parseInt(customerDao.getLastId().split("C")[1]);
        num++;
        return String.format("C%04d",num);
    }
    public Customer getCustomer(String id){
        CustomerEntity customerEntity = customerDao.search(id);
        return new ObjectMapper().convertValue(customerEntity,Customer.class);
    }
    public boolean delete(String id){
        return customerDao.deleteById(id);
    }
    public ObservableList<Customer>getAll(){
        ObservableList<CustomerEntity> customerEntities = customerDao.findAll();
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        customerEntities.forEach(customerEntity -> {
            customerList.add(new ObjectMapper().convertValue(customerEntity,Customer.class));
        });
        return customerList;
    }
    public boolean update(Customer customer){
        return customerDao.update(new ObjectMapper().convertValue(customer, CustomerEntity.class));
    }
}
