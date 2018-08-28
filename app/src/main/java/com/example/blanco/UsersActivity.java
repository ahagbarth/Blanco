package com.example.blanco;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView mUsersList;

    private FirebaseUser mCurrentUser;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mFriendsDatabase;

    private String mCurrent_user_id;

    private EditText mSearchUser;
    private ImageButton mButtonSearchUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_users);

        mSearchUser = findViewById(R.id.searchUser);
        mButtonSearchUser = findViewById(R.id.buttonSearchUser);



        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        //mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mUsersList = findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

        //mCurrent_user_id = mAuth.getCurrent
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();


    }

    @Override
    protected void onStart() {
        super.onStart();
        final String searchUsername = mSearchUser.getText().toString();

        mButtonSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String searchedName = mSearchUser.getText().toString();


                FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(

                        Users.class,
                        R.layout.users_single_layout,
                        UsersViewHolder.class,
                        mUsersDatabase.orderByChild("userName").equalTo(searchedName)

                ) {
                    @Override
                    protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {
                        if (model.getUserName().equals(searchedName)) {
                            viewHolder.setName(model.getUserName());
                        }
                        //String databaseUser = model.getUserName();






                        //viewHolder.setUserImage(model.getProfileImage(),getApplicationContext());

                        final String user_id = getRef(position).getKey();

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                                profileIntent.putExtra("user_id", user_id);
                                startActivity(profileIntent);
                            }
                        });

                    }
                };

                mUsersList.setAdapter(firebaseRecyclerAdapter);

            }
        });





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
