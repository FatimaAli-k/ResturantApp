package com.example.restaurantapp.model;

public class Cart {
    int id, itemId;

    public Cart (){};

    public Cart (int id,int itemId){
        setId(id);
        setItemId(itemId);

    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

}
