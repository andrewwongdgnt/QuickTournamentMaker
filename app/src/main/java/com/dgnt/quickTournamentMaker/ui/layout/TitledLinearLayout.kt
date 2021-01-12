package com.dgnt.quickTournamentMaker.ui.layout

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dgnt.quickTournamentMaker.R


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
        helpIcon.setOnClickListener { _ ->
            AlertDialog.Builder(context)
                .setMessage(helpText)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create().show()
        }
        resolveHelp()

        attributes.recycle()

    }

    private fun resolveHelp() {

        val helpIcon = findViewById<ImageView>(R.id.help_iv)
        helpIcon.visibility = if (helpText.isNullOrBlank()) View.GONE else View.VISIBLE
    }

}



