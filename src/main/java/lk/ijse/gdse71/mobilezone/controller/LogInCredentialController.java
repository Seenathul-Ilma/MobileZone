package lk.ijse.gdse71.mobilezone.controller;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lk.ijse.gdse71.mobilezone.dto.CategoryDTO;
import lk.ijse.gdse71.mobilezone.dto.LogInCredentialsDTO;
import lk.ijse.gdse71.mobilezone.dto.tm.CategoryTM;
import lk.ijse.gdse71.mobilezone.dto.tm.CustomerTM;
import lk.ijse.gdse71.mobilezone.dto.tm.LogInCredentialTM;
import lk.ijse.gdse71.mobilezone.model.LogInCredentialModel;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class LogInCredentialController implements Initializable {
    static String generatedVerificationCode;
    @FXML
    private AnchorPane pageLogInCred;
    @FXML
    private PasswordField txtEmailedPassword;
    @FXML
    private PasswordField txtAdminPassword;

    @FXML
    private VBox VboxCredentialDetails;

    @FXML
    private VBox vBoxSecurity;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<LogInCredentialTM, String> colPassword;

    @FXML
    private TableColumn<LogInCredentialTM, String> colRole;

    @FXML
    private TableColumn<LogInCredentialTM, String> colUserId;

    @FXML
    private TableColumn<LogInCredentialTM, String> colUsername;

    @FXML
    private Label lblUserId;

    @FXML
    private TableView<LogInCredentialTM> tblCredential;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserRole;

    @FXML
    private TextField txtUsername;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        try {
            if(VboxCredentialDetails.isVisible()){
                VboxCredentialDetails.setVisible(false);
            }
            if(tblCredential.isVisible()){
                tblCredential.setVisible(false);
            }
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load user Id").show();
        }
    }

    @FXML
    void continueOnAction(ActionEvent event) throws SQLException {
        String verificationCode = txtEmailedPassword.getText();
        String insertedPassword = txtAdminPassword.getText();
        String password = logInCredentialsModel.getAdminPassword();
        System.out.println("Password: "+password);
        System.out.println("inserted: "+insertedPassword);
        System.out.println("verify: "+verificationCode);
        if(verificationCode.equals("") || insertedPassword.equals("")){
            new Alert(Alert.AlertType.ERROR, "Password fields cannot be empty!.").show();
        } else if(verificationCode.equals(generatedVerificationCode) && insertedPassword.equals(password)){
            VboxCredentialDetails.setVisible(true);
            tblCredential.setVisible(true);
            vBoxSecurity.setDisable(true);
        }else{
            new Alert(Alert.AlertType.ERROR, "Wrong Password!").show();
        }
    }

    @FXML
    void goBackOnAction(ActionEvent event) throws IOException {
        AnchorPane nextPane = FXMLLoader.load(getClass().getResource("/view/MainLayout.fxml"));

        // Get the current stage and set the new scene
        Stage stage = (Stage) pageLogInCred.getScene().getWindow();
        Scene scene = new Scene(nextPane);
        stage.setScene(scene);
    }

    @FXML
    void btnDeleteCredentialsOnAction(ActionEvent event) throws SQLException {
        String userId = lblUserId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this user credential?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if(optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){
            boolean isDeleted = logInCredentialsModel.deleteCredentials(userId);
            if(isDeleted){
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "User credential deleted successfully!").show();
            } else{
                new Alert(Alert.AlertType.ERROR, "Failed to delete credential!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void btnSaveCredentialsOnAction(ActionEvent event) throws SQLException {
        String userId = lblUserId.getText();
        String role = txtUserRole.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        txtUserRole.setStyle(txtUserRole.getStyle() + ";-fx-border-color: #7367F0;");
        txtUsername.setStyle(txtUsername.getStyle() + ";-fx-border-color: #7367F0;");
        txtPassword.setStyle(txtPassword.getStyle() + ";-fx-border-color: #7367F0;");

        String userNamePattern = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        boolean isValidUserName = username.matches(userNamePattern);
        boolean isValidPassword = password.matches(passwordPattern);

        if (!isValidUserName) {
            txtUsername.setStyle(txtUsername.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid Username!");
            return;
        }

        if (!isValidPassword) {
            txtPassword.setStyle(txtPassword.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid Password!");
            return;
        }

        String ifInsertedRoleIsAdmin = "admin";
        String ifInsertedRoleIsEmployee = "employee";
        boolean isValidRole = role.equalsIgnoreCase(ifInsertedRoleIsEmployee) || role.equalsIgnoreCase(ifInsertedRoleIsAdmin) ;
        if(!isValidRole){
            new Alert(Alert.AlertType.ERROR, "Invalid role! \nRole must be admin or employee.").show();
        }

        if(role.equals(ifInsertedRoleIsAdmin)){
            role = ifInsertedRoleIsAdmin.substring(0,1).toUpperCase() + ifInsertedRoleIsAdmin.substring(1);
        }else if(role.equals(ifInsertedRoleIsEmployee)){
            role = ifInsertedRoleIsEmployee.substring(0,1).toUpperCase() + ifInsertedRoleIsEmployee.substring(1);
        }

        String existUsernamePassword = logInCredentialsModel.isExistUsernameAndPassword(username, password);
        if(!existUsernamePassword.equals("")){
            new Alert(Alert.AlertType.ERROR, "Already existed username and password...!\nTry another username and password!").show();
            return;
        }

        if (isValidPassword && isValidUserName && isValidRole) {
            LogInCredentialsDTO credentialDTO = new LogInCredentialsDTO(
                    userId,
                    role,
                    username,
                    password
            );

            boolean isSaved = logInCredentialsModel.saveCredentials(credentialDTO);
            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Credentials saved successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save credentials...!").show();
            }
        }
    }

    private void refreshPage() throws SQLException {
        loadNextUserId();
        loadTableData();

        btnSave.setDisable(false);

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnReset.setDisable(false);

        txtUserRole.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
    }

    LogInCredentialModel logInCredentialsModel = new LogInCredentialModel();

    private void loadTableData() throws SQLException {
        ArrayList<LogInCredentialsDTO> logInCredentialsDTOS = logInCredentialsModel.getAllCredentials();
        ObservableList<LogInCredentialTM> logInCredentialTMS = FXCollections.observableArrayList();

        for(LogInCredentialsDTO logInCredentialsDTO : logInCredentialsDTOS){
            LogInCredentialTM logInCredentialTM = new LogInCredentialTM(
                    logInCredentialsDTO.getUserId(),
                    logInCredentialsDTO.getRole(),
                    logInCredentialsDTO.getUserName(),
                    logInCredentialsDTO.getPassword()
            );
            logInCredentialTMS.add(logInCredentialTM);
        }
        tblCredential.setItems(logInCredentialTMS);
    }

    private void loadNextUserId() throws SQLException {
        String nextUserId = logInCredentialsModel.getNextUserId();
        lblUserId.setText(nextUserId);
    }

    @FXML
    void btnUpdateCredentialsOnAction(ActionEvent event) throws SQLException {
        String userId = lblUserId.getText();
        String role = txtUserRole.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        txtUserRole.setStyle(txtUserRole.getStyle() + ";-fx-border-color: #7367F0;");
        txtUsername.setStyle(txtUsername.getStyle() + ";-fx-border-color: #7367F0;");
        txtPassword.setStyle(txtPassword.getStyle() + ";-fx-border-color: #7367F0;");

        String userNamePattern = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        boolean isValidUserName = username.matches(userNamePattern);
        boolean isValidPassword = password.matches(passwordPattern);

        if (!isValidUserName) {
            txtUsername.setStyle(txtUsername.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid Username!");
            return;
        }

        if (!isValidPassword) {
            txtPassword.setStyle(txtPassword.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid Password!");
            return;
        }

        String existUsernamePassword = logInCredentialsModel.isExistUsernameAndPassword(username, password);
        if(!existUsernamePassword.equals(userId)) {
            new Alert(Alert.AlertType.ERROR, "Already existed username and password...!\nTry another username and password!").show();
            return;
        }

        String ifInsertedRoleIsAdmin = "admin";
        String ifInsertedRoleIsEmployee = "employee";
        boolean isValidRole = role.equalsIgnoreCase(ifInsertedRoleIsEmployee) || role.equalsIgnoreCase(ifInsertedRoleIsAdmin) ;

        if(!isValidRole){
            new Alert(Alert.AlertType.ERROR, "Invalid role! \nRole must be admin or employee.").show();
        }

        if(role.equals(ifInsertedRoleIsAdmin)){
            role = ifInsertedRoleIsAdmin.substring(0,1).toUpperCase() + ifInsertedRoleIsAdmin.substring(1);
        }else if(role.equals(ifInsertedRoleIsEmployee)){
            role = ifInsertedRoleIsEmployee.substring(0,1).toUpperCase() + ifInsertedRoleIsEmployee.substring(1);
        }

        if (isValidPassword && isValidUserName && isValidRole) {
            LogInCredentialsDTO credentialDTO = new LogInCredentialsDTO(
                    userId,
                    role,
                    username,
                    password
            );

            boolean isUpdated = logInCredentialsModel.updateCredentials(credentialDTO);
            if (isUpdated) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Credentials updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update credentials...!").show();
            }
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        LogInCredentialTM logInCredentialTM = tblCredential.getSelectionModel().getSelectedItem();
        if(logInCredentialTM != null){
            lblUserId.setText(logInCredentialTM.getUserId());
            txtUserRole.setText(logInCredentialTM.getRole());
            txtUsername.setText(logInCredentialTM.getUserName());
            txtPassword.setText(logInCredentialTM.getPassword());

            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }

    @FXML
    void ReceiveEmailOnAction(ActionEvent event) {
        final String FROM = "seenathulilma121243@gmail.com";
        String recipient = "zeenathulilma121243@gmail.com";
        String subject = "Verification code has been sent!";
        generatedVerificationCode = PasswordGenerator.generatePassword(12);
        String body = "Your verification code:- "+ generatedVerificationCode;
        System.out.println(generatedVerificationCode);
        sendEmailWithGmail(FROM, recipient, subject, body);
    }

    private void sendEmailWithGmail(String from, String recipient, String subject, String body) {
        String PASSWORD = "yciz pxwm wfts zppi";

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.host", "smtp.gmail.com");

        props.put("mail.smtp.port", "587");

        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            // Replace with your email and app password
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

            message.setSubject(subject);

            message.setText(body);

            Transport.send(message);

            new Alert(Alert.AlertType.INFORMATION, "Verification code sent successfully!").show();
        } catch (MessagingException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to send verification code..").show();
        }
    }
}



