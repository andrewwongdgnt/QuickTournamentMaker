package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.VerticalLinearLayoutBinding
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService
import org.kodein.di.DIAware

import org.kodein.di.instance


class MatchUpListDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val createDefaultTitleService: ICreateDefaultTitleService by instance()

    companion object {

        const val TAG = "MatchUpListDialogFragment"

        private const val KEY_MATCH_UPS = "KEY_MATCH_UPS"

        fun newInstance(roundGroups: List<RoundGroup>) =
            MatchUpListDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_MATCH_UPS, ArrayList<RoundGroup>(roundGroups))
                }

            }

    }

    private lateinit var binding: VerticalLinearLayoutBinding

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->


            binding = VerticalLinearLayoutBinding.inflate(activity.layoutInflater)
            val roundGroups = arguments?.getParcelableArrayList<RoundGroup>(KEY_MATCH_UPS)!!
            roundGroups.forEach {
                binding.container.apply {
                    if (roundGroups.size > 1) {

                        addView(TextView(activity).apply {
                            text = it.roundGroupIndex.toString()
                        })

                    }
                    addView(RecyclerView(activity).apply {
                        adapter = RoundExpandableRecyclerViewAdapter(it.rounds.map { RoundExpandableGroup(it, it.matchUps) }, createDefaultTitleService)
                    })
                }


            }

            AlertDialog.Builder(activity)
                .setTitle(getString(R.string.matchUpSelectionHint))
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->

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