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
import org.example.bo.custom.impl.SupplierBoImpl;
import org.example.dto.Supplier;
import org.example.util.BoType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SupplierManageFormController implements Initializable {
    public JFXButton btnExit;
    public JFXButton btnAdd;
    public JFXComboBox optSupplier;
    public TextField txtSupplier;
    public TextField txtName;
    public TextField txtPhoneNumber;
    public TextField txtEmail;
    public TextField txtAddress;
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
    private TableView<Supplier> tblSupplier;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXButton btnBack;

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
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field");
            alert.setContentText("Fill the empty fields");
            alert.showAndWait();
        }else{
            Supplier supplier=new Supplier(
                    txtSupplier.getText(),
                    txtName.getText(),
                    txtPhoneNumber.getText(),
                    txtEmail.getText(),
                    txtAddress.getText()
            );
            boolean isUpdate=supplierBo.update(supplier);
            Alert alert;
            if(isUpdate){
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("UPDATED");
                alert.setContentText("Supplier Updated Successfully!");
                alert.showAndWait();
                formClear();
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Supplier not Updated!");
                alert.showAndWait();
            }
            tblSupplier.setItems(supplierBo.getAll());
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        supplierBo.delete(txtSupplier.getText());
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DELETED");
        alert.setContentText("Supplier Deleted Successfully!!");
        alert.showAndWait();
        formClear();
        tblSupplier.setItems(supplierBo.getAll());
    }

    public void optSupplierOnAction(ActionEvent actionEvent) {
        Object value = optSupplier.getValue();
        System.out.println(value);
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
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field");
            alert.setContentText("Fill the empty fields");
            alert.showAndWait();
        }else{
           Supplier supplier=new Supplier(
                    txtSupplier.getText(),
                    txtName.getText(),
                    txtPhoneNumber.getText(),
                    txtEmail.getText(),
                    txtAddress.getText()
            );
            boolean isAdd=supplierBo.save(supplier);
            Alert alert;
            if(isAdd){
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("ADDED");
                alert.setContentText("Supplier Added Successfully!");
                alert.showAndWait();
                formClear();
                txtSupplier.setText(supplierBo.generateId());
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Supplier not Added!");
                alert.showAndWait();
            }
            tblSupplier.setItems(supplierBo.getAll());
        }
    }

    public void btnExitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }



    private void setComboBoxValues(){
        ObservableList<String> option= FXCollections.observableArrayList("Add Supplier","Update Supplier","Delete Supplier");
        optSupplier.setItems(option);
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
}
