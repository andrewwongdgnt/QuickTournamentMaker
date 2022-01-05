package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickTournamentMaker.data.base.BaseDAO
import org.joda.time.LocalDateTime

@Dao
interface TournamentDAO : BaseDAO<TournamentEntity> {

    @Query("SELECT * FROM $TOURNAMENT_TABLE WHERE epoch = :epoch")
    fun getAll(epoch: LocalDateTime): LiveData<List<TournamentEntity>>

}