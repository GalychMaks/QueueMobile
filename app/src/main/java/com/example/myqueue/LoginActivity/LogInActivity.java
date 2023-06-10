package com.example.myqueue.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.example.myqueue.DrawerActivity.DrawerActivity;
import com.example.myqueue.QueueListActivity.QueueListActivity;
import com.example.myqueue.R;
import com.example.myqueue.api.SignUpRequestBody;
import com.example.myqueue.databinding.ActivityLoginBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LogInActivity extends DrawerActivity implements LoginFragment.FragmentLoginListener, SignUpFragment.FragmentSignUpListener {
    private ActivityLoginBinding activityLoginBinding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        setLogInFragment();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Override
    public void setSignUpFragment() {
        SignUpFragment fragment = new SignUpFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void setLogInFragment() {
        LoginFragment fragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, QueueListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onLogin(String userName, String password) {
        loginViewModel.login(this, this, userName, password);
    }

    @Override
    public void onSignUp(String email, String name, String password) {
        loginViewModel.signup(this, this, new SignUpRequestBody(name, email, password, password));
    }

    @Override
    public View.OnClickListener getShowPasswordOnClickListener(View view, EditText editText) {
        View.OnClickListener showPassword = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    view.setBackgroundResource(R.drawable.ic_visibility_off);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    view.setBackgroundResource(R.drawable.ic_visibility);
                }
            }
        };
        return showPassword;
    }
}