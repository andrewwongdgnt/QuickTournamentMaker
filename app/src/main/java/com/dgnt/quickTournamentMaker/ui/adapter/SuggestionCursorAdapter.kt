package com.dgnt.quickTournamentMaker.ui.adapter

import android.content.Context
import android.widget.ImageView
import androidx.cursoradapter.widget.SimpleCursorAdapter


class SuggestionCursorAdapter(
    context: Context,
    layout: Int,
    from: Array<String>,
    to: IntArray,
    flags: Int,
    private val clearListener: (String) -> Unit
) : SimpleCursorAdapter(context, layout, null, from, to, flags) {

    override fun setViewImage(v: ImageView, value: String) {
        v.setOnClickListener {
            clearListener(value)
        }
    }
}