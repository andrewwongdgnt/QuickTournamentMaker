package com.dgnt.quickTournamentMaker.data.tournament

import org.joda.time.LocalDateTime

class TournamentRepository(private val dao: TournamentDAO) : ITournamentRepository {

    override fun getAll(epoch: LocalDateTime) = dao.getAll(epoch)
    override suspend fun insert(vararg entity: TournamentEntity) = dao.insert(*entity)
    override suspend fun insert(entities: List<TournamentEntity>) = dao.insert(entities)
    override suspend fun update(vararg entity: TournamentEntity) = dao.update(*entity)
    override suspend fun update(entities: List<TournamentEntity>) = dao.update(entities)
    override suspend fun delete(vararg entity: TournamentEntity) = dao.delete(*entity)
    override suspend fun delete(entities: List<TournamentEntity>) = dao.delete(entities)
}