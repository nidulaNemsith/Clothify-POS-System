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
import org.example.bo.custom.impl.SupplierBoImpl;
import org.example.dto.Supplier;
import org.example.util.BoType;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierManageFormController implements Initializable {
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnExit;
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
    private TableColumn<?, ?> colSupplierId;
    @FXML
    private JFXComboBox<String> optSupplier;
    @FXML
    private TableView<Supplier> tblSupplier;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private TextField txtSupplier;

    SupplierBoImpl  supplierBo=BoFactory.getInstance().getBo(BoType.SUPPLIER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tblSupplier.setItems(supplierBo.getAll());
        setComboBoxValues();
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
            Supplier supplier=new Supplier(
                    txtSupplier.getText(),
                    txtName.getText(),
                    txtPhoneNumber.getText(),
                    txtEmail.getText(),
                    txtAddress.getText()
            );

            if(supplierBo.update(supplier)){
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "UPDATED",
                        "Update Status",
                        "Supplier Updated Successfully..!"
                );
                formClear();
            }else{
                showAlert(
                        Alert.AlertType.ERROR,
                        "ERROR",
                        "Update Failure",
                        "Supplier not Updated..!"
                );
            }
            tblSupplier.setItems(supplierBo.getAll());
        }
    }
    public void btnDeleteOnAction(ActionEvent actionEvent) {

        Optional<ButtonType> delete = showAlert(
                Alert.AlertType.CONFIRMATION,
                "DELETE",
                "Confirmation Required",
                "Are you sure you want to delete this supplier..?"
        );

        if (delete.isPresent() && delete.get()==ButtonType.OK && supplierBo.delete(txtSupplier.getText())){
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "DELETE",
                        "Customer Removal",
                        "Supplier Removed Successfully..!!"
                );
        }else{
            showAlert(
                    Alert.AlertType.ERROR,
                    "ERROR",
                    "Removal Failure",
                    "Supplier not removed..!!"
            );
        }
        formClear();
        tblSupplier.setItems(supplierBo.getAll());

    }
    public void optSupplierOnAction(ActionEvent actionEvent) {
        Object value = optSupplier.getValue();
        if (value.equals("Add Supplier")){
            optAdd();
        } else if (value.equals("Update Supplier")){
            optUpdate();
        }else{
            optDelete();
        }
    }
    public void txtSupplierIdOnReleased(KeyEvent keyEvent) {
        try {
            Supplier getSupplier = supplierBo.getSupplier(txtSupplier.getText());
            txtAddress.setText(getSupplier.getAddress());
            txtEmail.setText(getSupplier.getEmail());
            txtName.setText(getSupplier.getName());
            txtPhoneNumber.setText(getSupplier.getPhoneNumber());
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

        Supplier supplier=new Supplier(
                txtSupplier.getText(),
                txtName.getText(),
                txtPhoneNumber.getText(),
                txtEmail.getText(),
                txtAddress.getText()
        );
        if(supplierBo.save(supplier)){
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Supplier Added",
                    "Success",
                    "Supplier Added Successfully..!!"
            );
            formClear();
            txtSupplier.setText(supplierBo.generateId());
        }else{
            showAlert(
                    Alert.AlertType.ERROR,
                    "Supplier Add Failed",
                    "Error Adding Supplier",
                    "An error occurred while trying to add the suppler. Please try again."
            );
        }
        tblSupplier.setItems(supplierBo.getAll());
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
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        String role = CurrentLogInUserController.getInstance().getRole();

        switch (role){
            case "Admin":loadAdminPage(actionEvent);break;
            case "Staff":loadStaffPage(actionEvent);break;
            default:break;
        }
    }
    private void setComboBoxValues(){
        optSupplier.setItems(FXCollections.observableArrayList(
                "Add Supplier",
                "Update Supplier",
                "Delete Supplier"
        ));
    }
    private void optAdd(){
        btnDelete.setVisible(false);
        btnUpdate.setVisible(false);
        btnAdd.setVisible(true);
        btnAdd.setDisable(false);
        txtSupplier.setEditable(false);
        formClear();
        txtSupplier.setText(supplierBo.generateId());
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
        txtSupplier.setText(null);
        txtSupplier.setEditable(true);
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
        txtSupplier.setText(null);
        txtSupplier.setEditable(true);
        txtAddress.setEditable(false);
        txtName.setEditable(false);
        txtPhoneNumber.setEditable(false);
        txtEmail.setEditable(false);
    }
    private void formClear(){
        txtSupplier.setText(null);
        txtAddress.setText(null);
        txtName.setText(null);
        txtPhoneNumber.setText(null);
        txtEmail.setText(null);
    }
    private boolean isTextFieldEmpty(){
        return txtAddress.getText().isEmpty() || txtName.getText().isEmpty() || txtPhoneNumber.getText().isEmpty() || txtEmail.getText().isEmpty();
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

}
