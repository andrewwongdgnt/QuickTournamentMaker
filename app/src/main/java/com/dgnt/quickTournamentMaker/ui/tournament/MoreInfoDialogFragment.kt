package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.MoreInfoFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.ExtendedTournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class MoreInfoDialogFragment(
    private val extendedTournamentInformation: ExtendedTournamentInformation,
    private val listener: OnEditListener<TournamentInformation>
) : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: MoreInfoViewModelFactory by instance()

    companion object {

        const val TAG = "MoreInfoDialogFragment"

        fun newInstance(
            fragmentManager: FragmentManager,
            extendedTournamentInformation: ExtendedTournamentInformation,
            listener: OnEditListener<TournamentInformation>
        ) =
            MoreInfoDialogFragment(extendedTournamentInformation, listener).show(fragmentManager, TAG)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?) =


        activity?.let { activity ->

            val binding = MoreInfoFragmentBinding.inflate(activity.layoutInflater)

            val viewModel = ViewModelProvider(this, viewModelFactory)[MoreInfoViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            extendedTournamentInformation.run {
                viewModel.setData(
                    basicTournamentInformation.title,
                    basicTournamentInformation.description,
                    getString(R.string.typeInfo, getString(basicTournamentInformation.tournamentType.stringResource)),
                    getString(R.string.seedTypeInfo, getString(basicTournamentInformation.seedType.resource)),
                    Pair({ date -> getString(R.string.createdDateInfo, date) }, basicTournamentInformation.creationDate),
                    Pair({ date -> getString(R.string.lastModifiedDateInfo, date) }, basicTournamentInformation.lastModifiedDate),
                    getString(R.string.roundInfo, extendedTournamentInformation.numRounds),
                    getString(R.string.matchUpInfo, extendedTournamentInformation.numMatchUps),
                    extendedTournamentInformation.numMatchUpsWithByes.let {
                        if (it == 0) "" else getString(R.string.matchUpSubInfo, it)
                    },
                    getString(R.string.playerInfo, extendedTournamentInformation.numParticipants),
                )
            }

            AlertDialog.Builder(activity)
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