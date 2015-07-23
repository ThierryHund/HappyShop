package com.iutlpweb.fr.happyshop.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.iutlpweb.fr.happyshop.R;
import com.iutlpweb.fr.happyshop.dao.DaoCategory;
import com.iutlpweb.fr.happyshop.dao.DaoProduct;
import com.iutlpweb.fr.happyshop.models.Category;
import com.iutlpweb.fr.happyshop.models.Product;

import java.util.ArrayList;

public class ModProdActivity extends MainMenuActivity implements View.OnClickListener {

    private static ArrayAdapter<String> adapterCateg = null;
    private EditText prodName;
    private Spinner spinnerCateg;
    private int idToMod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_prod);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.spinnerCateg = (Spinner) findViewById(R.id.spinnerCateg);

        // Ouverture de la BDD
        DaoCategory daoCategory = new DaoCategory(this);
        daoCategory.open();

        ArrayList<Category> arrayCat = daoCategory.getAllCategories();

        // Adapte tableau de catégorie au spinner
        this.adapterCateg = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayCat);
        this.adapterCateg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCateg.setAdapter(this.adapterCateg);

        // Repérage et préparation des éléments du formulaire
        Button validModProd = (Button) findViewById(R.id.validModProd);
        Button cancelAddProd = (Button) findViewById(R.id.cancelAddProd);
        validModProd.setOnClickListener(this);
        cancelAddProd.setOnClickListener(this);

        this.prodName = (EditText) findViewById(R.id.prodName);

        // Pré-remplissage des valeurs si modification (Intent de ListCatActivity
        if (getIntent().getExtras() != null) {
            Product product = (Product) getIntent().getSerializableExtra(
                    Product.NOM_MOD_PROD);

            this.idToMod = product.getId();
            this.prodName.setText(product.getName());
//            String c = adapterCateg.getItemId(product.getIdCateg());
//            int spinnerPosition = adapterCateg.getPosition(c);
//            this.spinnerCateg.setSelection(spinnerPosition);

        }

    }

    /**
     * Clic pour soit annuler soit enregistrer ajout/modif dans la bdd
     */
    @Override
    public void onClick(View v) {

        Category c = (Category) spinnerCateg.getSelectedItem();

        // Si clic sur annuler, retour à la page précédente
        if (v.getId() == R.id.cancelAddProd) {
            this.setResult(Activity.RESULT_CANCELED);

            // Si clic sur valider, on créé un produit avec éléments saisis
        } else if (v.getId() == R.id.validModProd) {
            Product prod = new Product();
            prod.setName(this.prodName.getEditableText().toString());
            prod.setIdCateg(c.getId());
            prod.setShop(0);
            prod.setQuantity("0");

            // Ouverture de la BDD
            DaoProduct daoProduct = new DaoProduct(this);
            daoProduct.open();

            // Si Intent de ListProdActivity, mise à jour, sinon création nlle catégorie
            if (getIntent().getExtras() != null) {
                daoProduct.updateProduct(this.idToMod, prod);
            } else{
                daoProduct.insertProduct(prod);
            }

        }
        this.finish();
    }

    @Override
    public int getIdMenuToHide() {
        return R.id.createProd;
    }


}
