package com.dgnt.quickTournamentMaker.ui.customSeed

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.CustomSeedFragmentBinding
import com.dgnt.quickTournamentMaker.databinding.SimpleMatchUpLayoutBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantType
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentActivity
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class CustomSeedDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: CustomSeedViewModelFactory by instance()

    companion object {
        const val TAG = "CustomSeedDialogFragment"

        private const val KEY_TOURNAMENT_INFO = "KEY_TOURNAMENT_INFO"
        private const val KEY_ORDERED_PARTICIPANTS = "KEY_ORDERED_PARTICIPANTS"

        fun newInstance(tournamentInformation: TournamentInformation, orderedParticipants: List<Participant>) =
            CustomSeedDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_TOURNAMENT_INFO, tournamentInformation)
                    putParcelableArrayList(KEY_ORDERED_PARTICIPANTS, ArrayList(orderedParticipants))
                }

            }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->
            val binding = CustomSeedFragmentBinding.inflate(activity.layoutInflater)
            val viewModel = ViewModelProvider(this, viewModelFactory).get(CustomSeedViewModel::class.java)
            binding.vm = viewModel
            binding.lifecycleOwner = this

            val tournamentInformation = arguments?.getParcelable<TournamentInformation>(KEY_TOURNAMENT_INFO)!!
            val orderedParticipants = arguments?.getParcelableArrayList<Participant>(KEY_ORDERED_PARTICIPANTS)!!

            viewModel.setData(orderedParticipants)
            viewModel.matchUps.observe(activity, {
                val matchUps = it
                val map = mutableMapOf<Int, SimpleMatchUpLayoutBinding>()
                val myFunc: (Pair<Int, Boolean?>) -> Unit = { pair ->
                    map[pair.first]?.player1?.background = ContextCompat.getDrawable(activity, R.drawable.p1_default)
                    map[pair.first]?.player2?.background = ContextCompat.getDrawable(activity, R.drawable.p2_default)
                    if (pair.second == true)
                        map[pair.first]?.player1?.background = ContextCompat.getDrawable(activity, R.drawable.p1_win)
                    else if (pair.second == false)
                        map[pair.first]?.player2?.background = ContextCompat.getDrawable(activity, R.drawable.p2_win)
                }
                it.forEach {
                    SimpleMatchUpLayoutBinding.inflate(activity.layoutInflater).apply {
                        player1.apply {
                            text = getName(it.participant1)
                            setOnClickListener { _ ->
                                viewModel.select(it.matchUpIndex, true).apply(myFunc)
                            }
                        }
                        player2.apply {
                            text = getName(it.participant2)
                            setOnClickListener { _ ->
                                viewModel.select(it.matchUpIndex, false).apply(myFunc)
                            }
                        }
                        binding.container.addView(this.root)
                        map[it.matchUpIndex] = this

                    }

                }
            })



            AlertDialog.Builder(activity)
                .setTitle(getString(R.string.customSeed))
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    startActivity(TournamentActivity.createIntent(activity, tournamentInformation, orderedParticipants))
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()
        } ?: run {
            super.onCreateDialog(savedInstanceState)
        }

    private fun getName(participant: Participant) = if (participant.participantType == ParticipantType.NORMAL) participant.displayName else getString(R.string.byeDefaultName)

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

}