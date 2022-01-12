package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.ListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Group

class GroupViewHolder(private val binding: ListItemBinding, private val onClickListener: (Group) -> Unit) : RecyclerView.ViewHolder(binding.root) {

    fun bind(group: Group) =
        binding.run {
            listItemTv.text = group.name
            root.setOnClickListener {
                onClickListener(group)
            }
        }
}
