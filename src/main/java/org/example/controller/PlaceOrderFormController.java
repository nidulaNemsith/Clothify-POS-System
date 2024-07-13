package org.example.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.*;
import org.example.dto.Order;
import org.example.dto.OrderHasItem;
import org.example.dto.Product;
import org.example.util.BoType;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;


public class PlaceOrderFormController implements Initializable {
    @FXML
    private JFXButton btnDeleteItem;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnCart;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnUpdate;
    @FXML
    private TableColumn<?, ?> colAmount;
    @FXML
    private TableColumn<?, ?> colCustomerId;
    @FXML
    private TableColumn<?, ?> colOrderId;
    @FXML
    private TableColumn<?, ?> colOrderId2;
    @FXML
    private TableColumn<?, ?> colProductIdItem;
    @FXML
    private TableColumn<?, ?> colQty;
    @FXML
    private TableColumn<?, ?> colQty2;
    @FXML
    private TableColumn<?, ?> colStaffId;
    @FXML
    private TableColumn<?, ?> colTotal;
    @FXML
    private Label lblAvailableQty;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblProductName;
    @FXML
    private Label lblQtyAlert;
    @FXML
    private Label lblSize;
    @FXML
    private JFXComboBox<String> optCustomerId;
    @FXML
    private JFXComboBox<String> optOrder;
    @FXML
    private JFXComboBox<String> optProductID;
    @FXML
    private JFXComboBox<String> optStaffId;
    @FXML
    private TableView<Order> tblOrder;
    @FXML
    private TableView<OrderHasItem> tblOrderItem;
    @FXML
    private TextField txtOrder;
    @FXML
    private TextField txtQty;
    @FXML
    private TextField txtTotal;

    private final OrderBoImpl orderBo= BoFactory.getInstance().getBo(BoType.ORDER);
    private final ProductBoImpl productBo=BoFactory.getInstance().getBo(BoType.PRODUCT);
    private final UserBoImpl userBo=BoFactory.getInstance().getBo(BoType.USER);
    private final CustomerBoImpl customerBo=BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private final OrderDetailBoImpl orderDetailBo=BoFactory.getInstance().getBo(BoType.ORDER_DETAIL);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStaffId.setCellValueFactory(new PropertyValueFactory<>("staffId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colOrderId2.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colProductIdItem.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colQty2.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        refreshOrderTable();
        setComboBoxValues();
        optStaffId.setItems(userBo.getUserId());
        optCustomerId.setItems(customerBo.getCustomerId());
    }
    public void btnExitOnAction(ActionEvent actionEvent) {
        Optional<ButtonType> exit = showAlert(
                Alert.AlertType.CONFIRMATION,
                "Exit",
                "Confirmation Required",
                "Are you sure you want to EXIT..?"
        );
        if (exit.isPresent()&&exit.get()==ButtonType.OK){
            System.exit(0);
        }

    }
    public void btnAddOnAction(ActionEvent actionEvent) {

        if (isAnyEmptyFieldForPlace()){
            showAlert(
                    Alert.AlertType.ERROR,
                    "ERROR",
                    "Form Incomplete",
                    "Fill in the Empty Fields..!!"
            );
        }else{
            Order order = new Order(
                    txtOrder.getText(),
                    optCustomerId.getValue(),
                    orderDetailBo.finalQty(txtOrder.getText()),
                    orderDetailBo.finalTotal(txtOrder.getText()),
                    optStaffId.getValue()
            );
            if (orderBo.save(order)){
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "PLACED",
                        "Success",
                        "Order Placed Successfully..!!"
                );
                finalizeOrder();
            }else{
                showAlert(
                        Alert.AlertType.ERROR,
                        "Place order failed",
                        "Order Failure",
                        "An error occurred while trying to place the order. Please try again."
                );
            }
        }
    }
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        Order order = orderBo.getOrder(txtOrder.getText());
        order.setQty(orderDetailBo.finalQty(txtOrder.getText()));
        order.setTotal(orderDetailBo.finalTotal(txtOrder.getText()));

