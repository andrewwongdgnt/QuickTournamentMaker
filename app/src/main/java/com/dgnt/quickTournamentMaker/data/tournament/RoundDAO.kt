package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dgnt.quickTournamentMaker.data.base.BaseDAO
import org.joda.time.LocalDateTime

@Dao
interface RoundDAO: BaseDAO<RoundEntity> {

    @Query("SELECT * FROM $ROUND_TABLE WHERE epoch = :epoch")
    fun getAll(epoch: LocalDateTime): LiveData<List<RoundEntity>>

}