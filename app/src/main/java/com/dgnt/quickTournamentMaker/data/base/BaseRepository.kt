package com.dgnt.quickTournamentMaker.data.base

import androidx.room.Transaction

abstract class BaseRepository<T>(private val dao: BaseDAO<T>) {

    suspend fun insert(entity: T) = dao.insert(entity)
    suspend fun insert(entities: List<T>) = dao.insert(entities)
    suspend fun update(entity: T) = dao.update(entity)
    suspend fun update(entities: List<T>) = dao.update(entities)
    suspend fun delete(entity: T) = dao.delete(entity)
    suspend fun delete(entities: List<T>) = dao.delete(entities)

    @Transaction
    suspend fun upsert(entity: T) = insert(entity).let {
        if (it == -1L) update(entity)
    }
    @Transaction
    suspend fun upsert(entities: List<T>) =
        insert(entities).zip(entities).filter { it.first == -1L }.map { it.second }.let { entitiesToBeUpdated ->
            if (entitiesToBeUpdated.isNotEmpty()) update(entitiesToBeUpdated)
        }
}