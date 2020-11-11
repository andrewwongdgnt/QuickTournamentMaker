package com.dgnt.quickTournamentMaker.data.management

class PersonRepository(private val dao: PersonDAO) {

    companion object {
        @Volatile private var instance: PersonRepository? = null

        fun getInstance(dao: PersonDAO) =
            instance ?: synchronized(this) {
                instance ?: PersonRepository(dao).also { instance = it }
            }
    }

    fun getAll() = dao.getAll()
    suspend fun insert(vararg entity: PersonEntity) = dao.insert(*entity)
    suspend fun insert(entities: List<PersonEntity>) = dao.insert(entities)
    suspend fun update(vararg entity: PersonEntity) = dao.update(*entity)
    suspend fun update(entities: List<PersonEntity>) = dao.update(entities)
    suspend fun delete(vararg entity: PersonEntity) = dao.delete(*entity)
    suspend fun delete(entities: List<PersonEntity>) = dao.delete(entities)
    suspend fun updateGroup(groupId: String, groupName: String) =dao.updateGroup(groupId,groupName)
}