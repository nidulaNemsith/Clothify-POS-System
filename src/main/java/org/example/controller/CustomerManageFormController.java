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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.CustomerBoImpl;
import org.example.dto.Customer;
import org.example.util.BoType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerManageFormController implements Initializable {
    public JFXButton btnExit;
    public JFXButton btnAdd;
    public JFXComboBox optCustomer;
    public TextField txtCustomer;
    public TextField txtName;
    public TextField txtPhoneNumber;
    public TextField txtEmail;
    public TextField txtAddress;
    @FXML
    private TableColumn<?, ?> colAddress;
    @FXML
    private TableColumn<?, ?> colCustId;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colPhoneNumber;
    @FXML
    private TableView<Customer> tblCustomer;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXButton btnBack;
    CustomerBoImpl customerBoImpl=BoFactory.getInstance().getBo(BoType.CUSTOMER);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tblCustomer.setItems(customerBoImpl.getAll());
        setComboBoxValues();
    }
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        customerBoImpl.delete(txtCustomer.getText());
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DELETED");
        alert.setContentText("Customer Deleted Successfully!!");
        alert.showAndWait();
        formClear();
        tblCustomer.setItems(customerBoImpl.getAll());
    }
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if (isTextFieldEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field");
            alert.setContentText("Fill the empty fields");
            alert.showAndWait();
        }else{
            Customer customer=new Customer(
                    txtCustomer.getText(),
                    txtName.getText(),
                    txtPhoneNumber.getText(),
                    txtEmail.getText(),
                    txtAddress.getText()
            );
            boolean isUpdate=customerBoImpl.update(customer);
            Alert alert;
            if(isUpdate){
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("UPDATED");
                alert.setContentText("Customer Updated Successfully!");
                alert.showAndWait();
                formClear();
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Customer not Updated!");
                alert.showAndWait();
            }
            tblCustomer.setItems(customerBoImpl.getAll());
        }
    }
    public void btnExitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }
    public void btnAddOnAction(ActionEvent actionEvent) {
        if (isTextFieldEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field");
            alert.setContentText("Fill the empty fields");
            alert.showAndWait();
        }else{
            Customer customer=new Customer(
                    txtCustomer.getText(),
                    txtName.getText(),
                    txtPhoneNumber.getText(),
                    txtEmail.getText(),
                    txtAddress.getText()
            );
            boolean isAdd=customerBoImpl.save(customer);
            Alert alert;
            if(isAdd){
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("ADDED");
                alert.setContentText("Customer Added Successfully!");
                alert.showAndWait();
                formClear();
                txtCustomer.setText(customerBoImpl.generateId());
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Customer not Added!");
                alert.showAndWait();
            }
            tblCustomer.setItems(customerBoImpl.getAll());
        }
    }
    public void optCustomerOnAction(ActionEvent actionEvent) {
        Object value = optCustomer.getValue();
        System.out.println(value);
        if (value.equals("Add Customer")){
            optAdd();
        } else if (value.equals("Update Customer")){
            optUpdate();
        }else{
            optDelete();
        }
    }
    public void txtCustomerIdOnReleased(KeyEvent keyEvent) {
        try {
            Customer getCustomer = customerBoImpl.getCustomer(txtCustomer.getText());
            txtAddress.setText(getCustomer.getAddress());
            txtEmail.setText(getCustomer.getEmail());
            txtName.setText(getCustomer.getName());
            txtPhoneNumber.setText(getCustomer.getPhoneNumber());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        } catch (Exception e) {
            txtName.setText(null);
            txtPhoneNumber.setText(null);
            txtEmail.setText(null);
            txtAddress.setText(null);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
        }
    }
    private void setComboBoxValues(){
        ObservableList<String> option= FXCollections.observableArrayList("Add Customer","Update Customer","Delete Customer");
        optCustomer.setItems(option);
    }
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/dashBoard-admin.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private void optAdd(){
        btnDelete.setVisible(false);
        btnUpdate.setVisible(false);
        btnAdd.setVisible(true);
        btnAdd.setDisable(false);
        txtCustomer.setEditable(false);
        formClear();
        txtCustomer.setText(customerBoImpl.generateId());
        txtAddress.setEditable(true);
        txtName.setEditable(true);
        txtPhoneNumber.setEditable(true);
        txtEmail.setEditable(true);
    }
    private void optUpdate(){
        btnDelete.setVisible(false);
        btnAdd.setVisible(false);
        btnUpdate.setVisible(true);
        formClear();
        txtCustomer.setEditable(true);
        txtAddress.setEditable(true);
        txtName.setEditable(true);
        txtPhoneNumber.setEditable(true);
        txtEmail.setEditable(true);
    }
    private void optDelete(){
        btnUpdate.setVisible(false);
        btnAdd.setVisible(false);
        btnDelete.setVisible(true);
        formClear();
        txtCustomer.setEditable(true);
        txtAddress.setEditable(false);
        txtName.setEditable(false);
        txtPhoneNumber.setEditable(false);
        txtEmail.setEditable(false);
    }
    private void formClear(){
        txtCustomer.clear();
        txtAddress.clear();
        txtName.clear();
        txtPhoneNumber.clear();
        txtEmail.clear();
    }
    private boolean isTextFieldEmpty(){
        return txtAddress.getText().isEmpty() || txtName.getText().isEmpty() || txtPhoneNumber.getText().isEmpty() || txtEmail.getText().isEmpty();
    }

}
