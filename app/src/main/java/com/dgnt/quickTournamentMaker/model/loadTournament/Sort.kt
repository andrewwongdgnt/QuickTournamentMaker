package com.dgnt.quickTournamentMaker.model.loadTournament

import androidx.annotation.StringRes
import com.dgnt.quickTournamentMaker.R

enum class Sort(@StringRes val stringRes: Int) {
    CREATION_DATE_NEWEST(R.string.creationTimeNewest),
    CREATION_DATE_OLDEST(R.string.creationTimeOldest),
    LAST_MODIFIED_DATE_NEWEST(R.string.lastModifiedTimeNewest),
    LAST_MODIFIED_DATE_OLDEST(R.string.lastModifiedTimeOldest),
    NAME(R.string.name),
    NAME_REVERSED(R.string.nameReversed),
    TOURNAMENT_TYPE(R.string.tournamentType),
    TOURNAMENT_TYPE_REVERSED(R.string.tournamentTypeReversed),
    PROGRESS(R.string.progress),
    PROGRESS_REVERSED(R.string.progressReversed),
}