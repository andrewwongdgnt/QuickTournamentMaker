package com.dgnt.quickTournamentMaker.util

import android.content.Context
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.ParticipantType
import java.text.DateFormat


class TournamentUtil {
    companion object {
        fun isPowerOf2(candidate: Int): Boolean {
            return candidate > 0 && candidate and candidate - 1 == 0
        }

        fun nextPowerOf2(candidate: Int): Int {
            var n = candidate
            n--
            n = n or (n shr 1) // Divide by 2^k for consecutive doublings of k up to 32,
            n = n or (n shr 2) // and then or the results.
            n = n or (n shr 4)
            n = n or (n shr 8)
            n = n or (n shr 16)
            n++ // The result is a number of 1 bits equal to the number
            // of bits in the original number, plus 1. That's the
            // next highest power of 2.
            return n
        }

        fun dpToPixels(context: Context, sizeInDp: Int): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (sizeInDp * scale + 0.5f).toInt()
        }

        fun shortenDescription(
            description: String,
            maxCharactersPerLine: Int,
            maxTotalCharacters: Int
        ): String {
            val resolvedMaxCharactersPerLine =
                if (maxCharactersPerLine > maxTotalCharacters) maxTotalCharacters else maxCharactersPerLine
            val sb = StringBuilder(description)
            var i = 0
            while (sb.indexOf(" ", i + resolvedMaxCharactersPerLine).also { i = it } != -1) {
                sb.replace(i, i + 1, "\n")
            }
            return if (sb.toString().length > maxTotalCharacters) {
                // "\u2026" is the unicode for ellipses
                sb.toString().substring(0, maxTotalCharacters) + "\u2026"
            } else sb.toString()
        }

//        fun createDefaultTitle(
//            context: Context,
//            tournamentType: String?,
//            CreationTimeInEpoch: Long
//        ): String? {
//            return context.resources
//                .getString(R.string.defaultTitle, tournamentType, epochToDate(CreationTimeInEpoch))
//        }

        fun epochToDate(epoch: Long): String? {
            return DateFormat.getDateInstance(DateFormat.SHORT).format(epoch)
        }

        //public static List<HistoricalRound> buildRoundList(final Tournament tournament)

        //public static List<HistoricalMatchUp> buildMatchUpList(final Tournament tournament)

        fun getNormalParticipantCount(participantList: List<Participant>): Int {
            return participantList.count{it.participantType== ParticipantType.NORMAL}
        }

        //public static String tournamentTypeToString(final Context context, final Tournament.TournamentType tournamentType)

        const val DEFAULT_DISPLAY_COLOR = -0x1000000
    }
}