package com.dgnt.quickTournamentMaker.data.management

import androidx.lifecycle.LiveData

interface IPersonRepository {


    fun getAll(): LiveData<List<PersonEntity>>
    suspend fun insert(vararg entity: PersonEntity): List<Long>
    suspend fun insert(entities: List<PersonEntity>): List<Long>
    suspend fun update(vararg entity: PersonEntity): Int
    suspend fun update(entities: List<PersonEntity>): Int
    suspend fun delete(vararg entity: PersonEntity)
    suspend fun delete(entities: List<PersonEntity>)
    suspend fun updateGroup(oldGroupNames: List<String>, groupName: String): Int
    suspend fun deleteViaGroup(groupNames: List<String>)


}