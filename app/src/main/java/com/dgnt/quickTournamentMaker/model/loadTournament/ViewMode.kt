package com.dgnt.quickTournamentMaker.model.loadTournament

import androidx.annotation.StringRes
import com.dgnt.quickTournamentMaker.R

enum class ViewMode(@StringRes val stringRes: Int) {
    MINIMAL(R.string.viewAsMinimal),
    BASIC(R.string.viewAsBasic),
    DETAILED(R.string.viewAsDetailed)
}