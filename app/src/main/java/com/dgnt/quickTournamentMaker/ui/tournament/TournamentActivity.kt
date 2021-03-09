package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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


class TournamentActivity : AppCompatActivity(), ITournamentEditorDialogFragmentListener, DIAware {
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
    private lateinit var tournamentInformation: TournamentInformation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tournament_activity)
        tournamentInformation = intent.getParcelableExtra(TOURNAMENT_ACTIVITY_EXTRA)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
        val tournamentActivity = this
        viewModel = ViewModelProvider(this, viewModelFactory).get(TournamentViewModel::class.java).apply {
            DataBindingUtil.setContentView<TournamentActivityBinding>(tournamentActivity, R.layout.tournament_activity).also {
                it.vm = this
            }

            setData(tournamentInformation)

            title.observe(tournamentActivity, Observer {
                tournamentActivity.title = it
            })
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actions_tournament, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_editTournament -> TournamentEditorDialogFragment.newInstance(viewModel.title.value ?: "", viewModel.description.value ?: "", viewModel.participants.value?.run { toTypedArray() } ?: arrayOf()).show(supportFragmentManager, TournamentEditorDialogFragment.TAG)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onEditTournament(newTitle: String, newDescription: String) {
        viewModel.run {
            title.value = newTitle
            description.value = newDescription
        }
    }


}