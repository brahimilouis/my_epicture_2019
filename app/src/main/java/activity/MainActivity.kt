package activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import api.ImgurData

/**
 *  class MainActivity
 *  This class lead our application, it's our "controlleur"
 *  main load login activity or fragments
 */

class MainActivity : AppCompatActivity() {

    /**
     * OnCreate
     *
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (ImgurData.isConnected) {
            navigation()
        } else {
            login()
        }
    }

    /**
     * function navigation
     * load fragments
     */

    private fun navigation() {
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_galerie,
                R.id.navigation_favori,
                R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /**
     * function login
     * load login activity
     */

    private fun login() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, 1)
    }
}
