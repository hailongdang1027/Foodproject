package com.example.myfoods.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.myfoods.Model.Favorites;
import com.example.myfoods.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "EatDB.db";
    private static final int DB_VERSION = 2;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public boolean checkFoodExists(String foodID, String userPhone){
        boolean flag = false;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = null;
        String SQLQuery = String.format("SELECT * FROM OrderDetail WHERE UserPhone='%s' AND IdProduct='%s'", userPhone, foodID);
        cursor = database.rawQuery(SQLQuery, null);
        if (cursor.getCount() > 0){
            flag =  true;
        }else {
            flag = false;
        }
        cursor.close();
        return flag;
    }

    public List<Order> getCarts(String userPhone){
        SQLiteDatabase database = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"UserPhone", "NameProduct", "IdProduct", "Quantity", "Price", "Discount", "Image"};
        String sqlTable = "OrderDetail";

        queryBuilder.setTables(sqlTable);
        Cursor cursor = queryBuilder.query(database, sqlSelect, "UserPhone=?", new String[]{userPhone}, null, null, null);
        final List<Order> result = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                result.add(new Order(
                        cursor.getString(cursor.getColumnIndexOrThrow("UserPhone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("IdProduct")),
                        cursor.getString(cursor.getColumnIndexOrThrow("NameProduct")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Discount")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Image"))
                ));
            }while (cursor.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order){
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("INSERT OR REPLACE INTO OrderDetail(UserPhone,IdProduct,NameProduct,Quantity,Price,Discount, Image) VALUES('%s','%s','%s','%s','%s','%s','%s');",
                order.getUserPhone(),
                order.getIdProduct(),
                order.getNameProduct(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                order.getImage());
        database.execSQL(query);
    }

    public void cleanCart(String userPhone){
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone='%s'", userPhone);
        database.execSQL(query);
    }

    public int getCountCart(String userPhone) {
        int count = 0;
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail WHERE UserPhone='%s'", userPhone);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                count = cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        return count;
    }

    public void updateMyCart(Order order) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity='%s' WHERE UserPhone='%s' AND IdProduct='%s'", order.getQuantity(), order.getUserPhone(), order.getIdProduct());
        database.execSQL(query);
    }

    public void increaseCart(String userPhone, String foodID) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity=Quantity+1 WHERE UserPhone='%s' AND IdProduct='%s'", userPhone, foodID);
        database.execSQL(query);
    }

    public void addFavorites(Favorites food){
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(" +
                "FoodID, UserPhone, FoodName, FoodPrice, FoodMenuId, FoodImage, FoodDiscount, FoodDescription) VALUES('%s','%s','%s','%s','%s','%s','%s','%s');",
                food.getFoodID(),
                food.getUserPhone(),
                food.getFoodName(),
                food.getFoodPrice(),
                food.getFoodMenuId(),
                food.getFoodImage(),
                food.getFoodDiscount(),
                food.getFoodDescription());
        database.execSQL(query);
    }

    public void removeFavorites(String foodID, String userPhone){
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE FoodID='%s' and UserPhone='%s';", foodID, userPhone);
        database.execSQL(query);
    }

    public boolean isFavorites(String foodID, String userPhone){
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE FoodID='%s'and UserPhone='%s';", foodID, userPhone);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }



    public void removeFromCart(String idProduct, String phone) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail", phone, idProduct);
        database.execSQL(query);
    }

    public List<Favorites> getAllFavorites(String userPhone){
        SQLiteDatabase database = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"UserPhone", "FoodID", "FoodName", "FoodPrice", "FoodMenuId", "FoodImage", "FoodDiscount", "FoodDescription"};
        String sqlTable = "Favorites";

        queryBuilder.setTables(sqlTable);
        Cursor cursor = queryBuilder.query(database, sqlSelect, "UserPhone=?", new String[]{userPhone}, null, null, null);
        final List<Favorites> result = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                result.add(new Favorites(
                        cursor.getString(cursor.getColumnIndexOrThrow("UserPhone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("FoodID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("FoodName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("FoodPrice")),
                        cursor.getString(cursor.getColumnIndexOrThrow("FoodMenuId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("FoodImage")),
                        cursor.getString(cursor.getColumnIndexOrThrow("FoodDiscount")),
                        cursor.getString(cursor.getColumnIndexOrThrow("FoodDescription"))
                        ));
            }while (cursor.moveToNext());
        }
        return result;
    }


}
