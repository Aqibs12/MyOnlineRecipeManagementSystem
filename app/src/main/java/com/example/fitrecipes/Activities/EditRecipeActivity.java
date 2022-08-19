package com.example.fitrecipes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Models.Ingredient;
import com.example.fitrecipes.Models.Recipe;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.adapters.IngredientsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditRecipeActivity extends AppCompatActivity {
    int Image_Request_Code = 1;
    private Uri filePathUri;
    EditText R_name, R_instr , R_Desc, R_Url;
    AutoCompleteTextView acIngredient;
    Spinner R_category, R_time, R_srv_people;
    Button btnSubmit;
    UserModel userModel;
    CircleImageView profile_image;
    private String uuid = "";
    private String USERID = "";
    private DatabaseReference productsRef;
    private FirebaseDatabase firebaseDatabase;
    Recipe recipe;
    RecipeModel recipeModel;
    ArrayAdapter<String> adapter, adapter1, adapter2;
    Context context;
    private IngredientsAdapter ingredientsAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe_modifications);
        context = this;
        uuid = LoginActivity.UUID;
        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        recipeModel = recipe.getRecipeModel();
        userModel = (UserModel) getIntent().getSerializableExtra("user");
        context = this;
        init();
        progressDialog = new ProgressDialog(EditRecipeActivity.this);
        progressDialog.setCancelable(false);
        String[] category = {"Category", "Russian", "American", "Thai", "Indonesian",
                "African", "Afghani", "Pakistani", "Malaysian", "Maxican", "Chinese"};
        String[] serving_people = {"Serving People", "1", "2", "3", "4",
                "5", "6", "7", "8", "9", "10"};
        String[] cook_time = {"Cook Time", "5 Minutes", "10 Minutes", "15 Minutes", "20 Minutes",
                "25 Minutes", "30 Minutes", "35 Minutes", "40 Minutes", "45 Minutes", "50 Minutes"};

        /** Category Adapter */
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        R_category.setAdapter(adapter);
        /** Serving People Adapter */
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, serving_people);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        R_srv_people.setAdapter(adapter1);
        /** Cooking Time Adapter */
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cook_time);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        R_time.setAdapter(adapter2);

        for(int i = 0; i < category.length; i++){
            if(category[i].equals(recipeModel.getRecipeCategory())){
                R_category.setSelection(i);
                break;
            }
        }
        for(int i = 0; i < cook_time.length; i++){
            if(cook_time[i].equals(recipeModel.getRecipeT())){
                R_time.setSelection(i);
                break;
            }
        }
        for(int i = 0; i < serving_people.length; i++){
            if(serving_people[i].equals(recipeModel.getRecipe_people())){
                R_srv_people.setSelection(i);
                break;
            }
        }

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
            }
        });

        productsRef = FirebaseDatabase.getInstance().getReference("Recipes").child(recipe.getRecipeId());
        btnSubmit.
                setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(isValid()){
                            progressDialog.setTitle("Recipe is Updating...");
                            progressDialog.show();
                            if(filePathUri==null){
                                updateRecipe();
                            }
                            else {
                                uploadPicture();
                            }
                        }
                        else {
                            Toast.makeText(EditRecipeActivity.this, "Please Add All Recipe Details", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void uploadPicture() {
            try {
                StorageReference storageReference2 = FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis() + "." + GetFileExtension(filePathUri));
                storageReference2.putFile(filePathUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final String[] photoLink = {""};
                                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        photoLink[0] = uri.toString();
                                        recipeModel.setRecipe_image(photoLink[0]);
                                        Glide.with(profile_image).load(photoLink[0]).into(profile_image);
                                        updateRecipe();
                                    }
                                });
//
                            }
                        });
            }catch (Exception e)
            {
                Log.d( "UploadImage",e.getMessage());
                e.printStackTrace();
            }

    }
    private void updateRecipe(){
            String RecipeName = R_name.getText().toString();
            //           String RecipeImg = R_Url.getText().toString();
            String RecipeD = R_Desc.getText().toString();
            String RecipeI = R_instr.getText().toString();
            String RecipeIng = acIngredient.getText().toString();
            String RecipeT = R_time.getSelectedItem().toString();
            String RecipeCategory = R_category.getSelectedItem().toString();
            String RecipePeople = R_srv_people.getSelectedItem().toString();
            if (RecipeName.equals("")) {
                Toast.makeText(getApplicationContext(), "Write down Recipe Name.", Toast.LENGTH_LONG).show();
            } else if (RecipeD.equals("")) {
                Toast.makeText(getApplicationContext(), "Write down Recipe Description.", Toast.LENGTH_LONG).show();
            } else {
                HashMap<String, Object> productMap = new HashMap<>();
                productMap.put("id", recipe.getRecipeId());
                productMap.put("name", RecipeName);
                productMap.put("recipe_image", recipeModel.getRecipe_image());
                productMap.put("recipeD", RecipeD);
                productMap.put("recipeI", RecipeI);
                productMap.put("recipeIng", RecipeIng);
                productMap.put("recipeT", RecipeT);
                productMap.put("recipeCategory", RecipeCategory);
                productMap.put("recipe_people", RecipePeople);
                productMap.put("ingredientList",ingredientsAdapter.dataList);
                productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditRecipeActivity.this, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    }
                });
            }
    }


    private void init() {
        R_name = findViewById(R.id.edit_tv_R_name);
        R_time = findViewById(R.id.edit_tv_R_time);
        R_category = findViewById(R.id.sp_category);
        R_instr = findViewById(R.id.edit_tv_R_Ins);
        acIngredient = findViewById(R.id.ingredients);
        R_srv_people = findViewById(R.id.edit_tv_R_people);
        R_Desc = findViewById(R.id.edit_tv_R_desc);
        btnSubmit = findViewById(R.id.edit_btn_submit);
        profile_image = findViewById(R.id.profile_image);
        Glide.with(profile_image).load(recipeModel.getRecipe_image()).into(profile_image);

        R_name.setText(recipeModel.getName());
        R_instr.setText(recipeModel.getRecipeI());
        acIngredient.setText(recipeModel.getRecipeIng());
      //  R_srv_people.getSelectedItem().toString();
        R_Desc.setText(recipeModel.getRecipeD());

        recyclerView = findViewById(R.id.recyclerview);
        ingredientsAdapter = new IngredientsAdapter(this,recipeModel.getIngredientList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ingredientsAdapter);

        String[] arrayIngredients = {"Egg","Bread","Rye Bread","Wheaten Bread","White Bread","White Bread","Wholewheat Bread",
                "Wort", "Arrack", "Beer","Bantu Beer","Brandy","Anise Brandy","Apple Brandy",
                "Armagnac Brandy","Blackberry Brandy","Cherry Brandy","Cognac Brandy","Papaya Brandy","Pear Brandy","Plum Brandy","Raspberry Brandy",
                "Weinbrand Brandy","Gin","Rum","Whisky","Malt Whisky","Scotch","Champagne","Cider","Sake","Sherry","Coffee","Mate",
                "Black Tea","Green Tea","Roibos Tea","Barley","Crispbread","Malt","Oats",
                "Rice","Corn","Corn Oil","Popcorn","Sweetcorn","Butter","Buttermilk","Cheese","Blue Cheese","Camembert Cheese","Cheddar Cheese",
                "Comte Cheese","Cottage Cheese","Cream Cheese","Domiati Cheese","Emmental Cheese","Feta Cheese","Goat Cheese",
                "Gruyere Cheese","Limburger Cheese","Mozzarella Cheese","Munster Cheese","Parmesan Cheese","Provolone Cheese","Romano Cheese",
                "Roquefort Cheese","Russian Cheese","Sheep Cheese","Swiss Cheese","Tilsit Cheese","Ghee","Milk","Milk Fat","Goat Milk","Milk Powder","Sheep Milk",
                "Skimmed Milk","Yogurt","Achilleas","Arar","Buchu","Cajeput","Camphor","Cascarilla",
                "Cedar","Chamomile","Citronella Oil","Citrus Peel Oil","Eucalyptus Oil","Fir","Geranium","Grapefruit Peel Oil","Grass",
                "Hops Oil","Hyacinth","Hyssop Oil","Lemon Grass","Lemon Peel Oil","Lime Peel Oil","Lovage", "Mastic Gum","Mentha Oil",
                "Myrrh","Neroli Oil","Orange Oil","Orris","Clary Sage","Red Sage","Spanish Sage","Sandalwood","Sea Buckthorns","Sweet Grass",
                "Valerian","Wattle","Yarrow","Ylang-Ylang","Clam","Crab","Crayfish","Kelp","Krill","Lobster","Mollusc","Oyster","Prawn",
                "Scallop","Shellfish","Shrimp","Trassi","Squid","Bonito","Caviar","Codfish","Fish","Fatty Fish","Lean Fish","Fish Oil","Smoked Fish",
                "Salmon","Artichoke","Champaca","Jasmine","Lavendar","Rose","Apple","Apple Sauce","Apricot","Avocado","Babaco","Banana","Beli",
                "Byrsonima crassifolia","Cashew Apple","Cherimoya","Coconut","Currant","Black Currant","Red Currant","White Currant","Dates",
                "Durian","Elderberry","Feijoa","Fig","Grape","Guava","Hogplum","Jackfruit","Kiwifruit","Litchi","Loquat","Malay Apple","Mango",
                "Melon","Musk Melon","Naranjilla","Orange","Bitter Orange","Papaya","Mountain Papaya","Passionfruit","Yellow Passionfruit","Pawpaw", "Peach",
                "Pear","Bartlett Pear","Prickly Pear","Pepino","Pineapple","Plum","Plumcot","Pumpkin","Quince", "Chinese Quince","Raisin","Roseapple",
                "Sapodilla","Soursop","Spineless Monkey Orange","Starfruit","Tamarind","Woodapple","Bilberry","Blackberry","Blueberry","Cherry",
                "Bitter Cherry","Sour Cherry","Sweet Cherry","Cloudberry","Cranberry","Gooseberry","Lingonberry","Loganberry","Raspberry","Strawberry", "Strawberry Jam",
                "Bergamot","Citrus Fruits","Grapefruit","Kumquat","Lemon","Lime","Mandarin Orange","Satsuma Orange","Tangerine","Vanilla","Mushroom",
                "Truffle", "Angelic","Artemisia","Basil","Buckwheat","Calamus","Chervil","Coriander","Cornmint","Dill",
                "Fennel","Fenugreek","Garlic","Lemon Balm","Liqourice","Mint","Rhubarb","Rosemary","Sage",
                "Spearmint","Scotch Spearmint","Tarragon","Thyme","Beef","Beef Processed","Chicken",
                "Frankfurter Sausage","Ham","Lamb","Meat","Mutton","Pork","Sukiyaki","Turkey","Almond", "Brazil Nut", "Cocoa", "Beans", "Lima Beans",
                "Kidney Beans","Peanut","Peas","Soybean","Soybean Oil","Soybean Sauce","Filbert","Hazelnut","Macadamia Nut","Pecans", "Walnut",
                "Muskmallow","Sesame","Allium","Alpinia","Ceriman","Chicory","Hops","Laurel","Myrtle",
                "Olive","Pine","Sassafras","Tea","Fermented Tea","Tobacco","Watercress","Creosote","Honey","Macaroni","Mustard Oil","Peanut Butter",
                "Peanut Oil","Rye","Storax","Vinegar","Anise","Anise Hyssop","Star Anise","Caraway","Cardamom","Cassia","Celery","Cinnamon",
                "Clove","Cumin","Ginger","Mace","Marjoram","Nutmeg","Oregano","Parsley","Pepper","Saffron",
                "Turmeric","Green Beans","Chive","Endive","Leek","Lettuce","Okra","Onion","Shallot","Peppermint",
                "Broccoli","Brussels Sprout","Cabbage","Cauliflower","Horseradish","Mustard","Radish","Turnip",
                "Kohlrabi","Rutabaga","Wasabi","Capsicum","Cherry Pepper","Tomato","Chayote","Cucumber",
                "Beetroot","Carrot","Parsnip","Sweet Potato","Asparagus","Cassava",
                "Potato","Fried Potato","Allspice","Asafoetida","Ashgourd","Bittergourd","Bottlegourd","Canola Oil","Carom Seed",
                "Chard","Apple Cider Vinegar","Colocasia","Curry Leaf","Drumstick Leaf","Eggplant","Flaxseed","Jalapeno","Kenaf",
                "Lotus","Nigella Seed","Kewda","Pomegranate","Poppy Seed","Spinach","Turkey Berry","Water Chestnut","White Pepper",
                "Garcinia Indica","Cluster Bean","Paneer","Pigeon Pea","Basmati Rice","Ricotta Cheese","Silver linden","Redskin onion",
                "Lemon verbena","Cashew nut","Burdock","Borage","Capers","Safflower","Chestnut","Chickpea","Pummelo","Arabica coffee",
                "Japanese persimmon","Black crowberry","Rocket salad","Tartary buckwheat","Black huckleberry","Sunflower","Swamp cabbage",
                "Grass pea","Lentils","Garden cress","Mexican oregano","Lupine","Medlar","Mulberry",
                "Black mulberry","Evening primrose","Millet","Scarlet bean","Pistachio","Purslane","Red raspberry","Black raspberry","Sorrel",
                "Summer savory","Winter savory","Cherry tomato","Rowanberry",
                "Sorghum","Dandelion","Linden","Small leaf linden","Wheat","Sparkleberry","Common verbena","Adzuki bean","Gram bean","Mung bean",
                "Climbing bean","Muscadine grape","Bayberry","Elliott's blueberry","Canada blueberry","Buffalo currant","Deerberry","Ginseng","Longan",
                "Rambutan","Red rice","Welsh onion","Hard wheat","Triticale","Komatsuna","Pak choy","Jostaberry","Kai lan","Pineappple sage",
                "Skunk currant","Breakfast cereal","Pasta","Biscuit","Sourdough","Spirit","Abalone","Abiyuch","Acerola","Acorn","Winter squash","Agar","Red king crab","Alfalfa",
                "Amaranth","Arrowhead","Arrowroot","Atlantic herring","Atlantic mackerel","Painted comber",
                "Atlantic pollock","Atlantic wolffish","Bamboo shoots","Striped bass","Beaver","Beech nut","Beluga whale","Bison","Black bear","Alaska blackfish",
                "Northern bluefin tuna","Bluefish","Wild boar","Bowhead whale","Breadfruit","Rapini","Brown bear","Buffalo","Burbot","Giant butterbur",
                "American butterfish","Butternut","Butternut squash","Cardoon","Caribou","Natal plum","Carob","Common carp","Channel catfish",
                "Chia","Chinese chestnut","Garland chrysanthemum","Cisco","Nuttall cockle","Common octopus","Corn salad","Cottonseed",
                "Catjang pea","Squashberry","Atlantic croaker","Cusk","Cuttlefish","Mule deer","Devilfish","Dock","Dolphin fish",
                "Freshwater drum","Wild duck","Freshwater eel","Elk","Emu","Oregon yampah","European anchovy","European chestnut",
                "Turbot","Fireweed","Florida pompano","Ginkgo nuts","Greylag goose","Greenland halibut","Groundcherry","Grouper","Haddock",
                "Hippoglossus","Horse","Hyacinth bean","Irish moss","Pacific jack mackerel","Japanese chestnut","Jerusalem artichoke",
                "Jujube","Jute","Kale","King mackerel","Lambsquarters","Leather chiton","Common ling","Lingcod","White lupine",
                "Malabar spinach","Mammee apple","Purple mangosteen","Alpine sweetvetch","Milkfish","Monkfish","Moose","Moth bean",
                "Mountain yam","Striped mullet","Muskrat","New Zealand spinach","Nopal","Ocean pout","North Pacific giant octopus",
                "Ohelo berry","Opossum","Ostrich","Spotted seal","Pacific herring","Pacific rockfish","Common persimmon","Pheasant",
                "Northern pike","Pili nut","Colorado pinyon","Pitanga","French plantain","American pokeweed","Polar bear","Prairie turnip",
                "Quinoa","European rabbit","Raccoon","Rainbow smelt","Rainbow trout","Malabar plum","Rose hip","Roselle","Orange roughy",
                "Sablefish","Pink salmon","Chum salmon","Coho salmon","Sockeye salmon","Chinook salmon","Atlantic salmon","Salmonberry",
                "Common salsify","Spanish mackerel","Pacific sardine","Scup","Sea cucumber","Steller sea lion","Bearded seal","Ringed seal","Sea trout",
                "Sesbania flower","American shad","Shark","Sheefish","Sheepshead","Hedge mustard","Snapper",
                "Spelt","Spirulina","Squab","Squirrel","Greater sturgeon","White sucker","Pumpkinseed sunfish","Swordfish","Taro",
                "Teff","Tilefish","Mexican groundcherry","Towel gourd","Salmonidae","Walleye","Alaska pollock","Whelk",
                "Broad whitefish","Whitefish","Whiting","Wild rice","Tea leaf willow","Winged bean","Yam","Jicama","Yautia","Yellowfin tuna",
                "Yellowtail amberjack","Pollock","Albacore tuna","Atlantic halibut","Smelt","Clupeinae","Spiny lobster","Black-eyed pea",
                "Deer","Percoidei","Perciformes","Rabbit","Beefalo","Bivalvia","Flatfish","Walrus","Alaska wild rhubarb","Oriental wheat",
                "Yardlong bean","Great horned owl","Quail","Boysenberry","Rowal","Jew's ear","Shiitake","Purple laver","Wakame","Enokitake",
                "Epazote","Oyster mushroom","Cloud ear fungus","Maitake","Ostrich fern","Spot croaker","Sourdock","Tinda","Atlantic menhaden",
                "Agave","Narrowleaf cattail","Jellyfish","Anchovy","Blue whiting","Carp bream","Chanterelle","Sturgeon","Charr","Common dab",
                "Spiny dogfish","Anatidae","Anguilliformes","True frog","Garfish","Gadiformes","Mountain hare","Lake trout",
                "Lemon sole","Lumpsucker","Scombridae","Norway haddock","Norway pout","Oil palm","Sago palm",
                "Persimmon","Pikeperch","Pleuronectidae","Rock ptarmigan","Pacific ocean perch","Black salsify",
                "True seal","Red algae","Kombu","Snail","True sole","Catfish","Thistle","Common Tuna","Cetacea","Columbidae","Conch",
                "Berry win","Vodka","Ice cream","Vermouth","Madeira wine","Nougat","Toffee","Cake","Pizza","Ymer","Pastry","DragÌ©e",
                "Chewing gum","Marzipan","Salad dressing","Salt","Cream","Sugar","Sausage","Meatball","Pate","Meat bouillon","Whey",
                "Casein","Leavening agent", "Marshmallow","Gelatin","Water","Milk Human","Dumpling","Soup","Syrup","Remoulade",
                "Chocolate spread","Fruit gum","Meringue","Cocoa butter","Cocoa powder","Chocolate","Hot chocolate","Kefir","Miso","Tofu","Zwieback",
                "Roe","Icing","Snack bar","Green turtle","Burrito","Hamburger","Taco","Tortilla","Nachos","Salad","Dulce de leche","Topping",
                "Sweet custard","Egg roll","Heart of palm","Potato chip","Tortilla chip","Corn chip","Hibiscus tea","Stew",
                "Gelatin dessert","Junket","Falafel","Frybread","Lasagna","Morchella","Pancake","Pectin","Pudding",
                "Waffle","Soy milk","Meatloaf","Cocktail","Couscous","Bulgur","Coffee mocha","Chimichanga","Semolina",
                "Tapioca pearl","Tostada","Quesadilla","Baked potato","Hot dog","Spread","Enchilada","Ketchup","Adobo",
                "Horned melon","Hushpuppy","Fruit juice","Relish","Fruit salad","Soy yogurt","Cold cut","Mixed nuts","Shea tree",
                "Oil-seed Camellia","Ucuhuba","Phyllo dough","Cooking oil","Pie crust","Pie","Shortening",
                "Soy cream","Ice cream cone","Molasses","Nance","Natto","Ravioli","Scrapple","Succotash","Tamale","Rice cake",
                "Tree fern","Evaporated milk","Flour","Pita bread","Focaccia","Bagel",
                "Piki bread","French toast","Oat bread","Potato bread","Cornbread","Corn grits","Multigrain bread","Rice bread",
                "Pan dulce","Raisin bread","Wonton wrapper","Trail mix","Greenthread tea","Vegetable juice",
                "Soft drink","Milkshake","Chocolate mousse","Pupusa","Empanada","Arepa","Ascidians","Gefilte fish",
                "Yellow pond lily","Fish burger","Pot pie","Fudge","Candy bar","Condensed milk",
                "Margarine","Hummus","Potato puffs", "Potato gratin","Chinese bayberry","Green zucchini", "Zucchini","Saskatoon berry",
                "Nanking cherry","Japanese pumpkin","Cucurbita","Anise Oil", "Apple Juice","Coconut Milk", "Coconut Oil","Hops Beer",
                "Lemon Juice","Brown Rice","Tomato Juice","Tomato Paste","Tomato Puree", "Coriander Seed","Cured Ham",
                "Yeast","Tequila","Sauerkraut","Baking Powder","Citric Acid","Cooking Spray","Gelatin","Food Coloring"
        };

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,arrayIngredients);
        acIngredient.setAdapter(adapter);
        acIngredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(acIngredient.getText().toString());
                ingredient.setQuantity(1);
                ingredient.setUnit(0);
                boolean isAdded = ingredientsAdapter.add(ingredient);
                acIngredient.setText("");
                if(isAdded){
                    Toast.makeText(EditRecipeActivity.this,"Ingredient is added successfully",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(EditRecipeActivity.this,"Ingredient is already added",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private boolean isValid() {

        if (R_name.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please Enter Recipe Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (adapter1 != null) {
            int position = R_srv_people.getSelectedItemPosition();
            if (position > 0) {
                //Toast.makeText(context, "Serving People selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please select Sering People", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        if (adapter2 != null) {
            int position1 = R_time.getSelectedItemPosition();
            if (position1 > 0) {
                //Toast.makeText(context, "Cook Time selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please select Time", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (adapter != null) {
            int pos = (int) R_category.getSelectedItemId();
            if (pos > 0) {
                //Toast.makeText(context, "Category is selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please select Category", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (R_Desc.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please Enter Recipe Description", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (R_instr.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please Enter Recipe Instructions", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ingredientsAdapter.dataList.size()<=0){
            Toast.makeText(context, "Please add Recipe Ingredients", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathUri);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}