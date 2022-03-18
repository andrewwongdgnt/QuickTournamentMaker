package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.data.tournament.TournamentEntity
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestoredTournamentInformation(
    val extendedTournamentInformation: ExtendedTournamentInformation,
    val foundationalTournamentEntities: FoundationalTournamentEntities
) : Parcelable {
    fun toTournamentEntity(rankingConfigService: IRankingConfigService) =
        extendedTournamentInformation.basicTournamentInformation.run {
            TournamentEntity(
                creationDate,
                lastModifiedDate,
                title,
                description,
                tournamentType,
                rankingConfigService.toString(rankConfig),
                seedType,
                extendedTournamentInformation.progress
            )

        }

}