package com.example.fitrecipes.Models;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private String name;
    private int quantity;
    private int unit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnit() {
        return unit;
    }

    public String getUnitName(){
        switch (getUnit()){
            case 1:
                return "1Kg";
            case 2:
                return "1Cup";
            case 3:
                return "1Litre";
            case 4:
                return "1Tsp";
            case 5:
                return "1Tbsp";
            case 0:
            default:
                return "1Gm";
        }
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

}
