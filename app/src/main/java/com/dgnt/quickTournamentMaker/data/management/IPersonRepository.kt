package com.dgnt.quickTournamentMaker.data.management

import androidx.lifecycle.LiveData
import com.dgnt.quickTournamentMaker.data.base.IRepository

interface IPersonRepository : IRepository<PersonEntity> {
    fun getAll(): LiveData<List<PersonEntity>>
    suspend fun updateGroup(oldGroupNames: List<String>, groupName: String): Int
    suspend fun deleteViaGroup(groupNames: List<String>)
}