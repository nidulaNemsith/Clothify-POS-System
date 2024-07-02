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
import org.example.bo.custom.impl.ProductBoImpl;
import org.example.bo.custom.impl.SupplierBoImpl;
import org.example.dto.Product;
import org.example.dto.User;
import org.example.util.BoType;

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
    public JFXButton backBtn;
    public JFXComboBox optSupplier;
    ProductBoImpl productBo= BoFactory.getInstance().getBo(BoType.PRODUCT);
    SupplierBoImpl supplierBo=BoFactory.getInstance().getBo(BoType.SUPPLIER);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        tblProduct.setItems(productBo.getAll());
        optSupplier.setItems(supplierBo.getSupplierId());
        setComboBoxValues();
        setCategoryComboBoxValues();
        setSizeComboBoxValues();
    }

    public void optCategoryOnAction(ActionEvent actionEvent) {

    }

    public void optSizeOnAction(ActionEvent actionEvent) {
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        productBo.delete(txtProduct.getText());
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DELETED");
        alert.setContentText("Product Deleted Successfully!!");
        alert.showAndWait();
        formClear();
        tblProduct.setItems(productBo.getAll());
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if (isTextFieldEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field");
            alert.setContentText("Fill the empty fields");
            alert.showAndWait();
        }else{
            Product product=new Product(
                    txtProduct.getText(),
                    txtProductName.getText(),
                    (String)optCategory.getValue(),
                    (String)optSize.getValue(),
                    Integer.parseInt(txtQty.getText()),
                    Double.parseDouble(txtPrice.getText()),
                    (String)optSupplier.getValue()
            );
            boolean isUpdate=productBo.update(product);
            Alert alert;
            if(isUpdate){
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("UPDATED");
                alert.setContentText("Product Updated Successfully!");
                alert.showAndWait();
                formClear();
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Product not Updated!");
                alert.showAndWait();
            }
            tblProduct.setItems(productBo.getAll());
        }
    }

    public void txtProductIdOnReleased(KeyEvent keyEvent) {
        try {
            Product getProduct = productBo.getProduct(txtProduct.getText());
            txtProductName.setText(getProduct.getProductName());
            txtPrice.setText(String.valueOf(getProduct.getPrice()));
            txtQty.setText(String.valueOf(getProduct.getQty()));
            optSupplier.setValue(getProduct.getSupplierID());
            optSize.setValue(getProduct.getSize());
            optCategory.setValue(getProduct.getCategory());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        } catch (Exception e) {
            txtProductName.setText(null);
            txtQty.setText(null);
            txtPrice.setText(null);
            optCategory.setValue(null);
            optSize.setValue(null);
            optSupplier.setValue(null);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
        }
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
        if (isTextFieldEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field");
            alert.setContentText("Fill the empty fields");
            alert.showAndWait();
        }else{
            Product product=new Product(
                    txtProduct.getText(),
                    txtProductName.getText(),
                    (String)optCategory.getValue(),
                    (String)optSize.getValue(),
                    Integer.parseInt(txtQty.getText()),
                    Double.parseDouble(txtPrice.getText()),
                    (String)optSupplier.getValue()
            );
            boolean isAdd=productBo.save(product);
            Alert alert;
            if(isAdd){
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("ADDED");
                alert.setContentText("Product Added Successfully!");
                alert.showAndWait();
                formClear();
                txtProduct.setText(productBo.generateId());
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Product not Added!");
                alert.showAndWait();
            }
            tblProduct.setItems(productBo.getAll());
        }
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

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/dashBoard-admin.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void txtQtykeyReleased(KeyEvent keyEvent){

    }

    public void txtPriceKeyReleased(KeyEvent keyEvent) {
    }

    private void optAdd(){
        btnDelete.setVisible(false);
        btnUpdate.setVisible(false);
        btnAdd.setVisible(true);
        formClear();
        txtProduct.setText(productBo.generateId());
        txtProduct.setEditable(false);
        txtProductName.setEditable(true);
        txtQty.setEditable(true);
        txtPrice.setEditable(true);
        optSize.setDisable(false);
        optCategory.setDisable(false);
        optSupplier.setDisable(false);
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
        optSize.setDisable(false);
        optCategory.setDisable(false);
        optSupplier.setDisable(false);
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
        optSize.setDisable(true);
        optCategory.setDisable(true);
        optSupplier.setDisable(true);
    }
    private void formClear(){
        txtProduct.setText(null);
        txtProductName.setText(null);
        txtQty.setText(null);
        txtPrice.setText(null);
    }
    private boolean isTextFieldEmpty(){
        return txtProductName.getText()==null || txtQty.getText()==null || txtPrice.getText()==null || optCategory.getValue()==null ||optSize.getValue()==null || optSupplier.getValue()==null;
    }


    public void optSupplierOnAction(ActionEvent actionEvent) {

    }
}
