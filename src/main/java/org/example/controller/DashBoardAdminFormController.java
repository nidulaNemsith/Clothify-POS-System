package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.EventObject;
import java.util.ResourceBundle;


public class DashBoardAdminFormController implements Initializable {

    public JFXButton btnExit;
    public JFXButton btnStaff;
    public JFXButton btnProduct;
    public JFXButton btnOrder;
    public JFXButton btnSuppliers;
    public JFXButton btnReport;
    public JFXButton btnLogOut;
    public Label lblTime;
    public Label lblDate;
    public Label lblName;
    public JFXButton btnCustomer;

    public void btnLogOutOnAction(ActionEvent actionEvent) {
    }

    public void btnReportOnAction(ActionEvent actionEvent) {
    }

    public void btnSuppliersOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/supplier-manage-form.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void btnProductOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/product-manage-form.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void btnStaffAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/staff-manage-form.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void btnOrderOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/place-order-form.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void btnExitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customer-manage-form.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void loadDateAndTime() {
        //Date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        //Time
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime time = LocalTime.now();
            lblTime.setText(
                    time.getHour() + " : " + time.getMinute() + " : " + time.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateAndTime();
    }
}
