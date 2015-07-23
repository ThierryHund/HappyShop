package com.iutlpweb.fr.happyshop.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.iutlpweb.fr.happyshop.R;
import com.iutlpweb.fr.happyshop.dao.DaoCategory;
import com.iutlpweb.fr.happyshop.dao.DaoProduct;
import com.iutlpweb.fr.happyshop.models.Category;

import java.util.ArrayList;
import java.util.Collections;

import static android.widget.AdapterView.OnItemClickListener;

public class ListCatActivity extends MainMenuActivity implements View.OnClickListener,
        OnItemClickListener, AdapterView.OnItemLongClickListener {


    private ArrayList<String> tabNameCategories;
    private ArrayAdapter<Category> adaptaterCateg;
    private ArrayList<Category> categoriesList;
    private DaoProduct daoProduct;
    private DaoCategory daoCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cat);

        // Ouverture de la BDD (table des produits)
        this.daoProduct = new DaoProduct(this);
        this.daoProduct.open();
        this.daoCategory = new DaoCategory(this);
        this.daoCategory.open();
    }

    @Override
    public int getIdMenuToHide() {
        return R.id.listCateg;
    }

    @Override
    protected void onStart() {

        super.onStart();


        // Récupère toutes les catégories dans un ArrayList de catégories
        categoriesList = daoCategory.getAllCategories();

        TextView addButton = (TextView) findViewById(R.id.btnAddCateg);
        addButton.setOnClickListener(this);

        // Ne réalise les traitements d'affichage de la liste de catégorie que si celle-ci n'est pas vide
        if(categoriesList != null) {
            Collections.sort(this.categoriesList);
            this.adaptaterCateg = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, this.categoriesList){

            // Met le fond de chaque catégorie de la liste en sa couleur
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                TextView tv =  (TextView) super.getView(position, convertView, parent);
                String resultColor;
                String col = categoriesList.get(position).getColor();
                switch (col){
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
                tv.setBackgroundColor(Color.parseColor(resultColor));
                return tv;
            }
        };

            ListView listViewCategory = (ListView) findViewById(R.id.listCategory);

            listViewCategory.setOnItemLongClickListener(this);
            listViewCategory.setAdapter(this.adaptaterCateg);
            listViewCategory.setBackgroundResource(R.drawable.hard_corners);
            listViewCategory.setOnItemClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ModCatActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Au clic sur le bouton Ajout catégorie, transfert à l'activité concernée
        Intent intent = new Intent(this, ModCatActivity.class);
        intent.putExtra(Category.NOM_MOD, this.adaptaterCateg.getItem(position));
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // Au long clic d'une catégorie de la liste, boite de dialogue pour suppression
        int pos = parent.getSelectedItemPosition();
        final Category c = (Category) parent.getItemAtPosition(position);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Suppresion!!");
        alert.setMessage("La suppression de cette catégorie entrainera la suppression des produits qui lui sont associés. Etes-vous sûr de votre choix?");

        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Si Oui, suppression de la catégorie et de tous ses produits
                daoProduct.removeProductAllWithIdCateg(c.getId());
                daoCategory.removeCategoryWithID(c.getId());
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

//        Test de drag and drop (testé sur logClick quand suppression n'existait pas)
//        ClipData data = ClipData.newPlainText("", "");
//        View.DragShadowBuilder viewShadow = new View.DragShadowBuilder(view);
//        view.startDrag(null, viewShadow, view, 0);

        return true;
    }

/*
    @Override
    public boolean onDrag(View v, DragEvent event) {

        System.out.println("evenement : " + event);
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    System.out.println("owner : " + owner);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(Color.LTGRAY);

                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(Color.RED);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
//                    View view = (View) event.getLocalState();
//                    ViewGroup owner = (ViewGroup) view.getParent();
//                    System.out.println("owner : " + owner);
//                    owner.removeView(view);
//                    LinearLayout container = (LinearLayout) v;
//                    container.addView(view);
//                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.CYAN);
                default:
                    break;
            }
            return true;

    }
*/


}

