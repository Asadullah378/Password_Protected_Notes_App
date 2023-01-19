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

public class UnlockNoteBottomSheet extends BottomSheetDialogFragment {


    EditText password;
    Button unlockNote;
    ViewNoteCustomInterface callback;
    String actual_password;
    int position;
    UnlockNoteBottomSheet(ViewNoteCustomInterface callback, int position, String actual_password){
        this.callback = callback;
        this.actual_password = actual_password;
        this.position = position;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.unlock_bottomsheet_layout,
                container, false);

        password = v.findViewById(R.id.password_entered);
        unlockNote = v.findViewById(R.id.unlock_note);

        unlockNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String pw = password.getText().toString();
                if(pw.isEmpty()){
                    Toast.makeText(getContext().getApplicationContext(), "Password Cannot be Empty",Toast.LENGTH_SHORT).show();

                }

                else {
                    System.out.println("Actual PAss" +actual_password);
                    callback.verifyPassword(pw,position,actual_password);
                    dismiss();
                }
            }
        });


        return v;
    }


}
