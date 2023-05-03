package com.example.myqueue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends AppCompatActivity {

    private TextView txtSighUp, txtCantLetYouIn;
    private EditText editTextLogInEmail, editTextLogInPassword;
    private Button btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
}