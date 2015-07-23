package com.iutlpweb.fr.happyshop.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iutlpweb.fr.happyshop.R;
import com.iutlpweb.fr.happyshop.dao.DaoCategory;
import com.iutlpweb.fr.happyshop.dao.DaoProduct;
import com.iutlpweb.fr.happyshop.models.Category;
import com.iutlpweb.fr.happyshop.models.Product;

import java.util.ArrayList;
import java.util.Collections;

public class ListProdActivity extends MainMenuActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ArrayList<String> tabNameProducts;
    private ArrayAdapter<Product> adaptaterProd;
    private ArrayList<Product> productsList;
    private ArrayList<Category> arrayCat;
    private DaoProduct daoProduct;
    private DaoCategory daoCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_prod);

        // Ouverture de la BDD (table des produits)
        this.daoProduct = new DaoProduct(this);
        this.daoProduct.open();
        this.daoCategory = new DaoCategory(this);
        this.daoCategory.open();
    }

    @Override
    protected void onStart() {

        super.onStart();

        // Récupère tous les produits dans un ArrayList de produits
        productsList = daoProduct.getAllProduct();

        this.arrayCat = daoCategory.getAllCategories();

        TextView addButton = (TextView) findViewById(R.id.btnAddProd);
        addButton.setOnClickListener(this);

        if(productsList != null) {
            Collections.sort(this.productsList);
            this.adaptaterProd = new ArrayAdapter<Product>(this, android.R.layout.simple_list_item_1, this.productsList);

            ListView listViewProduct = (ListView) findViewById(R.id.listProducts);

            listViewProduct.setOnItemLongClickListener(this);
            listViewProduct.setAdapter(this.adaptaterProd);
            listViewProduct.setBackgroundResource(R.drawable.hard_corners);
            listViewProduct.setOnItemClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(this.arrayCat == null){
            Toast.makeText(this, "Vous devez d'abord créer une catégorie.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, ModCatActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else {
            Intent intent = new Intent(this, ModProdActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ModProdActivity.class);
        intent.putExtra(Product.NOM_MOD_PROD, this.adaptaterProd.getItem(position));
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
        int pos = parent.getSelectedItemPosition();
        final Product p = (Product) parent.getItemAtPosition(position);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Suppresion!!");
        alert.setMessage("Etes vous sur de vouloir supprimer ce produit de la liste?");

        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                daoProduct.removeProductWithID(p.getId());
                dialog.dismiss();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
        return true;
    }

    @Override
    public int getIdMenuToHide() {
        return R.id.listProd;
    }


}
