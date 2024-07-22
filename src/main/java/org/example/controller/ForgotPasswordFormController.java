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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.UserBoImpl;
import org.example.dto.User;
import org.example.util.BoType;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;


public class ForgotPasswordFormController{
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btnReset;
    @FXML
    private JFXButton btnSendOtp;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtNewPassword;
    @FXML
    private TextField txtOtpCode;
    @FXML
    private TextField txtReEnterPassword;
    UserBoImpl userBo= BoFactory.getInstance().getBo(BoType.USER);
    private int otp;
    @FXML
    void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login-form.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void btnExitOnAction(ActionEvent event) {
        Optional<ButtonType> exit = showAlert(
                Alert.AlertType.CONFIRMATION,
                "Exit",
                "Confirmation Required",
                "Are you sure you want to EXIT..?"
        );
        if (exit.isPresent() && exit.get()==ButtonType.OK){
            System.exit(0);
        }
    }
    @FXML
    void btnResetOnAction(ActionEvent event) {

        if (!txtNewPassword.getText().equals(txtReEnterPassword.getText())){
            showAlert(
                    Alert.AlertType.ERROR,
                    "Password Mismatch",
                    "Password Error",
                    "The new password and the re-entered password do not match. Please try again."
            );
            return;
        }
        if(Integer.parseInt(txtOtpCode.getText())!=otp){
            showAlert(
                    Alert.AlertType.ERROR,
                    "OTP Mismatch",
                    "OTP Error",
                    "The OTP you entered does not match the one sent to your email. Please try again."
            );
            return;
        }

        String newPassword = userBo.hashPassword(txtNewPassword.getText());

        if (userBo.updateUserPassword(txtEmail.getText(),newPassword)){
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Password Changed",
                    "Success",
                    "Your password has been changed successfully."
            );
        }else{
            showAlert(
                    Alert.AlertType.ERROR,
                    "Password Change Failed",
                    "Error",
                    "Your password could not be changed. Please try again later."
            );
        }
        txtEmail.setText(null);
        txtNewPassword.setText(null);
        txtReEnterPassword.setText(null);
        txtOtpCode.setText(null);
    }
    @FXML
    void btnSendOtpOnAction(ActionEvent event)  {

        User userByEmail = userBo.getUserByEmail(txtEmail.getText());

        if (userByEmail==null){
            showAlert(
                    Alert.AlertType.ERROR,
                    "Invalid Email",
                    "Email Error",
                    "The email you entered does not exist. Please enter a valid email address."
            );
            txtEmail.setText(null);
            return;
        }
            Random random = new Random();
            otp = random.nextInt(999999)+100000;

            try {
                userBo.sendEmail(txtEmail.getText(),mailBody(),"Password Reset OTP");
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "OTP Sent",
                        "Success",
                        "OTP has been sent to your email successfully."
                );
            } catch (MessagingException e) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed",
                        "Failed to send OTP. Please try again."
                );
            }

    }
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }
    private String mailBody() {
        String name = userBo.getUserByEmail(txtEmail.getText()).getName();

        return "Dear "+name+","+
        "\n\nUse the following OTP to reset your password:"+
        "\n\nOTP:"+"["+otp+"]" +
                "\n\nIf you didn't request this, ignore this email." +
                "\n\nThank you," +
                "\n\n[Clothify Store]";
    }
}
