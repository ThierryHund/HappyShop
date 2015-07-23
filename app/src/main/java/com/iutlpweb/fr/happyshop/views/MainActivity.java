package com.iutlpweb.fr.happyshop.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iutlpweb.fr.happyshop.R;
import com.iutlpweb.fr.happyshop.dao.DaoCategory;
import com.iutlpweb.fr.happyshop.dao.DaoProduct;
import com.iutlpweb.fr.happyshop.models.Category;
import com.iutlpweb.fr.happyshop.models.Product;
import com.iutlpweb.fr.happyshop.models.ShoppingList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class MainActivity extends MainMenuActivity implements View.OnClickListener, View.OnLongClickListener, AdapterView.OnItemClickListener {


    private AutoCompleteTextView actvProduct;
    private EditText quantityProduct;
    private Button buttonAddProduct;
    private LinearLayout layoutShoppingList;
    private DaoProduct daoProduct;
    private DaoCategory daoCategory;
    private HashMap<Integer, ArrayList<Integer>> listGlobalIndex;
    private ShoppingList shoppingList;
    private Product prodToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Création d'une instance de ma classe ProductsBDD
        this.daoProduct = new DaoProduct(this);
        this.daoCategory = new DaoCategory(this);

        // On ouvre la base de données pour écrire dedans
        daoProduct.open();
        daoCategory.open();
    }

    protected void onStart() {
        super.onStart();

        this.actvProduct = (AutoCompleteTextView) findViewById(R.id.actvProduct);
        this.quantityProduct = (EditText) findViewById(R.id.quantityProduct);
        this.buttonAddProduct = (Button) findViewById(R.id.buttonAddProduct);
        this.buttonAddProduct.setOnClickListener(this);

//        daoProduct.removeProductAll();
//        daoCategory.removeCategoryAll();

        //on crée les arrayList pour stocker les objets produits et catégories (et on récupere toutes les categs juste pour tester si qque chose est dans la base)
        ArrayList<Category> categoriesList = daoCategory.getAllCategories();
        //on recupere tous les produits depuis la bdd (on simule ce qui se passera dans la réalité)
        ArrayList<Product> productsList = daoProduct.getAllProduct();

        // On test si la base contient qque chose sinon on cré un jeux d'essai
        if(categoriesList == null) {

            // JEUX D'ESSAI ///////////////////////////////////////////
            // On créé les objets categories
            Category category1 = new Category("Légumes", 1,"Vert");
            Category category2 = new Category("Conserves", 2,"Marron");
            Category category3 = new Category("Boissons", 3,"Bleu");


            //on  insère les objet dans la bdd
            daoCategory.insertCategory(category1);
            daoCategory.insertCategory(category2);
            daoCategory.insertCategory(category3);
            categoriesList = daoCategory.getAllCategories();


            Product product1 = new Product("Poivron",  daoCategory.getCategoryWithName("Légumes").getId(), "2 vert", 0);
            daoProduct.insertProduct(product1);
            Product product2 = new Product("Tomate",  daoCategory.getCategoryWithName("Légumes").getId(), "1 Barquette" , 1);
            daoProduct.insertProduct(product2);
            Product product3 = new Product("Choux", daoCategory.getCategoryWithName("Légumes").getId(), "1", 1);
            daoProduct.insertProduct(product3);

            Product product4 = new Product("Petits pois",  daoCategory.getCategoryWithName("Conserves").getId(), "2 boites", 0);
            daoProduct.insertProduct(product4);
            Product product5 = new Product("Maïs",  daoCategory.getCategoryWithName("Conserves").getId(), "env. 200g" , 0);
            daoProduct.insertProduct(product5);
            Product product6 = new Product("Haricots vert", daoCategory.getCategoryWithName("Conserves").getId(), "500g", 1);
            daoProduct.insertProduct(product6);

            Product product7 = new Product("Vin rouge",  daoCategory.getCategoryWithName("Boissons").getId(), "2 bouteilles", 1);
            daoProduct.insertProduct(product7);
            Product product8 = new Product("Bière",  daoCategory.getCategoryWithName("Boissons").getId(), "1pack de 12" , 1);
            daoProduct.insertProduct(product8);
            Product product9 = new Product("Jus d'orange", daoCategory.getCategoryWithName("Boissons").getId(), "1L", 1);
            daoProduct.insertProduct(product9);
            // JEUX D'ESSAI ///////////////////////////////////////////
        }

        // Trie des catégories dans l'ordre des idPos
        Collections.sort(categoriesList);

        productsList = daoProduct.getAllProduct();

        //on crée la shoppingList avec les arrayList récupérés depuis la base
        shoppingList = new ShoppingList(productsList, categoriesList);

        //on génère la shoppingList formatée
        shoppingList.createdFormatedShoppingList();

        //on affiche la liste des catégorie
        this.afficheListeCategs();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, productsList);
        actvProduct.setAdapter(adapter);

        // Actionne un événement au clic sur un item de l'AutoCompleteView
        actvProduct.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Récupère le produit sélectionné dans l'autocompleteview
        this.prodToAdd = (Product) parent.getItemAtPosition(position);
    }

    @Override
    public void onClick(View v) {

        // Si clic sur ajouter produit, ajout à la liste (avec quantité facultative (passe le isShop à 1)
        if(v.getId() == R.id.buttonAddProduct){
            this.prodToAdd.setShop(1);
            this.prodToAdd.setQuantity(this.quantityProduct.getEditableText().toString());
            this.daoProduct.updateProduct(this.prodToAdd.getId(), this.prodToAdd);

            Intent intent = getIntent();
            finish();
            startActivity(intent);
            Toast.makeText(this, this.prodToAdd.getName() + " ajouté à la liste.", Toast.LENGTH_LONG).show();
        }
        else {
            int clickIndex = v.getId();
            ArrayList<Integer> clicCours = (ArrayList<Integer>) listGlobalIndex.get(clickIndex);

            Iterator<Map.Entry<Integer, ArrayList<Integer>>> jours = listGlobalIndex.entrySet().iterator();

            //on rend invisible les produit autres que ceux de la categ cliquée
            while (jours.hasNext()) {
                Map.Entry pairs = (Map.Entry) jours.next();
                ArrayList<Integer> allcourse = (ArrayList<Integer>) listGlobalIndex.get(pairs.getKey());
                if ((Integer) pairs.getKey() != clickIndex) {
                    ListIterator<Integer> itr = allcourse.listIterator();
                    while (itr.hasNext()) {
                        findViewById(itr.next()).setVisibility(View.GONE);
                    }
                }
            }

            //on affiches les prod d'une categorie
            //si les produits sont déja affiché on les rend invisible
            ListIterator<Integer> itr2 = clicCours.listIterator();
            while (itr2.hasNext()) {
                View cour = findViewById(itr2.next());
                if (cour.getVisibility() == View.GONE) {
                    cour.setVisibility(View.VISIBLE);
                } else {
                    cour.setVisibility(View.GONE);
                }
                ;
            }
        }
    }

    private void afficheListeCategs() {

        // get the listview
        layoutShoppingList = (LinearLayout) findViewById(R.id.layoutShoppingList);
        LayoutInflater createurVue = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<String> listeCategories = shoppingList.getFormatedCategoriesList();


        HashMap<String, List<String>> listProducts = shoppingList.getFormatedProductsList();

        listGlobalIndex = new HashMap<Integer, ArrayList<Integer>>();
        int globalIndex = 0;
        globalIndex++;
        for (int i = 0; i < listeCategories.size(); i++) {
            TextView label = (TextView) createurVue.inflate(R.layout.list_categories, null);
            layoutShoppingList.addView(label);
            label.setText(listeCategories.get(i));

            label.setOnClickListener(this);
            ArrayList<String> liste = (ArrayList<String>) listProducts.get(listeCategories.get(i));
            ArrayList<Integer> list = new ArrayList<Integer>();

            for (int j = 0; j < liste.size(); j++) {
                TextView product = (TextView) createurVue.inflate(
                        R.layout.list_products, null);
                layoutShoppingList.addView(product);
                product.setOnLongClickListener(this);

                product.setId(globalIndex);
                list.add(globalIndex);
                // listGlobalIndex.(i).add(globalIndex);
                globalIndex++;
                product.setText(liste.get(j).replace("///","\n"));
                product.setVisibility(View.GONE);
            }

            label.setId(globalIndex);
            listGlobalIndex.put(globalIndex, list);
            globalIndex++;
        }
        System.out.println("index "+listGlobalIndex);

    }

    @Override
    public int getIdMenuToHide() {

        return R.id.home;
    }

    @Override
    public boolean onLongClick(final View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Suppresion!!");
        alert.setMessage("Etes vous sur de vouloir supprimer ce produit de la liste?");
        final DaoProduct daoProduct = new DaoProduct(this);
        // On ouvre la base de données pour écrire dedans
        daoProduct.open();
        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do your work here
                int clickIndex = v.getId();

                String[] text = ((TextView)v).getText().toString().split(":");
                String name = text[0].trim();

                Product prod = daoProduct.getProductWithName(name);
                //int idCateg = prod.getIdCateg();

                Iterator it = listGlobalIndex.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    ArrayList array = (ArrayList)pair.getValue();
                    if( array.indexOf(clickIndex) != -1){
                        ArrayList<Integer> listProd = listGlobalIndex.get((Integer)pair.getKey());
                        int index = listProd.indexOf(clickIndex);
                        listProd.remove(index);
                        listGlobalIndex.put((Integer)pair.getKey(), listProd);
                    }
                }
                prod.setShop(0);
                daoProduct.updateProduct(prod.getId(), prod);
                layoutShoppingList.removeView(v);
                dialog.dismiss();

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


}
