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
import org.example.bo.custom.impl.UserBoImpl;
import org.example.dto.Supplier;
import org.example.dto.User;
import org.example.util.BoType;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StaffManageFormController implements Initializable {
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnUpdate;
    @FXML
    private TableColumn<?, ?> colAddress;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colPhoneNumber;
    @FXML
    private TableColumn<?, ?> colStaffId;
    @FXML
    private JFXComboBox<String> optStaff;
    @FXML
    private TableView<User> tblStaff;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private TextField txtStaff;

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
        if (isTextFieldEmpty()){
            showAlert(
                    Alert.AlertType.ERROR,
                    "Empty Field",
                    "Form Incomplete",
                    "Fill in the empty fields"
            );
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
            if(userBo.save(user)){
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Staff Member Added",
                        "Success",
                        "Staff Member Added Successfully..!!"
                );
                txtStaff.setText(userBo.generateId());
            }else{
                showAlert(
                        Alert.AlertType.ERROR,
                        "Staff Member Add Failed",
                        "Error Adding Staff Member",
                        "An error occurred while trying to add the member. Please try again."
                );
            }
            formClear();
            tblStaff.setItems(userBo.getAll());
        }

    }
    public void optStaffOnAction(ActionEvent actionEvent) {
        Object value = optStaff.getValue();
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
        Optional<ButtonType> delete = showAlert(
                Alert.AlertType.CONFIRMATION,
                "DELETE",
                "Confirmation Required",
                "Are you sure you want to delete this staff member..?"
        );
        if (delete.isPresent()&&delete.get()==ButtonType.OK && userBo.delete(txtStaff.getText())){
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "DELETE",
                    "Member Removal",
                    "Staff Member Removed Successfully..!!"
            );
        }else{
            showAlert(
                    Alert.AlertType.ERROR,
                    "ERROR",
                    "Removal Failure",
                    "Staff Member not removed..!!"
            );
        }
        formClear();
        tblStaff.setItems(userBo.getAll());
    }
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if (isTextFieldEmpty()){
            showAlert(
                    Alert.AlertType.ERROR,
                    "Empty Field",
                    "Form Incomplete",
                    "Fill in the empty fields"
            );
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
            if(userBo.update(user)){
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "UPDATED",
                        "Update Status",
                        "Member Updated Successfully..!"
                );
            }else{
                showAlert(
                        Alert.AlertType.ERROR,
                        "ERROR",
                        "Update Failure",
                        "Member not Updated..!"
                );
            }
            formClear();
            tblStaff.setItems(userBo.getAll());
        }
    }
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/dashBoard-admin.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private void setComboBoxValues(){
        optStaff.setItems(FXCollections.observableArrayList(
                "Add Staff",
                "Update Staff",
                "Delete Staff"
        ));
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
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }


}
