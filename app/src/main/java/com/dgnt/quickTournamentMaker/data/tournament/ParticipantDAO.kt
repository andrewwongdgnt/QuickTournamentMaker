package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dgnt.quickTournamentMaker.data.base.BaseDAO
import org.joda.time.LocalDateTime

@Dao
interface ParticipantDAO: BaseDAO<ParticipantEntity> {

    @Query("SELECT * FROM $PARTICIPANT_TABLE WHERE epoch = :epoch")
    fun getAll(epoch: LocalDateTime): LiveData<List<ParticipantEntity>>

}