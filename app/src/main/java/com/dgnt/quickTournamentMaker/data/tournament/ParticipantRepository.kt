package com.dgnt.quickTournamentMaker.data.tournament

import com.dgnt.quickTournamentMaker.data.base.BaseRepository
import org.joda.time.LocalDateTime

class ParticipantRepository(dao: ParticipantDAO) : BaseRepository<ParticipantEntity>(dao), IParticipantRepository