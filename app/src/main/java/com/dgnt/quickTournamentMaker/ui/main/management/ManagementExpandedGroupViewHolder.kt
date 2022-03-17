package com.dgnt.quickTournamentMaker.ui.main.management

import android.graphics.Typeface
import android.view.View
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.ui.main.common.ExpandedGroupViewHolder


class ManagementExpandedGroupViewHolder(
    private val binding: GroupItemBinding,
    private val nonEmptyGroups: Set<Group>,
    private val selectedGroups: Set<Group>,
    private val clickListener: (Boolean, Group, ManagementFragment.GroupEditType) -> Unit
) : ExpandedGroupViewHolder(binding) {

    fun bind(group: Group, selectableType: ManagementFragmentActionModeCallBack.SelectType) =
        binding.run {
            val selectableGroup = selectableType == ManagementFragmentActionModeCallBack.SelectType.GROUP
            val selectablePerson = selectableType == ManagementFragmentActionModeCallBack.SelectType.PERSON
            sectionHeaderTv.run {
                text = group.name
                setTypeface(typeface, if (nonEmptyGroups.contains(group)) Typeface.BOLD else Typeface.ITALIC)
            }
            sectionHeaderCtv.run {
                isChecked = if (selectableType != ManagementFragmentActionModeCallBack.SelectType.GROUP) false else selectedGroups.contains(group)

                visibility = if (selectableGroup) View.VISIBLE else View.GONE

                setOnClickListener {
                    if (selectableGroup) {
                        this.isChecked = !isChecked
                        clickListener(this.isChecked, group, ManagementFragment.GroupEditType.CHECK)
                    }
                }
            }
            sectionHeaderIv.run {
                visibility = if (selectableGroup) View.GONE else if (selectablePerson) View.INVISIBLE else View.VISIBLE
                setOnClickListener {
                    if (!selectableGroup) {
                        sectionHeaderCtv.isChecked = !sectionHeaderCtv.isChecked
                        clickListener(sectionHeaderCtv.isChecked, group, ManagementFragment.GroupEditType.EDIT)
                    }
                }
            }
        }


}