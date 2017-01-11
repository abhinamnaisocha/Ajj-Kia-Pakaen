package com.mba.AjjkiaPakaen;


/**
 * Created by Muhammad Bilal on 27/03/2016.
 */
public class DishRecipie {

    private int dishId;
    float rated;
    byte[] img;
    private String dishName, dishIng, dishRecipie;


    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public float getRated() {
        return rated;
    }

    public void setRated(float rated) {
        this.rated = rated;
    }

    public String getDishIng() {
        return dishIng;
    }

    public void setDishIng(String dishIng) {
        this.dishIng = dishIng;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishRecipie() {
        return dishRecipie;
    }

    public void setDishRecipie(String dishRecipie) {
        this.dishRecipie = dishRecipie;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
