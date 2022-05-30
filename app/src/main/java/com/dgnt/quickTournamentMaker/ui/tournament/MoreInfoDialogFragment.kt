package com.dgnt.quickTournamentMaker.ui.tournament

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.MoreInfoFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.ExtendedTournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.ui.layout.TitledLinearLayout
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.util.getAllViews
import com.dgnt.quickTournamentMaker.util.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance


class MoreInfoDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: MoreInfoViewModelFactory by instance()
    private val binding by viewBinding(MoreInfoFragmentBinding::inflate)

    companion object {

        const val TAG = "MoreInfoDialogFragment"
        private const val KEY_EXTENDED_TOURNAMENT_INFO = "KEY_EXTENDED_TOURNAMENT_INFO"
        private const val KEY_LISTENER = "KEY_LISTENER"

        fun newInstance(
            extendedTournamentInformation: ExtendedTournamentInformation,
            listener: OnEditListener<TournamentInformation>
        ) =
            MoreInfoDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_EXTENDED_TOURNAMENT_INFO, extendedTournamentInformation)
                    putParcelable(KEY_LISTENER, listener)
                }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        context?.let { context ->
            val extendedTournamentInformation = arguments?.getParcelable<ExtendedTournamentInformation>(KEY_EXTENDED_TOURNAMENT_INFO)!!
            val listener = arguments?.getParcelable<OnEditListener<TournamentInformation>>(KEY_LISTENER)!!

            val viewModel = ViewModelProvider(this, viewModelFactory)[MoreInfoViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            binding.root.getAllViews().mapNotNull { it as? TitledLinearLayout }.forEach {
                it.setTextBackgroundColor(R.color.dialogBackground)
            }

            extendedTournamentInformation.run {
                viewModel.setData(
                    basicTournamentInformation.title,
                    basicTournamentInformation.description,
                    getString(R.string.typeInfo, getString(basicTournamentInformation.tournamentType.stringResource)),
                    getString(R.string.seedTypeInfo, getString(basicTournamentInformation.seedType.resource)),
                    Pair({ date -> getString(R.string.createdDateInfo, date) }, basicTournamentInformation.creationDate),
                    Pair({ date -> getString(R.string.lastModifiedDateInfo, date) }, basicTournamentInformation.lastModifiedDate),
                    getString(R.string.roundInfo, extendedTournamentInformation.numRounds),
                    getString(R.string.matchUpInfo, extendedTournamentInformation.numOpenMatchUps),
                    extendedTournamentInformation.numMatchUpsWithByes.let {
                        if (it == 0) "" else getString(R.string.matchUpSubInfo, it)
                    },
                    getString(R.string.playerInfo, extendedTournamentInformation.numParticipants),
                )
            }

            MaterialAlertDialogBuilder(context, R.style.MyDialogTheme)
                .setTitle(R.string.moreInfo)
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    listener.onEdit(TournamentInformation(viewModel.title.value ?: "", viewModel.description.value ?: "", viewModel.creationDate))
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()

        } ?: run {
            super.onCreateDialog(savedInstanceState)
        }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

}