package com.example.swapi

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigation = findViewById<View>(R.id.navigation) as BottomNavigationView
        navigation.setOnItemSelectedListener(mOnNavigationNavigationBarView)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fl_content, SearchFragment.newInstance())
        ft.commit()


    }
    private fun loadFragment(fragment: Fragment) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fl_content, fragment)
        ft.commit()
    }

    private val mOnNavigationNavigationBarView =
        NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_search -> {
                    loadFragment(SearchFragment.newInstance())
                }
                R.id.action_settings -> {
                    loadFragment(SettingsFragment.newInstance())
                }
                R.id.action_navigation -> {
                    loadFragment(NavigationFragment.newInstance())
                }
            }
            true
        }


}