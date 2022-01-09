package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickTournamentMaker.data.base.BaseDAO

@Dao
interface RoundDAO : BaseDAO<RoundEntity> {

    @Query("SELECT * FROM $ROUND_TABLE")
    override fun getAll(): LiveData<List<RoundEntity>>

}