package com.example.restaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantapp.controller.MyDBHandler;
import com.example.restaurantapp.controller.PopUpClass;
import com.example.restaurantapp.model.Cart;
import com.example.restaurantapp.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    MyDBHandler dbHandler;
    List<Item> itemList;
    List<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        dbHandler= new MyDBHandler(this,null);
        FloatingActionButton deleteFab;


        itemList =new ArrayList<Item>();
        cartList =new ArrayList<Cart>();

        Cursor cursor = dbHandler.loadCartHandler();
        if(cursor.getCount()==0){
            Toast.makeText(this,"no c data to retreive",Toast.LENGTH_LONG).show();
        }
        else{
            while (cursor.moveToNext()){
//
                Cart cart1= new Cart(cursor.getInt(0),cursor.getInt(1));
                cartList.add(cart1);


                Cursor cursor2 = dbHandler.loadCartItemsHandler(cursor.getInt(1));
                if(cursor2.getCount()==0){
                    Toast.makeText(this,"no ic data",Toast.LENGTH_LONG).show();
                }
                else{
                    while (cursor2.moveToNext()){
//

                        Item oneItem= new Item(cursor2.getInt(0),cursor2.getString(1),Integer.parseInt(cursor2.getString(2)),cursor2.getBlob(3));
                        itemList.add(oneItem);
//           Log.e("cursur", "onCreate: " +oneItem.getId());


                    }
                }
                cursor2.close();

            }

        }

        cursor.close();




        RecyclerView recyclerView = findViewById(R.id.cart_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(itemList, cartList));

        deleteFab=findViewById(R.id.delete_cart_fab);
        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCart();
            }
        });


    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView itemNameTxv,itemPriceTxv, itemIdTxv, cartIdTxv;

        private ImageView imageView;
        private Button minusBtn;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);


        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup container){
            super(inflater.inflate(R.layout.cart_single_item,container,false));

            itemNameTxv=itemView.findViewById(R.id.item_name_text_view);
            itemPriceTxv=itemView.findViewById(R.id.item_price_text_view);
            itemIdTxv =itemView.findViewById(R.id.item_id_text_view);
            imageView=itemView.findViewById(R.id.item_image_view);
            cartIdTxv =itemView.findViewById(R.id.cart_id_text_view);


            minusBtn=itemView.findViewById(R.id.minus_btn);



        }


    }
    public class RecyclerViewAdapter extends RecyclerView.Adapter<CartActivity.RecyclerViewHolder>{

        private List<Item> mItems;
        private List<Cart> mCart;

        public RecyclerViewAdapter(List<Item> item, List<Cart> cart){
            this.mItems=item;
            this.mCart=cart;
//            notifyDataSetChanged();
//        private List<String> mName,mPrice;
//        List<byte[]> mImage;
//        public RecyclerViewAdapter(List<String> name, List<String> price, List<byte[]> image){
//            this.mName=name;
//            this.mPrice=price;
//            this.mImage=image;


        }

        @NonNull
        @Override
        public CartActivity.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

            return new RecyclerViewHolder(inflater,viewGroup);
        }



        @Override
        public void onBindViewHolder(@NonNull final CartActivity.RecyclerViewHolder recyclerViewHolder, final int i) {

            Item item=mItems.get(i);
            Cart cart=mCart.get(i);
            recyclerViewHolder.cartIdTxv.setText(""+cart.getId());
            recyclerViewHolder.itemNameTxv.setText(item.getItemName());
            recyclerViewHolder.itemPriceTxv.setText(""+item.getItemPrice());
            recyclerViewHolder.itemIdTxv.setText(""+item.getId());
            Bitmap bitmap;

            if(item.getPic()==null)
            {
                bitmap= BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.placeholder);
            }
            else {
                bitmap = getBitmapFromBytes(item.getPic());
            }


//                Uri imageUri = Uri.parse(mUri.get(i));
//
//            Picasso.get().load(imageUri).into(recyclerViewHolder.imageView);
//            Log.e("pica",""+mImage.toString());



//            if (uri != null)
            recyclerViewHolder.imageView.setImageBitmap(bitmap);
//            else
//                recyclerViewHolder.imageView.setImageResource(R.drawable.friedegg);


            recyclerViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopUpClass popUpClass = new PopUpClass();
                    popUpClass.showPopupWindow(view,bitmap);

                }
            });





            updatePriceTotal();
            recyclerViewHolder.minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //pop up dialog
                    //delete if yes
                   boolean s= dbHandler.deleteFromCartHandler((Integer.parseInt(recyclerViewHolder.cartIdTxv.getText().toString())));


//                    Log.e("TAG"+i+" id "+recyclerViewHolder.cartIdTxv.getText().toString(),"n:"+recyclerViewHolder.itemNameTxv.getText()+ "onClick: " );

//                    Log.e("TAG"+i+" id "+recyclerViewHolder.cartIdTxv.getText().toString(), "onClick: "+s );

//                    Log.e("TAG"+i+" id "+cartList.get(i).getId(), "onClick: "+recyclerViewHolder.cartIdTxv.getText().toString()+s );
                    //replace later with adapter refresh
//                    finish();
//                    startActivity(getIntent());

                    if(s) {
                        removeAt(i);
                        updatePriceTotal();
                    }

//                    int t=0;
//                    t=dbHandler.findItemCartHandler(Integer.parseInt(recyclerViewHolder.itemIdTxv.getText().toString()));
//                    Log.e("TAG"+i, "onClick: "+t );

                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }


        private void removeAt(int position) {
            mCart.remove(position);
            mItems.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
            notifyItemRangeChanged(position, mCart.size());
            notifyItemRangeChanged(position, mItems.size());
        }

    }
    // convert from byte array to bitmap
    public static Bitmap getBitmapFromBytes(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void updatePriceTotal(){
        TextView priceTotal;
        priceTotal=findViewById(R.id.total_text_view);
        int t=0;
        Cursor cursor = dbHandler.loadCartHandler();
        if(cursor.getCount()==0){
//            Toast.makeText(this,"no c data to retreive",Toast.LENGTH_LONG).show();
        }
        else {
            while (cursor.moveToNext()) {
//

                t+=dbHandler.findItemCartHandler(cursor.getInt(1));



            }
        }
        cursor.close();
                priceTotal.setText("total price: "+t+" IQD");


    }
    public void deleteCart(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("empty cart? ").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    dbHandler.deleteCartHandler();
                    finish();
                    startActivity(new Intent( CartActivity.this,MainActivity.class));
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };


}