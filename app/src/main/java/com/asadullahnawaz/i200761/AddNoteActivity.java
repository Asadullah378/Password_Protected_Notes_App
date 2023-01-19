package com.asadullahnawaz.i200761;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class AddNoteActivity extends AppCompatActivity implements AddNoteCustomInterface{

    private AddNoteCustomInterface callback;
    ImageView back_btn;
    ImageView save_btn;
    ImageView lock_unlock_btn;
    ImageView note_image;
    TextView toolbar_title;
    ImageView add_image;
    ImageView set_color;
    LinearLayout background;
    boolean lock_status = false;
    String password = "";
    EditText title;
    EditText description;
    String image_uri = "";
    String note_color = "#FFFFFF";
    int n_id;
    boolean image_added = false;
    private final int GALLERY_REQ_CODE=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Intent intent = getIntent();
        callback = this;

        background = findViewById(R.id.note_background);
        set_color = findViewById(R.id.set_color);
        back_btn = findViewById(R.id.back_btn);
        save_btn = findViewById(R.id.save_btn);
        add_image = findViewById(R.id.add_image);
        note_image = findViewById(R.id.note_image);
        title = findViewById(R.id.title_txt);
        description = findViewById(R.id.description_txt);
        lock_unlock_btn = findViewById(R.id.set_password);
        toolbar_title = findViewById(R.id.add_toolbar_title);
        toolbar_title.setText(intent.getStringExtra(MainActivity.CATEGORY_TITLE));
        n_id =  intent.getIntExtra(MainActivity.CATEGORY_ID,0);

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
                send_back();
            }
        });

        lock_unlock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(lock_status==false){

                    SetPasswordBottomSheet bottomSheet = new SetPasswordBottomSheet(callback);
                    bottomSheet.show(getSupportFragmentManager(),"Lock Note");

                }

                else{

                    lock_unlock_btn.setImageResource(R.drawable.unlock_icon);
                    lock_status=false;
                    password = "";
                }
            }
        });

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(image_added == false) {

                    Intent iGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(iGallery,GALLERY_REQ_CODE);

                }

                else{

                    image_uri = "";
                    note_image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0f));
                    image_added=false;
                    add_image.setImageResource(R.drawable.add_image);
                }
            }
        });

        set_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseColorBottomSheet bottomSheet = new ChooseColorBottomSheet(callback);
                bottomSheet.show(getSupportFragmentManager(),"Set Color");
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

                    image_uri = uri.toString();
                    note_image.setImageURI(uri);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 3.0f);
                    note_image.setLayoutParams(lp);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        getContentResolver().takePersistableUriPermission (uri, Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    image_added = true;
                    add_image.setImageResource(R.drawable.remove_image);
                }
            }
        }
    }

    void send_back() {

        MyDBHelper helper = new MyDBHelper(AddNoteActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String n_title = title.getText().toString();
        String n_description = description.getText().toString();
        String n_date = MainActivity.getDateTime();
        String n_lock_status = Boolean.toString(lock_status);
        String n_password = password;
        String n_image = image_uri;

        if (n_title.isEmpty())
            Toast.makeText(getApplicationContext(), "Enter Title", Toast.LENGTH_SHORT).show();

        else if(n_description.isEmpty())
            Toast.makeText(getApplicationContext(),"Enter Description",Toast.LENGTH_SHORT).show();

        else {
            cv.put(MyContracts.Note._TITLE, n_title);
            cv.put(MyContracts.Note._DESCRIPTION, n_description);
            cv.put(MyContracts.Note._LOCK_STATUS, n_lock_status);
            cv.put(MyContracts.Note._PASSWORD, n_password);
            cv.put(MyContracts.Note._IMAGE, n_image);
            cv.put(MyContracts.Note._DATE_CREATED, n_date);
            cv.put(MyContracts.Note._DATE_UPDATED, n_date);
            cv.put(MyContracts.Note._CATEGORY_ID, n_id);
            cv.put(MyContracts.Note._COLOR, note_color);

            double res = db.insert(
                    MyContracts.Note.TABLE_NAME,
                    null,
                    cv);
            db.close();
            helper.close();
            Intent intent = new Intent();
            setResult(RESULT_OK);
            finish();
        }

    }

    public void setPassword(String pw){

        password = pw;
        lock_unlock_btn.setImageResource(R.drawable.lock_icon);
        lock_status=true;

    }

    @Override
    public void setColor(String color) {
        note_color = color;
        background.setBackgroundColor(Color.parseColor(color));
    }

}