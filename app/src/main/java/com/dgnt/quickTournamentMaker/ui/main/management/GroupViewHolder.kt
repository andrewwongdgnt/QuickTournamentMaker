package com.dgnt.quickTournamentMaker.ui.main.management

import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import android.widget.Checkable
import android.widget.CheckedTextView
import com.dgnt.quickTournamentMaker.databinding.SectionHeaderItemBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.section_header_item.view.*


class GroupViewHolder(private val binding: SectionHeaderItemBinding, private val selectedGroups: Set<String>, private val setDrawable:(CheckedTextView,Boolean)-> Unit, private val clickListener: (Checkable, Group) -> Unit) : GroupViewHolder(binding.root) {

    fun bind(group: Group, selectable: Boolean) {

        binding.sectionHeaderCtv.text = group.name
       setDrawable( binding.sectionHeaderCtv,selectable)
        if (!selectable)
            binding.sectionHeaderCtv.isChecked = false
        else
            binding.sectionHeaderCtv.isChecked = selectedGroups.contains(group.name)

        binding.sectionHeaderIv.visibility = if (selectable) View.GONE else View.VISIBLE


        itemView.section_header_ctv.setOnClickListener {

            if (selectable)
               clickListener(binding.sectionHeaderCtv, group)
            else
                this.onClick(it)
        }
        itemView.section_header_iv.setOnClickListener {
            if (!selectable)
                clickListener(binding.sectionHeaderCtv, group) else null
        }
    }

    override fun expand() {
        val rotate = RotateAnimation(360F, 180F, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        binding.arrowDown.animation = rotate
    }

    override fun collapse() {
        val rotate = RotateAnimation(180F, 360F, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        binding.arrowDown.animation = rotate
    }


}