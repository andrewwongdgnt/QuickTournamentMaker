package com.dgnt.quickTournamentMaker.ui.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dgnt.quickTournamentMaker.R


class TitledLinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr)
{
    init {
        inflate(context, R.layout.title_text_view, this)

        val padding = 10
        setPadding(padding,padding,padding,padding)
        background = ContextCompat.getDrawable(context,R.drawable.titled_linear_layout_background)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.TitledLinearLayout)
        val titleOfGroup = findViewById<TextView>(R.id.titleOfGroup_tv)
        titleOfGroup.text = attributes.getString(R.styleable.TitledLinearLayout_titleText)
        val help = findViewById<ImageView>(R.id.help_iv)
        help.visibility = if (attributes.getBoolean(R.styleable.TitledLinearLayout_showHelp,false)) View.VISIBLE else View.GONE
        attributes.recycle()

    }
}



