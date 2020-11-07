package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.databinding.DataBindingUtil
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.MultiChoiceHelper
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class GroupExpandableRecyclerViewAdapter(activity: AppCompatActivity, personGroups: List<ExpandableGroup<*>>, private val clickListener: (Person) -> Unit) : ExpandableRecyclerViewAdapter<GroupViewHolder, PersonViewHolder>(personGroups) {

    private val multiChoiceHelper: MultiChoiceHelper = MultiChoiceHelper(activity, this)

    init {
        multiChoiceHelper.setMultiChoiceModeListener(object : MultiChoiceHelper.MultiChoiceModeListener {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                notifyDataSetChanged();
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem?): Boolean {
                mode.finish();
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                notifyDataSetChanged();
            }

            override fun onItemCheckedStateChanged(mode: ActionMode, position: Int, id: Long, checked: Boolean) {
                mode.title = multiChoiceHelper.checkedItemCount.toString()
            }

        })
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder = GroupViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.section_header_item, parent, false))

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder = PersonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.checkable_list_item, parent, false), clickListener)


    override fun onBindChildViewHolder(holder: PersonViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        holder.bind((group as GroupExpandableGroup).items[childIndex])
        holder.bind(multiChoiceHelper, flatPosition)
    }

    override fun onBindGroupViewHolder(holder: GroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind(group)
}
