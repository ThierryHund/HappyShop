package com.iutlpweb.fr.happyshop.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.iutlpweb.fr.happyshop.R;
import com.iutlpweb.fr.happyshop.dao.DaoCategory;
import com.iutlpweb.fr.happyshop.models.Category;

import java.util.ArrayList;

public abstract class MainMenuActivity extends ActionBarActivity {

    private ArrayList<Category> arrayCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);

        // Ouverture de la BDD
        DaoCategory daoCategory = new DaoCategory(this);
        daoCategory.open();
        this.arrayCat = daoCategory.getAllCategories();
        MenuItem item = menu.findItem(this.getIdMenuToHide());
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.createCateg) {
            Intent intent = new Intent(this, ModCatActivity.class);
            startActivity(intent);
        } else if (id == R.id.listCateg) {
                Intent intent = new Intent(this, ListCatActivity.class);
                startActivity(intent);
        } else if (id == R.id.createProd) {
            // Si aucune catégorie dans la base, impossibilité de créer un produit, redirection vers ajout  catégorie
            if(this.arrayCat == null){
                Toast.makeText(this, "Vous devez d'abord créer une catégorie.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ModCatActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            } else {
                Intent intent = new Intent(this, ModProdActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.listProd) {
            Intent intent = new Intent(this, ListProdActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public abstract int getIdMenuToHide();
}
