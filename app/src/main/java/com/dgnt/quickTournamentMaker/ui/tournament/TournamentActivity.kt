package com.dgnt.quickTournamentMaker.ui.tournament

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.TournamentActivityBinding
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class TournamentActivity : AppCompatActivity(), DIAware {
    override val di by di()
    private val viewModelFactory: TournamentViewModelFactory by instance()

    private lateinit var viewModel: TournamentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tournament_activity)

//        val tournamentInformation = savedInstanceState?.run {
//            getParcelable<TournamentInformation>("KEY")
//        }

        val tournamentInformation =  intent.getParcelableExtra<TournamentInformation>("KEY")

        val binding = DataBindingUtil.setContentView<TournamentActivityBinding>(this, R.layout.tournament_activity)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TournamentViewModel::class.java)
        binding.vm = viewModel

    }
}