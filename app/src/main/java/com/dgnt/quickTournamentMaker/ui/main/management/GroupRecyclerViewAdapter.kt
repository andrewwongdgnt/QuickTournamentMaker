package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.management.Group

class GroupRecyclerViewAdapter(private val groups: List<Group>, private val onClickListener: (Group) -> Unit) : RecyclerView.Adapter<GroupViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GroupViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item, parent, false), onClickListener)

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) = holder.bind(groups[position])

    override fun getItemCount() = groups.size


}