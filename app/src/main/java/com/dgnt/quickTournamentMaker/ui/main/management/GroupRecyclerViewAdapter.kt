package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.ListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.util.viewBinding

class GroupRecyclerViewAdapter(private val groups: List<Group>, private val onClickListener: (Group) -> Unit) : RecyclerView.Adapter<GroupViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GroupViewHolder(parent.viewBinding(ListItemBinding::inflate), onClickListener)

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) = holder.bind(groups[position])

    override fun getItemCount() = groups.size


}