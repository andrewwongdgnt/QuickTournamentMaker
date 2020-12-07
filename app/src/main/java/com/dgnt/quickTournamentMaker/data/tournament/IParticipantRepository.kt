package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData

interface IParticipantRepository {


    fun getAll(epoch: Long): LiveData<List<ParticipantEntity>>
    suspend fun insert(vararg entity: ParticipantEntity): List<Long>
    suspend fun insert(entities: List<ParticipantEntity>): List<Long>
    suspend fun update(vararg entity: ParticipantEntity): Int
    suspend fun update(entities: List<ParticipantEntity>): Int
    suspend fun delete(vararg entity: ParticipantEntity)
    suspend fun delete(entities: List<ParticipantEntity>)
}