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

public class StaffManageFormController implements Initializable {
    public JFXButton btnExit;
    public JFXButton btnAdd;
    public JFXComboBox optStaff;
    public TextField txtStaff;
    public TextField txtName;
    public TextField txtPhoneNumber;
    public TextField txtAddress;
    public TextField txtEmail;
    public TableView tblStaff;
    public TableColumn colStaffId;
    public TableColumn colPhoneNumber;
    public TableColumn colAddress;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public TableColumn colName;
    public TableColumn colEmail;
    public JFXButton btnBack;

    public void btnExitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }
    public void btnAddOnAction(ActionEvent actionEvent) {
        System.out.println("Add");
    }
    public void optStaffOnAction(ActionEvent actionEvent) {
        Object value = optStaff.getValue();
        System.out.println(value);
        if (value.equals("Add Staff")){
            optAdd();
        } else if (value.equals("Update Staff")){
            optUpdate();
        }else{
            optDelete();
        }
    }
    public void txtStaffIdOnReleased(KeyEvent keyEvent) {
    }
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        System.out.println("Delete");
    }
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        System.out.println("Update");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setComboBoxValues();
    }
    private void setComboBoxValues(){
        ObservableList<String>option= FXCollections.observableArrayList("Add Staff","Update Staff","Delete Staff");
        optStaff.setItems(option);
    }
    private void optAdd(){
        btnDelete.setVisible(false);
        btnUpdate.setVisible(false);
        btnAdd.setVisible(true);
        formClear();
        txtStaff.setEditable(false);
    }
    private void optUpdate(){
        btnDelete.setVisible(false);
        btnAdd.setVisible(false);
        btnUpdate.setVisible(true);
        formClear();
        txtStaff.setEditable(true);
    }
    private void optDelete(){
        btnUpdate.setVisible(false);
        btnAdd.setVisible(false);
        btnDelete.setVisible(true);
        formClear();
        txtStaff.setEditable(true);
        txtAddress.setEditable(false);
        txtName.setEditable(false);
        txtPhoneNumber.setEditable(false);
        txtEmail.setEditable(false);
    }
    private void formClear(){
        txtStaff.clear();
        txtAddress.clear();
        txtName.clear();
        txtPhoneNumber.clear();
        txtEmail.clear();
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/dashBoard-admin.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
