package com.dgnt.quickTournamentMaker.data.management

import androidx.lifecycle.LiveData

interface IGroupRepository {
    fun getAll(): LiveData<List<GroupEntity>>
    suspend fun insert(vararg entity: GroupEntity): List<Long>
    suspend fun insert(entities: List<GroupEntity>): List<Long>
    suspend fun update(vararg entity: GroupEntity): Int
    suspend fun update(entities: List<GroupEntity>): Int
    suspend fun delete(vararg entity: GroupEntity)
    suspend fun delete(entities: List<GroupEntity>)
}