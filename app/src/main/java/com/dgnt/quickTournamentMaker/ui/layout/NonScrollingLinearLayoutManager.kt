package com.dgnt.quickTournamentMaker.ui.layout

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager


class NonScrollingLinearLayoutManager(context: Context) : LinearLayoutManager(context) {


    override fun canScrollVertically(): Boolean = false

}