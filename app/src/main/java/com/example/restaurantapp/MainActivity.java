package com.example.restaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantapp.controller.CircleAnimationUtil;
import com.example.restaurantapp.controller.MyDBHandler;
import com.example.restaurantapp.controller.PopUpClass;
import com.example.restaurantapp.model.Item;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MyDBHandler dbHandler;
    List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        ImageButton newItem,cart,deleteAll;
        TextView numItemsTxv;
        deleteAll=findViewById(R.id.delete_everything_imageButton);
        newItem=findViewById(R.id.add_new_btn);
        cart=findViewById(R.id.cart_btn);
        numItemsTxv=findViewById(R.id.cart_items_num_textView);
        dbHandler= new MyDBHandler(this,null);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();


        if( !pref.getBoolean("dataIsSet",false)){


            insetPlaceHolderData();

            editor.putBoolean("dataIsSet", true);
            editor.apply();
        }
       
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEverything();
            }
        });

        newItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddItem.class));
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });


//
        items=new ArrayList<Item>();


       Cursor cursor = dbHandler.loadItemsHandler();
       if(cursor.getCount()==0){
           Toast.makeText(this,"no data",Toast.LENGTH_LONG).show();
       }
       else{
       while (cursor.moveToNext()){
//
           Item oneItem= new Item(cursor.getInt(0),cursor.getString(1),Integer.parseInt(cursor.getString(2)),cursor.getBlob(3));
           items.add(oneItem);
//           Log.e("cursur", "onCreate: " +oneItem.getId());



       }
       }
       Cursor cursor2 = dbHandler.loadCartHandler();
        numItemsTxv.setText(""+cursor2.getCount());
        cursor2.close();
        cursor.close();




        RecyclerView recyclerView = findViewById(R.id.items_recycler_view);
        int gridSpanCount = getResources().getInteger(R.integer.grid_span_count);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridSpanCount));


        recyclerView.setAdapter(new RecyclerViewAdapter(items));

    }

    private void insetPlaceHolderData() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bundocucu);
        byte[] imgae=getBytesFromBitmap(bitmap);
        dbHandler.addItemHandler("crepe with nuts and Bundoqoque", 10000,imgae );

         bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.cake);
         imgae=getBytesFromBitmap(bitmap);
        dbHandler.addItemHandler("cake", 15000,imgae );

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.juice);
        imgae=getBytesFromBitmap(bitmap);
        dbHandler.addItemHandler("strawberry juice ", 5000,imgae );

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.friedegg);
        imgae=getBytesFromBitmap(bitmap);
        dbHandler.addItemHandler("egg ", 1000,imgae );

        Log.e("mainAct", "insetPlaceHolderData: " );


    }
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView itemNameTxv,itemPriceTxv, itemIdTxv;
        private ImageView imageView,minusBtn;
        private Button plusBtn;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);


        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup container){
            super(inflater.inflate(R.layout.single_item,container,false));

            itemNameTxv=itemView.findViewById(R.id.item_name_text_view);
            itemPriceTxv=itemView.findViewById(R.id.item_price_text_view);

            itemIdTxv =itemView.findViewById(R.id.item_id_text_view);
            imageView=itemView.findViewById(R.id.item_image_view);

            plusBtn=itemView.findViewById(R.id.plus_btn);
            minusBtn=itemView.findViewById(R.id.minus_btn);



        }


    }
    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

        private List<Item> mItems;

        public RecyclerViewAdapter(List<Item> item){
            this.mItems=item;
//        private List<String> mName,mPrice;
//        List<byte[]> mImage;
//        public RecyclerViewAdapter(List<String> name, List<String> price, List<byte[]> image){
//            this.mName=name;
//            this.mPrice=price;
//            this.mImage=image;


        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

            return new RecyclerViewHolder(inflater, viewGroup);
        }


        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewHolder recyclerViewHolder, final int i) {

            Item item=mItems.get(i);
            recyclerViewHolder.itemNameTxv.setText(item.getItemName());
            recyclerViewHolder.itemPriceTxv.setText(""+item.getItemPrice()+" IQD");
            recyclerViewHolder.itemIdTxv.setText(""+item.getId());

            Bitmap bitmap;

            if(item.getPic()==null)
            {
                bitmap=BitmapFactory.decodeResource(getApplicationContext().getResources(),
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



            recyclerViewHolder.plusBtn.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v)
                {

//                    recyclerViewHolder.plusBtn.setVisibility(View.GONE);
//                    recyclerViewHolder.minusBtn.setVisibility(View.VISIBLE);


                    makeFlyAnimation( recyclerViewHolder.minusBtn);

                    long result = dbHandler.addToCartHandler((Integer.parseInt(recyclerViewHolder.itemIdTxv.getText().toString())));

//                    int quantity=Integer.parseInt(recyclerViewHolder.quantityETxt.getText().toString());
//                    quantity+=1;
//                    recyclerViewHolder.quantityETxt.setText(""+quantity);
//                    Toast.makeText(getApplicationContext(), "id " + recyclerViewHolder.itemIdTxv.getText() + " is clicked.", Toast.LENGTH_SHORT).show();
//
//                    setCartCount();

//                    Log.e("TAG"+ i, "onClick: "+ recyclerViewHolder.itemIdTxv.getText() );
                }
            });

//            recyclerViewHolder.minusBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    recyclerViewHolder.minusBtn.setVisibility(View.GONE);
//                    recyclerViewHolder.plusBtn.setVisibility(View.VISIBLE);
////
////                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
////                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
////                            .setNegativeButton("No", dialogClickListener).show();
//                    dbHandler.deleteFromCartHandler((Integer.parseInt(recyclerViewHolder.itemIdTxv.getText().toString())));
////                    int quantity=Integer.parseInt(recyclerViewHolder.quantityETxt.getText().toString());
////                    quantity-=1;
////                    if(quantity<0){
////                        quantity=0;
////                    }
//
//                    setCartCount();
//
//                }
//            });
        }

        @Override
        public int getItemCount() {
            if(mItems != null)
            return mItems.size();
            else
                return 0;
        }



    }
    // convert from byte array to bitmap
    public static Bitmap getBitmapFromBytes(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }



    @Override
    protected void onResume() {
        super.onResume();
        setCartCount();

    }
    public void setCartCount(){
        Cursor cursor2 = dbHandler.loadCartHandler();
        TextView numItemsTxv;
        numItemsTxv=findViewById(R.id.cart_items_num_textView);
        numItemsTxv.setText(""+cursor2.getCount());
        cursor2.close();
    }
    private void deleteEverything() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("delete all items and empty cart? ").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    dbHandler.deleteCartHandler();
                    dbHandler.deleteItemsHandler();
                    finish();
                    startActivity(getIntent());

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };


    private void makeFlyAnimation( ImageView targetView) {




        TextView destView =  findViewById(R.id.cart_items_num_textView);

        new CircleAnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(1000).setDestView(destView).setAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                setCartCount();
//                Toast.makeText(MainActivity.this, "Continue Shopping...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).startAnimation();


    }

}