package com.dgnt.quickTournamentMaker.ui.tournament

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.NestedScrollViewBinding
import com.dgnt.quickTournamentMaker.model.tournament.Rank
import com.dgnt.quickTournamentMaker.util.SimpleParcelable
import com.dgnt.quickTournamentMaker.util.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kodein.di.DIAware
import org.kodein.di.android.x.di

class RankDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val binding by viewBinding(NestedScrollViewBinding::inflate)

    companion object {

        const val TAG = "RankDialogFragment"

        private const val KEY_RANK = "KEY_RANK"

        fun newInstance(rank: Rank) =
            RankDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_RANK, rank)
                }

            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->
            val rank = arguments?.getParcelable<Rank>(KEY_RANK)!!
            binding.container.apply {
                addView(RecyclerView(activity).apply {
                    val rankList = listOf(
                        Pair(getString(R.string.knownRanking), rank.known.mapIndexed { i, v -> v.map { "${rank.known.size - i}) ${it.name}" }.toList().sortedDescending() }.flatten().reversed()),
                        Pair(getString(R.string.unknownRanking), rank.unknown.map { it.name }.toList().sorted()),
                    )
                    adapter = SimpleExpandableRecyclerViewAdapter(rankList.map { SimpleExpandableGroup(it.first, it.second.map { SimpleParcelable(it) }) })
                        .apply {
                            groups
                                .filter { !isGroupExpanded(it) }
                                .forEach { g -> toggleGroup(g) }
                        }
                    layoutManager = LinearLayoutManager(activity)
                })
            }
            MaterialAlertDialogBuilder(activity, R.style.MyDialogTheme)
                .setTitle(getString(R.string.currentRanking))
                .setNegativeButton(android.R.string.ok, null)
                .apply {
                    setView(binding.root)

                }
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