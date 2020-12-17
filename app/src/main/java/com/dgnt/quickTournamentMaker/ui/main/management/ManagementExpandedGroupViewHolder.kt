package com.dgnt.quickTournamentMaker.ui.main.management

import android.graphics.Typeface
import android.view.View
import android.widget.Checkable
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.ui.main.common.ExpandedGroupViewHolder
import kotlinx.android.synthetic.main.group_item.view.*


class ManagementExpandedGroupViewHolder(private val binding: GroupItemBinding, val nonEmptyGroups:Set<Group>, private val clickListener: (Checkable, Group, ManagementFragment.GroupEditType) -> Unit) : ExpandedGroupViewHolder(binding) {

    fun bind(group: Group, selectableType: ManagementFragmentActionModeCallBack.SelectType) {

        val selectableGroup = selectableType == ManagementFragmentActionModeCallBack.SelectType.GROUP
        val selectablePerson = selectableType == ManagementFragmentActionModeCallBack.SelectType.PERSON
        binding.sectionHeaderTv.text = group.name
        binding.sectionHeaderTv.setTypeface(binding.sectionHeaderTv.typeface, if (nonEmptyGroups.contains(group)) Typeface.BOLD else Typeface.ITALIC)
        binding.sectionHeaderCtv.isChecked = false

        binding.sectionHeaderCtv.visibility = if (selectableGroup) View.VISIBLE else View.GONE
        binding.sectionHeaderIv.visibility = if (selectableGroup) View.GONE else if (selectablePerson) View.INVISIBLE else View.VISIBLE


        itemView.section_header_ctv.setOnClickListener {
            if (selectableGroup)
                clickListener(binding.sectionHeaderCtv, group, ManagementFragment.GroupEditType.CHECK)

        }
        itemView.section_header_iv.setOnClickListener {
            if (!selectableGroup)
                clickListener(binding.sectionHeaderCtv, group, ManagementFragment.GroupEditType.EDIT) else null
        }
    }


}