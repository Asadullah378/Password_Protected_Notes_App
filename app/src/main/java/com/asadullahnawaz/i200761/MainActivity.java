package com.asadullahnawaz.i200761;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements MainActivityCustomInterface,AdapterView.OnItemSelectedListener {

    private MainActivityCustomInterface callback;
    RecyclerView recyclerView_category;
    CategoryAdapter category_adpater;
    RecyclerView recyclerView_notes;
    NoteAdapter notes_adpater;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    ArrayList<CategoryModel> categories;
    ArrayList<NoteModel> notes;
    ArrayList<NoteModel> notesVisible;
    Button addCategory;
    int current_category;
    FloatingActionButton addNote;
    TextView toolbar_title;
    String category_title;
    SQLiteDatabase db;
    EditText search_txt;
    ImageView search_btn;
    ImageView drawer_btn;
    ImageView refresh_btn;
    ImageView layout_btn;
    Spinner sorting_spinner;
    String order_by;
    int screen_height;
    int screen_width;
    Boolean linear_layout = true;
    boolean show_updated=true;
    boolean searched=false;
    public static final String CATEGORY_TITLE = "com.asadullahnawaz.i200761.category";
    public static final String CATEGORY_ID = "com.asadullahnawaz.i200761.id";
    public static final int TEXT_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyDBHelper dbHelper=new MyDBHelper(MainActivity.this);
        db=dbHelper.getReadableDatabase();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screen_height = displayMetrics.heightPixels;
        screen_width = displayMetrics.widthPixels;

        callback = this;
        order_by=MyContracts.Note._TITLE+" ASC";
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.drawerNavigationView);
        addCategory = findViewById(R.id.add_category_btn);
        drawer_btn = findViewById(R.id.drawer_btn);
        layout_btn = findViewById(R.id.layout_btn);
        toolbar_title = findViewById(R.id.toolbar_title);
        refresh_btn = findViewById(R.id.refresh_btn);
        addNote = findViewById(R.id.add_note_btn);
        search_txt = findViewById(R.id.search_txt);
        search_btn = findViewById(R.id.search_btn);
        toolbar = findViewById(R.id.toolbar);
        sorting_spinner = findViewById(R.id.sortSpinner);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        recyclerView_category = findViewById(R.id.RecyclerViewCategory);
        recyclerView_category.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_category.addItemDecoration(new DividerItemDecoration(recyclerView_category.getContext(), DividerItemDecoration.VERTICAL));
        getCategoryData();

        recyclerView_notes = findViewById(R.id.RecyclerViewNotes);

        category_title = "Notes";
        current_category = 1;
        toolbar_title.setText(category_title);
        getNoteData(null,order_by);

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCategoryBottomSheet bottomSheet = new AddCategoryBottomSheet(callback);
                bottomSheet.show(getSupportFragmentManager(),"Add Category Sheet");


            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
                intent.putExtra(CATEGORY_TITLE,toolbar_title.getText());
                intent.putExtra(CATEGORY_ID,current_category);
                startActivityForResult(intent,TEXT_REQUEST);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!search_txt.getText().toString().isEmpty()) {
                    searched=true;
                    getNoteData(MyContracts.Note._TITLE + " LIKE '%" + search_txt.getText().toString() + "%' or " + MyContracts.Note._DATE_CREATED + " LIKE '%" + search_txt.getText().toString() + "%'", order_by);
                }

                else{
                    Toast.makeText(getApplicationContext(),"Enter Text to Search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searched==true)
                    getNoteData(null,order_by);

                search_txt.setText("");
                search_txt.clearFocus();
                searched=false;
            }
        });

        drawer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        String[] items = new String[]{"Title", "Created (Newest First)", "Created (Oldest First)", "Updated (Newest First)", "Updated (Oldest First)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sorting_spinner.setAdapter(adapter);
        sorting_spinner.setOnItemSelectedListener(this);

        layout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(linear_layout==true){

                    linear_layout=false;
                    setNewVisibleNotes();
                    layout_btn.setImageResource(R.drawable.linear_layout_icon);

                }

                else{
                    linear_layout=true;
                    setNewVisibleNotes();
                    layout_btn.setImageResource(R.drawable.grid_layout_icon);

                }

            }
        });
    }

    public void addCategory(String category_title){

        boolean check=false;
        for(int i=0; i<categories.size(); i++){

            if(categories.get(i).title.equals(category_title)) {
                check = true;
                break;
            }

        }

        if(check == false){
            MyDBHelper helper = new MyDBHelper(MainActivity.this);
            db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(MyContracts.Category._TITLE, category_title);

            long res = db.insert(
                    MyContracts.Category.TABLE_NAME,
                    null,
                    cv);

            db.close();
            helper.close();
            categories.add(new CategoryModel((int) res,category_title));
            recyclerView_category.getAdapter().notifyItemInserted(categories.size());
            recyclerView_category.smoothScrollToPosition(categories.size());
        }

        else{
            Toast.makeText(getApplicationContext(), "Category Already Exists", Toast.LENGTH_SHORT).show();
        }

    }

    public void setCurrentCategory(int id, String title){

        category_title = title;
        current_category = id;
        toolbar_title.setText(title);
        setNewVisibleNotes();
    }

    public void closeDrawer(){

        drawerLayout.closeDrawers();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                getNoteData(null, order_by);
            }
        }

    }

    @SuppressLint("Range")
    private void getNoteData(String selection, String sort) {

        System.out.println("NOTE DATA");
        MyDBHelper dbHelper=new MyDBHelper(MainActivity.this);
        String[] projection=new String[]{MyContracts.Note._ID,MyContracts.Note._TITLE,MyContracts.Note._DESCRIPTION,MyContracts.Note._LOCK_STATUS,MyContracts.Note._PASSWORD,MyContracts.Note._IMAGE, MyContracts.Note._DATE_CREATED, MyContracts.Note._DATE_UPDATED, MyContracts.Note._CATEGORY_ID, MyContracts.Note._COLOR };
        db=dbHelper.getReadableDatabase();
        Cursor c = db.query(
                MyContracts.Note.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                sort
        );
        notes = new ArrayList<NoteModel>();

        while (c.moveToNext())
        {
            notes.add(new NoteModel(
                    c.getInt(c.getColumnIndex(MyContracts.Note._ID)),
                    c.getString(c.getColumnIndex(MyContracts.Note._TITLE)),
                    c.getString(c.getColumnIndex(MyContracts.Note._DESCRIPTION)),
                    c.getString(c.getColumnIndex(MyContracts.Note._DATE_CREATED)),
                    c.getString(c.getColumnIndex(MyContracts.Note._DATE_UPDATED)),
                    c.getString(c.getColumnIndex(MyContracts.Note._IMAGE)),
                    Boolean.parseBoolean(c.getString(c.getColumnIndex(MyContracts.Note._LOCK_STATUS))),
                    c.getString(c.getColumnIndex(MyContracts.Note._PASSWORD)),
                    c.getInt(c.getColumnIndex(MyContracts.Note._CATEGORY_ID)),
                    c.getString(c.getColumnIndex(MyContracts.Note._COLOR))
            ));

        }
        db.close();
        dbHelper.close();
        c.close();
        setNewVisibleNotes();

    }

    void setNewVisibleNotes(){

        notesVisible = new ArrayList<NoteModel>();

        for(int i=0; i<notes.size(); i++){

            if(notes.get(i).category_id == current_category){
                notesVisible.add(notes.get(i));

            }

        }

        if(linear_layout){
            System.out.println("VISIBLE");
            recyclerView_notes.setLayoutManager(new LinearLayoutManager(this));

        }
        else {
            recyclerView_notes.setLayoutManager(new GridLayoutManager(this, 2));
        }
        notes_adpater = new NoteAdapter(this,notesVisible, category_title, show_updated, linear_layout, screen_width);
        recyclerView_notes.setAdapter(notes_adpater);
    }

    @SuppressLint("Range")
    private void getCategoryData() {

        MyDBHelper dbHelper=new MyDBHelper(MainActivity.this);
        String[] projection=new String[]{MyContracts.Category._ID,MyContracts.Category._TITLE};
        db=dbHelper.getReadableDatabase();
        Cursor c = db.query(
                MyContracts.Category.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        categories = new ArrayList<CategoryModel>();

        while (c.moveToNext())
        {
            categories.add(new CategoryModel(
                    c.getInt(c.getColumnIndex(MyContracts.Category._ID)),
                    c.getString(c.getColumnIndex(MyContracts.Category._TITLE))
            ));
        }

        db.close();
        dbHelper.close();
        c.close();

        category_adpater = new CategoryAdapter(this,categories, callback);
        recyclerView_category.setAdapter(category_adpater);
    }



    public static String getDateTime() {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);

    }

    public static String formatDate(String dateTime, String pattern) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime date = LocalDateTime.parse(dateTime,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return formatter.format(date);
        }
        return "DATE";

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                show_updated=true;
                order_by=MyContracts.Note._TITLE+" ASC";
                sorting_spinner.setBackgroundResource(R.drawable.sort_alpha);
                break;
            case 1:
                show_updated=false;
                order_by="DateTime("+MyContracts.Note._DATE_CREATED+") DESC";
                sorting_spinner.setBackgroundResource(R.drawable.sort_desc);
                break;
            case 2:
                show_updated=false;
                order_by="DateTime("+MyContracts.Note._DATE_CREATED+") ASC";
                sorting_spinner.setBackgroundResource(R.drawable.sort_asc);
                break;
            case 3:
                show_updated=true;
                order_by="DateTime("+MyContracts.Note._DATE_UPDATED+") DESC";
                sorting_spinner.setBackgroundResource(R.drawable.sort_desc);
                break;
            case 4:
                show_updated=true;
                order_by="DateTime("+MyContracts.Note._DATE_UPDATED+") ASC";
                sorting_spinner.setBackgroundResource(R.drawable.sort_asc);
                break;

        }
        getNoteData(null,order_by);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else {
            super.onBackPressed();
        }

    }
}


