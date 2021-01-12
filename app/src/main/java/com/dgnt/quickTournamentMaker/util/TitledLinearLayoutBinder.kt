package com.dgnt.quickTournamentMaker.util

import androidx.databinding.BindingAdapter
import com.dgnt.quickTournamentMaker.ui.layout.TitledLinearLayout


object TitledLinearLayoutBinder {

    @JvmStatic
    @BindingAdapter("helpText")
    fun setHelpText(titledLinearLayout: TitledLinearLayout, text: String?) {
        text?.let {
            titledLinearLayout.helpText = it
        }
    }

}
