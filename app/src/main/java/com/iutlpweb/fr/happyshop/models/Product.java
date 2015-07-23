package com.iutlpweb.fr.happyshop.models;

import java.io.Serializable;

public class Product implements Comparable<Product>, Serializable {
    /**
     * Creation d'un produit
     */


    private static final long serialVersionUID = 5094588814021021928L;
    public static final String NOM_MOD_PROD = "com.iutlpweb.fr.happyshop.models.Product";

    private int id;
    private String name;
    private int idCateg;
    private String quantity;
    private int isShop;


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int isShop() {
        return isShop;
    }

    public void setShop(int isShop) {
        this.isShop = isShop;
    }


    public Product(){}

    public Product(String name, int idCateg, String quantity, int isShop){
        this.name = name;
        this.idCateg = idCateg;
        this.quantity = quantity;
        this.isShop = isShop;

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

    public int getIdCateg() {
        return idCateg;
    }

    public void setIdCateg(int idCateg) {
        this.idCateg = idCateg;
    }

    @Override
    public String toString() {
        String statutProd = "";
        if(isShop == 0){
            statutProd = "Inactif";
        } else if (isShop == 1){
            statutProd = "Actif";
        }

        return name + ", Catégorie: " + idCateg + ", Statut " + statutProd;
    }

    @Override
    public int compareTo(Product another) {

        return this.name.compareTo(another.name);
    }



}