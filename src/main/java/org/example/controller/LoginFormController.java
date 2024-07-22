package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.UserBoImpl;
import org.example.dto.User;
import org.example.util.BoType;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class LoginFormController{
    @FXML
    private JFXButton btnForgotPassword;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;

    private final UserBoImpl userBo= BoFactory.getInstance().getBo(BoType.USER);

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        CurrentLogInUserController instance = CurrentLogInUserController.getInstance();

        if (isTextFieldsEmpty()){
            showAlert(
                    Alert.AlertType.ERROR,
                    "Login Error",
                    "Incomplete Credentials",
                    "Please enter both email and password to log in."
            );
            return;
        }
        if (!userBo.isValidEmail(txtEmail.getText())){
            showAlert(
                    Alert.AlertType.ERROR,
                    "Login Error",
                    "Invalid Email",
                    "Please enter a valid email to log in."
            );
            return;
        }
        User userByEmail = userBo.getUserByEmail(txtEmail.getText());

        if(userByEmail==null || !BCrypt.checkpw(txtPassword.getText(),userByEmail.getPassword())) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Login Error",
                    "Invalid Credentials",
                    "The email or password you entered is incorrect. Please try again."
            );
            return;
        }
        String userRole = userByEmail.getRole();
        instance.setRole(userByEmail.getRole());
        instance.setName(userByEmail.getName());
        instance.setId(userByEmail.getId());

        switch (userRole) {
            case "Admin": loadAdminPage(actionEvent);break;
            case "Staff": loadStaffPage(actionEvent);break;
            default:break;
        }
    }
    public void btnForgotPasswordOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/forgotPassword-form.fxml")));
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
    private boolean isTextFieldsEmpty() {
        return txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty();
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
