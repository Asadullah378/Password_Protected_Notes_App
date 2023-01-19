package com.asadullahnawaz.i200761;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements ViewNoteCustomInterface{

    Context context;
    static ArrayList<NoteModel>notes_list;
    ViewNoteCustomInterface callback;
    public static final String CATEGORY_TITLE = "com.asadullahnawaz.i200761.category";
    public static final String ID = "com.asadullahnawaz.i200761.id";
    String category;
    Boolean show_updated;
    Boolean linear_layout;
    int screen_width;

    public NoteAdapter(Context context, ArrayList<NoteModel> list, String category, Boolean show_updated, Boolean linear_layout, int screen_width) {
        this.context = context;
        this.notes_list = list;
        this.category = category;
        this.callback = this;
        this.show_updated = show_updated;
        this.linear_layout = linear_layout;
        this.screen_width = screen_width;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        if(linear_layout == true) {
            v = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        }
        else{
            v = LayoutInflater.from(context).inflate(R.layout.grid_note_item, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.note_title.setText(notes_list.get(position).title);

        if(linear_layout==true) {
            if (show_updated == true)
                holder.note_date.setText(MainActivity.formatDate(notes_list.get(position).date_updated, "MMM dd, yyyy  hh:mm a"));
            else
                holder.note_date.setText(MainActivity.formatDate(notes_list.get(position).date, "MMM dd, yyyy  hh:mm a"));
        }
        else{
            if (show_updated == true) {
                holder.note_date.setText(MainActivity.formatDate(notes_list.get(position).date_updated, "MMM dd, yyyy"));
                holder.note_time.setText(MainActivity.formatDate(notes_list.get(position).date_updated, "hh:mm a"));
            }

            else {
                holder.note_date.setText(MainActivity.formatDate(notes_list.get(position).date, "MMM dd, yyyy"));
                holder.note_time.setText(MainActivity.formatDate(notes_list.get(position).date, "hh:mm a"));
            }
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(notes_list.get(holder.getAdapterPosition()).lock_status==false) {
                    Intent intent = new Intent(context, ViewNoteActivity.class);
                    intent.putExtra(CATEGORY_TITLE, category);
                    intent.putExtra(ID,notes_list.get(holder.getAdapterPosition()).id);
                    ((AppCompatActivity)context).startActivityForResult(intent, MainActivity.TEXT_REQUEST);
                }

                else{

                    UnlockNoteBottomSheet bottomSheet = new UnlockNoteBottomSheet(callback,holder.getAdapterPosition(),notes_list.get(holder.getAdapterPosition()).password);
                    bottomSheet.show(((FragmentActivity)context).getSupportFragmentManager(),"Unlock Note");
                }

            }
        });

        if(!notes_list.get(position).lock_status){

            holder.lock_image.setImageResource(R.drawable.unlock_icon);

        }

        if(linear_layout) {
            if (holder.getAdapterPosition() % 2 == 0) {
                holder.item.setCardBackgroundColor(Color.parseColor("#d7dde9"));
            } else {
                holder.item.setCardBackgroundColor(Color.parseColor("#C1D3F8"));
            }

        }
        else{
            if (holder.getAdapterPosition()%4 == 0) {
                holder.item.setCardBackgroundColor(Color.parseColor("#d7dde9"));
            } else if (holder.getAdapterPosition()%4 == 1){
                holder.item.setCardBackgroundColor(Color.parseColor("#C1D3F8"));
            } else if (holder.getAdapterPosition()%4 == 2){
                holder.item.setCardBackgroundColor(Color.parseColor("#C1D3F8"));
            } else if (holder.getAdapterPosition()%4 == 3){
                holder.item.setCardBackgroundColor(Color.parseColor("#d7dde9"));
            }
        }



    }

    @Override
    public int getItemCount() {
        return notes_list.size();
    }



    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView note_title;
        TextView note_date;
        TextView note_time;
        CardView item;
        ImageView lock_image;
        LinearLayout grid_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            note_title = itemView.findViewById(R.id.note_title);
            note_date = itemView.findViewById(R.id.note_date);
            item = itemView.findViewById(R.id.note_card);
            lock_image = itemView.findViewById(R.id.lock_unlock);

            if(!linear_layout) {
                note_time = itemView.findViewById(R.id.note_time);
                grid_layout = itemView.findViewById(R.id.grid_linear_layout);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screen_width/2-20,screen_width/2-20,1);
                lp.setMargins(10,10,10,10);
                grid_layout.setLayoutParams(lp);

            }

        }
    }

    @Override
    public void verifyPassword(String password, int position, String actual_password) {

        if(password.equals(actual_password)){

            Intent intent = new Intent(context, ViewNoteActivity.class);
            intent.putExtra(CATEGORY_TITLE, category);
            intent.putExtra(ID,notes_list.get(position).id);
            ((AppCompatActivity)context).startActivityForResult(intent, MainActivity.TEXT_REQUEST);

        }

        else {

            Toast.makeText(context.getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
        }

    }

}
