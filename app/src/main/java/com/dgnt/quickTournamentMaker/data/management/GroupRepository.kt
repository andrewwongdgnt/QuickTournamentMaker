package com.dgnt.quickTournamentMaker.data.management

class GroupRepository(private val dao: GroupDAO) : IGroupRepository {

    override fun getAll() = dao.getAll()
    override suspend fun insert(vararg entity: GroupEntity) = dao.insert(*entity)
    override suspend fun insert(entities: List<GroupEntity>) = dao.insert(entities)
    override suspend fun update(vararg entity: GroupEntity) = dao.update(*entity)
    override suspend fun update(entities: List<GroupEntity>) = dao.update(entities)
    override suspend fun delete(vararg entity: GroupEntity) = dao.delete(*entity)
    override suspend fun delete(entities: List<GroupEntity>) = dao.delete(entities)
}