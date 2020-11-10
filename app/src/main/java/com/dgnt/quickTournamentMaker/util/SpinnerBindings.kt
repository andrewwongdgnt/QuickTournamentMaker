package com.dgnt.quickTournamentMaker.util

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.dgnt.quickTournamentMaker.util.SpinnerExtensions.setSpinnerEntries
import com.dgnt.quickTournamentMaker.util.SpinnerExtensions.setSpinnerItemSelectedListener
import com.dgnt.quickTournamentMaker.util.SpinnerExtensions.setSpinnerValue

object SpinnerBindings {
    @JvmStatic
    @BindingAdapter("entries")
    fun Spinner.setEntries(entries: List<Any>?) {
        setSpinnerEntries(entries)
    }
    @JvmStatic
    @BindingAdapter("onItemSelected")
    fun Spinner.setOnItemSelectedListener(itemSelectedListener: SpinnerExtensions.ItemSelectedListener?) {
        setSpinnerItemSelectedListener(itemSelectedListener)
    }
    @JvmStatic
    @BindingAdapter("newValue")
    fun Spinner.setNewValue(newValue: Any?) {
        setSpinnerValue(newValue)
    }

}