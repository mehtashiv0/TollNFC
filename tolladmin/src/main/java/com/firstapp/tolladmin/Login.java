package com.firstapp.tolladmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText user,password;
    Button signin,signup;
    Utility utility;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user=findViewById(R.id.user);
        password=findViewById(R.id.password);
        signin=findViewById(R.id.signin);
        utility = new Utility(Login.this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = true;


                if (!Validation.nullValidator(user)) {
                    user.setError("Enter valdi Email");
                    check = false;
                }

                if (!Validation.nullValidator(password)) {
                    password.setError("Enter valid Password");
                }

                if (check) {
                    if (utility.checkInternetConnectionALL()) {
                        if (user.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                            Intent i = new Intent(getApplicationContext(), Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getUid()!=null)
        {
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
    }
}
