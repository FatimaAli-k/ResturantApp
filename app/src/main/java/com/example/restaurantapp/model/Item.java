package com.example.restaurantapp.model;

public class Item {
    int id, itemPrice;
    String itemName, itemPictureURL;
    byte[] pic;
    public Item(){};

    public Item(int id,String name,int price, byte[] pic ){

        setId(id);
        setItemName(name);
        setItemPrice(price);
        setPic(pic);
    };
//    public Item(String name,int price, byte[] pic ){
//
//        setItemName(name);
//        setItemPrice(price);
//        setPic(pic);
//    };

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }
    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemPictureURL() {
        return itemPictureURL;
    }

    public void setItemPictureURL(String itemPictureURL) {
        this.itemPictureURL = itemPictureURL;
    }


}
