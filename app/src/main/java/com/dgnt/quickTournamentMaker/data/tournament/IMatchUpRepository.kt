package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import com.dgnt.quickTournamentMaker.data.base.IRepository
import org.joda.time.LocalDateTime

interface IMatchUpRepository : IRepository<MatchUpEntity> {
    fun getAll(epoch: LocalDateTime): LiveData<List<MatchUpEntity>>
}