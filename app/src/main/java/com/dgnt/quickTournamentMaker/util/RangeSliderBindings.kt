package com.dgnt.quickTournamentMaker.util

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

import com.google.android.material.slider.RangeSlider

object RangeSliderBindings {
    @JvmStatic
    @InverseBindingAdapter(attribute = "values")
    fun getRangeSlider(slider: RangeSlider): List<Float> {
        return slider.values
    }

    @JvmStatic
    @BindingAdapter("app:valuesAttrChanged")
    fun setListeners(
        slider: RangeSlider,
        attrChange: InverseBindingListener
    ) {
        val listener = RangeSlider.OnChangeListener { _, _, _ -> attrChange.onChange() }
        slider.addOnChangeListener(listener)
    }


}