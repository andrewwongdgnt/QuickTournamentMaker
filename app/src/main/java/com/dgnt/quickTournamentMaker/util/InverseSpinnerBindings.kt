package com.dgnt.quickTournamentMaker.util

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.dgnt.quickTournamentMaker.util.SpinnerExtensions.getSpinnerValue
import com.dgnt.quickTournamentMaker.util.SpinnerExtensions.setSpinnerInverseBindingListener
import com.dgnt.quickTournamentMaker.util.SpinnerExtensions.setSpinnerValue

object InverseSpinnerBindings {
    @JvmStatic
    @BindingAdapter("selectedValue")
    fun Spinner.setSelectedValue(selectedValue: Any?) {
        setSpinnerValue(selectedValue)
    }

    @JvmStatic
    @BindingAdapter("selectedValueAttrChanged")
    fun Spinner.setInverseBindingListener(inverseBindingListener: InverseBindingListener?) {
        setSpinnerInverseBindingListener(inverseBindingListener)
    }


    @JvmStatic
    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    fun Spinner.getSelectedValue(): Any? {
        return getSpinnerValue()
    }

}