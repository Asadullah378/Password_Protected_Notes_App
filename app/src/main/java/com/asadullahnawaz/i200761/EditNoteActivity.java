package com.asadullahnawaz.i200761;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditNoteActivity extends AppCompatActivity implements AddNoteCustomInterface{

    private AddNoteCustomInterface callback;
    int n_id;
    String title;
    String n_title;
    String n_description;
    String n_image;
    String n_color="#FFFFFF";
    LinearLayout note_backgorund;
    Boolean n_lock_status;
    String n_password;
    SQLiteDatabase db;
    ImageView edit_color;
    ImageView edit_password;
    ImageView edit_image;
    ImageView back_btn;
    ImageView save_btn;
    TextView toolbar_title;
    EditText note_title;
    EditText note_description;
    ImageView note_image;
    Boolean image_added;
    private final int GALLERY_REQ_CODE=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Intent intent = getIntent();

        callback = this;
        note_backgorund = findViewById(R.id.edit_note_background);
        toolbar_title = findViewById(R.id.edit_toolbar_title);
        edit_password = findViewById(R.id.edit_password);
        edit_color = findViewById(R.id.edit_color);
        edit_image = findViewById(R.id.edit_image);
        back_btn = findViewById(R.id.edit_back_btn);
        save_btn = findViewById(R.id.edit_btn);
        note_title = findViewById(R.id.edit_title_txt);
        note_description = findViewById(R.id.edit_description_txt);
        note_image = findViewById(R.id.edit_note_image);

        n_id = intent.getIntExtra(ViewNoteActivity.CATEGORY_ID,0);
        title = intent.getStringExtra(ViewNoteActivity.CATEGORY_TITLE);
        toolbar_title.setText(title);
        getNoteData();



        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(image_added == false) {

                    Intent iGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(iGallery,GALLERY_REQ_CODE);

                }

                else{

                    n_image = "";
                    note_image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0f));
                    image_added=false;
                    edit_image.setImageResource(R.drawable.add_image);
                }
            }
        });

        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(n_lock_status==false){

                    SetPasswordBottomSheet bottomSheet = new SetPasswordBottomSheet(callback);
                    bottomSheet.show(getSupportFragmentManager(),"Lock Note");

                }

                else{

                    edit_password.setImageResource(R.drawable.unlock_icon);
                    n_lock_status=false;
                    n_password = "";
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(RESULT_CANCELED);
                finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDBHelper helper=new MyDBHelper(EditNoteActivity.this);
                SQLiteDatabase db=helper.getWritableDatabase();
                ContentValues cv=new ContentValues();

                n_title = note_title.getText().toString();
                n_description = note_description.getText().toString();

                if(n_title.isEmpty())
                    Toast.makeText(getApplicationContext(),"Title Cannot be Empty",Toast.LENGTH_SHORT).show();

                else if(n_description.isEmpty())
                    Toast.makeText(getApplicationContext(),"Description Cannot be Empty",Toast.LENGTH_SHORT).show();

                else {

                    cv.put(MyContracts.Note._TITLE, n_title);
                    cv.put(MyContracts.Note._DESCRIPTION, n_description);
                    cv.put(MyContracts.Note._IMAGE, n_image);
                    cv.put(MyContracts.Note._LOCK_STATUS, Boolean.toString(n_lock_status));
                    cv.put(MyContracts.Note._PASSWORD, n_password);
                    cv.put(MyContracts.Note._DATE_UPDATED, MainActivity.getDateTime());
                    cv.put(MyContracts.Note._COLOR, n_color);
                    db.update(MyContracts.Note.TABLE_NAME, cv, MyContracts.Note._ID + "=" + n_id, null);
                    db.close();
                    helper.close();
                    setResult(RESULT_OK);
                    finish();
                }

            }
        });

        edit_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseColorBottomSheet bottomSheet = new ChooseColorBottomSheet(callback);
                bottomSheet.show(getSupportFragmentManager(),"Edit Color");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==GALLERY_REQ_CODE)
            {
                Uri uri=(Uri)data.getData();

                if(uri!=null){

                    n_image = uri.toString();
                    note_image.setImageURI(uri);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 3.0f);
                    note_image.setLayoutParams(lp);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        getContentResolver().takePersistableUriPermission (uri, Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    image_added = true;
                    edit_image.setImageResource(R.drawable.remove_image);
                }
            }
        }
    }

    @SuppressLint("Range")
    private void getNoteData() {
        MyDBHelper dbHelper=new MyDBHelper(EditNoteActivity.this);
        String[] projection=new String[]{MyContracts.Note._TITLE,MyContracts.Note._DESCRIPTION,MyContracts.Note._IMAGE, MyContracts.Note._LOCK_STATUS, MyContracts.Note._PASSWORD, MyContracts.Note._COLOR};
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
            n_lock_status = Boolean.parseBoolean(c.getString(c.getColumnIndex(MyContracts.Note._LOCK_STATUS)));
            n_password = c.getString(c.getColumnIndex(MyContracts.Note._PASSWORD));
            n_color = c.getString(c.getColumnIndex(MyContracts.Note._COLOR));
        }

        db.close();
        dbHelper.close();
        c.close();

        note_title.setText(n_title);
        note_description.setText(n_description);
        note_backgorund.setBackgroundColor(Color.parseColor(n_color));

        if(!n_image.equals("")){
            image_added = true;
            note_image.setImageURI(Uri.parse(n_image));
            edit_image.setImageResource(R.drawable.remove_image);

        }

        else{

            image_added = false;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0f);
            note_image.setLayoutParams(lp);
            edit_image.setImageResource(R.drawable.add_image);
        }

        if(n_lock_status==true){
            edit_password.setImageResource(R.drawable.lock_icon);
        }

        else{
            edit_password.setImageResource(R.drawable.unlock_icon);

        }

    }

    @Override
    public void setPassword(String pw) {

        n_password = pw;
        edit_password.setImageResource(R.drawable.lock_icon);
        n_lock_status=true;

    }

    @Override
    public void setColor(String color) {
        n_color = color;
        note_backgorund.setBackgroundColor(Color.parseColor(n_color));
    }
}