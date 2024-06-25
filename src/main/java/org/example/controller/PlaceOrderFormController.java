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


public class PlaceOrderFormController implements Initializable {
    public JFXButton btnExit;
    public JFXButton btnAdd;
    public JFXComboBox optOrder;
    public TextField txtOrder;
    public TextField txtOrderName;
    public TextField txtQty;
    public TextField txtPrice;
    public TableView tblOrder;
    public TableColumn colProductId;
    public TableColumn colProductName;
    public TableColumn colCategory;
    public TableColumn colSize;
    public TableColumn colQty;
    public TableColumn colPrice;
    public TableColumn colStaffId;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXComboBox optSize;
    public JFXComboBox optCategory;
    public TextField txtStaffId;
    public JFXButton backBtn;

    public void btnExitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
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
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    public void optSizeOnAction(ActionEvent actionEvent) {
    }

    public void optCategoryOnAction(ActionEvent actionEvent) {
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/dashBoard-admin.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setComboBoxValues();
        setCategoryComboBoxValues();
        setSizeComboBoxValues();
    }

    private void optAdd(){
        btnDelete.setVisible(false);
        btnUpdate.setVisible(false);
        btnAdd.setVisible(true);
        formClear();
        txtOrder.setEditable(false);
        txtOrderName.setEditable(true);
        txtQty.setEditable(true);
        txtPrice.setEditable(true);
        txtStaffId.setEditable(true);
        optSize.setDisable(false);
        optCategory.setDisable(false);
    }
    private void optUpdate(){
        btnDelete.setVisible(false);
        btnAdd.setVisible(false);
        btnUpdate.setVisible(true);
        formClear();
        txtOrder.setEditable(true);
        txtOrderName.setEditable(true);
        txtQty.setEditable(true);
        txtPrice.setEditable(true);
        txtStaffId.setEditable(true);
        optSize.setDisable(false);
        optCategory.setDisable(false);
    }
    private void optDelete(){
        btnUpdate.setVisible(false);
        btnAdd.setVisible(false);
        btnDelete.setVisible(true);
        formClear();
        txtOrder.setEditable(true);
        txtOrderName.setEditable(false);
        txtQty.setEditable(false);
        txtPrice.setEditable(false);
        txtStaffId.setEditable(false);
        optSize.setDisable(true);
        optCategory.setDisable(true);
    }
    private void formClear(){
        txtOrder.clear();
        txtOrderName.clear();
        txtQty.clear();
        txtPrice.clear();
        txtStaffId.clear();
    }
    private void setComboBoxValues(){
        ObservableList<String> option= FXCollections.observableArrayList("Place Order","Update Order","Return Order");
        optOrder.setItems(option);
    }
    private void setCategoryComboBoxValues(){
        ObservableList<String> option= FXCollections.observableArrayList("Gents","Ladies","Kids");
        optCategory.setItems(option);
    }
    private void setSizeComboBoxValues(){
        ObservableList<String> option= FXCollections.observableArrayList("Small-S","Medium-M","Large-L","Extra Large-XXL");
        optSize.setItems(option);
    }
}
