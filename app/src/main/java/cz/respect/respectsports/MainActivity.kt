package cz.respect.respectsports

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import cz.respect.respectsports.databinding.ActivityMainBinding
import cz.respect.respectsports.databinding.NavHeaderMainBinding
import cz.respect.respectsports.ui.login.LoginActivity


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

/*
        var launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.i("MY_INFO", intent.getStringExtra("username").toString())
            if (result.resultCode == Activity.RESULT_OK) {
                val viewHeader = binding.navView.getHeaderView(0)
                val navViewHeaderBinding : NavHeaderMainBinding = NavHeaderMainBinding.bind(viewHeader)
                val intent = result.data
                // passing obtained username of the logged user to the activity_main view (nav_view_header)
                intent?.getStringExtra("username")?.let { navViewHeaderBinding.username = it/*binding.appBarMain.toolbar.title = getString(R.string.menu_home)+" - "+it */ }
                //binding.appBarMain.toolbar.title = getString(R.string.user_welcome_start)+" - "+it

            }

        }

 */




        /*
        val intent = Intent(this, LoginActivity::class.java).apply {
            //putExtra("name", "asljfk")
        }

         */


        //val intent = Intent(this, LoginActivity::class.java)
        //launchSomeActivity.launch(intent)
        //startActivity(intent)
        //finish()

        //Toast.makeText(applicationContext, "fadslfjasdl", Toast.LENGTH_SHORT).show()
        //intent.data.toString()


//getDatabase(this)
/*
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()

        val userDao = db.userDao()
        val users: List<User> = userDao.getAll()

 */

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


        // obtained username from loginactivity after successfull login
        intent.getStringExtra("username")?.let {
            val viewHeader = binding.navView.getHeaderView(0)
            val navViewHeaderBinding: NavHeaderMainBinding = NavHeaderMainBinding.bind(viewHeader)
            navViewHeaderBinding.username = it
        }

        /*
        binding.navView.menu.getItem(3).setOnMenuItemClickListener {
            Log.i("MY_INFO", "Logout run")
            //finish()
            //startActivity(getIntent())
            true
        }

         */

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
}