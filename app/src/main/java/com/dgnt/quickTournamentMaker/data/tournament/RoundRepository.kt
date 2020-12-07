package com.dgnt.quickTournamentMaker.data.tournament

class RoundRepository(private val dao: RoundDAO):IRoundRepository {

  override fun getAll(epoch: Long) = dao.getAll(epoch)
  override suspend fun insert(vararg entity: RoundEntity) = dao.insert(*entity)
  override suspend fun insert(entities: List<RoundEntity>) = dao.insert(entities)
  override suspend fun update(vararg entity: RoundEntity) = dao.update(*entity)
  override suspend fun update(entities: List<RoundEntity>) = dao.update(entities)
  override suspend fun delete(vararg entity: RoundEntity) = dao.delete(*entity)
  override suspend fun delete(entities: List<RoundEntity>) = dao.delete(entities)
}