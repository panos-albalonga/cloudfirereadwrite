package com.albalonga.cloudfirereadwrite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.albalonga.cloudfirereadwrite.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser fbUser;
    private Button btnNewTable, btnJoinTable, btnSignOut;
    private TextView txvNewTableCode;
    private EditText edtJoinTableCode, edtNickName;
    private Deck fullDeck;
    private ArrayList<Card> deckCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fbUser = firebaseAuth.getCurrentUser();
                if(fbUser!=null){
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged: signed in " + fbUser.getUid());
                    lclToast("Signed in with " + fbUser.getUid());
                } else{
                    // User is NOT signed in
                    Log.d(TAG, "onAuthStateChanged: signed OUT ");
                    lclToast("User NOT Signed in");
                }
            }
        };


        txvNewTableCode = (TextView) findViewById(R.id.txvNewTableCode);
        edtJoinTableCode = (EditText)findViewById(R.id.edtTableCode);
        edtNickName     = (EditText)findViewById(R.id.edtNickName);
        btnJoinTable    = (Button)findViewById(R.id.btnJoinTable);
        btnNewTable     = (Button)findViewById(R.id.btnNewTable);
        btnSignOut      = (Button)findViewById(R.id.btnSignOut);

        btnNewTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RandomString randomString = new RandomString();
                txvNewTableCode.setText(randomString.getRandomStr());
                edtJoinTableCode.setText(randomString.getRandomStr());
                txvNewTableCode.setText(fbUser.getUid());
            }
        });

        btnJoinTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Table.class);
                String tblCode = edtJoinTableCode.getText().toString();
                String nickName = edtNickName.getText().toString();
                if(!tblCode.equals("")){
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    Map<String, Object> player = new HashMap<>();
//                    player.put("name", "panos");
//                    db.collection(playerCollection).add(player);
                    intent.putExtra("TableCode", tblCode);
                    intent.putExtra("UserId", fbUser.getUid());
                    intent.putExtra("Player", nickName);
                    startActivity(intent);
                }else {
                    lclToast("No Table Code Entered");
                }
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                onStart();
                lclToast("Signed out");

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser fbUser = mAuth.getCurrentUser();
        if(fbUser!=null){
            Log.d("SIGN", "YES");
            lclToast("Sign-in with: " + "");
        }else{
            Log.d("SIGN", "NO");
            lclToast("Need to sign-in");
            Intent intent = new Intent(MainActivity.this, SignIn.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }
        private void lclToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
