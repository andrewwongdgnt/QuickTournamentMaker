package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
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
import org.kodein.di.DIAware
import org.kodein.di.android.x.di

class RoundListDialogFragment(
    private val listener: OnEditListener<Round>
) : DialogFragment(), DIAware {
    override val di by di()

    companion object {

        const val TAG = "RoundListDialogFragment"

        private const val KEY_ROUND_GROUPS = "KEY_ROUND_GROUPS"

        fun newInstance(
            roundGroups: List<RoundGroup>,
            listener: OnEditListener<Round>
        ) =
            RoundListDialogFragment(listener).apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_ROUND_GROUPS, ArrayList<RoundGroup>(roundGroups))
                }

            }

    }

    private lateinit var binding: NestedScrollViewBinding

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->
            val roundGroups = arguments?.getParcelableArrayList<RoundGroup>(KEY_ROUND_GROUPS)!!

            binding = NestedScrollViewBinding.inflate(activity.layoutInflater)
            val alert = AlertDialog.Builder(activity)
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