package com.iutlpweb.fr.happyshop.models;

import java.io.Serializable;

public class Category implements Comparable<Category>, Serializable {
    /**
     * Création d'une categorie
     * @author thierry hund
     */

    private static final long serialVersionUID = 5094588814021021928L;
    public static final String NOM_MOD = "com.iutlpweb.fr.happyshop.models.Category";

    private int id;
    private String name;
    private int idPos;
    private String color;



    public Category(){}

    public Category(String name, int idPos, String color){
        this.name = name;
        this.idPos = idPos;
        this.color = color;

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdPos() {
        return idPos;
    }

    public void setIdPos(int idPos) {
        this.idPos = idPos;
    }

    @Override
    public String toString() {
        return name + ", Position: " + idPos;
    }


    @Override
    public int compareTo(Category another) {
        int idPos = ((Category) another).getIdPos();

        return (int) (this.idPos - idPos);
    }
}

