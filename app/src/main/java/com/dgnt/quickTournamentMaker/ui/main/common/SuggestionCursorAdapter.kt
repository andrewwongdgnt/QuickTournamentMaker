package com.dgnt.quickTournamentMaker.ui.main.common

import android.content.Context
import android.widget.ImageView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.dgnt.quickTournamentMaker.util.SimpleLogger


class SuggestionCursorAdapter(
    context: Context,
    layout: Int,
    from: Array<String>,
    to: IntArray,
    flags: Int
) : SimpleCursorAdapter(context, layout, null, from, to, flags) {

    override fun setViewImage(v: ImageView, value: String) {


        v.setOnClickListener {
            SimpleLogger.d(this, "Delete $value")
        }
    }
}