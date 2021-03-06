package com.example.blanco;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinLobbyActivity extends AppCompatActivity {
    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabase;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_join_lobby);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Lobby_list");

        mUsersList = findViewById(R.id.friends_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));





    }

    @Override
    protected void onStart() {
        super.onStart();
        //  final String searchUsername = mSearchUser.getText().toString();




        FirebaseRecyclerAdapter<Users, JoinLobbyActivity.UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, JoinLobbyActivity.UsersViewHolder>(

                Users.class,
                R.layout.users_single_layout,
                JoinLobbyActivity.UsersViewHolder.class,
                mUsersDatabase

        ) {
            @Override
            protected void populateViewHolder(JoinLobbyActivity.UsersViewHolder viewHolder, Users model, int position) {

                viewHolder.setName(model.getUserName());
                //viewHolder.setUserImage(model.getProfileImage(),getApplicationContext());

                final String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*
                        Intent profileIntent = new Intent(JoinLobbyActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);
                        */

                    }
                });

            }
        };

        mUsersList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setName(String name) {
            TextView userNameView = mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }


    }

}
