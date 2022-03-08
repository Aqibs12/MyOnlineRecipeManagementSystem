package com.example.fitrecipes.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


import com.example.fitrecipes.Models.ImagesModel;
import com.example.fitrecipes.Models.IngredientModel;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.Models.UserModel;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "fitrecipedb";

    // Table Names
    private static final String TABLE_USERS = "tbl_users";
    private static final String TABLE_RECIPES = "tbl_recipes";
    private static final String TABLE_INGREDIENTS = "tbl_ingredients";
    private static final String TABLE_IMAGES = "tbl_images";



    //	Field names
    private static final String KEY_ID = "id";
    private static final String KEY_RECIPE_ID = "recipe_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_name = "name";
    private static final String KEY_email = "email";
    private static final String KEY_phone = "phone";
    private static final String KEY_photo = "photo";
    private static final String KEY_serving_persons = "serving_persons";
    private static final String KEY_cook_time = "cook_time";
    private static final String KEY_category = "category";
    private static final String KEY_desc = "description";
    private static final String KEY_instruc = "instruc";
    private static final String KEY_ingredient = "ingredient";
    private static final String KEY_password = "password";
    private static final String KEY_security_question = "security_question";
    private static final String KEY_security_answer = "security_answer";






    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_name + " TEXT,"
            + KEY_email + " TEXT,"
            + KEY_password + " TEXT,"
            + KEY_phone + " TEXT,"
            + KEY_security_question + " TEXT,"
            + KEY_security_answer + " TEXT"
            + ")";

   private static final String CREATE_TABLE_RECIPES = "CREATE TABLE "
            + TABLE_RECIPES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            +KEY_USER_ID + " INTEGER,"
            + KEY_name + " TEXT,"
            + KEY_serving_persons + " TEXT,"
            + KEY_cook_time + " TEXT,"
            + KEY_category + " TEXT,"
            + KEY_desc + " TEXT,"
            + KEY_instruc + " TEXT"
            + ")";

    private static final String CREATE_TABLE_INGREDIENTS = "CREATE TABLE "
                + TABLE_INGREDIENTS
                + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                +KEY_RECIPE_ID + " INTEGER,"
                + KEY_name + " TEXT"
                + ")";
    private static final String CREATE_TABLE_IMAGES = "CREATE TABLE "
                + TABLE_IMAGES
                + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                +KEY_RECIPE_ID + " INTEGER,"
                + KEY_name + " TEXT"
                + ")";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_INGREDIENTS);
        db.execSQL(CREATE_TABLE_IMAGES);


    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);



        // create new tables
        onCreate(db);
    }



    public String saveUserData(UserModel c) {
        String returnValue="added";
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery1 = "SELECT  * FROM " + TABLE_USERS+ " WHERE "
                + KEY_email + " = '"+c.getEmail()+"'";
        Cursor c2 = db.rawQuery(selectQuery1, null);
        int count2 = c2.getCount();
        c2.close();
        if (count2>0) {
            returnValue="existing";
        }
        else {
            db = this.getWritableDatabase();
            String sql = "INSERT INTO " + TABLE_USERS + " VALUES(?, ?, ?,?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            try {

                statement.clearBindings();
                statement.bindString(2, c.getName());
                statement.bindString(3, c.getEmail());
                statement.bindString(4, c.getPassword());
                statement.bindString(5, c.getPhone());
                statement.bindString(6, c.getSecurity_question());
                statement.bindString(7, c.getSecurity_answer());


                statement.execute();

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

        }

        db.close();
       return returnValue;
    }

    public String saveRecipeData(RecipeModel c, ArrayList<IngredientModel> ingredientModelArrayList, ArrayList<ImagesModel> imagesModelArrayList) {
        String returnValue="added";
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery1 =  "SELECT  * FROM " + TABLE_RECIPES + " WHERE "
                + KEY_name + " = '"+c.getName() +"' AND " +KEY_USER_ID+ " = '"+c.getUser_id() +"'";
        Cursor c2 = db.rawQuery(selectQuery1, null);
        int count2 = c2.getCount();
        c2.close();
        if (count2>0) {
            returnValue="existing";
        }
        else {
            db = this.getWritableDatabase();
            String sql = "INSERT INTO " + TABLE_RECIPES + " VALUES(?, ?, ?,?, ?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            try {

                statement.clearBindings();
                statement.bindLong(2, Long.parseLong(String.valueOf(c.getUser_id())));
                statement.bindString(3, c.getName());
                statement.bindString(4, c.getServing_persons());
                statement.bindString(5, c.getCook_time());
                statement.bindString(6, c.getCategory());
                statement.bindString(7, c.getDescription());
                statement.bindString(8, c.getInstruc());


                statement.execute();

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

            int recipeId=getLastAddedRowId(db);
            saveIngredients(ingredientModelArrayList,recipeId);
            saveImages(imagesModelArrayList,recipeId);

        }

        db.close();
       return returnValue;
    }
    public void saveIngredients(List<IngredientModel> ingredientModelList,int lastId) {
        List<IngredientModel> list = new ArrayList<IngredientModel>();
        SQLiteDatabase db = this.getReadableDatabase();

        for (int i = 0; i < ingredientModelList.size(); i++) {

                db = this.getWritableDatabase();
                String sql = "INSERT INTO " + TABLE_INGREDIENTS + " VALUES(?, ?, ?)";
                SQLiteStatement statement = db.compileStatement(sql);
                db.beginTransaction();

                try {

                    statement.clearBindings();
                    statement.bindLong(2, Long.parseLong(String.valueOf(lastId)));
                    statement.bindString(3, ingredientModelList.get(i).getIngredient());
                    statement.execute();

                    db.setTransactionSuccessful();
                } catch (Exception e) {

                } finally {
                    db.endTransaction();
                }
            }



        db.close();

    }
    public void saveImages(List<ImagesModel> imagesModelList,int lastId) {
        List<IngredientModel> list = new ArrayList<IngredientModel>();
        SQLiteDatabase db = this.getReadableDatabase();

        for (int i = 0; i < imagesModelList.size(); i++) {

                db = this.getWritableDatabase();
                String sql = "INSERT INTO " + TABLE_IMAGES+ " VALUES(?, ?, ?)";
                SQLiteStatement statement = db.compileStatement(sql);
                db.beginTransaction();

                try {

                    statement.clearBindings();
                    statement.bindLong(2, Long.parseLong(String.valueOf(lastId)));
                    statement.bindString(3, imagesModelList.get(i).getImage());
                    statement.execute();

                    db.setTransactionSuccessful();
                } catch (Exception e) {

                } finally {
                    db.endTransaction();
                }
            }



        db.close();

    }

    public int getLastAddedRowId(SQLiteDatabase database) {
        String queryLastRowInserted = "select last_insert_rowid()";

        final Cursor cursor = database.rawQuery(queryLastRowInserted, null);
        int _idLastInsertedRow = 0;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    _idLastInsertedRow = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }

        return _idLastInsertedRow;

    }
    public List<UserModel> getLoginData(String email , String pass) {
        List<UserModel> list = new ArrayList<UserModel>();
        String selectQuery1 = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_email + " = '"+email +"' AND " +KEY_password+ " = '"+pass +"'";


        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor c1 = db1.rawQuery(selectQuery1, null);

        if (c1.moveToFirst()) {
            do {
                UserModel userModel = new UserModel();
                userModel.setId(c1.getInt(c1.getColumnIndex(KEY_ID)));
                userModel.setName(c1.getString(c1.getColumnIndex(KEY_name)));
                userModel.setPhone(c1.getString(c1.getColumnIndex(KEY_phone)));
                userModel.setEmail(c1.getString(c1.getColumnIndex(KEY_email)));
                userModel.setPassword(c1.getString(c1.getColumnIndex(KEY_password)));
                userModel.setSecurity_answer(c1.getString(c1.getColumnIndex(KEY_security_answer)));
                userModel.setSecurity_question(c1.getString(c1.getColumnIndex(KEY_security_question)));

                // adding to todo list
                list.add(userModel);
            } while (c1.moveToNext());
        }
        c1.close();
        db1.close();
        return list;
    }

    public void deleteRecipe(int id){
        String query = "Delete from " + TABLE_RECIPES + " WHERE "+ KEY_ID +" = "+id;

        SQLiteDatabase db1 = this.getReadableDatabase();
        db1.execSQL(query);
        db1.close();

    }

    public UserModel getUser(int id) {
        String selectQuery1 = "SELECT  * FROM " + TABLE_USERS + " WHERE " + KEY_ID + " = "+id ;

        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor c1 = db1.rawQuery(selectQuery1, null);

        if (c1.moveToFirst()) {
                UserModel userModel = new UserModel();
                userModel.setId(c1.getInt(c1.getColumnIndex(KEY_ID)));
                userModel.setName(c1.getString(c1.getColumnIndex(KEY_name)));
                userModel.setPhone(c1.getString(c1.getColumnIndex(KEY_phone)));
                userModel.setEmail(c1.getString(c1.getColumnIndex(KEY_email)));
                userModel.setPassword(c1.getString(c1.getColumnIndex(KEY_password)));
                userModel.setSecurity_answer(c1.getString(c1.getColumnIndex(KEY_security_answer)));
                userModel.setSecurity_question(c1.getString(c1.getColumnIndex(KEY_security_question)));
                return userModel;
            }

        c1.close();
        db1.close();
        return UserModel.getAnonymousUser();
    }


    public List<ImagesModel> getAllImagesData(String recipeId) {
        List<ImagesModel> list = new ArrayList<ImagesModel>();
        String selectQuery1 = "SELECT  * FROM " + TABLE_IMAGES + " WHERE "
                + KEY_RECIPE_ID + " = '"+recipeId +"'";


        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor c1 = db1.rawQuery(selectQuery1, null);

        if (c1.moveToFirst()) {
            do {
                ImagesModel imagesModel = new ImagesModel();
                imagesModel.setId(c1.getInt(c1.getColumnIndex(KEY_ID)));
                imagesModel.setRecipeId(c1.getInt(c1.getColumnIndex(KEY_RECIPE_ID)));
                imagesModel.setImage(c1.getString(c1.getColumnIndex(KEY_name)));



                // adding to todo list
                list.add(imagesModel);
            } while (c1.moveToNext());
        }
        c1.close();
        db1.close();


        return list;
    }
    public List<IngredientModel> getAllIngrediebtData(String recipeId) {
        List<IngredientModel> list = new ArrayList<IngredientModel>();
        String selectQuery1 = "SELECT  * FROM " + TABLE_INGREDIENTS + " WHERE "
                + KEY_RECIPE_ID + " = '"+recipeId +"'";


        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor c1 = db1.rawQuery(selectQuery1, null);

        if (c1.moveToFirst()) {
            do {
                IngredientModel ingredientModel = new IngredientModel();
                ingredientModel.setId(c1.getInt(c1.getColumnIndex(KEY_ID)));
                ingredientModel.setRecipeId(c1.getInt(c1.getColumnIndex(KEY_RECIPE_ID)));
                ingredientModel.setIngredient(c1.getString(c1.getColumnIndex(KEY_name)));



                // adding to todo list
                list.add(ingredientModel);
            } while (c1.moveToNext());
        }
        c1.close();
        db1.close();


        return list;
    }
    public List<RecipeModel> getAllRecipeData(String usrId) {

        List<RecipeModel> list = new ArrayList<RecipeModel>();
        String selectQuery1 = "SELECT  * FROM " + TABLE_RECIPES;


        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor c1 = db1.rawQuery(selectQuery1, null);

        if (c1.moveToFirst()) {
            do {
                RecipeModel recipeModel = new RecipeModel();
                recipeModel.setId(c1.getInt(c1.getColumnIndex(KEY_ID)));
                recipeModel.setUser_id(c1.getInt(c1.getColumnIndex(KEY_USER_ID)));
                recipeModel.setName(c1.getString(c1.getColumnIndex(KEY_name)));
                recipeModel.setServing_persons(c1.getString(c1.getColumnIndex(KEY_serving_persons)));
                recipeModel.setCook_time(c1.getString(c1.getColumnIndex(KEY_cook_time)));
                recipeModel.setCategory(c1.getString(c1.getColumnIndex(KEY_category)));
                recipeModel.setDescription(c1.getString(c1.getColumnIndex(KEY_desc)));
                recipeModel.setInstruc(c1.getString(c1.getColumnIndex(KEY_instruc)));
                recipeModel.setIngredientModelArrayList(getAllIngrediebtData(String.valueOf(recipeModel.getId())));
                recipeModel.setImagesModelArrayList(getAllImagesData(String.valueOf(recipeModel.getId())));
                if(recipeModel.getUser_id()>0)
                {
                    /** Adding the owner user to recipe model */
                    recipeModel.setUser(getUser(recipeModel.getUser_id()));
                }
                else {
                    /** Adding the anonymous user to recipe model */
                    recipeModel.setUser(UserModel.getAnonymousUser());
                }



                // adding to todo list
                list.add(recipeModel);
            } while (c1.moveToNext());
        }
        c1.close();
        db1.close();


        return list;
    }

    public List<UserModel> getForgotPasswordData(String email) {
        List<UserModel> list = new ArrayList<UserModel>();
        String selectQuery1 = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_email + " = '"+email +"'";


        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor c1 = db1.rawQuery(selectQuery1, null);

        if (c1.moveToFirst()) {
            do {
                UserModel userModel = new UserModel();
                userModel.setId(c1.getInt(c1.getColumnIndex(KEY_ID)));
                userModel.setSecurity_answer(c1.getString(c1.getColumnIndex(KEY_security_answer)));
                userModel.setSecurity_question(c1.getString(c1.getColumnIndex(KEY_security_question)));

                // adding to todo list
                list.add(userModel);
            } while (c1.moveToNext());
        }
        c1.close();
        db1.close();
        return list;
    }

    public void updatePassword(String id , String pass) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_password,pass);
        database.update(TABLE_USERS, cv, ""+KEY_ID+"= '"+ id +"'", null);
        database.close();

    }
    public void updateUserData(UserModel c) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_name,c.getName());
        cv.put(KEY_email,c.getEmail());
        cv.put(KEY_phone,c.getPhone());
        cv.put(KEY_password,c.getPassword());
        cv.put(KEY_security_question,c.getSecurity_question());
        cv.put(KEY_security_answer,c.getSecurity_answer());
        database.update(TABLE_USERS, cv, ""+KEY_ID+"= '"+ c.getId() +"'", null);
        database.close();

    }

    public void updateRecipeData(RecipeModel c, ArrayList<IngredientModel> ingredientModelArrayList, ArrayList<ImagesModel> imagesModelArrayList) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_name,c.getName());
        cv.put(KEY_serving_persons,c.getServing_persons());
        cv.put(KEY_cook_time,c.getCook_time());
        cv.put(KEY_category,c.getCategory());
        cv.put(KEY_desc,c.getDescription());
        cv.put(KEY_instruc,c.getInstruc());
        database.update(TABLE_RECIPES, cv, ""+KEY_ID+"= '"+ c.getId() +"'", null);
        database.close();
        deleteIngredients(String.valueOf(c.getId()));
        deleteImage(String.valueOf(c.getId()));
        saveIngredients(ingredientModelArrayList,c.getId());
        saveImages(imagesModelArrayList,c.getId());

    }
    public void deleteImage(String img) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + TABLE_IMAGES + " where recipe_id='" + Integer.parseInt(img) + "'");

        db.close();
    }
    public void deleteIngredients(String ing) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + TABLE_INGREDIENTS + " where recipe_id='" + Integer.parseInt(ing) + "'");

        db.close();
    }





}
