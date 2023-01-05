package fr.univtln.lhd.controllers;

import fr.univtln.lhd.view.authentification.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML public Button connectButton;
    @FXML public Parent main;
    @FXML public EdtLhdController mainController;
    public Text ident;
    public Text pwd;
    public PasswordField passwordField;
    public TextField textField;
    public Label warning;
    public Label guestButton;

    @FXML private Label label;

    @Override
    /**
     * Initialize the login view and disable the schedule to be able to
     * interact with the view
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        main.setDisable(true);
        mainController.initialize();
        mainController.hideEDT();
        textField.setStyle("-fx-text-inner-color: #eceff4;");
        textField.setStyle("-fx-control-inner-background:  #3b4252");
        passwordField.setStyle("-fx-text-inner-color: #eceff4;");
        passwordField.setStyle("-fx-control-inner-background:  #3b4252");
    }


    /**
     * Utilitary methode to get the scene
     * @return
     */
    private Scene getCurrentScene() {
        return ident.getScene();
    }

    @FXML
    /**
     * Methode for when the login button is pressed
     * it checked the validity of the email and login field
     * and switch to the schedule if it's correct
     * if not it simply warn the user with the warning label
     */
    public void verifyUser(ActionEvent actionEvent) {
        String password = passwordField.getText();
        String login = textField.getText();
        Auth.AuthType typeOfLoginForm =Auth.isAcorectCombination(login,password);
        if (!(typeOfLoginForm.equals(Auth.AuthType.GUEST))){
            getCurrentScene().getStylesheets().add("/styles/edt-lhd-style.css");
            getCurrentScene().getStylesheets().add("/styles/slot-info-panel-style.css");
            main.setDisable(false);
            mainController.setCurrentAuth(typeOfLoginForm,login,password);
            mainController.showEDT();
            clearField();
        }
        else {
            warning.setText("ÉCHEC DE LA CONNEXION");
            warning.setStyle("-fx-border-color: #d08770;");
        }
    }

    /**
     * Methode when the user click on the guest label,
     * it update the schedule with the interface for guest
     * and switch to it
     */
    public void onMouseClickedGuestLabel() {
        guestButton.setStyle("-fx-background-color:#38ee00;");
        getCurrentScene().getStylesheets().add("/styles/edt-lhd-style.css");
        getCurrentScene().getStylesheets().add("/styles/slot-info-panel-style.css");
        main.setDisable(false);
        mainController.setCurrentAuthAsGuest();
        mainController.showEDT();
        clearField();
    }

    /**
     * cleare the text field and the
     * warning label
     */
    private void clearField(){
        passwordField.clear();
        warning.setText("");
        warning.setStyle("");
        textField.clear();
    }

    public void onMouseEnteredGuestLabel( ) {
        guestButton.setStyle("-fx-text-fill:#b48ead;");
    }

    public void onMouseEnteredLoginButton( ) {
        connectButton.setStyle("-fx-background-color:#81a1c1;");
    }

    public void onMouseExitGuestLabel( ) {
        guestButton.setStyle("-fx-text-fill:#d08770;");
    }

    public void onMouseExitLoginButton( ) {
        connectButton.setStyle("-fx-background-color: #5e81ac;");
    }

    public void onMousePressedLoginButton( ) {
        connectButton.setStyle("-fx-background-color: #8fbcbb;");
    }

    public void onMouseReleasedLoginButton( ) {
        connectButton.setStyle("-fx-background-color: #5e81ac;");
    }
}