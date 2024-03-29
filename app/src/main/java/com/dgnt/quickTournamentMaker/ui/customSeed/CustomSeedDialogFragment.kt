package com.dgnt.quickTournamentMaker.ui.customSeed

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
import com.dgnt.quickTournamentMaker.service.interfaces.MatchUpInformation
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentActivity
import com.dgnt.quickTournamentMaker.util.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class CustomSeedDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: CustomSeedViewModelFactory by instance()
    private val binding by viewBinding(CustomSeedFragmentBinding::inflate)

    companion object {
        const val TAG = "CustomSeedDialogFragment"

        private const val KEY_TOURNAMENT_INFO = "KEY_TOURNAMENT_INFO"
        private const val KEY_ORDERED_PARTICIPANTS = "KEY_ORDERED_PARTICIPANTS"
        private const val KEY_LISTENER = "KEY_LISTENER"

        fun newInstance(
            tournamentInformation: TournamentInformation,
            orderedParticipants: List<Participant>,
            listener: OnEditListener<Unit>
        ) =
            CustomSeedDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_TOURNAMENT_INFO, tournamentInformation)
                    putParcelableArrayList(KEY_ORDERED_PARTICIPANTS, ArrayList(orderedParticipants))
                    putParcelable(KEY_LISTENER, listener)
                }

            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        context?.let { context ->
            val viewModel = ViewModelProvider(this, viewModelFactory)[CustomSeedViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            val tournamentInformation = arguments?.getParcelable<TournamentInformation>(KEY_TOURNAMENT_INFO)!!
            val orderedParticipants = arguments?.getParcelableArrayList<Participant>(KEY_ORDERED_PARTICIPANTS)!!
            val listener = arguments?.getParcelable<OnEditListener<Unit>>(KEY_LISTENER)!!

            viewModel.setData(orderedParticipants)
            viewModel.matchUps.observe(this) {
                val map = mutableMapOf<Int, SimpleMatchUpLayoutBinding>()
                val myFunc: (Pair<MatchUpInformation, MatchUpInformation>) -> Unit = { pair ->
                    listOf(pair.first, pair.second).forEach { mui ->

                        map[mui.matchUp.matchUpIndex]?.player1?.apply {
                            background = ContextCompat.getDrawable(context, R.drawable.p1_default)
                            text = getName(mui.matchUp.participant1)
                        }

                        map[mui.matchUp.matchUpIndex]?.player2?.apply {
                            background = ContextCompat.getDrawable(context, R.drawable.p2_default)
                            text = getName(mui.matchUp.participant2)
                        }
                        if (mui.isParticipant1Highlighted == true)
                            map[mui.matchUp.matchUpIndex]?.player1?.background = ContextCompat.getDrawable(context, R.drawable.p1_win)
                        else if (mui.isParticipant1Highlighted == false)
                            map[mui.matchUp.matchUpIndex]?.player2?.background = ContextCompat.getDrawable(context, R.drawable.p2_win)
                    }
                }
                it.forEach {
                    val simpleMatchUpLayoutBinding by viewBinding(SimpleMatchUpLayoutBinding::inflate)
                    simpleMatchUpLayoutBinding.apply {
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
            }

            MaterialAlertDialogBuilder(context, R.style.MyDialogTheme)
                .setTitle(getString(R.string.customSeed))
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    viewModel.matchUps.value?.apply {
                        startActivity(TournamentActivity.createIntent(
                            context,
                            tournamentInformation,
                            flatMap { listOf(it.participant1, it.participant2) }
                        ))
                        listener.onEdit(Unit)
                    }
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()
        } ?: run {
            super.onCreateDialog(savedInstanceState)
        }

    private fun getName(participant: Participant) = if (participant.participantType == ParticipantType.NORMAL) participant.name else getString(R.string.byeDefaultName)

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


}