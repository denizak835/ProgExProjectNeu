package com.example.progexprojectneu;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Label Menu;

    @FXML
    private Label Menuback;

    @FXML
    private AnchorPane Kids_Slide;

    @FXML
    private AnchorPane Slider;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private VBox clothingContainer;

    protected String loggedinusername;
    protected String loggedinpassword;
    protected static LoginController instance;


    public String getLoggedinusername(){
        return loggedinusername;
    }
    public void setLoggedinusername(String username) {
        this.loggedinusername = username;
    }


    public void setLoggedinpassword(String password){
        this.loggedinpassword = password;

    }

    public String getLoggedinpassword(){
        return loggedinpassword;
    }

    public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }

    private void slideSlider(double translateX) {
        TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), Slider);
        slide.setToX(translateX);
        slide.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void handleLoginButton(javafx.event.ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Account account = new Account();
        boolean loggedIn = account.login(username, password);
        if (loggedIn) {
            try {
                FXMLLoader loader = new FXMLLoader((getClass().getResource("Home.fxml")));
                Parent root = loader.load();
                HomeController controller = loader.getController();

                // Setzen Sie die Werte für loggedinusername und loggedinpassword
                controller.setLoggedInCredentials(username, password);

                Scene scene = new Scene(root);
                Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                currentStage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Anmeldung fehlgeschlagen!");
        }
    }






    private List<Clothing> getAllClothingFromDatabase() {
        List<Clothing> clothingList = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", "denizdeniz");
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM clothing";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("idclothing");
                String product = resultSet.getString("product");
                Double price = resultSet.getDouble("price");
                String gender = resultSet.getString("gender");
                String size = resultSet.getString("size");
                String filter = resultSet.getString("filter");
                String imagePath = resultSet.getString("image");

                // Load the image from the file
                Image image = new Image("file:///" + imagePath);

                // Create the clothing object with the loaded image
                Clothing clothing = new Pants(product, price, gender, size, filter, imagePath);
                clothingList.add(clothing);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clothingList;
    }
}
