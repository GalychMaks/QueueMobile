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
import com.example.queue.util.Constants.Companion.SHARED_PREFERENCES

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

//        // Code for floating button
//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener { _, _, _ ->
            invalidateOptionsMenu()
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_create_queue, R.id.nav_find_queue,
                R.id.nav_setting, R.id.nav_about
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> {
                    viewModel.logout()

                    navController.navigate(R.id.nav_sign_in)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                else -> {
                    NavigationUI.onNavDestinationSelected(it, navController)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }

        viewModel.loggedInUserName.observe(this, Observer { newName ->
            val menuLogin = navView.menu.findItem(R.id.nav_sign_in)
            val menuLogout = navView.menu.findItem(R.id.logout)
            val headerUsername =
                navView.getHeaderView(0).findViewById<TextView>(R.id.header_username)
            if (newName.isEmpty()) {
                menuLogin.isVisible = true
                menuLogout.isVisible = false
                headerUsername.text = getString(R.string.unknown_user)
            } else {
                menuLogin.isVisible = false
                menuLogout.isVisible = true
                headerUsername.text = newName
            }


            sharedPreferences.edit().apply {
                this.putString(Constants.LOGGED_IN_USER_NAME, newName)
                this.apply()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}