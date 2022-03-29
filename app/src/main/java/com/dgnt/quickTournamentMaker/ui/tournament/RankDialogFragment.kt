package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.NestedScrollViewBinding
import com.dgnt.quickTournamentMaker.model.tournament.Rank
import com.dgnt.quickTournamentMaker.util.SimpleParcelable
import org.kodein.di.DIAware
import org.kodein.di.android.x.di

class RankDialogFragment : DialogFragment(), DIAware {
    override val di by di()

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

    private lateinit var binding: NestedScrollViewBinding

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->
            val rank = arguments?.getParcelable<Rank>(KEY_RANK)!!

            binding = NestedScrollViewBinding.inflate(activity.layoutInflater)
            val alert = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.currentRanking))
                .setNegativeButton(android.R.string.ok, null)
                .apply {
                    setView(binding.root)

                }
                .create()


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