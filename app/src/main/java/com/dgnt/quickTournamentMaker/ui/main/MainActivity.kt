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

        val homeFragment = HomeFragment.newInstance()
        val managementFragment = ManagementFragment.newInstance()
        val loadTournamentFragment = LoadTournamentFragment.newInstance()
        var activeFragment: Fragment = homeFragment
        supportFragmentManager.beginTransaction().apply {
            add(R.id.main_fragment_container, homeFragment)
            add(R.id.main_fragment_container, managementFragment).hide(managementFragment)
            add(R.id.main_fragment_container, loadTournamentFragment).hide(loadTournamentFragment)

        }.commit()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.navigation_home -> homeFragment
                R.id.navigation_management -> managementFragment
                R.id.navigation_loadTournament -> loadTournamentFragment
                else -> homeFragment
            }
            supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
            activeFragment = fragment

            true
        }

    }


}
