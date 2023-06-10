package com.example.myqueue.LoginActivity;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.myqueue.DrawerActivity.ProgressBarCallBack;
import com.example.myqueue.Repository;
import com.example.myqueue.api.SignUpRequestBody;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends AndroidViewModel {
    private final Repository repository;

    @Inject
    public LoginViewModel(@NonNull Application application, Repository repository) {
        super(application);
        this.repository = repository;
    }

    public void login(Context context, ProgressBarCallBack progressBarCallBack, String userName, String password) {
        repository.login(context, progressBarCallBack, userName, password);
    }

    public void signup(Context context, ProgressBarCallBack progressBarCallBack, SignUpRequestBody signUpRequestBody) {
        repository.signup(context, progressBarCallBack, signUpRequestBody);
    }
}
