package com.example.alexa.centreforinternationalrelationsuab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alexa.centreforinternationalrelationsuab.campus.CInformation;
import com.example.alexa.centreforinternationalrelationsuab.user.UserProfile;
import com.example.alexa.centreforinternationalrelationsuab.user.UserWelcome;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView imageProfile;
    private TextView txtProfileName;
    private boolean doubleBackToExitPressedOnce = false;
    private RecyclerView recyclerView;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Progress dialog Loading page
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("         loading...");
        mProgress.show();
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                mProgress.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // DISPLAY USER NAME AND EMAIL ON SIDEBAR(drawer header)
        ////////Code is used to fetch the user name////////////
        ////////Code is used to display the user name after fetchig it from other activity////////////
        TextView txtProfileEmail = navigationView.getHeaderView(0).findViewById(R.id.profile_email);
        txtProfileName = navigationView.getHeaderView(0).findViewById(R.id.welcome_name);
        imageProfile = navigationView.getHeaderView(0).findViewById(R.id.imageViewProfile);
        FirebaseUser user = mAuth.getCurrentUser();
        @SuppressWarnings("ConstantConditions") String user_id = user.getUid();

        txtProfileEmail.setText(user.getEmail());

        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                @SuppressWarnings("ConstantConditions") String display_name = dataSnapshot.child("Firstname").getValue().toString();
                // String avatar = "https://firebasestorage.googleapis.com/v0/b/fir-auth-5396b.appspot.com/o/User_Profile%2Fcropped1014172214.jpg?alt=media&token=fd3e8f8d-ad32-4693-b6f1-ae82c17087c0";
                @SuppressWarnings("ConstantConditions") String avatar = dataSnapshot.child("Image_Profile_URL").getValue().toString().trim();

                if (Objects.equals(display_name, "User")) {
                    txtProfileName.setText("Welcome, " + display_name);
                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
                    Toast.makeText(MainActivity.this, "Please complete your profile data!", Toast.LENGTH_LONG).show();
                } else {
                    txtProfileName.setText("Welcome, " + display_name);
                }
                Picasso.with(MainActivity.this).load(avatar).placeholder(R.drawable.logo).into(imageProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // SIGNOUT ACTION FROM HEADER MENU
        Button signout = navigationView.getHeaderView(0).findViewById(R.id.signout_btn);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), UserWelcome.class));
            }
        });

        // OPEN PROFILE EDIT PAGE
        LinearLayout open_profile = navigationView.getHeaderView(0).findViewById(R.id.open_profile);
        open_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserProfile.class));
            }
        });

        // GET POSTS FROM FIREBASE WITH RECYCLERVIEW
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        recyclerView = findViewById(R.id.show_data);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        //Toast.makeText(MainActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();


    }


    // CREATE POST ITEM & FETCHING DATA FROM FIREBASE
    @Override
    protected void onStart() {
        super.onStart();
        //Log.d("LOGGED", "IN onStart ");
        FirebaseRecyclerAdapter<ShowNewPost, ShowDataViewHolder> mFirebaseAdapter = new FirebaseRecyclerAdapter<ShowNewPost, ShowDataViewHolder>(ShowNewPost.class, R.layout.show_news_item, ShowDataViewHolder.class, myRef) {

            public void populateViewHolder(final ShowDataViewHolder viewHolder, ShowNewPost model, final int position) {
                viewHolder.Image_URL(model.getImage_URL());
                viewHolder.Post_Title(model.getPost_Title());
                viewHolder.Post_Content(model.getPost_Content());
            }
        };

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mFirebaseAdapter);

    }

    //View Holder For Recycler View
    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView post_title;
        private final ImageView image_url;
        private final TextView post_content;

        public ShowDataViewHolder(final View itemView) {
            super(itemView);
            image_url = itemView.findViewById(R.id.fetch_image);
            post_title = itemView.findViewById(R.id.fetch_post_title);
            post_content = itemView.findViewById(R.id.fetch_post_content);
        }

        private void Post_Title(String title) {
            post_title.setText(title);
        }

        private void Post_Content(String title) {
            post_content.setText(title);
        }

        private void Image_URL(String title) {
            // image_url.setImageResource(R.drawable.loading);
            Glide.with(itemView.getContext())
                    .load(title)
                    .crossFade()
                    // .placeholder(R.drawable.loading)
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image_url);
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            System.exit(0);

            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please press BACK again to exit",
                Toast.LENGTH_SHORT).show();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);

            return true;
        }else if (id == R.id.nav_information) {
            Intent i = new Intent(MainActivity.this, CInformation.class);
            startActivity(i);

            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}