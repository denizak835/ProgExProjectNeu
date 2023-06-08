package com.example.progexprojectneu;

import java.util.ArrayList;
import java.util.List;

public class Store {
    private List<Clothing> clothingList;

    public Store() {
        clothingList = new ArrayList<>();
    }

    public void addClothing(Clothing clothing) {
        clothingList.add(clothing);
    }

    public void removeClothing(Clothing clothing) {
        clothingList.remove(clothing);
    }

    public List<Clothing> getAllClothes() {
        List<Clothing> clothingList = new ArrayList<>();
        for (Clothing clothing : clothingList) {
            {
                clothingList.add(clothing);
            }
        }
        return clothingList;
    }
    public List<Clothing> getAllPants() {
        List<Clothing> pantsList = new ArrayList<>();
        for (Clothing clothing : clothingList) {
            if (clothing instanceof Pants) {
                pantsList.add(clothing);
            }
        }
        return pantsList;
    }

    public List<Clothing> getAllJackets() {
        List<Clothing> jacketsList = new ArrayList<>();
        for (Clothing clothing : clothingList) {
            if (clothing instanceof Jackets) {
                jacketsList.add(clothing);
            }
        }
        return jacketsList;
    }


}


