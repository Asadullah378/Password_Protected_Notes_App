package com.asadullahnawaz.i200761;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    ArrayList<CategoryModel> category_list;
    MainActivityCustomInterface callback;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> list, MainActivityCustomInterface callback) {
        this.context = context;
        this.category_list = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.category_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.category_title.setText(category_list.get(position).title);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callback.setCurrentCategory(category_list.get(holder.getAdapterPosition()).id,category_list.get(holder.getAdapterPosition()).title);
                callback.closeDrawer();
            }
        });

        if(holder.getAdapterPosition()%2==0){
            holder.item.setCardBackgroundColor(Color.parseColor("#d7dde9"));
        }

        else{
            holder.item.setCardBackgroundColor(Color.parseColor("#C1D3F8"));
        }

    }

    @Override
    public int getItemCount() {
        return category_list.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView category_title;
        CardView item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_title = itemView.findViewById(R.id.category_title);
            item = itemView.findViewById(R.id.item_card);

        }
    }

}
