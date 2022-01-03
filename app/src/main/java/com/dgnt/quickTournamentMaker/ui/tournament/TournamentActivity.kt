package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.TournamentActivityBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentDataTransformerService
import com.dgnt.quickTournamentMaker.util.TournamentUtil.Companion.jsonMapper
import com.moagrius.widget.ScalingScrollView
import com.obsez.android.lib.filechooser.ChooserDialog
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance


class TournamentActivity : AppCompatActivity(), IMoreInfoDialogFragmentListener, IParticipantEditorDialogFragmentListener, IMatchUpEditorDialogFragmentListener, IRoundEditorDialogFragmentListener, DIAware {
    override val di by di()
    private val viewModelFactory: TournamentViewModelFactory by instance()
    private val createDefaultTitleService: ICreateDefaultTitleService by instance()
    private val tournamentDataTransformerService: ITournamentDataTransformerService by instance()

    private var extraLayout: View? = null
    fun extraLayoutHeight() = extraLayout?.height

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

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

            title.observe(tournamentActivity, {
                tournamentActivity.title = it
                tournamentInformation.title = it
            })
            description.observe(tournamentActivity, {
                tournamentInformation.description = it
            })

            tournament.observe(tournamentActivity, {
                binding.container.draw(it) { m, p ->
                    updateTournament(m, p)
                }
            })
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data

                //  doSomeOperations()
            }
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


    @ExperimentalSerializationApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        viewModel.tournament.value?.apply {
            when (item.itemId) {
                R.id.action_currentRanking -> RankDialogFragment.newInstance(getRanking()).show(supportFragmentManager, RankDialogFragment.TAG)
                R.id.action_moreInfo -> MoreInfoDialogFragment.newInstance(
                    tournamentInformation,
                    rounds.size,
                    matchUps.size,
                    getMatchUpsWithSingleByes().size,
                    sortedNormalParticipants.size
                ).show(supportFragmentManager, MoreInfoDialogFragment.TAG)
                R.id.action_rebuildTournament -> RebuildTournamentDialogFragment.newInstance(tournamentInformation, orderedParticipants).show(supportFragmentManager, RebuildTournamentDialogFragment.TAG)
                R.id.action_editAParticipant -> {
                    val sortedParticipants = sortedNormalParticipants
                    AlertDialog.Builder(this@TournamentActivity)
                        .setAdapter(ParticipantArrayAdapter(this@TournamentActivity, sortedParticipants)) { _, i ->
                            ParticipantEditorDialogFragment.newInstance(sortedParticipants[i]).show(supportFragmentManager, ParticipantEditorDialogFragment.TAG)
                        }
                        .setTitle(R.string.participantSelectionHint)
                        .create()
                        .show()
                }
                R.id.action_editAMatchUp -> MatchUpListDialogFragment.newInstance(roundGroups).show(supportFragmentManager, MatchUpListDialogFragment.TAG)
                R.id.action_editARound -> RoundListDialogFragment.newInstance(roundGroups).show(supportFragmentManager, RoundListDialogFragment.TAG)
                R.id.action_exportTournamentAsFile -> {
                    ChooserDialog(this@TournamentActivity)
                        .withFilter(true, false)
                        .withStartFile(null)
                        .withResources(R.string.chooseFolder, android.R.string.ok, android.R.string.cancel)
                        .withChosenListener { path, pathFile ->
                            viewModel.tournament.value?.run {
                                val tournamentData = tournamentDataTransformerService.transform(this)

                                val str = jsonMapper.encodeToString(tournamentData)

                            }

                        }
                        .build()
                        .show()
                }
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
            sortedNormalParticipants.find { it.key == key }?.let {
                it.name = name
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

