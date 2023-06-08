package com.example.progexprojectneu;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URI;

public class Pants extends Clothing {
    private String category = "Pants";

    public Pants(String productName, double price, String gender, String size, String filter, String imagePath) {
        super(productName, price, gender, size, filter, imagePath);
        File file = new File(imagePath);
        URI uri = file.toURI();
        String uriString = uri.toString();
        this.imagePath = uriString;
    }


    @Override
    protected void showImage() {
        Stage stage = new Stage();
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);
        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    protected String getCategory() {
        return category;
    }
}