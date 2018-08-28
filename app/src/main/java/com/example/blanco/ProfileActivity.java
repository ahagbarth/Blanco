package com.example.blanco;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

   // private ImageView mProfileImage;
    private TextView mProfileName, mProfileFriendsCount;
   private Button mProfileSendReqBtn;
   private Button mProfileDecline;

    private DatabaseReference mUsersDatabase;
    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendsList;

    private FirebaseUser mCurrent_user;

    private String mCurrent_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        final String user_id = getIntent().getStringExtra("user_id");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_request");
        mFriendsList = FirebaseDatabase.getInstance().getReference().child("Friends");

        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

       // mProfileImage = findViewById(R.id.profile_image);
        mProfileName = findViewById(R.id.profile_displayName);
        //mProfileFriendsCount = findViewById(R.id.profileFriendCount);
         mProfileSendReqBtn = findViewById(R.id.buttonFriendRequest);
         mProfileDecline = findViewById(R.id.profile_decline_btn);

        mCurrent_state = "not_friends";
        mProfileDecline.setVisibility(View.VISIBLE);

        mUsersDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("userName").getValue().toString();

                mProfileName.setText(display_name);

                //Friends List/ Request feature
                mFriendReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(user_id)){
                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if(req_type.equals("received")){

                                mCurrent_state = "req_received";
                                mProfileSendReqBtn.setText("Accept Friend Request");
                                mProfileDecline.setVisibility(View.VISIBLE);


                            } else if (req_type.equals("sent")){

                                mCurrent_state = "req_sent";
                                mProfileSendReqBtn.setText("Cancel Friend Request");
                                mProfileDecline.setVisibility(View.GONE);
                            }



                        } else {
                            mFriendsList.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(user_id)){

                                        mProfileSendReqBtn.setEnabled(true);
                                        mCurrent_state = "friends";
                                        mProfileSendReqBtn.setText("Unfriend");
                                        mProfileDecline.setVisibility(View.GONE);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfileSendReqBtn.setEnabled(false);
            }
        });

        mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProfileSendReqBtn.setEnabled(false);


                //Not Friends State

                if(mCurrent_state.equals("not_friends")) {
                    mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {

                                    mCurrent_state = "req_sent";
                                    mProfileSendReqBtn.setText("Cancel Friend Request");

                                    mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(ProfileActivity.this, "Friend Request Sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }) ;
                                }   else {
                                    Toast.makeText(ProfileActivity.this, "Failed to send request", Toast.LENGTH_SHORT).show();
                                }
                                mProfileSendReqBtn.setEnabled(true);
                        }
                    });
  
                }

                //Cancel Request state
                if(mCurrent_state.equals("req_sent")) {
                    mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProfileSendReqBtn.setEnabled(true);
                                    mCurrent_state = "not_friends";
                                    mProfileSendReqBtn.setText("Send Friend Request");
                                    Toast.makeText(ProfileActivity.this, "Friend request removed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }

                //Accept Request

                if (mCurrent_state.equals("req_received")){

                    mUsersDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String display_name = dataSnapshot.child("userName").getValue().toString();
                            final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                            mFriendsList.child(mCurrent_user.getUid()).child(user_id).child("userName").setValue(display_name).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mFriendsList.child(user_id).child(mCurrent_user.getUid()).child("userName").setValue(mCurrent_user.getDisplayName()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {


                                            mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            mProfileSendReqBtn.setEnabled(true);
                                                            mCurrent_state = "friends";
                                                            mProfileSendReqBtn.setText("Unfriend");
                                                            mProfileDecline.setVisibility(View.GONE);
                                                            Toast.makeText(ProfileActivity.this, "Friend Added", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });


                                        }
                                    });
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                //Unfriend User

                if(mCurrent_state.equals("friends")) {
                    mFriendsList.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendsList.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProfileSendReqBtn.setEnabled(true);
                                    mProfileDecline.setVisibility(View.VISIBLE);
                                    mProfileSendReqBtn.setText("Add Friend");
                                    mCurrent_state = "not_friends";
                                    Toast.makeText(ProfileActivity.this, "Unfriended", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }


            }
        });
    }

}
