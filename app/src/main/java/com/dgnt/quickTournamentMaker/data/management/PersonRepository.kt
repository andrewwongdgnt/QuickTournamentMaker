package com.dgnt.quickTournamentMaker.data.management

class PersonRepository(private val dao: PersonDAO) {
    fun getAll() = dao.getAll()
    suspend fun insert(vararg entity: PersonEntity) = dao.insert(*entity)
    suspend fun insert(entities: List<PersonEntity>) = dao.insert(entities)
    suspend fun update(vararg entity: PersonEntity) = dao.update(*entity)
    suspend fun update(entities: List<PersonEntity>) = dao.update(entities)
    suspend fun delete(vararg entity: PersonEntity) = dao.delete(*entity)
    suspend fun delete(entities: List<PersonEntity>) = dao.delete(entities)
}