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

public class SetPasswordBottomSheet extends BottomSheetDialogFragment {

    private AddNoteCustomInterface callback;
    EditText password;
    Button lockNote;

    public SetPasswordBottomSheet(AddNoteCustomInterface callback) {

        this.callback=callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.password_bottomsheet_layout,
                container, false);

        password = v.findViewById(R.id.password_txt);
        lockNote = v.findViewById(R.id.lock_note);

        lockNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String pw = password.getText().toString();
                if(pw.isEmpty()){
                    Toast.makeText(getContext().getApplicationContext(), "Password Cannot be Empty",Toast.LENGTH_SHORT).show();

                }

                else {
                    callback.setPassword(pw);
                    dismiss();
                }
            }
        });


        return v;
    }


}
