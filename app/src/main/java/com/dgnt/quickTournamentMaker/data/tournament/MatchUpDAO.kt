package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickTournamentMaker.data.base.BaseDAO

@Dao
interface MatchUpDAO : BaseDAO<MatchUpEntity> {

    @Query("SELECT * FROM $MATCH_UP_TABLE")
    override fun getAll(): LiveData<List<MatchUpEntity>>

}