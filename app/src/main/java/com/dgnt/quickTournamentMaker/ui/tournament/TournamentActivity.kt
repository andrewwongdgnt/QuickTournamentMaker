package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.TournamentActivityBinding
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance


class TournamentActivity : AppCompatActivity(), ITournamentEditorDialogFragmentListener, IParticipantEditorDialogFragmentListener, IMatchUpEditorDialogFragmentListener, DIAware {
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
        val tournamentInformation = intent.getParcelableExtra<TournamentInformation>(TOURNAMENT_ACTIVITY_EXTRA)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
        val tournamentActivity = this
        viewModel = ViewModelProvider(tournamentActivity, viewModelFactory).get(TournamentViewModel::class.java).apply {
            setContentView(TournamentActivityBinding.inflate(layoutInflater).also {
                it.vm = this
            }.root)

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
            R.id.action_editTournament -> viewModel.tournament.value?.run {
                TournamentEditorDialogFragment.newInstance(tournamentInformation).show(supportFragmentManager, TournamentEditorDialogFragment.TAG)
            }
            R.id.action_editAParticipant -> viewModel.tournament.value?.let {
                val participants = it.tournamentInformation.participants.sorted()
                AlertDialog.Builder(this)
                    .setAdapter(ParticipantArrayAdapter(this, participants)) { _, i ->
                        ParticipantEditorDialogFragment.newInstance(participants[i]).show(supportFragmentManager, ParticipantEditorDialogFragment.TAG)
                    }
                    .setTitle(R.string.participantSelectionHint)
                    .create()
                    .show()


            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onEditTournament(newTitle: String, newDescription: String) {
        viewModel.run {
            title.value = newTitle
            description.value = newDescription
        }
    }

    override fun onEditParticipant(key: String, name: String, note: String, color: Int) {
        viewModel.tournament.value?.run {
            tournamentInformation.participants.find { it.key == key }?.let {
                it.displayName = name
                it.note = note
                it.color = color
            }
        }
    }

    override fun onEditMatchUp(key: Triple<Int, Int, Int>, note: String, color: Int) {
        TODO("Not yet implemented")
    }


}