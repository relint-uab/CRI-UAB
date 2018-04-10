package com.example.alexa.centreforinternationalrelationsuab.user;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexa.centreforinternationalrelationsuab.MainActivity;
import com.example.alexa.centreforinternationalrelationsuab.MainErasmus;
import com.example.alexa.centreforinternationalrelationsuab.MainErasmusAcademic;
import com.example.alexa.centreforinternationalrelationsuab.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private DatabaseReference mUserDatabase, mAccountType;
    private FirebaseUser mCurrentUser;
    private StorageReference mImageStorage;

    private ProgressDialog mProgressDialogUploadingImage;

    // UserProfile layout id's
    private CircleImageView mDisplayProfileImage;
    private TextView mDisplayFirstname,
            mDisplayLastname,
            mDisplayEmail,
            mDisplayFirstnameRow,
            mDisplayLastnameRow,
            mDisplayGender,
            mDisplayDateOfBirth,
            mDisplayUniversity,
            mDisplayCountry;

    private DatePickerDialog.OnDateSetListener mDateOfBirthSetListner;

    private final Context c = this;
    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mDisplayProfileImage = findViewById(R.id.profile_image) ;
        mDisplayFirstname = findViewById(R.id.display_firstname);
        mDisplayLastname = findViewById(R.id.display_lastname);
        mDisplayEmail = findViewById(R.id.display_email);
        mDisplayFirstnameRow = findViewById(R.id.display_firstname_row);
        mDisplayLastnameRow = findViewById(R.id.display_lastname_row);
        mDisplayGender = findViewById(R.id.display_gender_row);
        mDisplayDateOfBirth = findViewById(R.id.display_date_of_birth_row);
        mDisplayUniversity = findViewById(R.id.display_university_row);
        mDisplayCountry = findViewById(R.id.display_country_row);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mCurrentUser != null;
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageProfile = dataSnapshot.child("Image_Profile_URL").getValue().toString();
                String firstname = dataSnapshot.child("Firstname").getValue().toString();
                String lastname = dataSnapshot.child("Lastname").getValue().toString();
                String gender = dataSnapshot.child("Gender").getValue().toString();
                String dateofbirth = dataSnapshot.child("Date_of_birth").getValue().toString();
                String university = dataSnapshot.child("University").getValue().toString();
                String country = dataSnapshot.child("Country").getValue().toString();
                String email = mCurrentUser.getEmail();

                mDisplayFirstname.setText(firstname);
                mDisplayLastname.setText(lastname);
                mDisplayFirstnameRow.setText(firstname);
                mDisplayLastnameRow.setText(lastname);
                mDisplayEmail.setText(email);
                mDisplayGender.setText(gender);
                mDisplayDateOfBirth.setText(dateofbirth);
                mDisplayUniversity.setText(university);
                mDisplayCountry.setText(country);
                Picasso.with(UserProfile.this).load(imageProfile).placeholder(R.drawable.logo).into(mDisplayProfileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Button save_profile = findViewById(R.id.save_profile);
        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StyleableToast.makeText(getApplicationContext(), "Profile saved", R.style.successToast).show();
                //Toast.makeText(UserProfile.this, "Profile saved", Toast.LENGTH_SHORT).show();

                String current_uid = mCurrentUser.getUid();
                mAccountType = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("Account_type");

                mAccountType.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String Account_type = dataSnapshot.getValue().toString();

                        if (Objects.equals(Account_type, "Erasmus student")){
                            Intent i = new Intent(getApplicationContext(), MainErasmus.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        } else if (Objects.equals(Account_type, "Erasmus academic")) {
                            Intent i = new Intent(getApplicationContext(), MainErasmusAcademic.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        } else if (Objects.equals(Account_type, "International student")){
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(getApplicationContext(), SelectAccountType.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



// START EDIT FIRSTNAME FIELD
        TextView btn_edit_firstname = mDisplayFirstnameRow;
        btn_edit_firstname.setOnClickListener(new View.OnClickListener() {

            private DatabaseReference mUserDatabase;

            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterEditFirstname = LayoutInflater.from(c);
                View ViewFirstname = layoutInflaterEditFirstname.inflate(R.layout.edit_firstname_dialog, null);
                AlertDialog.Builder alertDialogEditFirstname = new AlertDialog.Builder(c);
                alertDialogEditFirstname.setView(ViewFirstname);

                assert mCurrentUser != null;
                String current_uid = mCurrentUser.getUid();
                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

                final EditText userInputDialogEditFirstname = ViewFirstname.findViewById(R.id.firstnameInputDialog);
                alertDialogEditFirstname
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                String firstname = userInputDialogEditFirstname.getText().toString();
                                mUserDatabase.child("Firstname").setValue(firstname);
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogFirstname = alertDialogEditFirstname.create();
                alertDialogFirstname.show();
            }
        });


// START EDIT LASTNAME FIELD
        TextView btn_edit_lastname = findViewById(R.id.display_lastname_row);
        btn_edit_lastname.setOnClickListener(new View.OnClickListener() {

            private DatabaseReference mUserDatabase;

            @Override
            public void onClick(View view) {

                assert mCurrentUser != null;
                String current_uid = mCurrentUser.getUid();
                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

                // START edit lastname dialog
                LayoutInflater layoutInflaterEditLastname = LayoutInflater.from(c);
                View ViewLastname = layoutInflaterEditLastname.inflate(R.layout.edit_lastname_dialog, null);
                AlertDialog.Builder alertDialogEditLastname = new AlertDialog.Builder(c);
                alertDialogEditLastname.setView(ViewLastname);

                final EditText userInputDialogEditLastname = ViewLastname.findViewById(R.id.lastnameInputDialog);
                alertDialogEditLastname
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                String lastname = userInputDialogEditLastname.getText().toString();
                                mUserDatabase.child("Lastname").setValue(lastname);
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogLastname = alertDialogEditLastname.create();
                alertDialogLastname.show();
                // FINISH edit lastname dialog
            }
        });


// START EDIT GENDER FIELD
        TextView btn_edit_gender = findViewById(R.id.display_gender_row);
        btn_edit_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert mCurrentUser != null;
                String current_uid = mCurrentUser.getUid();
                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

                LayoutInflater layoutInflaterEditGender = LayoutInflater.from(c);
                final View ViewGender = layoutInflaterEditGender.inflate(R.layout.edit_gender_dialog, null);
                AlertDialog.Builder alertDialogEditGender = new AlertDialog.Builder(c);
                alertDialogEditGender.setView(ViewGender);

                final RadioGroup RadioGroupGender = ViewGender.findViewById(R.id.radio_group_gender);

                alertDialogEditGender
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogBox, int id) {
                                int selectedId = RadioGroupGender.getCheckedRadioButtonId();
                                RadioButton RadioButtonGender= ViewGender.findViewById(selectedId);
                                Toast.makeText(UserProfile.this,RadioButtonGender.getText(),Toast.LENGTH_SHORT).show();
                                mUserDatabase.child("Gender").setValue(RadioButtonGender.getText());
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });
                AlertDialog alertDialogGender = alertDialogEditGender.create();
                alertDialogGender.show();
            }
        });


