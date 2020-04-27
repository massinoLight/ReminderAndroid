package com.massino.reminder.ui.gallery

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.massino.reminder.MainActivity
import com.massino.reminder.R

class GalleryFragment : Fragment() {
    companion object {

        fun newInstance(nom: String): GalleryFragment {

            val args = Bundle()

            args.putString(MainActivity.EXTRA_NOM, nom)
            val fragment = GalleryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)

        Log.e(TAG, "on est dans le fragement gallery")
       // val email = arguments?.getString(MainActivity.EXTRA_EMAIL)
        val nom = arguments?.getString(MainActivity.EXTRA_NOM)


        Log.e(TAG, " nom $nom")
        textView.text = "bienvenue $nom"




        return root
    }
}
