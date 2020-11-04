package com.dgnt.quickTournamentMaker.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.ui.main.home.HomeFragment
import com.dgnt.quickTournamentMaker.ui.main.loadTournament.LoadTournamentFragment
import com.dgnt.quickTournamentMaker.ui.main.management.ManagementFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val replace: (Fragment) -> Unit = { f ->
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment_container, f)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
        }
        replace(HomeFragment())
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            replace(
                when (item.itemId) {
                    R.id.navigation_management -> ManagementFragment()
                    R.id.navigation_loadTournament -> LoadTournamentFragment()
                    else -> HomeFragment()//R.id.navigation_home
                }
            )
            true
        }
    }
}
