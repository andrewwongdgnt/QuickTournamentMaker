package com.dgnt.quickTournamentMaker.data.management

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dgnt.quickTournamentMaker.data.base.BaseDAO

@Dao
interface GroupDAO: BaseDAO<GroupEntity> {

    @Query("SELECT * FROM $GROUP_TABLE")
    fun getAll():LiveData<List<GroupEntity>>

}