package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.TournamentActivityBinding
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.util.AlertUtil
import com.dgnt.quickTournamentMaker.util.TournamentUtil.Companion.jsonMapper
import com.dgnt.quickTournamentMaker.util.writeText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moagrius.widget.ScalingScrollView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance


class TournamentActivity : AppCompatActivity(), DIAware {
    override val di by di()
    private val viewModelFactory: TournamentViewModelFactory by instance()
    private val createDefaultTitleService: ICreateDefaultTitleService by instance()

    private var extraLayout: View? = null
    fun extraLayoutHeight() = extraLayout?.height

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    companion object {

        private const val TOURNAMENT_ACTIVITY_INFO = "com.dgnt.quickTournamentMaker.TOURNAMENT_ACTIVITY_INFO"
        private const val TOURNAMENT_ACTIVITY_PARTICIPANTS = "com.dgnt.quickTournamentMaker.TOURNAMENT_ACTIVITY_PARTICIPANTS"
        private const val TOURNAMENT_ACTIVITY_RESTORED_INFO = "com.dgnt.quickTournamentMaker.TOURNAMENT_ACTIVITY_RESTORED_INFO"

        fun createIntent(
            context: Context,
            tournamentInformation: TournamentInformation,
            orderedParticipants: List<Participant>
        ): Intent =
            Intent(context, TournamentActivity::class.java).apply {
                putExtra(TOURNAMENT_ACTIVITY_INFO, tournamentInformation)
                putParcelableArrayListExtra(TOURNAMENT_ACTIVITY_PARTICIPANTS, ArrayList(orderedParticipants))
            }

        fun createIntent(
            context: Context,
            restoredTournamentInformation: RestoredTournamentInformation
        ): Intent =
            Intent(context, TournamentActivity::class.java).apply {
                putExtra(TOURNAMENT_ACTIVITY_RESTORED_INFO, restoredTournamentInformation)
            }
    }

    private lateinit var viewModel: TournamentViewModel

    @ExperimentalSerializationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tournamentData = extractTournamentData(intent)

        val tournamentInformation = tournamentData.first
        val orderedParticipants = tournamentData.second

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
        getString(R.string.main_ad_id)
        val tournamentActivity = this
        viewModel = ViewModelProvider(tournamentActivity, viewModelFactory)[TournamentViewModel::class.java].apply {

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
                { mIndex: Int, participant1: Participant, participant2: Participant -> createDefaultTitleService.forMatchUp(resources, mIndex, participant1, participant2) },
                getRestoredTournamentInfo(intent)?.foundationalTournamentEntities
            )

            var firstTitleChange = true

            title.observe(tournamentActivity) {
                tournamentActivity.title = it
                tournamentInformation.title = it
                if (!firstTitleChange)
                    hasChanges.value = true
                firstTitleChange = false
            }
            var firstDescriptionChange = true
            description.observe(tournamentActivity) {
                tournamentInformation.description = it
                if (!firstDescriptionChange)
                    hasChanges.value = true
                firstDescriptionChange = false
            }

