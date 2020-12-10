package com.dgnt.quickTournamentMaker.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.NumberPicker
import com.dgnt.quickTournamentMaker.R

class MyNumberPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : NumberPicker(context, attrs) {
    init {
        setFormatter { (it.toDouble() * 0.5).toString() }
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.MyNumberPicker)

        minValue = attributes.getInt(R.styleable.MyNumberPicker_minValue, 0)
        maxValue = attributes.getInt(R.styleable.MyNumberPicker_maxValue, 40)

        // NOTE: workaround for a bug that rendered the selected value wrong until user scrolled, see also: https://stackoverflow.com/q/27343772/3451975
        (NumberPicker::class.java.getDeclaredField("mInputText").apply { isAccessible = true }.get(this) as EditText).filters = emptyArray()

        attributes.recycle()
    }
}