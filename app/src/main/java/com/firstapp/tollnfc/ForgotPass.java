package com.firstapp.tollnfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {
    EditText etforgot;
    Button btforgot;
    Utility utility;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        etforgot = findViewById(R.id.etforgot);
        btforgot = findViewById(R.id.btforgot);
        utility = new Utility(ForgotPass.this);
        firebaseAuth= FirebaseAuth.getInstance();


        btforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = true;

                if (!Validation.nullValidator(etforgot)) {
                    check = false;
                    etforgot.setError("Enter email");
                }
                    if (check) {
                        if (utility.checkInternetConnectionALL()) {
                            firebaseAuth.sendPasswordResetEmail(etforgot.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Password reset link is been sent to your Email address", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage().toString(), Toast.LENGTH_SHORT);

                                    }
                                }
                            });
                        }
                    }

            }
        });
    }
}