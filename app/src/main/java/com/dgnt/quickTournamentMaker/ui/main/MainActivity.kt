package com.dgnt.quickTournamentMaker.ui.main

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.MainActivityBinding
import com.dgnt.quickTournamentMaker.ui.main.home.HomeFragment
import com.dgnt.quickTournamentMaker.ui.main.loadTournament.LoadTournamentFragment
import com.dgnt.quickTournamentMaker.ui.main.management.ManagementFragment

private const val HOME_FRAGMENT_TAG = "HOME_FRAGMENT_TAG"
private const val MANAGEMENT_FRAGMENT_TAG = "MANAGEMENT_FRAGMENT_TAG"
private const val LOAD_TOURNAMENT_FRAGMENT_TAG = "LOAD_TOURNAMENT_FRAGMENT_TAG"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityBinding.inflate(layoutInflater).run {
            setContentView(root)

            val homeFragment = supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG) ?: HomeFragment.newInstance()
            val managementFragment = supportFragmentManager.findFragmentByTag(MANAGEMENT_FRAGMENT_TAG) ?: ManagementFragment.newInstance()
            val loadTournamentFragment = supportFragmentManager.findFragmentByTag(LOAD_TOURNAMENT_FRAGMENT_TAG) ?: LoadTournamentFragment.newInstance()
            supportFragmentManager.beginTransaction().apply {
                addIfNotExists(mainFragmentContainer.id, homeFragment, HOME_FRAGMENT_TAG)
                addIfNotExists(mainFragmentContainer.id, managementFragment, MANAGEMENT_FRAGMENT_TAG)?.hide(managementFragment)
                addIfNotExists(mainFragmentContainer.id, loadTournamentFragment, LOAD_TOURNAMENT_FRAGMENT_TAG)?.hide(loadTournamentFragment)
            }.commit()

            navView.setOnItemSelectedListener { item ->
                val fragment = when (item.itemId) {
                    R.id.navigation_home -> homeFragment
                    R.id.navigation_management -> managementFragment
                    R.id.navigation_loadTournament -> loadTournamentFragment
                    else -> homeFragment
                }
                supportFragmentManager
                    .beginTransaction()
                    .hide(homeFragment)
                    .hide(managementFragment)
                    .hide(loadTournamentFragment)
                    .show(fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()

                true
            }
        }
    }

    private fun FragmentTransaction.addIfNotExists(
        @IdRes containerViewId: Int, fragment: Fragment,
        @Nullable tag: String
    ) =
        if (supportFragmentManager.findFragmentByTag(tag) == null)
            add(containerViewId, fragment, tag)
        else null


}
