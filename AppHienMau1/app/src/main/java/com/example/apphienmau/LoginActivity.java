package com.example.apphienmau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private TextView backButton;
    private TextInputEditText loginEmail, loginPassword;
    private TextView forgotPassword;
    private AppCompatButton loginButton;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private long backPressedTime;
    private Toast mToast;

//    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = mAuth.getCurrentUser();
//                if (user != null){
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        };

       backButton = findViewById(R.id.backButton);
       loginEmail = findViewById(R.id.loginEmail);
       loginPassword = findViewById(R.id.loginPassword);
       forgotPassword = findViewById(R.id.forgotPassword);
       loginButton = findViewById(R.id.loginButton);
       loader = new ProgressDialog(this);


       backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SelectRegistrationActivity.class);
                startActivity(intent);
            }
        });

       loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final String email = loginEmail.getText().toString().trim();
               final String password = loginPassword.getText().toString().trim();

               if (TextUtils.isEmpty(email)){
                   loginEmail.setError("Email tr???ng!");
               }
               if (TextUtils.isEmpty(password)){
                   loginPassword.setError("Password tr???ng!");
               }else {
                   loader.setMessage("??ang ????ng Nh???p...");
                   loader.setCanceledOnTouchOutside(false);
                   loader.show();


                   mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful()){
                              Toast.makeText(LoginActivity.this, "????ng Nh???p Th??nh C??ng!", Toast.LENGTH_SHORT).show();
                              Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                              startActivity(intent);
                              finish();
                          }else {
                              Toast.makeText(LoginActivity.this, "Sai m???t kh???u ho???c Emai!", Toast.LENGTH_SHORT).show();
                          }

                          loader.dismiss();
                       }
                   });
               }
           }
       });
       forgotPassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onClickForgotPass();
           }
       });
    }

    private void onClickForgotPass() {
        loader.setMessage("??ang Th???c Hi???n...");
        loader.setCanceledOnTouchOutside(false);
        loader.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String mail = loginEmail.getText().toString().trim();
        if (TextUtils.isEmpty(mail)){
            loginEmail.setError("Vui l??ng nh???p email ????? ti???p t???c!");
        }else {
            auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){
                   Toast.makeText(LoginActivity.this, "Vui l??ng check mail!", Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(LoginActivity.this, "G???i mail th???t b???i!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        loader.dismiss();
    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mAuth.removeAuthStateListener(authStateListener);
//    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            mToast.cancel();
            super.onBackPressed();
            return;
        }else {
            mToast = Toast.makeText(LoginActivity.this, "Nh???n l???n n???a ????? tho??t!", Toast.LENGTH_SHORT);
            mToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
