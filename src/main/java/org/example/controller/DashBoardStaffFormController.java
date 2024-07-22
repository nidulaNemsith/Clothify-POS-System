package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.OrderBoImpl;
import org.example.bo.custom.impl.OrderDetailBoImpl;
import org.example.util.BoType;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashBoardStaffFormController implements Initializable {
    public Label lblTime;
    public Label lblDate;
    public JFXButton btnExit;
    public JFXButton btnCustomer;
    public JFXButton btnProduct;
    public JFXButton btnOrder;
    public JFXButton btnSuppliers;
    public JFXButton btnReport;
    public Label lblName;
    public JFXButton btnLogOut;
    @FXML
    private PieChart mostSellProductChart;
    @FXML
    private BarChart<String,Number> topOrders;

    private final OrderDetailBoImpl orderDetailBo= BoFactory.getInstance().getBo(BoType.ORDER_DETAIL);
    private final OrderBoImpl orderBo= BoFactory.getInstance().getBo(BoType.ORDER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblName.setText(CurrentLogInUserController.getInstance().getName());
        loadDateAndTime();

        XYChart.Series<String, Number> topOrdersSeries = orderBo.getTopOrdersSeries(10);
        topOrders.getData().add(topOrdersSeries);

        ObservableList<PieChart.Data> mostSoldProducts = orderDetailBo.getMostSoldProducts();
        mostSellProductChart.setData(mostSoldProducts);
    }
    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Optional<ButtonType> buttonType = showAlert(
                Alert.AlertType.CONFIRMATION,
                "Log Out",
                "Confirm Log Out",
                "Are you sure you want to log out?"
        );
        if (buttonType.isPresent()&&buttonType.get()==ButtonType.OK){
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login-form.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
    public void btnReportOnAction(ActionEvent actionEvent) {
    }
    public void btnSuppliersOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/supplier-manage-form.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void btnOrderOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/place-order-form.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void btnProductOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/product-manage-form.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/customer-manage-form.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
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

}
