package com.dgnt.quickTournamentMaker.ui.main.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfigType
import com.dgnt.quickTournamentMaker.ui.adapter.IItemTouchHelperAdapter
import java.util.*

class RankPriorityRecyclerViewAdapter(private val context: Context, private val items: List<RankPriorityConfigType>) : RecyclerView.Adapter<DraggableItemViewHolder>(), IItemTouchHelperAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraggableItemViewHolder = DraggableItemViewHolder(context,DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.draggable_list_item, parent, false))

    override fun onBindViewHolder(holder: DraggableItemViewHolder, position: Int) =

       holder.bind(when (items[position]){
           RankPriorityConfigType.WIN -> context.getString(R.string.win)
           RankPriorityConfigType.LOSS -> context.getString(R.string.loss)
           RankPriorityConfigType.TIE -> context.getString(R.string.tie)
        })



    override fun getItemCount(): Int = items.size

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {

    }
}