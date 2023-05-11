package com.example.myqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class DrawerActivity extends AppCompatActivity {
    ActionBarDrawerToggle actionBarDrawerToggle;
    public static final String IS_LOGGED_IN = "com.example.myqueue.IS_LOGGED_IN";
    private SharedPreferences sharedPreferences;
    private NavigationView navigationView;

    @Override
    public void setContentView(View view) {
        DrawerLayout drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView = drawerLayout.findViewById(R.id.navView);

        sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.commit();

        setDrawerMenu();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent = new Intent(DrawerActivity.this, QueueListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case R.id.nav_createQueue:
                        Toast.makeText(DrawerActivity.this, "Don't touch it", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        //startActivity(new Intent(DrawerActivity.this, CreateQueueActivity.class));
                        break;
                    case R.id.nav_profile:
                    case R.id.nav_favoriteQueues:
                    case R.id.nav_setting:
                    case R.id.nav_aboutUs:
                        Toast.makeText(DrawerActivity.this, "Nothing yet", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_logIn:
                        startActivity(new Intent(DrawerActivity.this, LogInActivity.class));
                        break;
                    case R.id.nav_logOut:
                        SharedPreferences.Editor editor = sharedPreferences.edit().putBoolean(IS_LOGGED_IN, false);
                        editor.commit();
                        setDrawerMenu();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.btnSearch) {
            Toast.makeText(this, "search yourself", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    protected void setDrawerMenu(){
        if (sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_menu);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_menu_login);
        }
    }
}