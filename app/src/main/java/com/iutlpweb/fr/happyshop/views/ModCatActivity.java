package com.iutlpweb.fr.happyshop.views;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.iutlpweb.fr.happyshop.R;
import com.iutlpweb.fr.happyshop.dao.DaoCategory;
import com.iutlpweb.fr.happyshop.models.Category;

import java.util.ArrayList;

public class ModCatActivity extends MainMenuActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static ArrayAdapter<CharSequence> adapterColor = null;
    private EditText catName;
    private NumberPicker catOrder;
    private Spinner spinnerColor;
    private int idToMod;
    private String colorCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_cat);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.spinnerColor = (Spinner) findViewById(R.id.spinnerColor);

        // Adapte le strin array dans le spinner
        adapterColor = ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(adapterColor);
        spinnerColor.setOnItemSelectedListener(this);

        // Repérage et préparation des éléments du formulaire
        Button validModCat = (Button) findViewById(R.id.validModCat);
        Button cancelAddCat = (Button) findViewById(R.id.cancelAddCat);
        validModCat.setOnClickListener(this);
        cancelAddCat.setOnClickListener(this);

        this.catName = (EditText) findViewById(R.id.catName);
        this.catOrder = (NumberPicker) findViewById(R.id.catOrder);
        this.catOrder.setWrapSelectorWheel(true);
        this.catOrder.setMinValue(1);
        this.catOrder.setMaxValue(50);
        this.catOrder.setValue(1);

        // Pré-remplissage des valeurs si modification (Intent de ListCatActivity
        if (getIntent().getExtras() != null) {
            Category category = (Category) getIntent().getSerializableExtra(
                    Category.NOM_MOD);

            this.idToMod = category.getId();
            this.catName.setText(category.getName());
            int spinnerPosition = adapterColor.getPosition(category.getColor());
            this.spinnerColor.setSelection(spinnerPosition);
            this.catOrder.setValue(category.getIdPos());

        }
    }

    /**
     * Clic pour soit annuler soit enregistrer ajout/modif dans la bdd
     */
    @Override
    public void onClick(View v) {

        // Si clic sur annuler, retour à la page précédente
        if (v.getId() == R.id.cancelAddCat) {
            this.setResult(Activity.RESULT_CANCELED);

        // Si clic sur valider, on créé une catégorie avec éléments saisis
        } else if (v.getId() == R.id.validModCat) {
            Category cat = new Category();
            cat.setName(this.catName.getEditableText().toString());
            cat.setIdPos(catOrder.getValue());
            cat.setColor(spinnerColor.getSelectedItem().toString());

            // Ouverture de la BDD
            DaoCategory daoCategory = new DaoCategory(this);
            daoCategory.open();

            // Pour toutes les catégories déjà existantes, on met à jour la position en fonction de celle saisie dernièrement
            ArrayList<Category> a = daoCategory.getAllCategories();
            if(a != null) {
                for (Category c : a) {
//                    System.out.println("uuuuuuuuuuuuuuuuu" + c.getIdPos());
                    if (c.getIdPos() >= cat.getIdPos()) {
                        c.setIdPos(c.getIdPos() + 1);
                        daoCategory.updateCategory(c.getId(), c);
                    }
                }
            }

            // Si Intent de ListCatActivity, mise à jour, sinon création nlle catégorie
            if (getIntent().getExtras() != null) {
                daoCategory.updateCategory(this.idToMod, cat);
            } else{
                daoCategory.insertCategory(cat);
            }

        }
        this.finish();
    }

    @Override
    public int getIdMenuToHide() {
        return R.id.createCateg;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String val = parent.getSelectedItem().toString();
        String resultColor;
        switch (val){
            case "Rouge": resultColor =  "#B3FF0000"; break;
            case "Bleu": resultColor =  "#B34169E1"; break;
            case "Vert": resultColor =  "#B3008000"; break;
            case "Orange": resultColor =  "#B3FF8C00"; break;
            case "Jaune": resultColor =  "#B3FFD700"; break;
            case "Beige": resultColor =  "#B3D2B48C"; break;
            case "Marron": resultColor =  "#B3D2691E"; break;
            case "Rose": resultColor =  "#B3FFB6C1"; break;
            case "Violet": resultColor =  "#B3E6E6FA"; break;
            case "Tomate": resultColor =  "#B3FF6347"; break;
            default: resultColor = "#FFFFFF"; break;

        }
        view.setBackgroundColor(Color.parseColor(resultColor));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}