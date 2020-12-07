package com.dgnt.quickTournamentMaker.data.management

class PersonRepository(private val dao: PersonDAO) : IPersonRepository {

    override fun getAll() = dao.getAll()
    override suspend fun insert(vararg entity: PersonEntity) = dao.insert(*entity)
    override suspend fun insert(entities: List<PersonEntity>) = dao.insert(entities)
    override suspend fun update(vararg entity: PersonEntity) = dao.update(*entity)
    override suspend fun update(entities: List<PersonEntity>) = dao.update(entities)
    override suspend fun delete(vararg entity: PersonEntity) = dao.delete(*entity)
    override suspend fun delete(entities: List<PersonEntity>) = dao.delete(entities)
    override suspend fun updateGroup(oldGroupNames: List<String>, groupName: String) = dao.updateGroup(oldGroupNames, groupName)
    override suspend fun deleteViaGroup(groupNames: List<String>) = dao.deleteViaGroup(groupNames)


}