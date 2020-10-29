package com.dgnt.quickTournamentMaker.data.tournament

class MatchUpRepository(private val dao: MatchUpDAO) {
    fun getALL(epoch: Long) = dao.getAll(epoch)
    suspend fun insert(vararg entity: MatchUpEntity) = dao.insert(*entity)
    suspend fun insert(entities: List<MatchUpEntity>) = dao.insert(entities)
    suspend fun update(vararg entity: MatchUpEntity) = dao.update(*entity)
    suspend fun update(entities: List<MatchUpEntity>) = dao.update(entities)
    suspend fun delete(vararg entity: MatchUpEntity) = dao.delete(*entity)
    suspend fun delete(entities: List<MatchUpEntity>) = dao.delete(entities)
}