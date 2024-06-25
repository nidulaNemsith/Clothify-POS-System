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

public class ProductManageFormController implements Initializable {

    public JFXButton btnExit;
    public JFXButton btnAdd;
    public JFXComboBox optProduct;
    public TextField txtProduct;
    public TextField txtQty;
    public TextField txtPrice;
    public TableView tblProduct;
    public TextField txtProductName;
    public TableColumn colProductId;
    public TableColumn colProductName;
    public TableColumn colCategory;
    public TableColumn colSize;
    public TableColumn colQty;
    public TableColumn colSupplierId;
    public TableColumn colPrice;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXComboBox optSize;
    public JFXComboBox optCategory;
    public TextField txtSupplierId;
    public JFXButton backBtn;

    public void optCategoryOnAction(ActionEvent actionEvent) {
    }

    public void optSizeOnAction(ActionEvent actionEvent) {
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
    }

    public void txtProductIdOnReleased(KeyEvent keyEvent) {
    }

    public void optProductOnAction(ActionEvent actionEvent) {
        Object value = optProduct.getValue();
        System.out.println(value);
        if (value.equals("Add Product")){
            optAdd();
        } else if (value.equals("Update Product")){
            optUpdate();
        }else{
            optDelete();
        }
    }

    public void btnExitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setComboBoxValues();
        setCategoryComboBoxValues();
        setSizeComboBoxValues();

    }

    private void setComboBoxValues(){
        ObservableList<String> option= FXCollections.observableArrayList("Add Product","Update Product","Delete Product");
        optProduct.setItems(option);
    }
    private void setCategoryComboBoxValues(){
        ObservableList<String> option= FXCollections.observableArrayList("Gents","Ladies","Kids");
        optCategory.setItems(option);
    }
    private void setSizeComboBoxValues(){
        ObservableList<String> option= FXCollections.observableArrayList("Small-S","Medium-M","Large-L","Extra Large-XXL");
        optSize.setItems(option);
    }
    private void optAdd(){
        btnDelete.setVisible(false);
        btnUpdate.setVisible(false);
        btnAdd.setVisible(true);
        formClear();
        txtProduct.setEditable(false);
        txtProductName.setEditable(true);
        txtQty.setEditable(true);
        txtPrice.setEditable(true);
        txtSupplierId.setEditable(true);
        optSize.setDisable(false);
        optCategory.setDisable(false);
    }
    private void optUpdate(){
        btnDelete.setVisible(false);
        btnAdd.setVisible(false);
        btnUpdate.setVisible(true);
        formClear();
        txtProduct.setEditable(true);
        txtProductName.setEditable(true);
        txtQty.setEditable(true);
        txtPrice.setEditable(true);
        txtSupplierId.setEditable(true);
        optSize.setDisable(false);
        optCategory.setDisable(false);
    }
    private void optDelete(){
        btnUpdate.setVisible(false);
        btnAdd.setVisible(false);
        btnDelete.setVisible(true);
        formClear();
        txtProduct.setEditable(true);
        txtProductName.setEditable(false);
        txtQty.setEditable(false);
        txtPrice.setEditable(false);
        txtSupplierId.setEditable(false);
        optSize.setDisable(true);
        optCategory.setDisable(true);
    }
    private void formClear(){
        txtProduct.clear();
        txtProductName.clear();
        txtQty.clear();
        txtPrice.clear();
        txtSupplierId.clear();
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/dashBoard-admin.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void txtQtykeyReleased(KeyEvent keyEvent) {
    }

    public void txtPriceKeyReleased(KeyEvent keyEvent) {
    }
}
