package com.dgnt.quickTournamentMaker.data.management

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickTournamentMaker.data.base.BaseDAO

@Dao
interface GroupDAO : BaseDAO<GroupEntity> {

    @Query("SELECT * FROM $GROUP_TABLE")
    override fun getAll(): LiveData<List<GroupEntity>>

}