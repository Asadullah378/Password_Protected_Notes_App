package com.asadullahnawaz.i200761;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChooseColorBottomSheet extends BottomSheetDialogFragment {

    private AddNoteCustomInterface callback;
    CardView C1;
    CardView C2;
    CardView C3;
    CardView C4;
    CardView C5;
    CardView C6;

    public ChooseColorBottomSheet(AddNoteCustomInterface callback) {

        this.callback=callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.set_color_bottomsheet_layout,
                container, false);

        C1 = v.findViewById(R.id.C1);
        C2 = v.findViewById(R.id.C2);
        C3 = v.findViewById(R.id.C3);
        C4 = v.findViewById(R.id.C4);
        C5 = v.findViewById(R.id.C5);
        C6 = v.findViewById(R.id.C6);

        C1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callback.setColor("#EF9A9A");
                dismiss();
            }
        });

        C2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callback.setColor("#C4BCD3");
                dismiss();
            }
        });

        C3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callback.setColor("#EDC890");
                dismiss();
            }
        });

        C4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callback.setColor("#C5E1A5");
                dismiss();
            }
        });

        C5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callback.setColor("#FFF59D");
                dismiss();
            }
        });

        C6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callback.setColor("#FFFFFF");
                dismiss();
            }
        });


        return v;
    }


}
