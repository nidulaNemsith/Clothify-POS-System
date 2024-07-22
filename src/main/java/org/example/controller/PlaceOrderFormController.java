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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.*;
import org.example.dto.*;
import org.example.util.BoType;

import javax.mail.MessagingException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.List;


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
    private JFXButton btnReport;
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
    private Label lblItemCount;
    @FXML
    private Label lblNetTotal;
    @FXML
    private JFXComboBox<String> optCustomerId;
    @FXML
    private JFXComboBox<String> optOrder;
    @FXML
    private JFXComboBox<String> optProductID;
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

    private String orderIdF;

    private final OrderBoImpl orderBo= BoFactory.getInstance().getBo(BoType.ORDER);
    private final ProductBoImpl productBo=BoFactory.getInstance().getBo(BoType.PRODUCT);
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
    public void btnAddOnAction(ActionEvent actionEvent) throws JRException, MessagingException {
        String customerName=customerBo.getCustomer(optCustomerId.getValue()).getName();
        String mailBody="Dear "+customerName+",\n" +
                "\n" +
                "Thank you for your purchase! Please find your order details below:";

        if (isAnyEmptyFieldForPlace()){
            showAlert(
                    Alert.AlertType.ERROR,
                    "ERROR",
                    "Form Incomplete",
                    "Fill in the Empty Fields..!!"
            );
            return;
        }

        Order order = new Order(
                txtOrder.getText(),
                optCustomerId.getValue(),
                orderDetailBo.finalQty(txtOrder.getText()),
                orderDetailBo.finalTotal(txtOrder.getText()),
                CurrentLogInUserController.getInstance().getId(),
                getDate(),
                getTime()
        );
        if (orderBo.save(order)){
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "PLACED",
                    "Success",
                    "Order Placed Successfully..!!"
            );
            generateOrderReceipt(mailBody);
            orderIdF=txtOrder.getText();
        }else{
            showAlert(
                    Alert.AlertType.ERROR,
                    "Place order failed",
                    "Order Failure",
                    "An error occurred while trying to place the order. Please try again."
            );
        }
        finalizeOrder();
    }
    public void btnUpdateOnAction(ActionEvent actionEvent) throws JRException, MessagingException {
        Order order = orderBo.getOrder(txtOrder.getText());
        String customerName=customerBo.getCustomer(optCustomerId.getValue()).getName();

        String mailBody="Dear " + customerName + ",\n\n" +
                "Thank you for your purchase! Your order has been updated.\n" +
                "Please find your updated order details below:\n\n";

        if (txtOrder.getText().isEmpty() || txtOrder.getText()==null){
            showAlert(
                    Alert.AlertType.ERROR,
                    "ERROR",
                    "Invalid Input",
                    "The order ID field is empty. Please enter a valid order ID."
            );
            return;
        }
        if (order==null){
            showAlert(
                    Alert.AlertType.ERROR,
                    "ERROR",
                    "Order Not Found",
                    "The entered order ID does not exist. Please check and try again."
            );
            return;
        }
        order.setQty(orderDetailBo.finalQty(txtOrder.getText()));
        order.setTotal(orderDetailBo.finalTotal(txtOrder.getText()));

        if (orderBo.update(order)) {
            showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "UPDATED",
                    "Update Status",
                    "Order Updated Successfully...!!"
            );
            generateOrderReceipt(mailBody);
        }else{
            showAlert(
                    Alert.AlertType.ERROR,
                    "ERROR",
                    "Update Failure",
                    "Order not Updated..!"
            );
        }
        finalizeUpdate();
    }
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        ObservableList<OrderHasItem> allByOrderId = orderDetailBo.getAllByOrderId(txtOrder.getText());

        if (txtOrder.getText().isEmpty() || txtOrder.getText()==null){
            showAlert(
                    Alert.AlertType.ERROR,
                    "ERROR",
                    "Invalid Input",
                    "The order ID field is empty. Please enter a valid order ID."
            );
            return;
        }
        if (orderBo.getOrder(txtOrder.getText())==null){
            showAlert(
                    Alert.AlertType.ERROR,
                    "ERROR",
                    "Order Not Found",
                    "The entered order ID does not exist. Please check and try again."
            );
            return;
        }

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
            finalizeDelete();
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
        }else{
            handleNewProduct(orderId,productId,qty,amount);
        }
        displayTotalAndQty();
        cartFormClear();
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
                optCustomerId.setValue(order.getCustomerId());
                btnDeleteItem.setDisable(false);
                btnDelete.setDisable(false);
                btnUpdate.setDisable(false);
                btnCart.setDisable(false);
                displayTotalAndQty();
                if (optOrder.getValue().equals("Return Order")){
                    optProductID.setItems(orderDetailBo.getProductIds(txtOrder.getText()));
                }else{
                    optProductID.setItems(productBo.getProductId());
                }
            }catch (Exception e){
                tblOrderItem.getItems().clear();
                optCustomerId.setValue(null);
                optProductID.setItems(null);
                btnDeleteItem.setDisable(true);
                btnDelete.setDisable(true);
                btnUpdate.setDisable(true);
                btnCart.setDisable(true);
                lblItemCount.setText("00");
                lblNetTotal.setText("0000.00");
            }
        }else{
            btnDeleteItem.setDisable(false);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            btnCart.setDisable(false);
        }

    }
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        String role = CurrentLogInUserController.getInstance().getRole();
        if (role == null) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Navigation Error",
                    "No Role Found",
                    "The user role is not defined. Cannot navigate to the appropriate page."
            );
            return;
        }

        switch (role){
            case "Admin":loadAdminPage(actionEvent);break;
            case "Staff":loadStaffPage(actionEvent);break;
            default:
                showAlert(
                        Alert.AlertType.ERROR,
                        "Navigation Error",
                        "Invalid Role",
                        "The user role is not recognized. Cannot navigate to the appropriate page."
                );
                break;
        }
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
    public void btnViewReportOnAction(ActionEvent actionEvent) throws IOException {

        if (optOrder.getValue().equals("Update Order") || optOrder.getValue().equals("Return Order")){
            orderIdF=txtOrder.getText();
        }

        File file = new File("E:\\clothify-pos\\src\\main\\resources" +
                "\\reportPdf\\orderReport\\"+orderIdF+".pdf");

        if (file.exists()){
            if (Desktop.isDesktopSupported()){
                Desktop.getDesktop().open(file);
            }else {

            }
        }else {
            new Alert(Alert.AlertType.ERROR,"Report Not Found..!!!").show();
        }
    }
    private void generateOrderReceipt(String mailBody) throws JRException, MessagingException {

        Customer customer = customerBo.getCustomer(optCustomerId.getValue());

        String path = "E:\\clothify-pos\\src\\main\\resources\\report\\Order_Invoice.jrxml";
        String savePath = "E:\\clothify-pos\\src\\main\\resources\\reportPdf\\orderReport\\" + txtOrder.getText() + ".pdf";


        Map<String, Object> parameters = new HashMap<>();
        JasperReport report = JasperCompileManager.compileReport(path);

        parameters.put("customerId", customer.getCustomerId());
        parameters.put("customerName", customer.getName());
        parameters.put("customerNumber", customer.getPhoneNumber());
        parameters.put("customerEmail", customer.getEmail());
        parameters.put("orderID", txtOrder.getText());
        parameters.put("itemCount", orderDetailBo.finalQty(txtOrder.getText()));
        parameters.put("netTotal", orderDetailBo.finalTotal(txtOrder.getText()));

        CurrentLogInUserController instance = CurrentLogInUserController.getInstance();

        parameters.put("empID", instance.getId());
        parameters.put("empName", instance.getName());

        List<ProductOnCart> productOnCarts = new ArrayList<>();

        for (OrderHasItem orderHasItem : orderDetailBo.getAllByOrderId(txtOrder.getText())) {
            OrderHasItem orderItem = orderDetailBo.getOrderItem(orderHasItem.getOrderId(), orderHasItem.getProductId());
            Product product = productBo.getProduct(orderItem.getProductId());

            ProductOnCart productOnCart = new ProductOnCart(
                    orderItem.getProductId(),
                    product.getName(),
                    orderItem.getQty(),
                    orderItem.getAmount()
            );
            productOnCarts.add(productOnCart);
            System.out.println(productOnCart);

        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productOnCarts);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, savePath);
        productOnCarts.clear();

        File file = new File(savePath);
        orderBo.sendEmail(customer.getEmail(),mailBody,file);

        showAlert(
                Alert.AlertType.INFORMATION,
                "Receipt Sent",
                "Email Sent Successfully",
                "The receipt has been sent to your email successfully."
        );

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
        return optProductID.getValue()==null || txtQty.getText().isEmpty();
    }
    private boolean isAnyEmptyFieldForPlace(){
        return optCustomerId.getValue()==null || orderDetailBo.getAllByOrderId(txtOrder.getText()).isEmpty();
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
        lblNetTotal.setText("00");
        lblItemCount.setText("0000.00");
        optCustomerId.setValue(null);
    }
    private void finalizeUpdate(){
        cartFormClear();
        refreshOrderTable();
        lblNetTotal.setText("00");
        lblItemCount.setText("0000.00");
        optCustomerId.setValue(null);
    }
    private void finalizeDelete(){
        lblItemCount.setText("00");
        lblNetTotal.setText("0000.00");
        optCustomerId.setValue(null);
        cartFormClear();
        txtOrder.setText(null);
        optProductID.setItems(null);
        refreshOrderTable();
        refreshOrderItemTable(txtOrder.getText());
    }
    private void displayTotalAndQty(){
        lblItemCount.setText(String.valueOf(orderDetailBo.finalQty(txtOrder.getText())));
        lblNetTotal.setText(String.valueOf(orderDetailBo.finalTotal(txtOrder.getText())));
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
        optCustomerId.setDisable(false);
        optProductID.setDisable(false);
        optProductID.setItems(productBo.getProductId());
        btnReport.setVisible(true);
        formClear();
        txtOrder.setText(orderBo.generateId());
        refreshOrderItemTable(txtOrder.getText());
        txtOrder.setEditable(false);
        txtQty.setEditable(true);
        optCustomerId.setValue(null);

    }
    private void optUpdate(){
        btnDelete.setVisible(false);
        btnDeleteItem.setVisible(false);
        btnAdd.setVisible(false);
        btnCart.setVisible(true);
        btnCart.setDisable(false);
        btnUpdate.setVisible(true);
        optProductID.setDisable(false);
        optCustomerId.setDisable(true);
        btnReport.setVisible(true);
        formClear();
        txtOrder.setEditable(true);
        txtQty.setEditable(true);
        optCustomerId.setValue(null);
    }
    private void optDelete(){
        btnUpdate.setVisible(false);
        btnAdd.setVisible(false);
        btnCart.setVisible(false);
        btnDeleteItem.setVisible(true);
        btnDelete.setVisible(true);
        optProductID.setDisable(false);
        optCustomerId.setDisable(true);
        btnReport.setVisible(false);
        formClear();
        txtOrder.setEditable(true);
        txtQty.setEditable(false);
        optCustomerId.setValue(null);
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
    private void loadAdminPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/dashBoard-admin.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private void loadStaffPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/dashBoard-staff.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }
    private String getDate() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(date);
    }
    private String getTime(){
        LocalTime now = LocalTime.now();
        return now.getHour()+":"+now.getMinute()+":"+now.getSecond();
    }
}
