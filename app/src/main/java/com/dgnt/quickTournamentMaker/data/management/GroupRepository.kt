package com.dgnt.quickTournamentMaker.data.management

class GroupRepository(private val dao: GroupDAO) {
    fun getAll() = dao.getAll()
    suspend fun insert(vararg entity: GroupEntity) = dao.insert(*entity)
    suspend fun insert(entities: List<GroupEntity>) = dao.insert(entities)
    suspend fun update(vararg entity: GroupEntity) = dao.update(*entity)
    suspend fun update(entities: List<GroupEntity>) = dao.update(entities)
    suspend fun delete(vararg entity: GroupEntity) = dao.delete(*entity)
    suspend fun delete(entities: List<GroupEntity>) = dao.delete(entities)
}