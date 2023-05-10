package com.example.myqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myqueue.databinding.ActivityLoginBinding;

public class LogInActivity extends DrawerActivity {
    private TextView txtSighUp, txtCantLetYouIn;
    private EditText editTextLogInEmail, editTextLogInPassword;
    private Button btnLogIn;
    private ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        initViews();

        txtSighUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(!logIn(editTextLogInEmail.getText().toString(), editTextLogInPassword.getText().toString())) {
                //    txtCantLetYouIn.setVisibility(View.VISIBLE);
                //    return;
                //}
                User user = Utils.getInstance(LogInActivity.this).findUserByEmail(editTextLogInEmail.getText().toString());

                if (null == user) {
                    txtCantLetYouIn.setText("There no such user");
                    txtCantLetYouIn.setVisibility(View.VISIBLE);
                    return;
                }
                if (!user.getPassword().equals(editTextLogInPassword.getText().toString())) {
                    txtCantLetYouIn.setText("Wrong password");
                    txtCantLetYouIn.setVisibility(View.VISIBLE);
                    return;
                }

                txtCantLetYouIn.setVisibility(View.GONE);
                Utils.getInstance(LogInActivity.this).setIsLoggedIn(true);
                Intent intent = new Intent(LogInActivity.this, QueueListActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean logIn(String email, String password) {
        User user = Utils.getInstance(this).findUserByEmail(email);
        return (null != user && user.getPassword().equals(password));
    }

    public void initViews() {
        txtSighUp = findViewById(R.id.txtSignUp);
        txtCantLetYouIn = findViewById(R.id.txtCantLetYouIn);

        editTextLogInEmail = findViewById(R.id.editTextLogInEmail);
        editTextLogInPassword = findViewById(R.id.editTextLogInPassword);

        btnLogIn = findViewById(R.id.btnLogIn);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, QueueListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}