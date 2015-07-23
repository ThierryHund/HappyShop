package com.iutlpweb.fr.happyshop.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MyBaseSQLite extends SQLiteOpenHelper{

    private static final String TABLE_CATEGORIES = "table_categories";
    private static final String TABLE_PRODUCTS = "table_products";

    private static final String COL_IDCAT = "ID";
    private static final String COL_NAMECAT = "Name";
    private static final String COL_IDPOS = "IdPos";
    private static final String COL_COLOR = "Color";

    private static final String COL_ID = "ID";
    private static final String COL_NAME = "Name";
    private static final String COL_IDCATEG = "IdCateg";
    private static final String COL_QUANTITY = "Quantity";
    private static final String COL_ISSHOP = "IsShop";



    private static final String CREATE_BDD_CAT = "CREATE TABLE " + TABLE_CATEGORIES + " ("
            + COL_IDCAT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAMECAT + " TEXT NOT NULL, "
            + COL_IDPOS + " INTEGER NOT NULL, " + COL_COLOR + " TEXT NOT NULL);";

    private static final String CREATE_BDD_PROD = "CREATE TABLE " + TABLE_PRODUCTS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + " TEXT NOT NULL, "
            + COL_IDCATEG + " INTEGER NOT NULL, " + COL_ISSHOP + " INTEGER NOT NULL, " + COL_QUANTITY + " TEXT);";

    public MyBaseSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on créé la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_BDD_CAT);
        db.execSQL(CREATE_BDD_PROD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + TABLE_PRODUCTS + ";");
        db.execSQL("DROP TABLE " + TABLE_CATEGORIES + ";");

        onCreate(db);
    }

}
