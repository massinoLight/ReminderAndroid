package com.massino.reminder.ui.home

import android.content.ContentValues
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

class HomeFragment : Fragment() {

    companion object {
        fun newInstance(email: String): HomeFragment {

            val args = Bundle()
            args.putString(MainActivity.EXTRA_EMAIL, email)

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        Log.e(ContentValues.TAG, "on est dans le fragement home")
        val email = arguments?.getString(MainActivity.EXTRA_EMAIL)
        //val nom = arguments?.getString(MainActivity.EXTRA_NOM)


        Log.e(ContentValues.TAG, " mail $email")
        textView.text = "bienvenue $email"


        return root
    }
}