package com.massino.reminder


import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.massino.reminder.ui.gallery.GalleryFragment
import com.massino.reminder.ui.home.HomeFragment
import com.massino.reminder.ui.slideshow.SlideshowFragment
import kotlinx.android.synthetic.main.activity_main.*

class MenuPrincipal : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener  {

    var emailRe=""
    var nomRe=""


    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

       emailRe = intent.getStringExtra(MainActivity.EXTRA_EMAIL)
        nomRe = intent.getStringExtra(MainActivity.EXTRA_NOM)


        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "bienvenue $nomRe", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        nav_view.setNavigationItemSelectedListener(this)
    }

    /**
     *  cette méthode permet d’afficher les
     *  boutons sur la barre d’actions de l’application.
     * **/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /**
     * permet d’initialiser le menu setting
     * de la barre des tâches de l’application.
     * **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * cette méthode permet d’associer un événement
     * lorsque l’utilisateur sélectionne un item dans
     * le menu de navigation.
     * **/
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        Log.e(ContentValues.TAG, "onNavigationItemSelected est invoqué ")

        /*Log.e(ContentValues.TAG, "$emailRe ")
        Log.e(ContentValues.TAG, "$nomRe ")*/
        var fragment: Fragment = HomeFragment.newInstance("Personne")

        when (item.itemId) {

            R.id.nav_gallery -> {

                fragment = GalleryFragment.newInstance(nomRe)
            }
            R.id.nav_slideshow -> {
                fragment = SlideshowFragment.newInstance()
            }
            R.id.nav_home -> {

                fragment = HomeFragment.newInstance(emailRe)
            }

        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, fragment, "")
            .commit()
        return true
    }

    /**
     * permet d’associer le bouton back du téléphone à un événement de
     * l’application. Lorsque le menu de navigation est ouvert le bouton “back”
     * permet de fermer le menu. Lorsque le menu est fermé le bouton back récupère son rôle par
     * défaut, c’est-à-dire revenir en arrière.
     * **/

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}