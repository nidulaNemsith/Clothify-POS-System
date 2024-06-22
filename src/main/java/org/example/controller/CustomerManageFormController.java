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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

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
    public TableColumn colCustomerId;
    public TableColumn colName;
    public TableColumn colPhoneNumber;
    public TableColumn colEmail;
    public TableColumn colAddress;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public TableView tblCustomer;
    public JFXButton btnBack;

    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
    }

    public void btnExitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
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

    }
    private void setComboBoxValues(){
        ObservableList<String> option= FXCollections.observableArrayList("Add Customer","Update Customer","Delete Customer");
        optCustomer.setItems(option);
    }
    private void optAdd(){
        btnDelete.setVisible(false);
        btnUpdate.setVisible(false);
        btnAdd.setVisible(true);
        formClear();
        txtCustomer.setEditable(false);
    }
    private void optUpdate(){
        btnDelete.setVisible(false);
        btnAdd.setVisible(false);
        btnUpdate.setVisible(true);
        formClear();
        txtCustomer.setEditable(true);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setComboBoxValues();
    }


    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/dashBoard-admin.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
