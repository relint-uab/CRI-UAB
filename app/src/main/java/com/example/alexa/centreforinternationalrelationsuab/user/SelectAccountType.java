package com.example.alexa.centreforinternationalrelationsuab.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.alexa.centreforinternationalrelationsuab.MainActivity;
import com.example.alexa.centreforinternationalrelationsuab.MainErasmus;
import com.example.alexa.centreforinternationalrelationsuab.MainErasmusAcademic;
import com.example.alexa.centreforinternationalrelationsuab.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.Objects;


public class SelectAccountType extends AppCompatActivity {

    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account_type);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mCurrentUser != null;
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String accountType = dataSnapshot.child("Account_type").getValue().toString();

                if (Objects.equals(accountType, "Undefined")) {
                    selectAccountType();
                } else {
                    if (Objects.equals(accountType, "Erasmus student")){
                        Intent i = new Intent(SelectAccountType.this, MainErasmus.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }else
                        if (Objects.equals(accountType, "Erasmus academic")){
                            Intent i = new Intent(SelectAccountType.this, MainErasmusAcademic.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        } else if (Objects.equals(accountType, "International student")){
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                        } else {
                            selectAccountType();
                        }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button select_account_type = findViewById(R.id.open_select_account_type_dialog);
        select_account_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAccountType();
            }
        });
    }


    public void selectAccountType(){

        assert mCurrentUser != null;
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        LayoutInflater layoutInflaterEditGender = LayoutInflater.from(SelectAccountType.this);
        @SuppressLint("InflateParams") final View ViewType = layoutInflaterEditGender.inflate(R.layout.select_type_of_account_dialog, null);
        AlertDialog.Builder alertDialogAccountType = new AlertDialog.Builder(SelectAccountType.this);
        alertDialogAccountType.setView(ViewType);

        final RadioGroup RadioGroupAccountType = ViewType.findViewById(R.id.radio_group_account_type);

        alertDialogAccountType
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogBox, int id) {
                        int selectedId = RadioGroupAccountType.getCheckedRadioButtonId();
                        RadioButton RadioButtonAccType= ViewType.findViewById(selectedId);
                        //Toast.makeText(SelectAccountType.this,"Your account type: "+RadioButtonAccType.getText(), Toast.LENGTH_SHORT).show();
                        StyleableToast.makeText(getApplicationContext(), "Your account type: "+RadioButtonAccType.getText(), R.style.infoToast).show();
                        mUserDatabase.child("Account_type").setValue(RadioButtonAccType.getText());
                        Intent intent = new Intent(SelectAccountType.this, UserProfile.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        AlertDialog alertDialogAccount = alertDialogAccountType.create();
        alertDialogAccount.show();

    }

    public void onBackPressed(){

    }


}
