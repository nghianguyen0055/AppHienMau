package com.example.apphienmau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private AppCompatButton btnChange;
    private EditText  edtPassNew;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đổi Mật Khẩu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtPassNew = findViewById(R.id.edtPassNew);
        btnChange = findViewById(R.id.btnChangePass);
        loader = new ProgressDialog(this);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onClickChangePass();
            }
        });
    }

    private void onClickChangePass() {
        final String newPass = edtPassNew.getText().toString().trim();
        if (TextUtils.isEmpty(newPass)) {
            Toast.makeText(ChangePassActivity.this, "Vui lòng nhập mật khẩu mới!", Toast.LENGTH_SHORT).show();
        } else {
            loader.setMessage("Đang Đổi Mật Khẩu...");
            loader.setCanceledOnTouchOutside(false);
            loader.show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                return;
            }
            user.updatePassword(newPass)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePassActivity.this, "Đổi thành công!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ChangePassActivity.this, "Đổi thất bại!", Toast.LENGTH_SHORT).show();
                            }
                            loader.dismiss();
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}