package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickTournamentMaker.data.base.BaseDAO

@Dao
interface TournamentDAO : BaseDAO<TournamentEntity> {

    @Query("SELECT * FROM $TOURNAMENT_TABLE")
    override fun getAll(): LiveData<List<TournamentEntity>>

}