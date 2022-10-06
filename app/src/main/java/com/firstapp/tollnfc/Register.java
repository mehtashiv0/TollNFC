package com.firstapp.tollnfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class Register extends AppCompatActivity {


    EditText email,name, password;
    Button signup, signin;
    Utility utility;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        utility = new Utility(Register.this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = true;

                if (!Validation.nullValidator(name)) {
                    check = false;
                    name.setError("Enter name");
                }
                if (!Validation.nullValidator(email)) {
                    check = false;
                    email.setError("Enter email");
                }
                if (!Validation.nullValidator(password)) {
                    check = false;
                    password.setError("Enter password");
                }

                if (check) {
                    if (utility.checkInternetConnectionALL()) {
                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                                password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    GetterSetter g = new GetterSetter(
                                            firebaseAuth.getCurrentUser().getUid().toString(),
                                            email.getText().toString(),
                                            name.getText().toString()

                                    );
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid().toString()).setValue(g);
                                    Toast.makeText(Register.this,
                                            "Successfully Registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
 }
