package cz.respect.respectsports

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import cz.respect.respectsports.databinding.ActivityMainBinding
import cz.respect.respectsports.databinding.NavHeaderMainBinding
import cz.respect.respectsports.ui.tableTennis.TableTennisMatchDetailViewModel
import cz.respect.respectsports.ui.tableTennis.TableTennisNewMatchViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        val viewHeader = binding.navView.getHeaderView(0)
        val navViewHeaderBinding: NavHeaderMainBinding = NavHeaderMainBinding.bind(viewHeader)
        // obtained username from loginactivity after successfull login
        intent.getStringExtra("username")?.let {
            //Log.i("MY_INFO","USERNAME OBTAINED")
            navViewHeaderBinding.username = it
        }

        intent.getStringExtra("email")?.let {
            //Log.i("MY_INFO","EMAIL OBTAINED")
            navViewHeaderBinding.email = it
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_table_tennis, R.id.nav_contacts
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    class TableTennisMatchDetailViewModelFactory(
        private val app: Application,
        private val matchId: String
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {


            if (modelClass.isAssignableFrom(
                    TableTennisMatchDetailViewModel::class.java
                )
            ) {

                return TableTennisMatchDetailViewModel(app, matchId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun showResultMessage(apiResponseString: String) {
        Toast.makeText(
            this,
            apiResponseString,
            Toast.LENGTH_LONG
        ).show()
    }

    fun setActionBarTitle(title: String?) {
        //Log.i("MY_INFO", "TITLE CHANGE: " + title!!)
        //val newTitle = supportActionBar!!.title.toString() + title
        supportActionBar!!.title = title
    }
}