            tournament.observe(tournamentActivity) {
                val allViews = binding.container.draw(it) { m, p ->
                    updateTournament(m, p)
                    hasChanges.value = true
                }
                binding.tournamentViewRoot.allViews = allViews
            }
            hasChanges.observe(tournamentActivity) {
                invalidateOptionsMenu()
            }
            hasChanges.value = !fromRestoration(intent)
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                result.data?.data?.let {
                    viewModel.transformTournament()?.let { tournamentData ->
                        val content = jsonMapper.encodeToString(tournamentData)
                        CoroutineScope(Dispatchers.Main).launch { write(this@TournamentActivity, it, content) }
                    }
                }
            }
        }

    }

    private fun fromRestoration(intent: Intent) = intent.hasExtra(TOURNAMENT_ACTIVITY_RESTORED_INFO)

    private fun getRestoredTournamentInfo(intent: Intent) = intent.getParcelableExtra<RestoredTournamentInformation>(TOURNAMENT_ACTIVITY_RESTORED_INFO)

    private fun extractTournamentData(intent: Intent) =

        intent.getParcelableExtra<TournamentInformation>(TOURNAMENT_ACTIVITY_INFO)?.let { tournamentInformation ->
            intent.getParcelableArrayListExtra<Participant>(TOURNAMENT_ACTIVITY_PARTICIPANTS)?.let { participants ->
                Pair(tournamentInformation, participants)
            }
        } ?: getRestoredTournamentInfo(intent)?.let { restoredTournamentInformation ->
            Pair(
                restoredTournamentInformation.extendedTournamentInformation.basicTournamentInformation,
                restoredTournamentInformation.foundationalTournamentEntities.participantEntities
                    .sortedBy { pe -> pe.seedIndex }
                    .map { Participant.fromEntity(it) }
            )
        } ?: throw IllegalArgumentException("Tournament cannot be instantiated because there is not enough information about it")

    private suspend fun write(context: Context, source: Uri, text: String) = withContext(Dispatchers.IO)
    {
        try {
            context.contentResolver.openOutputStream(source)?.use { stream ->
                stream.writeText(text)
            }
        } catch (e: Exception) {
            this@TournamentActivity.runOnUiThread {
                AlertUtil.showError(this@TournamentActivity, R.string.exportAsFileFail, e)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        viewModel.hasChanges.value?.let {
            menu.findItem(R.id.action_saveTournament)?.setIcon(if (it) R.drawable.ic_save_warning else R.drawable.ic_save)
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actions_tournament, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.tournament.value?.apply {
            when (item.itemId) {
                R.id.action_currentRanking -> RankDialogFragment.newInstance(getRanking()).show(supportFragmentManager, RankDialogFragment.TAG)
                R.id.action_moreInfo -> MoreInfoDialogFragment.newInstance(
                    ExtendedTournamentInformation(
                        tournamentInformation,
                        rounds.size,
                        getNumOpenMatchUps(),
                        getNumMatchUpsWithSingleByes(),
                        sortedNormalParticipants.size,
                        getProgress()
                    ),
                    tournamentEditListener
                ).show(supportFragmentManager, MoreInfoDialogFragment.TAG)
                R.id.action_rebuildTournament -> RebuildTournamentDialogFragment.newInstance(tournamentInformation, orderedParticipants).show(supportFragmentManager, RebuildTournamentDialogFragment.TAG)
                R.id.action_editAParticipant -> {
                    val sortedParticipants = sortedNormalParticipants
                    MaterialAlertDialogBuilder(this@TournamentActivity, R.style.MyDialogTheme)
                        .setAdapter(ParticipantArrayAdapter(this@TournamentActivity, sortedParticipants)) { _, i ->
                            ParticipantEditorDialogFragment.newInstance(sortedParticipants[i], participantEditListener).show(supportFragmentManager, ParticipantEditorDialogFragment.TAG)
                        }
                        .setTitle(R.string.participantSelectionHint)
                        .create()
                        .show()
                }
                R.id.action_editAMatchUp -> MatchUpListDialogFragment.newInstance(roundGroups, matchUpEditListener).show(supportFragmentManager, MatchUpListDialogFragment.TAG)
                R.id.action_editARound -> RoundListDialogFragment.newInstance(roundGroups, roundEditListener).show(supportFragmentManager, RoundListDialogFragment.TAG)
                R.id.action_exportTournamentAsFile -> {
                    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                        .setType("application/qtm")
                        .addCategory(Intent.CATEGORY_OPENABLE)
                    intent.putExtra(Intent.EXTRA_TITLE, "${tournamentInformation.title}.qtm")
                    resultLauncher.launch(intent)
                }
                R.id.action_saveTournament -> saveChanges(false)
                R.id.action_saveAsTournament -> saveChanges(true)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        viewModel.hasChanges.value?.takeIf { it }?.run {
            MaterialAlertDialogBuilder(this@TournamentActivity, R.style.MyDialogTheme)
                .setMessage(R.string.unSavedChangesWarningMsg)
                .setPositiveButton(R.string.save) { _, _ ->
                    viewModel.saveTournament(false)
                    super.onBackPressed()
                }
                .setNeutralButton(R.string.dontSave) { _, _ ->
                    super.onBackPressed()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show()
        } ?: run { super.onBackPressed() }
    }

    private fun saveChanges(duplicate: Boolean) {
        viewModel.run {
            hasChanges.value = false
            saveTournament(duplicate)
        }
    }

    private val tournamentEditListener = object : OnEditListener<TournamentInformation> {
        override fun onEdit(editedValue: TournamentInformation) =
            viewModel.run {
                this.title.value = editedValue.title
                this.description.value = editedValue.description
                hasChanges.value = true
            }
    }

    private val participantEditListener = object : OnEditListener<Participant> {
        override fun onEdit(editedValue: Participant) =
            viewModel.run {
                tournament.value?.run {
                    sortedNormalParticipants.find { it.key == editedValue.key }?.let {
                        it.name = editedValue.name
                        it.note = editedValue.note
                        it.color = editedValue.color
                    }
                    tournament.value = this
                }
                hasChanges.value = true
            }
    }

    private val matchUpEditListener = object : OnEditListener<MatchUp> {
        override fun onEdit(editedValue: MatchUp) =
            viewModel.run {
                tournament.value?.run {
                    roundGroups[editedValue.key.first].rounds[editedValue.key.second].matchUps[editedValue.key.third].let {
                        it.useTitle = editedValue.useTitle
                        it.title = editedValue.title
                        it.note = editedValue.note
                        it.color = editedValue.color
                    }
                }
                hasChanges.value = true
            }

    }

    private val roundEditListener = object : OnEditListener<Round> {
        override fun onEdit(editedValue: Round) =
            viewModel.run {
                tournament.value?.run {
                    roundGroups[editedValue.key.first].rounds[editedValue.key.second].let {
                        it.title = editedValue.title
                        it.note = editedValue.note
                        it.color = editedValue.color
                    }
                }
                hasChanges.value = true
            }
    }

}

