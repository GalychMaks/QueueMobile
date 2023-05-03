package com.example.myqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

public class MainActivity extends AppCompatActivity {
    private TextView txtEmailError, txtUserNameError, txtPasswordError, txtConfirmPasswordError, txtLogIn;
    private EditText editTextEmail, editTextUserName, editTextPassword, editTextConfirmPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        txtLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateEmail(editTextEmail.getText().toString())
                        && validateName(editTextUserName.getText().toString())
                        && validatePassword(editTextPassword.getText().toString(), editTextConfirmPassword.getText().toString())) {

                    Utils utils = Utils.getInstance(MainActivity.this);

                    if(utils.addUser(new User(editTextEmail.getText().toString(),
                            editTextUserName.getText().toString(),
                            editTextPassword.getText().toString()))){

                        Toast.makeText(MainActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, QueueListActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
    public boolean validateEmail(String email) {
        String regex = "^[A-Za-z0-9._+\\-\\']+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(regex, email)) {
            txtEmailError.setText("Invalid email");
            txtEmailError.setVisibility(View.VISIBLE);
            return false;
        } else if (Utils.getInstance(MainActivity.this).findUserByEmail(email) != null) {
            txtEmailError.setText("Email is already taken");
            txtEmailError.setVisibility(View.VISIBLE);
            return false;
        }
        txtEmailError.setVisibility(View.GONE);
        return true;
    }

    public boolean validateName(String name) {
        if (name.length() == 0) {
            txtUserNameError.setText("Enter the name");
            txtUserNameError.setVisibility(View.VISIBLE);
            return false;
        }
        txtUserNameError.setVisibility(View.GONE);
        return true;
    }

    public boolean validatePassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            txtPasswordError.setText("Password should be at least 6 characters");
            txtPasswordError.setVisibility(View.VISIBLE);
            return false;
        }
        txtPasswordError.setVisibility(View.GONE);

        if (!password.equals(confirmPassword)) {
            txtConfirmPasswordError.setText("Passwords does not match");
            txtConfirmPasswordError.setVisibility(View.VISIBLE);
            return false;
        }
        txtConfirmPasswordError.setVisibility(View.GONE);
        return true;
    }

    public void initViews() {
        txtEmailError = findViewById(R.id.txtEmailError);
        txtUserNameError = findViewById(R.id.txtUserNameError);
        txtPasswordError = findViewById(R.id.txtPasswordError);
        txtConfirmPasswordError = findViewById(R.id.txtConfirmPasswordError);
        txtLogIn = findViewById(R.id.txtLogIn);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        btnSignUp = findViewById(R.id.btnSignUp);
    }
}