package com.example.blanco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LobbyActivity extends AppCompatActivity {

    Button deleteLobby;
    DatabaseReference mLobbyDatabase;
    String lobby_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_lobby);
        lobby_name = getIntent().getExtras().getString("lobby_name");
        deleteLobby = findViewById(R.id.deleteLobby);
        mLobbyDatabase = FirebaseDatabase.getInstance().getReference().child("Lobby_list").child(lobby_name);

        deleteLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLobbyDatabase.removeValue();
                startActivity(new Intent(LobbyActivity.this, CreateLobbyActivity.class));
            }
        });
    }
}
