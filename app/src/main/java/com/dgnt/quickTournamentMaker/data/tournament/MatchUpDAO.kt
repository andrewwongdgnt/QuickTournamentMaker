package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dgnt.quickTournamentMaker.data.base.BaseDAO
import org.joda.time.LocalDateTime

@Dao
interface MatchUpDAO: BaseDAO<MatchUpEntity> {

    @Query("SELECT * FROM $MATCH_UP_TABLE WHERE epoch = :epoch")
    fun getAll(epoch: LocalDateTime): LiveData<List<MatchUpEntity>>

}