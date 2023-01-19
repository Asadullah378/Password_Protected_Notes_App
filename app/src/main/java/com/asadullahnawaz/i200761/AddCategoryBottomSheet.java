package com.asadullahnawaz.i200761;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddCategoryBottomSheet extends BottomSheetDialogFragment {

    EditText categoryTitle;
    Button saveCategory;
    MainActivityCustomInterface callback;

    AddCategoryBottomSheet(MainActivityCustomInterface callback){
        this.callback = callback;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_sheet_layout,
                container, false);

        categoryTitle = v.findViewById(R.id.new_category_name);
        saveCategory = v.findViewById(R.id.save_category_btn);

        saveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String title = categoryTitle.getText().toString();
                if(title.isEmpty()){
                    Toast.makeText(getContext().getApplicationContext(), "Category Title Cannot be Empty",Toast.LENGTH_SHORT).show();

                }

                else {
                    callback.addCategory(title);
                    dismiss();
                }
            }
        });


        return v;
    }


}
