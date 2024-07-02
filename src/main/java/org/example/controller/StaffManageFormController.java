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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.UserBoImpl;
import org.example.dto.Supplier;
import org.example.dto.User;
import org.example.util.BoType;

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

    UserBoImpl userBo= BoFactory.getInstance().getBo(BoType.USER);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colStaffId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStaff.setItems(userBo.getAll());
        setComboBoxValues();
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
            User user=new User(
                    txtStaff.getText(),
                    txtName.getText(),
                    txtEmail.getText(),
                    txtPhoneNumber.getText(),
                    "0000",
                    "Staff",
                    txtAddress.getText()
            );
            boolean isAdd=userBo.save(user);
            Alert alert;
            if(isAdd){
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("ADDED");
                alert.setContentText("Staff Member Added Successfully!");
                alert.showAndWait();
                formClear();
                txtStaff.setText(userBo.generateId());
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("User not Added!");
                alert.showAndWait();
            }
            tblStaff.setItems(userBo.getAll());
        }

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
        try {
            User getUser = userBo.getUser(txtStaff.getText());
            txtAddress.setText(getUser.getAddress());
            txtEmail.setText(getUser.getEmail());
            txtName.setText(getUser.getName());
            txtPhoneNumber.setText(getUser.getPhoneNumber());
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
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        userBo.delete(txtStaff.getText());
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DELETED");
        alert.setContentText("Staff Member Deleted Successfully!!");
        alert.showAndWait();
        formClear();
        tblStaff.setItems(userBo.getAll());
    }
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if (isTextFieldEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field");
            alert.setContentText("Fill the empty fields");
            alert.showAndWait();
        }else{
            User user=new User(
                    txtStaff.getText(),
                    txtName.getText(),
                    txtEmail.getText(),
                    txtPhoneNumber.getText(),
                    "0000",
                    "Staff",
                    txtAddress.getText()
            );
            boolean isUpdate=userBo.update(user);
            Alert alert;
            if(isUpdate){
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("UPDATED");
                alert.setContentText("Staff Updated Successfully!");
                alert.showAndWait();
                formClear();
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Staff not Updated!");
                alert.showAndWait();
            }
            tblStaff.setItems(userBo.getAll());
        }
    }

    private void setComboBoxValues(){
        ObservableList<String>option= FXCollections.observableArrayList("Add Staff","Update Staff","Delete Staff");
        optStaff.setItems(option);
    }
    private void optAdd(){
        btnDelete.setVisible(false);
        btnUpdate.setVisible(false);
        btnAdd.setVisible(true);
        txtStaff.setEditable(false);
        formClear();
        txtStaff.setText(userBo.generateId());
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
        txtStaff.setText(null);
        txtStaff.setEditable(true);
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
        txtStaff.setText(null);
        txtStaff.setEditable(true);
        txtAddress.setEditable(false);
        txtName.setEditable(false);
        txtPhoneNumber.setEditable(false);
        txtEmail.setEditable(false);
    }
    private void formClear(){
        txtStaff.setText(null);
        txtAddress.setText(null);
        txtName.setText(null);
        txtPhoneNumber.setText(null);
        txtEmail.setText(null);
    }
    private boolean isTextFieldEmpty(){
        return txtAddress.getText()==null || txtName.getText()==null || txtPhoneNumber.getText()==null || txtEmail.getText()==null;
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/dashBoard-admin.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