// START EDIT DATE OF BIRTH FIELD
        TextView btn_edit_date_of_birth = findViewById(R.id.display_date_of_birth_row);
        btn_edit_date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                int year = calendar.get(java.util.Calendar.YEAR);
                int month = calendar.get(java.util.Calendar.MONTH);
                int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        UserProfile.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateOfBirthSetListner,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateOfBirthSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String dateOfBirth = day + "/" + month + "/" + year;
                mUserDatabase.child("Date_of_birth").setValue(dateOfBirth);
            }
        };


// START EDIT UNIVERSITY FIELD
        TextView btn_edit_university = findViewById(R.id.display_university_row);
        btn_edit_university.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert mCurrentUser != null;
                String current_uid = mCurrentUser.getUid();
                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

                LayoutInflater layoutInflaterEditUniversity = LayoutInflater.from(c);
                final View ViewUniversity = layoutInflaterEditUniversity.inflate(R.layout.edit_university_dialog, null);
                AlertDialog.Builder alertDialogEditUniversity = new AlertDialog.Builder(c);
                alertDialogEditUniversity.setView(ViewUniversity);

                final RadioGroup RadioGroupUniversity = ViewUniversity.findViewById(R.id.radio_group_university);

                alertDialogEditUniversity
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogBox, int id) {
                                int selectedId = RadioGroupUniversity.getCheckedRadioButtonId();
                                RadioButton RadioButtonUniversity = ViewUniversity.findViewById(selectedId);
                                Toast.makeText(UserProfile.this,RadioButtonUniversity.getText(),Toast.LENGTH_SHORT).show();
                                mUserDatabase.child("University").setValue(RadioButtonUniversity.getText());
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogGender = alertDialogEditUniversity.create();
                alertDialogGender.show();
            }
        });


//START EDIT COUNTRY FIELD
        TextView btn_edit_country = findViewById(R.id.display_country_row);
        btn_edit_country.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // START edit gender dialog
                assert mCurrentUser != null;
                String current_uid = mCurrentUser.getUid();
                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

                LayoutInflater layoutInflaterEditCountry = LayoutInflater.from(c);
                final View ViewCountry = layoutInflaterEditCountry.inflate(R.layout.edit_country_dialog, null);
                AlertDialog.Builder alertDialogEditCountry = new AlertDialog.Builder(c);
                alertDialogEditCountry.setView(ViewCountry);

                final RadioGroup RadioGroupCountry = ViewCountry.findViewById(R.id.radio_group_country);

                alertDialogEditCountry
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                int selectedId = RadioGroupCountry.getCheckedRadioButtonId();
                                RadioButton RadioButtonCountry = ViewCountry.findViewById(selectedId);
                                Toast.makeText(UserProfile.this,RadioButtonCountry.getText(),Toast.LENGTH_SHORT).show();
                                mUserDatabase.child("Country").setValue(RadioButtonCountry.getText());
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogGender = alertDialogEditCountry.create();
                alertDialogGender.show();
                // END edit country dialog
            }
        });


// CHANGE PICTURE PROFILE
        mDisplayProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });
    }

    // Crop image, upload image to firebase storage & firebase database
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mProgressDialogUploadingImage = new ProgressDialog(UserProfile.this);
                mProgressDialogUploadingImage.setTitle("Uploading image");
                mProgressDialogUploadingImage.setMessage("Please wait...");
                mProgressDialogUploadingImage.setCanceledOnTouchOutside(false);
                mProgressDialogUploadingImage.show();

                Uri resultUri = result.getUri();
                String current_user_id = mCurrentUser.getUid();
                StorageReference filepath = mImageStorage.child("User_Profile").child(current_user_id + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){

                            String download_profile_image_url = task.getResult().getDownloadUrl().toString();

                            mUserDatabase.child("Image_Profile_URL").setValue(download_profile_image_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        mProgressDialogUploadingImage.dismiss();
                                        Toast.makeText(UserProfile.this, "Upload image successful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            Toast.makeText(UserProfile.this, "Upload successful", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(UserProfile.this, "Error", Toast.LENGTH_SHORT).show();
                            mProgressDialogUploadingImage.dismiss();
                        }

                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(UserProfile.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onBackPressed(){

    }
}
