package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.MoreInfoFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class MoreInfoDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: MoreInfoViewModelFactory by instance()

    companion object {

        const val TAG = "MoreInfoDialogFragment"
        private const val KEY_TOURNAMENT_INFO = "KEY_TOURNAMENT_INFO"
        private const val KEY_ROUNDS_SIZE = "KEY_ROUNDS_SIZE"
        private const val KEY_MATCH_UPS_SIZE = "KEY_MATCH_UPS_SIZE"
        private const val KEY_MATCH_UPS_BYE_SIZE = "KEY_MATCH_UPS_BYE_SIZE"
        private const val KEY_PARTICIPANTS_SIZE = "KEY_PARTICIPANTS_SIZE"

        fun newInstance(
            tournamentInformation: TournamentInformation,
            roundsSize: Int,
            matchUpsSize: Int,
            matchUpsWithSingleByesSize: Int,
            participantsSize: Int
        ) =
            MoreInfoDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_TOURNAMENT_INFO, tournamentInformation)
                    putInt(KEY_ROUNDS_SIZE, roundsSize)
                    putInt(KEY_MATCH_UPS_SIZE, matchUpsSize)
                    putInt(KEY_MATCH_UPS_BYE_SIZE, matchUpsWithSingleByesSize)
                    putInt(KEY_PARTICIPANTS_SIZE, participantsSize)
                }
            }
    }

    private lateinit var listenerEditor: IMoreInfoDialogFragmentListener

    private lateinit var binding: MoreInfoFragmentBinding
    private lateinit var viewModel: MoreInfoViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)

        listenerEditor = (context as? IMoreInfoDialogFragmentListener) ?: (targetFragment as? IMoreInfoDialogFragmentListener) ?: (throw IllegalArgumentException("IMoreInfoDialogFragmentListener not found"))

    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =


        activity?.let { activity ->


            binding = MoreInfoFragmentBinding.inflate(activity.layoutInflater)

            viewModel = ViewModelProvider(this, viewModelFactory).get(MoreInfoViewModel::class.java)
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.setData(
                arguments?.getParcelable(KEY_TOURNAMENT_INFO)!!,
                { type -> getString(R.string.typeInfo, getString(type.resource)) },
                { seedType -> getString(R.string.seedTypeInfo, getString(seedType.resource)) },
                getString(R.string.roundInfo, arguments?.getInt(KEY_ROUNDS_SIZE)!!),
                getString(R.string.matchUpInfo, arguments?.getInt(KEY_MATCH_UPS_SIZE)!!),
                arguments?.getInt(KEY_MATCH_UPS_BYE_SIZE)!!.let {
                    if (it == 0) "" else getString(R.string.matchUpSubInfo, it)
                },
                getString(R.string.playerInfo, arguments?.getInt(KEY_PARTICIPANTS_SIZE)!!),
            )

            AlertDialog.Builder(activity)
                .setTitle(R.string.moreInfo)
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    listenerEditor.onEditTournament(viewModel.title.value ?: "", viewModel.description.value ?: "")
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