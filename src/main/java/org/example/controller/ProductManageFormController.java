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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.ProductBoImpl;
import org.example.bo.custom.impl.SupplierBoImpl;
import org.example.dto.Product;
import org.example.dto.User;
import org.example.util.BoType;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductManageFormController implements Initializable {
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btnUpdate;
    @FXML
    private TableColumn<?, ?> colCategory;
    @FXML
    private TableColumn<?, ?> colPrice;
    @FXML
    private TableColumn<?, ?> colProductId;
    @FXML
    private TableColumn<?, ?> colProductName;
    @FXML
    private TableColumn<?, ?> colQty;
    @FXML
    private TableColumn<?, ?> colSize;
    @FXML
    private TableColumn<?, ?> colSupplierId;
    @FXML
    private JFXComboBox<String> optCategory;
    @FXML
    private JFXComboBox<String> optProduct;
    @FXML
    private JFXComboBox<String> optSize;
    @FXML
    private JFXComboBox<String> optSupplier;
    @FXML
    private TableView<Product> tblProduct;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtProduct;
    @FXML
    private TextField txtProductName;
    @FXML
    private TextField txtQty;

    ProductBoImpl productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
    SupplierBoImpl supplierBo=BoFactory.getInstance().getBo(BoType.SUPPLIER);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
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
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Optional<ButtonType> delete = showAlert(
                Alert.AlertType.CONFIRMATION,
                "DELETE",
                "Confirmation Required",
                "Are you sure you want to delete this product..?"
        );
        if (delete.isPresent() && delete.get()==ButtonType.OK && productBo.delete(txtProduct.getText())){
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "DELETE",
                        "Product Removal",
                        "Product Removed Successfully..!!"
                );
        }else{
            showAlert(
                    Alert.AlertType.ERROR,
                    "ERROR",
                    "Removal Failure",
                    "Product not removed..!!"
            );
        }
        formClear();
        tblProduct.setItems(productBo.getAll());
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
            Product product=new Product(
                    txtProduct.getText(),
                    txtProductName.getText(),
                    optCategory.getValue(),
                    optSize.getValue(),
                    Integer.parseInt(txtQty.getText()),
                    Double.parseDouble(txtPrice.getText()),
                    optSupplier.getValue()
            );
            if(productBo.update(product)){
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "UPDATED",
                        "Update Status",
                        "Product Updated Successfully..!"
                );
            }else{
                showAlert(
                        Alert.AlertType.ERROR,
                        "ERROR",
                        "Update Failure",
                        "Product not Updated..!"
                );
            }
            formClear();
            tblProduct.setItems(productBo.getAll());
        }
    }
    public void txtProductIdOnReleased(KeyEvent keyEvent) {
        try {
            Product getProduct = productBo.getProduct(txtProduct.getText());
            txtProductName.setText(getProduct.getName());
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
            Product product=new Product(
                    txtProduct.getText(),
                    txtProductName.getText(),
                    optCategory.getValue(),
                    optSize.getValue(),
                    Integer.parseInt(txtQty.getText()),
                    Double.parseDouble(txtPrice.getText()),
                    optSupplier.getValue()
            );

            if(productBo.save(product)){
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Product Added",
                        "Success",
                        "Product Added Successfully..!!"
                );
                txtProduct.setText(productBo.generateId());
            }else{
                showAlert(
                        Alert.AlertType.ERROR,
                        "Product Add Failed",
                        "Error Adding Product",
                        "An error occurred while trying to add the product. Please try again."
                );
            }
            formClear();
            tblProduct.setItems(productBo.getAll());
        }
    }
    private void setComboBoxValues(){
        optProduct.setItems(FXCollections.observableArrayList(
                "Add Product",
                "Update Product",
                "Delete Product"
        ));
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
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

}
