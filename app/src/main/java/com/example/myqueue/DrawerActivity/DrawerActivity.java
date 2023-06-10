package com.example.myqueue.DrawerActivity;

import static com.example.myqueue.LoginActivity.LoginFragment.LOGGED_IN_USER;
import static com.example.myqueue.QueueListActivity.QueueListActivity.EXTRA_ID;
import static com.example.myqueue.api.WebRepository.LOGGED_IN_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqueue.LoginActivity.LogInActivity;
import com.example.myqueue.QueueListActivity.QueueListActivity;
import com.example.myqueue.QueueMembersActivity.QueueMembersActivity;
import com.example.myqueue.R;
import com.example.myqueue.SettingsActivity;
import com.example.myqueue.api.UserModel;
import com.example.myqueue.api.WebRepository;
import com.example.myqueue.db.Queue;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DrawerActivity extends AppCompatActivity implements ProgressBarCallBack, SearchQueueDialog.SearchQueueDialogListener {
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

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        TextView txtName = navigationView.getHeaderView(0).findViewById(R.id.txtHeaderUserName);
        TextView txtEmail = navigationView.getHeaderView(0).findViewById(R.id.txtHeaderEmail);
        setName(txtName, txtEmail);
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
                    case R.id.nav_setting:
                        startActivity(new Intent(DrawerActivity.this, SettingsActivity.class));
                        break;
                    case R.id.nav_createQueue:
                    case R.id.nav_findQueue:
                    case R.id.nav_aboutUs:
                        Toast.makeText(DrawerActivity.this, "Nothing yet", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_logIn:
                        startActivity(new Intent(DrawerActivity.this, LogInActivity.class));
                        break;
                    case R.id.nav_logOut:
                        String key = sharedPreferences.getString(LOGGED_IN_KEY, null);
                        if (key != null) {
                            WebRepository repo = new WebRepository(DrawerActivity.this);
                            repo.logout(DrawerActivity.this);
                        }

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(IS_LOGGED_IN, false);
                        editor.putString(LOGGED_IN_USER, null);
                        editor.putString(LOGGED_IN_KEY, null);
                        editor.commit();

                        Intent intent1 = new Intent(DrawerActivity.this, QueueListActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
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
            SearchQueueDialog searchQueueDialog = new SearchQueueDialog();
            searchQueueDialog.show(getSupportFragmentManager(), "search queue dialog");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    protected void setName(TextView txtName, TextView txtEmail) {
        if (sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            Gson gson = new Gson();
            UserModel user = gson.fromJson(sharedPreferences.getString(LOGGED_IN_USER, null), new TypeToken<UserModel>() {
            }.getType());
            ;
            if (user != null) {
                txtName.setText(user.getUserName());
                txtEmail.setText(user.getEmail());
                txtEmail.setVisibility(View.VISIBLE);
            }
        } else {
            txtName.setText("Unknown User");
            txtEmail.setVisibility(View.GONE);
        }
    }

    public void showProgressDialog() {
        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
    }

    public void hideProgressDialog() {
        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);
    }

    protected void setDrawerMenu() {
        if (sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_menu);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_menu_login);
        }
    }

    @Override
    public void searchQueue(String code) {
        DrawerActivityViewModel drawerActivityViewModel = new ViewModelProvider(this).get(DrawerActivityViewModel.class);
        drawerActivityViewModel.getQueue(this, code).observe(this, new Observer<Queue>() {
            @Override
            public void onChanged(Queue queue) {
                if(queue == null) {
                    Toast.makeText(DrawerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(DrawerActivity.this, QueueMembersActivity.class);
                intent.putExtra(EXTRA_ID, queue.getId());
                startActivity(intent);
            }
        });
    }
}