        if (orderBo.update(order)) {
            showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "UPDATED",
                    "Update Status",
                    "Order Updated Successfully...!!"
            );
        }else{
            showAlert(
                    Alert.AlertType.ERROR,
                    "ERROR",
                    "Update Failure",
                    "Order not Updated..!"
            );
        }
        cartFormClear();
        refreshOrderTable();
    }
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        ObservableList<OrderHasItem> allByOrderId = orderDetailBo.getAllByOrderId(txtOrder.getText());

        Optional<ButtonType> deleteOrder = showAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Order",
                "Confirmation Required",
                "Are you sure you want to delete whole order..?"
        );

        if (deleteOrder.isPresent() && deleteOrder.get()==ButtonType.OK){
            for (OrderHasItem orderHasItem : allByOrderId) {
                productBo.increaseQtyById(orderHasItem.getProductId(), orderHasItem.getQty());
                orderDetailBo.delete(txtOrder.getText());
            }
            if(orderBo.delete(txtOrder.getText())){
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Removed",
                        "Order Removal",
                        "Order Removed Successfully..!"
                );
            }else{
                showAlert(
                        Alert.AlertType.ERROR,
                        "ERROR",
                        "Removal Failure",
                        "Order not removed..!"
                );
            }
            cartFormClear();
            refreshOrderTable();
            refreshOrderItemTable(txtOrder.getText());
            optProductID.setItems(null);
        }
    }
    public void btnDeleteItemOnAction(ActionEvent event) {

        Optional<ButtonType> deleteItem = showAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Item",
                "Confirmation Required",
                "Are you sure you want to delete this item..?"
        );

        if (deleteItem.isPresent() && deleteItem.get()==ButtonType.OK && orderDetailBo.deleteItem(txtOrder.getText(),optProductID.getValue())){
                productBo.increaseQtyById(optProductID.getValue(), Integer.parseInt(txtQty.getText()));
                showAlert(
                        Alert.AlertType.CONFIRMATION,
                        "REMOVED",
                        "Item Removal",
                        "Item Removed..!!"
                );
                cartFormClear();
                refreshOrderItemTable(txtOrder.getText());
            }

    }
    public void btnCartOnAction(ActionEvent actionEvent) {

        if (isAnyEmptyFieldForCart()){
            showAlert(
                    Alert.AlertType.ERROR,
                    "ERROR",
                    "Form Incomplete",
                    "Fill in the empty fields..!!"
            );
            cartFormClear();
            return;
        }

        String orderId=txtOrder.getText();
        String productId=optProductID.getValue();
        int qty= Integer.parseInt(txtQty.getText());
        double amount= Double.parseDouble(txtTotal.getText());
        boolean isPlaceOrder=optOrder.getValue().equals("Place Order");

        if (orderDetailBo.isProductInCart(orderId,productId)){
            handleExistingItem(orderId,productId,qty,amount,isPlaceOrder);
            cartFormClear();
        }else{
            handleNewProduct(orderId,productId,qty,amount);
            cartFormClear();
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
        if (txtOrder.getText()!=null){
            try {
                Order order = orderBo.getOrder(txtOrder.getText());
                tblOrderItem.setItems(orderDetailBo.getAllByOrderId(txtOrder.getText()));
                optStaffId.setValue(order.getStaffId());
                optCustomerId.setValue(order.getCustomerId());
                btnDeleteItem.setDisable(false);
                btnDelete.setDisable(false);
                btnUpdate.setDisable(false);
                btnCart.setDisable(false);

                if (optOrder.getValue().equals("Return Order")){
                    optProductID.setItems(orderDetailBo.getProductIds(txtOrder.getText()));
                }else{
                    optProductID.setItems(productBo.getProductId());
                }
            }catch (Exception e){
                tblOrderItem.getItems().clear();
                optStaffId.setValue(null);
                optCustomerId.setValue(null);
                optProductID.setItems(null);
                btnDeleteItem.setDisable(true);
                btnDelete.setDisable(true);
                btnUpdate.setDisable(true);
                btnCart.setDisable(true);
            }
        }else{
            btnDeleteItem.setDisable(false);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            btnCart.setDisable(false);
        }

    }
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/dashBoard-admin.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
            btnCart.setDisable(true);
            btnAdd.setDisable(true);
        }

    }
    public void optProductIdOnAction(ActionEvent actionEvent) {
        if (optProductID.getValue()!=null){
            Product product = productBo.getProduct(optProductID.getValue());
            lblProductName.setText(product.getName());
            lblSize.setText(product.getSize());
            lblAvailableQty.setText(String.valueOf(product.getQty()));
            lblPrice.setText(product.getPrice()+"");

            if (optOrder.getValue().equals("Update Order") || optOrder.getValue().equals("Return Order")){
                ObservableList<OrderHasItem> allByOrderId = orderDetailBo.getAllByOrderId(txtOrder.getText());
                allByOrderId.forEach(orderHasItem -> {
                    if (orderHasItem.getProductId().equals(optProductID.getValue())){
                        txtQty.setText(orderHasItem.getQty()+"");
                        txtTotal.setText(orderHasItem.getAmount()+"");
                    }
                });
            }
        }
    }
    private void handleExistingItem(String orderId, String productId, int qty, double amount, boolean isPlaceOrder){
        OrderHasItem orderItem = orderDetailBo.getOrderItem(orderId, productId);

        if (isPlaceOrder){
            orderItem.setQty(orderItem.getQty()+qty);
            orderItem.setAmount(orderItem.getAmount()+amount);
            productBo.decreaseQtyById(productId,qty);

            if (orderDetailBo.update(orderItem)){
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Item Added",
                        "Success",
                        "Item added to the order..!!"
                );
            }else{
                showAlert(
                        Alert.AlertType.ERROR,
                        "Item Add failed",
                        "Error Adding Item",
                        "An error occurred while trying to add the item. Please try again..!"
                );
            }
            refreshOrderItemTable(orderId);
        }else{
            productBo.increaseQtyById(productId,orderItem.getQty());
            orderItem.setQty(qty);
            orderItem.setAmount(amount);
            productBo.decreaseQtyById(productId,qty);

            if (orderDetailBo.update(orderItem)){
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Item Updated",
                        "Update Status",
                        "Item Updated..!"
                );
                refreshOrderItemTable(orderId);
            }else{
                showAlert(
                        Alert.AlertType.ERROR,
                        "Item Update Failed",
                        "Update Failure",
                        "Item not Updated..!"
                );
            }
        }
    }
    private void handleNewProduct(String orderId,String productId,int qty,double amount){
        OrderHasItem orderHasItem = new OrderHasItem(
                orderDetailBo.generateId(),
                orderId,
                productId,
                qty,
                amount
        );

        if (orderDetailBo.save(orderHasItem)){
            productBo.decreaseQtyById(productId,qty);
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Item added",
                    "Success",
                    "Item Added to the Cart..!"
            );
        }else{
            showAlert(
                    Alert.AlertType.ERROR,
                    "Item added failed",
                    "Error adding item",
                    "An error occurred while trying to add the item. Please try again..!"
            );
        }
        refreshOrderItemTable(orderId);
    }
    private void refreshOrderItemTable(String orderId){
        tblOrderItem.setItems(orderDetailBo.getAllByOrderId(orderId));
    }
    private void refreshOrderTable(){
        tblOrder.setItems(orderBo.getAll());
    }
    private boolean isAnyEmptyFieldForCart(){
        return optProductID.getValue()==null || txtQty.getText()==null;
    }
    private boolean isAnyEmptyFieldForPlace(){
        return optCustomerId.getValue()==null || optStaffId.getValue()==null || orderDetailBo.getAllByOrderId(txtOrder.getText()).isEmpty();
    }
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title,String header,String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }
    private void finalizeOrder(){
        txtOrder.setText(orderBo.generateId());
        orderDetailBo.generateId();
        refreshOrderTable();
        tblOrderItem.getItems().clear();
    }
    private void setComboBoxValues(){
        ObservableList<String> option= FXCollections.observableArrayList("Place Order","Update Order","Return Order");
        optOrder.setItems(option);
    }
    private void optAdd(){
        btnDelete.setVisible(false);
        btnDeleteItem.setVisible(false);
        btnUpdate.setVisible(false);
        btnAdd.setDisable(false);
        btnCart.setDisable(false);
        btnAdd.setVisible(true);
        btnCart.setVisible(true);
        optStaffId.setDisable(false);
        optCustomerId.setDisable(false);
        optProductID.setDisable(false);
        optProductID.setItems(productBo.getProductId());
        formClear();
        txtOrder.setText(orderBo.generateId());
        txtOrder.setEditable(false);
        txtQty.setEditable(true);

    }
    private void optUpdate(){
        btnDelete.setVisible(false);
        btnDeleteItem.setVisible(false);
        btnAdd.setVisible(false);
        btnCart.setVisible(true);
        btnUpdate.setVisible(true);
        optProductID.setDisable(false);
        optStaffId.setDisable(true);
        optCustomerId.setDisable(true);
        formClear();
        txtOrder.setEditable(true);
        txtQty.setEditable(true);
    }
    private void optDelete(){
        btnUpdate.setVisible(false);
        btnAdd.setVisible(false);
        btnCart.setVisible(false);
        btnDeleteItem.setVisible(true);
        btnDelete.setVisible(true);
        optProductID.setDisable(false);
        optCustomerId.setDisable(true);
        optStaffId.setDisable(true);
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
}
