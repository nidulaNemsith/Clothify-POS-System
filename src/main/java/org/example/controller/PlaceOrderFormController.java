package org.example.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.*;
import org.example.dto.Order;
import org.example.dto.OrderHasItem;
import org.example.dto.Product;
import org.example.util.BoType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PlaceOrderFormController implements Initializable {
    public JFXButton btnExit;
    public JFXButton btnAdd;
    public JFXComboBox optOrder;
    public TextField txtOrder;
    public TextField txtQty;
    public TableView tblOrder;
    public TableColumn colProductId;
    public TableColumn colQty;
    public TableColumn colStaffId;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXButton backBtn;
    public JFXButton btnCart;
    public JFXComboBox optStaffId;
    public JFXComboBox optProductID;

    public TableView tblOrderItem;
    public TableColumn colProductId1;
    public TableColumn colCategory1;
    public TableColumn colSize1;
    public TableColumn colProductName1;
    public TableColumn colQty1;
    public TableColumn colPrice1;
    public TextField txtTotal;
    public JFXComboBox optCustomerId;
    public TableColumn colTotal;
    public TableColumn colStaffId1;
    public Label lblProductName;
    public Label lblAvailableQty;
    public Label lblPrice;
    public Label lblSize;
    public Label lblQtyAlert;
    public TableColumn colOrderId;
    public TableColumn colOrderItemId;
    public TableColumn colOrderId2;
    public TableColumn colProductIdItem;
    public TableColumn colQty2;
    public TableColumn colAmount;
    public TableColumn colCustomerId;

    private OrderBoImpl orderBo= BoFactory.getInstance().getBo(BoType.ORDER);
    private ProductBoImpl productBo=BoFactory.getInstance().getBo(BoType.PRODUCT);
    private UserBoImpl userBo=BoFactory.getInstance().getBo(BoType.USER);
    private CustomerBoImpl customerBo=BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private OrderDetailBoImpl orderDetailBo=BoFactory.getInstance().getBo(BoType.ORDER_DETAIL);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStaffId.setCellValueFactory(new PropertyValueFactory<>("staffId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("custID"));
        colOrderItemId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOrderId2.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colProductIdItem.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colQty2.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tblOrder.setItems(orderBo.getAll());
        //tblOrderItem.setItems(orderDetailBo.getAll());
        setComboBoxValues();
        optProductID.setItems(productBo.getProductId());
        optStaffId.setItems(userBo.getUserId());
        optCustomerId.setItems(customerBo.getCustomerId());

    }
    public void btnExitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void btnAddOnAction(ActionEvent actionEvent) {

        if (optCustomerId.getValue()==null || optStaffId.getValue()==null){
            Alert alert;
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Fill the Empty Fields..!");
            alert.showAndWait();
        }else{
            Order order = new Order(
                    txtOrder.getText(),
                    optCustomerId.getValue().toString(),
                    orderDetailBo.finalQty(txtOrder.getText()),
                    orderDetailBo.FinalTotal(txtOrder.getText()),
                    optStaffId.getValue().toString()
            );
            boolean save = orderBo.save(order);
            if (save){
                Alert alert;
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("PLACED");
                alert.setContentText("Order Placed Successfully..!");
                alert.showAndWait();
                txtOrder.setText(orderBo.generateId());
                orderDetailBo.generateId();
                tblOrder.setItems(orderBo.getAll());
                tblOrderItem.getItems().clear();
            }
        }

    }
    public void btnCartOnAction(ActionEvent actionEvent) {

        OrderHasItem orderHasItem=new OrderHasItem(
                orderDetailBo.generateId(),
                txtOrder.getText(),
                optProductID.getValue().toString(),
                Integer.parseInt(txtQty.getText()),
                Double.parseDouble(txtTotal.getText())
        );
        boolean isAdd = orderDetailBo.save(orderHasItem);
        if (isAdd){
            Alert alert;
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ADDED");
            alert.setContentText("Item Added to the Cart..!");
            alert.showAndWait();
            cartFormClear();
            orderDetailBo.generateId();
            tblOrderItem.setItems(orderDetailBo.getAllByOrderId(txtOrder.getText()));
        }
    }
    public void optOrderOnAction(ActionEvent actionEvent) {
        Object value = optOrder.getValue();
        if (value.equals("Place Order")){
            optAdd();
        } else if (value.equals("Update Order")){
            optUpdate();
        }else{
            optDelete();
        }
    }
    public void txtOrderIdOnReleased(KeyEvent keyEvent) {
        try {
            Order order = orderBo.getOrder(txtOrder.getText());
            tblOrderItem.setItems(orderDetailBo.getAllByOrderId(txtOrder.getText()));
            optStaffId.setValue(order.getStaffId());
            optCustomerId.setValue(order.getCustID());
        }catch (Exception e){
            tblOrderItem.getItems().clear();
            optStaffId.setValue(null);
            optCustomerId.setValue(null);
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }
    
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/dashBoard-admin.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private void setComboBoxValues(){
        ObservableList<String> option= FXCollections.observableArrayList("Place Order","Update Order","Return Order");
        optOrder.setItems(option);
    }

    public void txtQtyOnAction(ActionEvent actionEvent) {
    }

    public void txtQtyOnKEyReleased(KeyEvent keyEvent) {

        try{
            int qty = Integer.parseInt(txtQty.getText());
            int availableQty=Integer.parseInt(lblAvailableQty.getText());

            if (qty<=availableQty){
                    double total=qty*Double.parseDouble(lblPrice.getText());
                    txtTotal.setText(Double.toString(total));
                    lblQtyAlert.setVisible(false);
                    btnCart.setDisable(false);
                    btnAdd.setDisable(false);
            }else{
                lblQtyAlert.setVisible(true);
                txtTotal.setText(null);
                btnCart.setDisable(true);
                btnAdd.setDisable(true);
            }
        }catch (NumberFormatException e){
                txtTotal.setText(null);
        }

    }

    public void optStaffIdOnAction(ActionEvent actionEvent) {
    }

    public void optProductIdOnAction(ActionEvent actionEvent) {
        if (optProductID.getValue()!=null){
            Product product = productBo.getProduct(optProductID.getValue().toString());
            lblProductName.setText(product.getName());
            lblSize.setText(product.getSize());
            lblAvailableQty.setText(String.valueOf(product.getQty()));
            lblPrice.setText(product.getPrice()+"");
        }
    }

    public void txtOrderOnAction(ActionEvent actionEvent) {
    }

    private void optAdd(){
        btnDelete.setVisible(false);
        btnUpdate.setVisible(false);
        btnAdd.setDisable(false);
        btnCart.setDisable(false);
        optStaffId.setDisable(false);
        optCustomerId.setDisable(false);
        optProductID.setDisable(false);
        formClear();
        txtOrder.setText(orderBo.generateId());
        txtOrder.setEditable(false);
        txtQty.setEditable(true);

    }
    private void optUpdate(){
        btnDelete.setVisible(false);
        btnAdd.setVisible(false);
        btnCart.setVisible(true);
        btnUpdate.setVisible(true);
        formClear();
        txtOrder.setEditable(true);
        txtQty.setEditable(true);
    }
    private void optDelete(){
        btnUpdate.setVisible(false);
        btnAdd.setVisible(false);
        btnCart.setVisible(false);
        btnDelete.setVisible(true);
        formClear();
        txtOrder.setEditable(true);
        txtQty.setEditable(false);
    }
    private void formClear(){
        txtOrder.clear();
        txtQty.clear();
        txtTotal.clear();
    }
    private void cartFormClear(){
        optProductID.setValue(null);
        lblProductName.setText(null);
        lblPrice.setText(null);
        lblSize.setText(null);
        lblAvailableQty.setText(null);
        txtQty.clear();
        txtTotal.clear();
    }

    public void optCustomerIdOnAction(ActionEvent actionEvent) {
    }
}
