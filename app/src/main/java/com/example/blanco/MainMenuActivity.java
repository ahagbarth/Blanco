package com.example.blanco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenuActivity extends AppCompatActivity {
    TextView textView;
    ImageView imageView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);

        mAuth = FirebaseAuth.getInstance();

        imageView = findViewById(R.id.profilePicture);
        textView = findViewById(R.id.editTextDisplayName);

        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfile();
            }
        });
        */

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
                    Glide.with(this).load(user.getPhotoUrl().toString()).into(imageView);
                }
                if (user.getDisplayName() != null) {
                    textView.setText(user.getDisplayName());

                }

            }


        }
    }


}
