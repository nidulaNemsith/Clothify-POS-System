package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class LoginFormController {
    public TextField txtEmail;
    public JFXButton btnLogin;
    public JFXButton btnForgotPassword;
    public TextField txtPassword;
    public JFXButton btnExit;

    public void btnLoginOnAction(ActionEvent actionEvent) {
    }

    public void btnforgotPasswordOnAction(ActionEvent actionEvent) {
    }

    public void btnExitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}
