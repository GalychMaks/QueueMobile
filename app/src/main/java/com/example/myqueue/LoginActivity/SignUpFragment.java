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

import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {
    private SignUpFragment.FragmentSignUpListener listener;

    public interface FragmentSignUpListener {
        void onSignUp(String email, String name, String password);

        void setLogInFragment();
        View.OnClickListener getShowPasswordOnClickListener(View view, EditText editText);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.signup_fragment, container, false);

        TextView txtSignUp = v.findViewById(R.id.txtLogIn);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setLogInFragment();
            }
        });

        EditText editTextUserName = v.findViewById(R.id.editTextUserName);
        EditText editTextEmail = v.findViewById(R.id.editTextEmail);
        EditText editTextPassword = v.findViewById(R.id.editTextPassword);
        EditText editTextConfirmPassword = v.findViewById(R.id.editTextConfirmPassword);

        Button btnSignUp = v.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextUserName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password1 = editTextPassword.getText().toString();
                String password2 = editTextConfirmPassword.getText().toString();

                if (name.isEmpty() || email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
                    Toast.makeText(getContext(), "Fill the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(getContext(), "Incorrect email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password1.equals(password2)) {
                    Toast.makeText(getContext(), "Passwords does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onSignUp(email, name, password1);
            }
        });

        View showPassword = v.findViewById(R.id.showPassword);
        showPassword.setOnClickListener(listener.getShowPasswordOnClickListener(showPassword, editTextPassword));

        View showConfirmPassword = v.findViewById(R.id.showConfirmPassword);
        showConfirmPassword.setOnClickListener(listener.getShowPasswordOnClickListener(showConfirmPassword, editTextConfirmPassword));

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.FragmentLoginListener) {
            listener = (SignUpFragment.FragmentSignUpListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implementFragmentLoginListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public boolean isValidEmail(String email){
        return true;
    }
}
