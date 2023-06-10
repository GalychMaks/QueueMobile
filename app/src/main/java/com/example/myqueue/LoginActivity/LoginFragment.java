package com.example.myqueue.LoginActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myqueue.R;

public class LoginFragment extends Fragment {
    public static final String LOGGED_IN_USER = "com.example.myqueue.LOGGED_IN_USER";
    private FragmentLoginListener listener;
    private Button btnLogin;
    private EditText editTextEmail, editTextPassword;

    public interface FragmentLoginListener {
        void onLogin(String userName, String password);
        void setSignUpFragment();
        View.OnClickListener getShowPasswordOnClickListener(View view, EditText editText);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment, container, false);

        editTextEmail = v.findViewById(R.id.editTextLogInEmail);
        editTextPassword = v.findViewById(R.id.editTextLogInPassword);

        btnLogin = v.findViewById(R.id.btnLogIn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                if(!(email.isEmpty() && password.isEmpty())){
                    listener.onLogin(email, password);
                } else {
                    Toast.makeText(getContext(), "enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView txtSignUp = v.findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSignUpFragment();
            }
        });

        View showPassword = v.findViewById(R.id.showPassword);

        showPassword.setOnClickListener(listener.getShowPasswordOnClickListener(showPassword, editTextPassword));

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentLoginListener) {
            listener = (FragmentLoginListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implementFragmentLoginListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
