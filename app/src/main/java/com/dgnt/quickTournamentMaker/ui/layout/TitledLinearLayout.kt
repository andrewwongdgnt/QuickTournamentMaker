package com.dgnt.quickTournamentMaker.ui.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.dgnt.quickTournamentMaker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class TitledLinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    var helpText: String = ""
        set(value) {
            field = value
            resolveHelp()

        }

    init {
        inflate(context, R.layout.title_text_view, this)

        val padding = 10
        setPadding(padding, padding, padding, padding)
        background = ContextCompat.getDrawable(context, R.drawable.titled_linear_layout_background)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.TitledLinearLayout)
        val titleOfGroup = findViewById<TextView>(R.id.titleOfGroup_tv)
        titleOfGroup.text = attributes.getString(R.styleable.TitledLinearLayout_titleText)
        val helpIcon = findViewById<ImageView>(R.id.help_iv)
        helpIcon.setOnClickListener {
            MaterialAlertDialogBuilder(context, R.style.MyDialogTheme)
                .setMessage(helpText)
                .setPositiveButton(android.R.string.ok, null)
                .create().show()
        }
        resolveHelp()

        attributes.recycle()

    }

    fun setTextBackgroundColor(@ColorRes color: Int){
        findViewById<LinearLayout>(R.id.titledLayout).setBackgroundResource(color)
    }

    private fun resolveHelp() {

        val helpIcon = findViewById<ImageView>(R.id.help_iv)
        helpIcon.visibility = if (helpText.isBlank()) View.GONE else View.VISIBLE
    }

}



