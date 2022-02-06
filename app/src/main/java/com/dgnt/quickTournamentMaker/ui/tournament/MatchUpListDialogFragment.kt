package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.NestedScrollViewBinding
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class MatchUpListDialogFragment(
    private val roundGroups: List<RoundGroup>,
    private val listener: OnEditListener<MatchUp>
) : DialogFragment(), DIAware {
    override val di by di()
    private val createDefaultTitleService: ICreateDefaultTitleService by instance()

    companion object {

        const val TAG = "MatchUpListDialogFragment"

        fun newInstance(
            fragmentManager: FragmentManager,
            roundGroups: List<RoundGroup>,
            listener: OnEditListener<MatchUp>
        ) =
            MatchUpListDialogFragment(roundGroups, listener).show(fragmentManager, TAG)

    }

    private lateinit var binding: NestedScrollViewBinding

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->

            binding = NestedScrollViewBinding.inflate(activity.layoutInflater)
            val alert = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.matchUpSelectionHint))
                .setView(binding.root)
                .setNegativeButton(android.R.string.cancel, null)
                .create()

            roundGroups.forEach {
                binding.container.apply {
                    if (roundGroups.size > 1) {
                        addView(TextView(activity).apply {
                            text = it.title
                            TextViewCompat.setTextAppearance(this, android.R.style.TextAppearance_Large)
                        })

                    }
                    addView(RecyclerView(activity).apply {
                        adapter = RoundExpandableRecyclerViewAdapter(it.rounds.map { RoundExpandableGroup(it, it.matchUps) }, createDefaultTitleService)
                        { matchUp ->
                            MatchUpEditorDialogFragment.newInstance(
                                activity.supportFragmentManager,
                                matchUp,
                                matchUp.roundGroupIndex,
                                matchUp.roundIndex,
                                matchUp.matchUpIndex,
                                listener
                            )
                            alert.dismiss()

                        }.apply {
                            groups
                                .filter { !isGroupExpanded(it) }
                                .forEach { g -> toggleGroup(g) }
                        }
                        layoutManager = LinearLayoutManager(activity)
                    })
                }
            }
            if (roundGroups.size > 1) {
                binding.container.setPadding(50, 40, 0, 0)
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