package com.example.blanco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenuActivity extends AppCompatActivity {
    TextView textView;

    ImageView imageViewProfile;
    ImageView imageViewInfo;
    ImageView imageViewSettings;
    ImageView imageViewLeaderboard;

    Button buttonfriendList;
    Button buttoncreateLobby;
    Button buttonjoinLobby;


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);

        mAuth = FirebaseAuth.getInstance();

        imageViewProfile = findViewById(R.id.profilePicture);
        imageViewInfo = findViewById(R.id.imageViewInfo);
        imageViewLeaderboard = findViewById(R.id.imageViewLeaderboard);
        imageViewSettings = findViewById(R.id.imageViewSettings);

        buttonfriendList = findViewById(R.id.button_friendList);
        buttoncreateLobby = findViewById(R.id.button_createLobby);
        buttonjoinLobby = findViewById(R.id.button_joinLobby);

        textView = findViewById(R.id.editTextDisplayName);


        //These will make the image clickable and take them to their corresponding pages
        imageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, InfoActivity.class));
            }
        });

        imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, SettingsActivity.class));
            }
        });
        imageViewLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(MainMenuActivity.this, LeaderboardActivity.class));
            }
        });

        //Make the buttons work
        buttonfriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, FriendsActivity.class));
            }
        });
        buttoncreateLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, CreateLobbyActivity.class));
            }
        });
        buttonjoinLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, JoinLobbyActivity.class));
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {

                if (user.getPhotoUrl() != null) {
                    Glide.with(this).load(user.getPhotoUrl().toString()).into(imageViewProfile);
                }
                if (user.getDisplayName() != null) {
                    textView.setText(user.getDisplayName());

                }

            }


        }
    }



}
