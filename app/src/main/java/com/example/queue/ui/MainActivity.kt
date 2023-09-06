package com.example.queue.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.example.queue.R
import com.example.queue.databinding.ActivityMainBinding
import com.example.queue.repository.Repository
import com.example.queue.util.Constants
import com.example.queue.util.Constants.Companion.AUTHORIZATION_TOKEN
import com.example.queue.util.Constants.Companion.LOGGED_IN_USER
import com.example.queue.util.Constants.Companion.SHARED_PREFERENCES
import com.example.queue.util.toJson

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var navController: NavController
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = application.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        val repository = Repository()
        val viewModelProviderFactory = MainViewModelProviderFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_create_queue, R.id.nav_find_queue,
                R.id.nav_setting, R.id.nav_about
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        observeLoggedInUser()
        observeAuthorizationToken()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun observeLoggedInUser() {
        viewModel.loggedInUser.observe(this) { user ->
            val headerUsername =
                binding.navView.getHeaderView(0).findViewById<TextView>(R.id.header_username)

            headerUsername.text = user?.username ?: getString(R.string.unknown_user)

            sharedPreferences.edit().apply {
                this.putString(LOGGED_IN_USER, user.toJson())
                this.apply()
            }
        }
    }

    private fun observeAuthorizationToken() {
        viewModel.authorizationToken.observe(this) { token ->
            sharedPreferences.edit().apply {
                this.putString(AUTHORIZATION_TOKEN, token)
                this.apply()
            }
        }
    }
}
