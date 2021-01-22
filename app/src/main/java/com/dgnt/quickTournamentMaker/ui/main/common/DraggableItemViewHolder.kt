package com.dgnt.quickTournamentMaker.ui.main.common

import android.R
import android.content.Context
import android.graphics.Color
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.DraggableListItemBinding


class DraggableItemViewHolder(private val context: Context, private val binding: DraggableListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(value: String) {
        binding.titleTv.text = value

    }

    fun onItemSelected() {
        itemView.setBackgroundColor(ResourcesCompat.getColor(context.resources, com.dgnt.quickTournamentMaker.R.color.dragHighlightColor, null))
    }

    fun onItemClear() {
        itemView.setBackgroundColor(Color.TRANSPARENT)
    }
}