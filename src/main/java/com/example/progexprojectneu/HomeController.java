package com.example.progexprojectneu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private ImageView imageView;
    @FXML
    private VBox ClothingVBox;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label descriptionLabel;

    private Clothing clothing;
    private String loggedInUsername;
    private String loggedInPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Hier können Initialisierungsaktionen durchgeführt werden

        // Beispiel: Setzen der Daten für das Kleidungsstück
        if (clothing != null) {
            imageView.setImage(clothing.getImage());
            nameLabel.setText(clothing.get_product());
            priceLabel.setText(String.valueOf(clothing.get_price()));
        }
    }

    public void setLoggedInCredentials(String username, String password) {
        loggedInUsername = username;
        loggedInPassword = password;
    }

    @FXML
    private void handleMyAccountButton(ActionEvent event) {
        // Account-Instanz erstellen
        Account account = new Account();

        // Datenbankverbindung herstellen
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", loggedInUsername, loggedInPassword);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE username = ?")) {
            // Parameter in die SQL-Abfrage einsetzen
            statement.setString(1, loggedInUsername);
            ResultSet resultSet = statement.executeQuery();
            // VBox leeren, um vorhandene Daten zu entfernen
            ClothingVBox.getChildren().clear();
            // Attribute aus der ResultSet abrufen und in der VBox anzeigen

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                // Die Attribute in Labels anzeigen
                Label usernameLabel = new Label("Username: " + username);
                Label passwordLabel = new Label("Password: " + password);

                // Buttons für das Ändern des Benutzernamens und des Passworts erstellen
                Button changeUsernameButton = new Button("Change Username");
                Button changePasswordButton = new Button("Change Password");
                Button changeEmailButton = new Button("Change E-Mail Adress");

                // Aktionen für die Buttons festlegen
                changeUsernameButton.setOnAction(e -> handleChangeUsernameButton(username));
                changePasswordButton.setOnAction(e -> handleChangePasswordButton(username));

                Label titleLabel = new Label("My Account Information");
                titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;-fx-text-fill: lightblue;");
                ClothingVBox.getChildren().addAll(titleLabel);
                ClothingVBox.setSpacing(20);
                // Labels und Buttons zur VBox hinzufügen
                ClothingVBox.getChildren().addAll(usernameLabel, changeUsernameButton, passwordLabel, changePasswordButton);


                Label emailLabel = new Label("Email: " + email);

                // Labels zur VBox hinzufügen
                ClothingVBox.getChildren().addAll(emailLabel, changeEmailButton);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Fehler beim Zugriff auf die Datenbank: " + e.getMessage());
        }
    }

    private void handleChangeUsernameButton(String username) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Username");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newUsername -> {
            // Datenbankverbindung herstellen
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", loggedInUsername, loggedInPassword)) {
                // Benutzername in der Tabelle "accounts" aktualisieren
                String updateAccountsQuery = "UPDATE accounts SET username = ? WHERE username = ?";
                try (PreparedStatement statement = connection.prepareStatement(updateAccountsQuery)) {
                    statement.setString(1, newUsername);
                    statement.setString(2, username);

                    int affectedRowsAccounts = statement.executeUpdate();

                    if (affectedRowsAccounts > 0) {
                        System.out.println("Username updated in 'accounts' table successfully.");
                    } else {
                        System.out.println("Failed to update username in 'accounts' table.");
                    }
                }

                // Benutzername in der Tabelle "user" aktualisieren
                String updateUserQuery = "UPDATE MYSQL.user SET username = ? WHERE username = ?";
                try (PreparedStatement statement = connection.prepareStatement(updateUserQuery)) {
                    statement.setString(1, newUsername);
                    statement.setString(2, username);

                    int affectedRowsUser = statement.executeUpdate();

                    if (affectedRowsUser > 0) {
                        System.out.println("Username updated in 'user' table successfully.");
                    } else {
                        System.out.println("Failed to update username in 'user' table.");
                    }
                }

                handleMyAccountButton(null); // Aktualisiere die Anzeige der Account-Daten
            } catch (SQLException e) {
                System.out.println("Fehler beim Zugriff auf die Datenbank: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleChangePasswordButton(String username) {
        // Erstellen Sie ein Textfeld für die Eingabe des neuen Passworts
        TextField newPasswordField = new TextField();
        newPasswordField.setPromptText("Enter new password");

        // Erstellen Sie einen Button zum Bestätigen des neuen Passworts
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(event -> {
            String newPassword = newPasswordField.getText();
            if (!newPassword.isEmpty()) {
                // Datenbankverbindung herstellen und Passwort aktualisieren
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", loggedInUsername, loggedInPassword);
                     PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET password = ? WHERE username = ?")) {
                    statement.setString(1, newPassword);
                    statement.setString(2, username);
                    int affectedRows = statement.executeUpdate();

                    if (affectedRows > 0) {
                        System.out.println("Password updated successfully.");
                        handleMyAccountButton(null); // Aktualisiere die Anzeige der Account-Daten
                    } else {
                        System.out.println("Failed to update password.");
                    }
                } catch (SQLException e) {
                    System.out.println("Fehler beim Zugriff auf die Datenbank: " + e.getMessage());
                }
            }
        });

        // Fügen Sie das Textfeld und den Button zur VBox hinzu
        ClothingVBox.getChildren().addAll(newPasswordField, confirmButton);
    }

    public void setClothing(Clothing clothing) {
        this.clothing = clothing;

        // Setze die Daten für das Kleidungsstück
        if (clothing != null) {
            imageView.setImage(clothing.getImage());
            nameLabel.setText(clothing.get_product());
            priceLabel.setText(String.valueOf(clothing.get_price()));
        }
    }

    public void openClothingDetails() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/src/Clothing.fxml"));
            Parent root = fxmlLoader.load();

            ClothingDetailsController controller = fxmlLoader.getController();
            controller.setClothing(clothing);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
