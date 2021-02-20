package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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

    companion object {

        private const val TOURNAMENT_ACTIVITY_EXTRA = "com.dgnt.quickTournamentMaker.TOURNAMENT_ACTIVITY_EXTRA"

        fun createIntent(context: Context, tournamentInformation: TournamentInformation): Intent =
            Intent(context, TournamentActivity::class.java).apply {
                putExtra(TOURNAMENT_ACTIVITY_EXTRA, tournamentInformation)
            }
    }

    private lateinit var viewModel: TournamentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tournament_activity)


        val binding = DataBindingUtil.setContentView<TournamentActivityBinding>(this, R.layout.tournament_activity)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TournamentViewModel::class.java)
        binding.vm = viewModel

        val tournamentInformation = intent.getParcelableExtra<TournamentInformation>(TOURNAMENT_ACTIVITY_EXTRA)
        viewModel.setData(tournamentInformation)

        viewModel.title.observe(this, Observer {
            title = it
        })

    }
}