package com.sushilmaurya.firebasetutorial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    //TODO: Authenticate
    //TODO: Add Firebase Authentication
    //TODO: Instantiate and add authstatelistener
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("MyMessages");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Message message = null;
                for (DataSnapshot each : dataSnapshot.getChildren()){
                    message = each.getValue(Message.class);
                }
                TextView tvName = findViewById(R.id.tvName);
                TextView tvMessage = findViewById(R.id.tvMessage);
                if (message!= null) {
                    tvName.setText(message.getName());
                    tvMessage.setText(message.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };

        firebaseAuth.addAuthStateListener(authStateListener);

    }

    public void btnSaveClicked(View view) {
        EditText etName = findViewById(R.id.etName);
        EditText etMessage = findViewById(R.id.etMessage);
        databaseReference.push().setValue(new Message(etName.getText().toString(), etMessage.getText().toString()));
    }
}
