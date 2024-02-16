package com.example.restaurantapp.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.model.Cart;
import com.example.restaurantapp.model.Item;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Cart> cartList;
    private List<Item> ItemList;
    private LayoutInflater mInflater;

    private ItemClickListener mClickListener;

    // data is passed into the constructor
//    MyRecyclerViewAdapter(Context context, List<String> data) {
//
//        this.mInflater = LayoutInflater.from(context);
//        this.mData = data;
//    }
   public MyRecyclerViewAdapter(Context context, List<Cart> itemData) {

        this.mInflater = LayoutInflater.from(context);
        this.cartList = itemData;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cart_single_item, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row

    //    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        String id = mData.get(position);
//
//        holder.itemId.setText(id);
//    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Cart cart = cartList.get(position);

        holder.catId.setText(String.valueOf(cart.getId()));
        holder.catId.setVisibility(View.GONE);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return cartList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView catName, catId;


        ViewHolder(View itemView) {
            super(itemView);

//            catName = itemView.findViewById(R.id.catName);
//            catId = itemView.findViewById(R.id.catId);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
//    String getItem(int id) {
//        return mData.get(id);
//    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}