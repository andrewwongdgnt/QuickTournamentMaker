package com.dgnt.quickTournamentMaker.data.tournament

class RoundRepository(private val dao: RoundDAO) {
    fun getALL(epoch: Long) = dao.getAll(epoch)
    suspend fun insert(vararg entity: RoundEntity) = dao.insert(*entity)
    suspend fun insert(entities: List<RoundEntity>) = dao.insert(entities)
    suspend fun update(vararg entity: RoundEntity) = dao.update(*entity)
    suspend fun update(entities: List<RoundEntity>) = dao.update(entities)
    suspend fun delete(vararg entity: RoundEntity) = dao.delete(*entity)
    suspend fun delete(entities: List<RoundEntity>) = dao.delete(entities)
}