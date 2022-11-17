package com.example.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private TextView notRegText, signUpText;
    private EditText inputEmail, inputPassword;
    private AppCompatButton signInBtn;

    private String email, password;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signInBtn = findViewById(R.id.signInButton);
        notRegText = findViewById(R.id.notRegTxt);
        signUpText = findViewById(R.id.signUpTxt);
        inputEmail = findViewById(R.id.emailEditText);
        inputPassword = findViewById(R.id.passwordEditText);

        //go from sign in to sign up
        Intent goSignUp = new Intent(SignInActivity.this, SignUpActivity.class);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(goSignUp);
            }
        });

        //go from sign in to home screen
        Intent goHome = new Intent(SignInActivity.this, HomeActivity.class);

        //On button press
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                rootNode= FirebaseDatabase.getInstance();
                reference=rootNode.getReference("users");
                Query checkuser=reference.orderByChild("name").equalTo(email);
                checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String pas=snapshot.child(validator(email)).child("password").getValue(String.class);
                            if(pas.equals(password))
                            {
                                Toast.makeText(getApplicationContext(), "User exists",
                                        Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Users does not exist",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Try after sometime",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    //checks if the input email is valid or not.
    private boolean validateEmail(String email){
        if(email.isEmpty() || email.equals("")){
            Toast.makeText(getApplicationContext(), "E-Mail cannot be empty!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.toLowerCase().contains("@mnit.ac.in")){
            return true;
        }else{
            Toast.makeText(getApplicationContext(), "Only MNITians can login!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //checks if the input password is valid or not.
    private boolean validatePassword(String password){
        if(password.isEmpty() || password.equals("")){
            Toast.makeText(getApplicationContext(), "Password cannot be empty!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    String validator(String str)
    {
        String nstr="";
        for(int ndx=0;ndx<str.length();ndx++)
        {
            char ch=str.charAt(ndx);
            if(Character.isLetterOrDigit(ch))
                nstr+=ch;
        }
        return nstr;
    }
}