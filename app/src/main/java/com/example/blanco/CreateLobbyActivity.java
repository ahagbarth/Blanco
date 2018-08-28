package com.example.blanco;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateLobbyActivity extends AppCompatActivity {

    private EditText lobbyName, lobbyPassword;
    private Button buttonCreateLobby;

    private DatabaseReference mLobbyDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_lobby);

        lobbyName = findViewById(R.id.lobbyName);
        lobbyPassword = findViewById(R.id.lobbyPassword);

        mLobbyDatabase = FirebaseDatabase.getInstance().getReference().child("Lobby_list");

        buttonCreateLobby = findViewById(R.id.buttonCreateLobby);
        buttonCreateLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLobby();
            }
        });

    }

    public void createLobby(){
        String lobby_name = lobbyName.getText().toString();
        String lobby_password = lobbyPassword.getText().toString();

        if(lobby_name.isEmpty()) {
            lobbyName.setError("Lobby Name Required");
            lobbyName.requestFocus();
            return;
        } else {

           mLobbyDatabase.child(lobby_name).child("userName").setValue(lobby_name);
           mLobbyDatabase.child(lobby_name).child("Password").setValue(lobby_password);

           Intent intent = new Intent(CreateLobbyActivity.this, LobbyActivity.class);
           intent.putExtra("lobby_name",lobby_name);
           startActivity(intent);

        }

    }
}