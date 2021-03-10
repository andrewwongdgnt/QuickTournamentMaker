package com.dgnt.quickTournamentMaker.service.data

import com.dgnt.quickTournamentMaker.service.interfaces.*

data class TournamentTypeServices(val roundGeneratorService: IRoundGeneratorService, val roundUpdateService: IRoundUpdateService, val rankingService: IRankingService, val seedService: ISeedService, val matchUpStatusTransformService: IMatchUpStatusTransformService) {


}