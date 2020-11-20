package com.dgnt.quickTournamentMaker.ui.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.dgnt.quickTournamentMaker.R


class TitledLinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr)
{
    init {
        inflate(context, R.layout.title_text_view, this)

        val textView = findViewById<TextView>(R.id.titleOfGroup)
        val padding = 10
        setPadding(padding,padding,padding,padding)
        background = context.getDrawable(R.drawable.titled_linear_layout_background)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.TitledLinearLayout)
        textView.text = attributes.getString(R.styleable.TitledLinearLayout_titleText)
        attributes.recycle()

    }
}



