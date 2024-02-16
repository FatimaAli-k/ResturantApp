package com.example.restaurantapp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restaurantapp.model.Cart;
import com.example.restaurantapp.model.Item;

import java.util.ArrayList;
import java.util.List;


public class MyDBHandler extends SQLiteOpenHelper {
    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "restaurantDB.db";
    public static final String TABLE_ITEM = "Item";
    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_PRICE = "Price";
    public static final String COLUMN_PIC = "Pic";

    public static final String TABLE_CART = "Cart";
    public static final String COLUMN_CART_ID = "CartId";
    public static final String COLUMN_ITEM_ID = "ItemId";


    //initialize the database
    public MyDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE  = "CREATE TABLE "
                + TABLE_ITEM + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_NAME + " TEXT , "
                + COLUMN_PRICE + " INTEGER, "
                + COLUMN_PIC + " BLOB " + " )";
        db.execSQL(CREATE_ITEMS_TABLE);

        String CREATE_CART_TABLE  = "CREATE TABLE "
                + TABLE_CART + "("
                + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_ITEM_ID + " INTEGER, "
                + " FOREIGN KEY ("+ COLUMN_ITEM_ID +") REFERENCES "+TABLE_ITEM+"("+COLUMN_ID+")"+")";

        db.execSQL(CREATE_CART_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        onCreate(db);

    }

    public long addItemHandler(String name, int price, byte[] image) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_PIC, image);

        SQLiteDatabase db = this.getWritableDatabase();

        long rowInserted=db.insert(TABLE_ITEM, null, values);

        db.close();
        return rowInserted;

    }
    public Cursor loadItemsHandler() {
        String result = "";
        String query = "Select*FROM " + TABLE_ITEM;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

//        while (cursor.moveToNext()) {
//            int result_0 = cursor.getInt(0);
//            String result_1 = cursor.getString(1);
//            result += String.valueOf(result_0) + " " + result_1 +
//                    System.getProperty("line.separator");
//        }
//        cursor.close();
//        db.close();
//        return result;
        return cursor;
    }
    public Cursor loadCartHandler() {
        String result = "";
        String query = "Select*FROM " + TABLE_CART;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

//        while (cursor.moveToNext()) {
//            int result_0 = cursor.getInt(0);
//            String result_1 = cursor.getString(1);
//            result += String.valueOf(result_0) + " " + result_1 +
//                    System.getProperty("line.separator");
//        }
//        cursor.close();
//        db.close();
//        return result;
        return cursor;
    }
    public Cursor loadCartItemsHandler(int itemId) {
        String result = "";
        String query = "Select*FROM " + TABLE_ITEM + " WHERE " + COLUMN_ID + " = '" + String.valueOf(itemId) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

//        while (cursor.moveToNext()) {
//            int result_0 = cursor.getInt(0);
//            String result_1 = cursor.getString(1);
//            result += String.valueOf(result_0) + " " + result_1 +
//                    System.getProperty("line.separator");
//        }
//        cursor.close();
//        db.close();
//        return result;
        return cursor;
    }




    public long addToCartHandler(int cartItem) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_ITEM_ID, cartItem);


        SQLiteDatabase db = this.getWritableDatabase();

        long rowInserted=db.insert(TABLE_CART, null, values);

        db.close();
        return rowInserted;

    }

    public boolean deleteFromCartHandler(int ItemId) {

        String query = "Select*FROM " + TABLE_CART + " WHERE " + COLUMN_CART_ID + " = '" + String.valueOf(ItemId) + "'";
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_CART, COLUMN_CART_ID + "=" +"'"+ ItemId +"'", null) > 0;
    }
    public boolean deleteCartHandler() {

        String query = "Select*FROM " + TABLE_CART ;
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_CART,null, null) > 0;
    }
    public boolean deleteItemsHandler() {

        String query = "Select*FROM " + TABLE_ITEM ;
        SQLiteDatabase db = this.getWritableDatabase();

       return db.delete(TABLE_ITEM,null, null) > 0;

    }

    public int findItemCartHandler(int ItemId) {
        int result =0;
        String query = "Select Price FROM " + TABLE_ITEM + " WHERE " + COLUMN_ID + " = '" + String.valueOf(ItemId) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);

        }
        cursor.close();
        db.close();
        return result;

    }









//    public String getRow(int r){
//        String res="";
//        String query = "Select*FROM " + TABLE_POSTS;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//
//            cursor.moveToPosition(r);
//            String result_1 = cursor.getString(1);
//            res+=  result_1;
//
//        cursor.close();
//        db.close();
//        return res;
//
//    }



//
//    public boolean updateHandler(int ID, String name) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues args = new ContentValues();
//        args.put(COLUMN_ID, ID);
//        args.put(COLUMN_NAME, name);
//        return db.update(TABLE_POSTS, args, COLUMN_ID + "=" + ID, null) > 0;
//    }
    }
