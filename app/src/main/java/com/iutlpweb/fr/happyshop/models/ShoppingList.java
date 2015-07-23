package com.iutlpweb.fr.happyshop.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ShoppingList {

    /**
     * Creation d'une liste de course
     * @author thierry hund
     */


    public ShoppingList(ArrayList<Product> productsListe,ArrayList<Category> categoriesListe) {
        this.productsListe = productsListe;
        this.categoriesListe = categoriesListe;
    }

    private ArrayList<Product> productsListe;
    private ArrayList<Category> categoriesListe;

    public HashMap<String, List<String>> getFormatedProductsList() {
        return formatedProductsList;
    }

    public ArrayList<String> getFormatedCategoriesList() {
        return formatedCategoriesList;
    }

    private HashMap<String, List<String>> formatedProductsList;
    private ArrayList<String> formatedCategoriesList;



    public ArrayList<Product> getProductsListe() {
        return productsListe;
    }

    public void setProductsListe(ArrayList<Product> productsListe) {
        this.productsListe = productsListe;
    }

    public ArrayList<Category> getCategoriesListe() {
        return categoriesListe;
    }

    public void setCategoriesListe(ArrayList<Category> categoriesListe) {
        this.categoriesListe = categoriesListe;
    }

    public void addCategory(Category category) {
        //TODO ajout verif si existe deja
        categoriesListe.add(category);
    }

    public void removeCategory(Category category) {
        categoriesListe.remove(category);
    }

    public void addProduct(Product product) {
        //TODO ajout verif si existe deja
        productsListe.add(product);
    }

    public void removeProduct(Product product) {
        productsListe.remove(product);
    }

    public void createdFormatedShoppingList() {

        this.formatedProductsList = new HashMap<String, List<String>>();
        this.formatedCategoriesList = new ArrayList<String>();

        Iterator<Category> iterateur = categoriesListe.iterator();

        while (iterateur.hasNext()) {
            Category category = iterateur.next();
            String categoryString = category.getName();
            int idCateg = category.getId();

            this.formatedCategoriesList.add(categoryString);

            List<String> donneesCategory = new ArrayList<String>();

            for (int c=0; c < productsListe.size(); c++) {
                int prod_Idcateg= productsListe.get(c).getIdCateg();
                int prod_IsShop= productsListe.get(c).isShop();

                String unProduit = new String();
                if(prod_Idcateg == idCateg && prod_IsShop == 1){
                    unProduit = productsListe.get(c).getName();
                    String quantity = productsListe.get(c).getQuantity();
                    if(quantity != "" && quantity != null){
                        unProduit += " : "+quantity;
                    }
                    donneesCategory.add(unProduit);

                }

            }

            this.formatedProductsList.put(categoryString, donneesCategory);
        }
    }


}
