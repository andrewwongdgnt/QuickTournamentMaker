package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickTournamentMaker.data.base.BaseDAO

@Dao
interface ParticipantDAO : BaseDAO<ParticipantEntity> {

    @Query("SELECT * FROM $PARTICIPANT_TABLE")
    override fun getAll(): LiveData<List<ParticipantEntity>>

}