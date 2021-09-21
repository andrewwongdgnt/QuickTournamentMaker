package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.TournamentActivityBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService
import com.moagrius.widget.ScalingScrollView
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance


class TournamentActivity : AppCompatActivity(), ITournamentEditorDialogFragmentListener, IParticipantEditorDialogFragmentListener, IMatchUpEditorDialogFragmentListener, IRoundEditorDialogFragmentListener, DIAware {
    override val di by di()
    private val viewModelFactory: TournamentViewModelFactory by instance()
    private val createDefaultTitleService: ICreateDefaultTitleService by instance()

    private var extraLayout: View? = null
    fun extraLayoutHeight() = extraLayout?.height

    companion object {

        private const val TOURNAMENT_ACTIVITY_INFO = "com.dgnt.quickTournamentMaker.TOURNAMENT_ACTIVITY_INFO"
        private const val TOURNAMENT_ACTIVITY_PARTICIPANTS = "com.dgnt.quickTournamentMaker.TOURNAMENT_ACTIVITY_PARTICIPANTS"

        fun createIntent(context: Context, tournamentInformation: TournamentInformation, orderedParticipants: List<Participant>): Intent =
            Intent(context, TournamentActivity::class.java).apply {
                putExtra(TOURNAMENT_ACTIVITY_INFO, tournamentInformation)
                putParcelableArrayListExtra(TOURNAMENT_ACTIVITY_PARTICIPANTS, ArrayList(orderedParticipants))
            }
    }

    private lateinit var viewModel: TournamentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tournamentInformation = intent.getParcelableExtra<TournamentInformation>(TOURNAMENT_ACTIVITY_INFO)!!
        val orderedParticipants = intent.getParcelableArrayListExtra<Participant>(TOURNAMENT_ACTIVITY_PARTICIPANTS)!!

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
        getString(R.string.main_ad_id)
        val tournamentActivity = this
        viewModel = ViewModelProvider(tournamentActivity, viewModelFactory).get(TournamentViewModel::class.java).apply {

            val binding = TournamentActivityBinding.inflate(layoutInflater).also {
                it.vm = this
                it.lifecycleOwner = tournamentActivity
                it.tournamentViewRoot.apply {
                    setShouldVisuallyScaleContents(true)
                    setMinimumScaleMode(ScalingScrollView.MinimumScaleMode.CONTAIN)
                }
                extraLayout = it.extraLayout
            }

            setContentView(binding.root)

            setData(
                tournamentInformation,
                orderedParticipants,
                { rgIndex: Int -> createDefaultTitleService.forRoundGroup(resources, tournamentInformation.tournamentType, rgIndex) },
                { rIndex: Int -> createDefaultTitleService.forRound(resources, rIndex) },
                { mIndex: Int, participant1: Participant, participant2: Participant -> createDefaultTitleService.forMatchUp(resources, mIndex, participant1, participant2) }
            )

            title.observe(tournamentActivity, Observer {
                tournamentActivity.title = it
            })
            tournament.observe(tournamentActivity, Observer {
                binding.container.draw(it) { m, p ->
                    updateTournament(m, p)
                }
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
                TournamentEditorDialogFragment.newInstance(viewModel.title.value ?: "", viewModel.description.value ?: "").show(supportFragmentManager, TournamentEditorDialogFragment.TAG)
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
            R.id.action_editAMatchUp -> viewModel.tournament.value?.let {
                MatchUpListDialogFragment.newInstance(it.roundGroups).show(supportFragmentManager, MatchUpListDialogFragment.TAG)

            }
            R.id.action_editARound -> viewModel.tournament.value?.let {

                RoundListDialogFragment.newInstance(it.roundGroups).show(supportFragmentManager, RoundListDialogFragment.TAG)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onEditTournament(title: String, description: String) {
        viewModel.run {
            this.title.value = title
            this.description.value = description
        }
    }

    override fun onEditParticipant(key: String, name: String, note: String, color: Int) {
        viewModel.tournament.value?.run {
            tournamentInformation.participants.find { it.key == key }?.let {
                it.displayName = name
                it.note = note
                it.color = color
            }
            viewModel.tournament.value = this
        }
    }

    override fun onEditMatchUp(key: Triple<Int, Int, Int>, useTitle: Boolean, title: String, note: String, color: Int) {
        viewModel.tournament.value?.run {
            roundGroups[key.first].rounds[key.second].matchUps[key.third].let {
                it.useTitle = useTitle
                it.title = title
                it.note = note
                it.color = color
            }
        }
    }

    override fun onEditRound(key: Pair<Int, Int>, title: String, note: String, color: Int) {
        viewModel.tournament.value?.run {
            roundGroups[key.first].rounds[key.second].let {
                it.title = title
                it.note = note
                it.color = color
            }
        }
    }


}

