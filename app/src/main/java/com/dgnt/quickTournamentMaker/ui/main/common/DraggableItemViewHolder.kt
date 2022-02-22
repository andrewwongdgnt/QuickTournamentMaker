package com.dgnt.quickTournamentMaker.ui.main.common

import android.R
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.DraggableListItemBinding


class DraggableItemViewHolder(private val context: Context, private val binding: DraggableListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    @ColorInt
    val color: Int

    init {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        color = typedValue.data
    }

    fun bind(value: String) {
        binding.titleTv.text = value
    }

    fun onItemSelected() = itemView.setBackgroundColor(color)

    fun onItemClear() = itemView.setBackgroundColor(Color.TRANSPARENT)

}