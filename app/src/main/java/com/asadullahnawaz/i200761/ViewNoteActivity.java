package com.asadullahnawaz.i200761;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewNoteActivity extends AppCompatActivity {

    EditText note_title;
    ImageView back_btn;
    EditText note_description;
    ImageView note_image;
    ImageView delete_note;
    ImageView edit_note;
    TextView toolbar_title;
    LinearLayout note_background;
    int n_id;
    String title;
    String n_title;
    String n_description;
    String n_image;
    String n_color="#FFFFFF";
    SQLiteDatabase db;
    Boolean edit_status;

    public static final String CATEGORY_TITLE = "com.asadullahnawaz.i200761.title";
    public static final String CATEGORY_ID = "com.asadullahnawaz.i200761.id";
    public static final int TEXT_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        Intent intent = getIntent();

        edit_status = false;
        note_background = findViewById(R.id.view_note_background);
        note_title = findViewById(R.id.view_title_txt);
        note_description = findViewById(R.id.view_description_txt);
        note_image = findViewById(R.id.view_note_image);
        toolbar_title = findViewById(R.id.view_toolbar_title);
        back_btn = findViewById(R.id.view_back_btn);
        delete_note = findViewById(R.id.delete_note);
        edit_note = findViewById(R.id.edit_note);
        n_id = intent.getIntExtra(NoteAdapter.ID,-1);
        title = intent.getStringExtra(NoteAdapter.CATEGORY_TITLE);
        toolbar_title.setText(title);
        getNoteData();



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edit_status==false)
                    setResult(RESULT_CANCELED);
                else
                    setResult(RESULT_OK);

                finish();
            }
        });

        delete_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDBHelper helper = new MyDBHelper(ViewNoteActivity.this);
                db=helper.getWritableDatabase();
                db.delete(MyContracts.Note.TABLE_NAME,MyContracts.Note._ID +"="+ n_id,null);
                setResult(RESULT_OK);
                finish();
            }
        });

        edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ViewNoteActivity.this, EditNoteActivity.class);
                intent.putExtra(CATEGORY_TITLE, title);
                intent.putExtra(CATEGORY_ID,n_id);
                startActivityForResult(intent, TEXT_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==TEXT_REQUEST){

            if(resultCode==RESULT_OK){

                edit_status = true;
                getNoteData();
            }
        }
    }

    @SuppressLint("Range")
    private void getNoteData() {
        MyDBHelper dbHelper=new MyDBHelper(ViewNoteActivity.this);
        String[] projection=new String[]{MyContracts.Note._TITLE,MyContracts.Note._DESCRIPTION,MyContracts.Note._IMAGE, MyContracts.Note._COLOR};
        db=dbHelper.getReadableDatabase();
        Cursor c = db.query(
                MyContracts.Note.TABLE_NAME,
                projection,
                MyContracts.Note._ID + " = " + n_id,
                null,
                null,
                null,
                null
        );

        while (c.moveToNext())
        {
                    n_title = c.getString(c.getColumnIndex(MyContracts.Note._TITLE));
                    n_description = c.getString(c.getColumnIndex(MyContracts.Note._DESCRIPTION));
                    n_image = c.getString(c.getColumnIndex(MyContracts.Note._IMAGE));
                    n_color = c.getString(c.getColumnIndex(MyContracts.Note._COLOR));
        }

        db.close();
        dbHelper.close();
        c.close();

        note_title.setText(n_title);
        note_description.setText(n_description);
        note_background.setBackgroundColor(Color.parseColor(n_color));


        if(!n_image.equals("")){
            System.out.println("IMAGE NOT EMPTY");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 3f);
            note_image.setLayoutParams(lp);
            note_image.setImageURI(Uri.parse(n_image));

        }

        else{
            System.out.println("IMAGE EMPTY");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0f);
            note_image.setLayoutParams(lp);
        }

    }

}