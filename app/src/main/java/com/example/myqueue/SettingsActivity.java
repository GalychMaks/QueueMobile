package com.example.myqueue;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myqueue.DrawerActivity.DrawerActivity;
import com.example.myqueue.databinding.ActivitySettingsBinding;

import java.util.regex.Pattern;

public class SettingsActivity extends DrawerActivity {

    ActivitySettingsBinding activitySettingsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(activitySettingsBinding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        EditText editTextLocalHost = activitySettingsBinding.editTextLocalHost;
        editTextLocalHost.setText(sharedPreferences.getString("ip", "127.0.0.1"));

        Pattern ipRegex = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");

        Button btnOK = activitySettingsBinding.btnOK;
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editTextLocalHost.getText().toString();
                if(!ipRegex.matcher(text).matches()){
                    Toast.makeText(SettingsActivity.this, "Invalid ip", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ip", text);
                editor.apply();
                Toast.makeText(SettingsActivity.this, "Address has changed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}