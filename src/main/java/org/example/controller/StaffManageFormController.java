package org.example.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
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
import org.example.dto.User;
import org.example.util.BoType;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
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
            return;
        }

        if (isExistEmailAndPhoneNumber()){
            showAlert(
                    Alert.AlertType.ERROR,
                    "User Already Exists",
                    "Duplicate Entry",
                    "A user with this email or phone number already exists. " +
                            "Please use a different email or phone number."
            );
            return;
        }
        if (!userBo.isValidEmail(txtEmail.getText())){
            showAlert(
                    Alert.AlertType.ERROR,
                    "Login Error",
                    "Invalid Email",
                    "Please enter a valid email."
            );
            txtEmail.setText(null);
            return;
        }
        User user=new User(
                txtStaff.getText(),
                txtName.getText(),
                txtEmail.getText(),
                txtPhoneNumber.getText(),
                userBo.hashPassword(generatePassword()),
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
            try {
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Password Sent",
                        "Check Your Email",
                        "Your password has been sent to your email address. Please check your inbox."
                );
                userBo.sendEmail(txtEmail.getText(),mailBody(),"Password");

            } catch (MessagingException e) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Password Send Error",
                        "Email Not Sent",
                        "There was an error sending your password to your email address. Please try again."
                );
            }
            formClear();
            txtStaff.setText(userBo.generateId());
        }else{
            showAlert(
                    Alert.AlertType.ERROR,
                    "Staff Member Add Failed",
                    "Error Adding Staff Member",
                    "An error occurred while trying to add the member. Please try again."
            );
        }
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
            return;
        }
        if (isExistEmailAndPhoneNumber()){
            showAlert(
                    Alert.AlertType.ERROR,
                    "User Already Exists",
                    "Duplicate Entry",
                    "A user with this email or phone number already exists. " +
                            "Please use a different email or phone number."
            );
            return;
        }

        User user = userBo.getUser(txtStaff.getText());
        user.setName(txtName.getText());
        user.setEmail(txtEmail.getText());
        user.setAddress(txtAddress.getText());
        user.setPhoneNumber(txtPhoneNumber.getText());

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
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        String role = CurrentLogInUserController.getInstance().getRole();

        switch (role){
            case "Admin":loadAdminPage(actionEvent);break;
            case "Staff":loadStaffPage(actionEvent);break;
            default:break;
        }
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
    private static String generatePassword() {
        Random random=new Random();
        int p = random.nextInt(99999999) + 10000000;
        System.out.println(p);
        return Integer.toString(p);
    }
    private String mailBody(){
        return "Dear "+txtName.getText()+","+
                "\n\nYour password is: "+"["+generatePassword()+"]"+
                "\n\nPlease log in and change your password as soon as possible.\nThank you,\n[Clothify Store]";

    }
    private boolean isExistEmailAndPhoneNumber(){
        return userBo.getUserByEmail(txtEmail.getText())!=null ||
                userBo.getUserByPhoneNumber(txtPhoneNumber.getText())!=null;
    }
}
