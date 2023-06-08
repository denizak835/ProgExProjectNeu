package com.example.progexprojectneu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage stage) throws IOException {
        Account account = new Account("root", "denizdeniz");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("PureFashion");
        stage.setScene(scene);
        stage.show();

        //String imagePath = "C:/Users/Deniz/IdeaProjects/ProgExProject/GUI/images_website_px/redjeans.png";
        //Pants pants = new Pants("Rote Hose", 99.99, "male", "XL", "Kids", imagePath);

        // Öffne das Kleidungsdetailfenster für das Hosenobjekt
        //ClothingController clothingController = fxmlLoader.getController();
        //clothingController.setClothing(pants);
        //clothingController.openClothingDetails();
    }

    public static class ShoppingCart {
        private HashSet<Clothing> clothings;

        public ShoppingCart() {
            clothings = new HashSet<>();
        }

        public void addItem(Clothing clothing) {
            clothings.add(clothing);
        }

        public void removeItem(Clothing clothing) {
            clothings.remove(clothing);
        }

        public double getTotalPrice() {
            double total = 0;
            for (Clothing item : clothings) {
                total += item.get_price();
            }
            return total;
        }

        public void displayItems() {
            System.out.println("Clothing in shopping cart:");
            for (Clothing item : clothings) {
                System.out.println(item.get_product() + " - $" + item.get_price());
            }
        }
    }
}
