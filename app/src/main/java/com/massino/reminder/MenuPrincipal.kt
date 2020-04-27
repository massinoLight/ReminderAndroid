package com.massino.reminder


import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.massino.reminder.ui.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL

class MenuPrincipal : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener  {


    var IMAGE: Bitmap? = null


    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val emailRe = intent.getStringExtra(MainActivity.EXTRA_EMAIL)
        val nomRe = intent.getStringExtra(MainActivity.EXTRA_NOM)
        val urlphoto= intent.getStringExtra(MainActivity.EXTRA_PHOTO)



        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "bienvenue $nomRe", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        /**
         * le bouton déconnéction
         * */
        val out: Button = findViewById(R.id.logout)
        out.setOnClickListener { view ->
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    val intent2 = Intent(this, MainActivity::class.java)
                    startActivity(intent2)
                    finish()
                }


        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        //on attribue les valeurs au header----------------------
        val headerView:View=navView.getHeaderView(0)
        val textViewNom: TextView = headerView.findViewById(R.id.nom)
        val textViewEmail: TextView = headerView.findViewById(R.id.email)
        val photoDeProfil:ImageView=headerView.findViewById(R.id.photo)
        RecuperationImage(photoDeProfil)
            .execute(urlphoto)
        textViewNom.text=nomRe
        textViewEmail.text=emailRe

        //-------------------------------------------------------------------
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

        /*when (item.itemId) {

            R.id.nav_gallery -> {

                fragment = GalleryFragment.newInstance(nomRe)
            }

            R.id.nav_slideshow -> {
                fragment = SlideshowFragment.newInstance()
            }
            R.id.nav_home -> {

                fragment = HomeFragment.newInstance(emailRe)
            }

        }*/
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

    inner class RecuperationImage() : AsyncTask<String, Void, Bitmap?>() {

        lateinit var bmImg:ImageView
        constructor (bImage: ImageView) : this() {
            bmImg = bImage
        }

        override fun doInBackground(vararg urls: String): Bitmap? {
            Log.i("doInBackground", "ca recup les images en http avec les url passé en paramétre doInBackground")
            val urlOfImage = urls[0]
            return try {
                val inputStream = URL(urlOfImage).openStream()
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) { // Catch the download exception
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                Log.e("onPostExecute", "ca affecte les image bitmap a la liste onPostExecute")
                IMAGE=result
                val resizedBitmap = resizeBitmap(result,150,150)
                bmImg.setImageBitmap(resizedBitmap)

            } else {
                Log.e("onPostExecute", "Erreur image mal telechargé")
                //toast("Erreur lors du telechargement")
            }
        }
    }


    private fun resizeBitmap(bitmap:Bitmap, width:Int, height:Int):Bitmap{
        return Bitmap.createScaledBitmap(
            bitmap,
            width,
            height,
            false
        )
    }

}