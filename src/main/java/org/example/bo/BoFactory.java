package org.example.bo;

import org.example.bo.custom.impl.*;
import org.example.util.BoType;

public class BoFactory {
    private static BoFactory instance;
    private BoFactory(){}
    public static BoFactory getInstance(){
        return instance!=null?instance:(instance=new BoFactory());
    }
    public <T extends SuperBo>T getBo(BoType type){
        switch (type){
            case CUSTOMER:return (T) new CustomerBoImpl();
            case USER:return (T) new UserBoImpl();
            case ORDER:return (T)new OrderBoImpl();
            case PRODUCT:return (T)new ProductBoImpl();
            case SUPPLIER:return (T)new SupplierBoImpl();
            case ORDER_DETAIL:return (T)new OrderDetailBoImpl();
        }
        return null;
    }
}
