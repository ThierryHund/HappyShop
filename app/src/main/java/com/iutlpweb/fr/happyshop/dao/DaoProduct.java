package com.iutlpweb.fr.happyshop.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iutlpweb.fr.happyshop.models.Product;

import java.util.ArrayList;

public class DaoProduct {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "course.db";

    private static final String TABLE_PRODUCTS = "table_products";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NAME = "Name";
    private static final int NUM_COL_NAME = 1;
    private static final String COL_IDCATEG = "IdCateg";
    private static final int NUM_COL_IDCATEG = 2;
    private static final String COL_QUANTITY = "Quantity";
    private static final int NUM_COL_QUANTITY = 3;
    private static final String COL_ISSHOP = "IsShop";
    private static final int NUM_COL_ISSHOP = 4;

    private SQLiteDatabase bdd;

    private MyBaseSQLite myBaseSQLite;

    public DaoProduct(Context context){
        //On créer la BDD et sa table
        myBaseSQLite = new MyBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = myBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertProduct(Product product){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_NAME, product.getName());
        values.put(COL_IDCATEG, product.getIdCateg());
        values.put(COL_QUANTITY, product.getQuantity());
        values.put(COL_ISSHOP, product.isShop());

        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_PRODUCTS, null, values);
    }

    public int updateProduct(int id, Product product){
        //La mise à jour d'un produit dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quelle produit on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_NAME, product.getName());
        values.put(COL_IDCATEG, product.getIdCateg());
        values.put(COL_QUANTITY, product.getQuantity());
        values.put(COL_ISSHOP, product.isShop());
        return bdd.update(TABLE_PRODUCTS, values, COL_ID + " = " +id, null);
    }

    public int removeProductWithID(int id){
        //Suppression d'un produit de la BDD grâce à l'ID
        return bdd.delete(TABLE_PRODUCTS, COL_ID + " = " +id, null);
    }

    public int removeProductAll(){
        //Suppression d'un produit de la BDD grâce à l'ID
        return bdd.delete(TABLE_PRODUCTS, null, null);
    }

    public int removeProductAllWithIdCateg(int idCateg){
        //Suppression de tous les produits d'une catégorie
        return bdd.delete(TABLE_PRODUCTS, COL_IDCATEG + " = " + idCateg, null);
    }

    public int removeProductWithName(String name){
        //Suppression d'un produit de la BDD grâce à l'ID
        return bdd.delete(TABLE_PRODUCTS, COL_NAME + " LIKE \"" +name +"\"", null);
    }

    public Product getProductWithName(String name){
        //Récupère dans un Cursor les valeur correspondant à un produit contenu dans la BDD (ici on sélectionne le produit grâce à son titre)
        Cursor c = bdd.query(TABLE_PRODUCTS, new String[] {COL_ID, COL_NAME, COL_IDCATEG, COL_QUANTITY, COL_ISSHOP}, COL_NAME + " LIKE \"" + name +"\"", null, null, null, null);
        return cursorToProduct(c);
    }

    public Product getProductWithID(int id){
        //Récupère dans un Cursor les valeur correspondant à un produit contenu dans la BDD (ici on sélectionne le produit grâce à son titre)
        Cursor c = bdd.query(TABLE_PRODUCTS, new String[] {COL_ID, COL_NAME, COL_IDCATEG, COL_QUANTITY, COL_ISSHOP}, COL_ID + " LIKE \"" + id +"\"", null, null, null, null);
        return cursorToProduct(c);
    }

    public ArrayList<Product> getAllProduct(){
        //Récupère dans un Cursor les valeur correspondant à un produit contenu dans la BDD (ici on sélectionne le produit grâce à son titre)
        Cursor c = bdd.query(TABLE_PRODUCTS, new String[] {COL_ID, COL_NAME, COL_IDCATEG, COL_QUANTITY, COL_ISSHOP}, null, null, null, null, null);
        return cursorToProductsList(c);
    }

    //Cette méthode permet de convertir un cursor en un produit
    private Product cursorToProduct(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un produit
        Product product = new Product();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        product.setId(c.getInt(NUM_COL_ID));
        product.setName(c.getString(NUM_COL_NAME));
        product.setIdCateg(c.getInt(NUM_COL_IDCATEG));
        product.setQuantity(c.getString(NUM_COL_QUANTITY));
        product.setShop(c.getInt(NUM_COL_ISSHOP));

        //On ferme le cursor
        c.close();

        //On retourne le produit
        return product;
    }

    //Cette méthode permet de convertir un cursor en un produit
    private ArrayList<Product> cursorToProductsList(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;
        ArrayList<Product> productsListe= new ArrayList<Product>();

        //Sinon on se place sur le premier élément
        //c.moveToFirst();
        while(c.moveToNext()){
            //On créé un produit
            Product product = new Product();
            //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
            product.setId(c.getInt(NUM_COL_ID));
            product.setName(c.getString(NUM_COL_NAME));
            product.setIdCateg(c.getInt(NUM_COL_IDCATEG));
            product.setQuantity(c.getString(NUM_COL_QUANTITY));
            product.setShop(c.getInt(NUM_COL_ISSHOP));
            productsListe.add(product);
        }
        //On ferme le cursor
        c.close();

        //On retourne le produit
        return productsListe;
    }
}