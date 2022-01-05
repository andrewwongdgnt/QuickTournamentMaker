package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import com.dgnt.quickTournamentMaker.data.base.IRepository
import org.joda.time.LocalDateTime

interface ITournamentRepository : IRepository<TournamentEntity> {
    fun getAll(epoch: LocalDateTime): LiveData<List<TournamentEntity>>
}