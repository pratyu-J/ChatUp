package com.example.chatup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText username, email, password;
    Button reg;

    FirebaseAuth auth;
    DatabaseReference reference;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        reg = findViewById(R.id.register);

        auth = FirebaseAuth.getInstance();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(name.isEmpty()){
                    username.setError("Username cannot be empty");
                    username.requestFocus();
                    return;
                }

                if(Email.isEmpty()){
                    email.setError("Username cannot be empty");
                    email.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    email.setError("enter the email address in correct format");
                    email.requestFocus();
                    return;
                }
                if(pass.isEmpty()){
                    password.setError("Username cannot be empty");
                    password.requestFocus();
                    return;
                }

                register(name, Email, pass);
            }
        });

    }

    public void register(final String userName, String email, String password){

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    String userid = user.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", userName);
                    hashMap.put("imageUrl", "default");
                    hashMap.put("status", "offline");
                    hashMap.put("search", userName.toLowerCase());

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, ChatActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
                else{
                    Log.d("MESSAGE", "failed to register");
                    Toast.makeText(RegisterActivity.this, "failed to register", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
