package com.dgnt.quickTournamentMaker.ui.tournament

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.NestedScrollViewBinding
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.util.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kodein.di.DIAware
import org.kodein.di.android.x.di

class RoundListDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val binding by viewBinding(NestedScrollViewBinding::inflate)

    companion object {

        const val TAG = "RoundListDialogFragment"

        private const val KEY_ROUND_GROUPS = "KEY_ROUND_GROUPS"
        private const val KEY_LISTENER = "KEY_LISTENER"

        fun newInstance(
            roundGroups: List<RoundGroup>,
            listener: OnEditListener<Round>
        ) =
            RoundListDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_ROUND_GROUPS, ArrayList<RoundGroup>(roundGroups))
                    putParcelable(KEY_LISTENER, listener)
                }

            }

    }


    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->
            val roundGroups = arguments?.getParcelableArrayList<RoundGroup>(KEY_ROUND_GROUPS)!!
            val listener = arguments?.getParcelable<OnEditListener<Round>>(KEY_LISTENER)!!

            val alert = MaterialAlertDialogBuilder(activity, R.style.MyDialogTheme)
                .setTitle(getString(R.string.roundSelectionHint))
                .setNegativeButton(android.R.string.cancel, null)
                .apply {
                    if (roundGroups.size > 1)
                        setView(binding.root)
                    else
                        setAdapter(RoundArrayAdapter(activity, roundGroups[0].rounds)) { _, i ->
                            roundGroups[0].rounds[i].let { round ->
                                RoundEditorDialogFragment.newInstance(round, round.roundGroupIndex, round.roundIndex, listener).show(activity.supportFragmentManager, RoundEditorDialogFragment.TAG)
                            }
                        }
                }
                .create()


            binding.container.apply {

                addView(RecyclerView(activity).apply {
                    adapter = RoundGroupExpandableRecyclerViewAdapter(roundGroups.map { RoundGroupExpandableGroup(it, it.rounds) })
                    { round ->
                        RoundEditorDialogFragment.newInstance(round, round.roundGroupIndex, round.roundIndex, listener).show(activity.supportFragmentManager, RoundEditorDialogFragment.TAG)
                        alert.dismiss()

                    }.apply {
                        groups
                            .filter { !isGroupExpanded(it) }
                            .forEach { g -> toggleGroup(g) }
                    }


                    layoutManager = LinearLayoutManager(activity)
                })
            }




            alert

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