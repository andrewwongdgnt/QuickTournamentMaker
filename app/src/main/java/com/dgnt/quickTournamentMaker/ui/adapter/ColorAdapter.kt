package com.dgnt.quickTournamentMaker.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.dgnt.quickTournamentMaker.model.tournament.ColorInfo


class ColorAdapter<T>(context: Context, resource: Int, objects: List<T>) : ArrayAdapter<T>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
        super.getView(position, convertView, parent).also {
            updateView(it, position)
        }


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View =
        super.getDropDownView(position, convertView, parent).also {
            updateView(it, position)
        }

    private fun updateView(view: View, position: Int) {
        val item = getItem(position)
        if (item is ColorInfo && view is TextView) {
            view.setTextColor(item.hex)
        }
    }

}