package com.iutlpweb.fr.happyshop.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iutlpweb.fr.happyshop.models.Category;

import java.util.ArrayList;

public class DaoCategory {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "course.db";

    public static final String TABLE_CATEGORIES = "table_categories";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NAME = "Name";
    private static final int NUM_COL_NAME = 1;
    private static final String COL_IDPOS = "IdPos";
    private static final int NUM_COL_IDPOS = 2;
    private static final String COL_COLOR = "Color";
    private static final int NUM_COL_COLOR = 3;

    private SQLiteDatabase bdd;

    private MyBaseSQLite myBaseSQLite;

    public DaoCategory(Context context){
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

    public long insertCategory(Category category){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_NAME, category.getName());
        values.put(COL_IDPOS, category.getIdPos());
        values.put(COL_COLOR, category.getColor());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_CATEGORIES, null, values);
    }

    public int updateCategory(int id, Category category){
        //La mise à jour d'un categorie dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simple préciser quelle categorie on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_NAME, category.getName());
        values.put(COL_IDPOS, category.getIdPos());
        values.put(COL_COLOR, category.getColor());
        return bdd.update(TABLE_CATEGORIES, values, COL_ID + " = " +id, null);
    }

    public int removeCategoryWithID(int id){
        //Suppression d'un categorie de la BDD grâce à l'ID
        return bdd.delete(TABLE_CATEGORIES, COL_ID + " = " +id, null);
    }

    public int removeCategoryAll(){
        //Suppression d'un categorie de la BDD grâce à l'ID
        return bdd.delete(TABLE_CATEGORIES, null, null);
    }

    public Category getCategoryWithName(String name){
        //Récupère dans un Cursor les valeur correspondant à un categorie contenu dans la BDD (ici on sélectionne le categorie grâce à son titre)
        Cursor c = bdd.query(TABLE_CATEGORIES, new String[] {COL_ID, COL_NAME, COL_IDPOS, COL_COLOR}, COL_NAME + " LIKE \"" + name +"\"", null, null, null, null);
        return cursorToCategory(c);
    }

    public ArrayList<Category> getAllCategories(){
        //Récupère dans un Cursor les valeur correspondant à un categorie contenu dans la BDD (ici on sélectionne le categorie grâce à son titre)
        Cursor c = bdd.query(TABLE_CATEGORIES, new String[] {COL_ID, COL_NAME, COL_IDPOS, COL_COLOR}, null, null, null, null, null);
        return cursorToCategoriesList(c);
    }

    //Cette méthode permet de convertir un cursor en un categorie
    private Category cursorToCategory(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un categorie
        Category category = new Category();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        category.setId(c.getInt(NUM_COL_ID));
        category.setName(c.getString(NUM_COL_NAME));
        category.setIdPos(c.getInt(NUM_COL_IDPOS));
        category.setColor(c.getString(NUM_COL_COLOR));

        //On ferme le cursor
        c.close();

        //On retourne le categorie
        return category;
    }

    //Cette méthode permet de convertir un cursor en un categorie
    private ArrayList<Category> cursorToCategoriesList(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;
        ArrayList<Category> categoriesListe= new ArrayList<Category>();
        //Sinon on se place sur le premier élément
        //c.moveToFirst();
        while(c.moveToNext()){
            //On créé un categorie
            Category category = new Category();
            //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
            category.setId(c.getInt(NUM_COL_ID));
            category.setName(c.getString(NUM_COL_NAME));
            category.setIdPos(c.getInt(NUM_COL_IDPOS));
            category.setColor(c.getString(NUM_COL_COLOR));
            categoriesListe.add(category);
        }
        //On ferme le cursor
        c.close();

        //On retourne le categorie
        return categoriesListe;
    }
}