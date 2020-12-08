package com.albalonga.cloudfirereadwrite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    private static final String TAG = "SIGN-IN";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText edtEmail, edtPswd;
    private Button btnSignIn, btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Declare UI elements/views
        edtEmail = findViewById(R.id.edtEmail);
        edtPswd  = findViewById(R.id.edtPswd);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignOut = findViewById(R.id.btnSignOut);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fbUser = firebaseAuth.getCurrentUser();
                if(fbUser!=null){
                    // User is signed in
                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                    startActivity(intent);

                    Log.d(TAG, "onAuthStateChanged: signed in " + fbUser.getUid());
                    lclToast("Signed in with " + fbUser.getUid());
                } else{
                    // User is NOT signed in
                    Log.d(TAG, "onAuthStateChanged: signed OUT ");
                    lclToast("User NOT Signed in");
                }
            }
        };
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String pswd  = edtPswd.getText().toString();
                if(!email.equals("") || !pswd.equals("")){
                    lclToast("Logging In");
                    mAuth.signInWithEmailAndPassword(email, pswd);
                }else{
                    lclToast("Fill in email or pswd");
                }
            }
        });

    }
    private void lclToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!=null){
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

}
