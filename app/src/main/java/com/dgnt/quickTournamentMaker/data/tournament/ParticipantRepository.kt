package com.dgnt.quickTournamentMaker.data.tournament

class ParticipantRepository(private val dao: ParticipantDAO) {

    companion object {
        @Volatile private var instance: ParticipantRepository? = null

        fun getInstance(dao: ParticipantDAO) =
            instance ?: synchronized(this) {
                instance ?: ParticipantRepository(dao).also { instance = it }
            }
    }
    
    fun getALL(epoch: Long) = dao.getAll(epoch)
    suspend fun insert(vararg entity: ParticipantEntity) = dao.insert(*entity)
    suspend fun insert(entities: List<ParticipantEntity>) = dao.insert(entities)
    suspend fun update(vararg entity: ParticipantEntity) = dao.update(*entity)
    suspend fun update(entities: List<ParticipantEntity>) = dao.update(entities)
    suspend fun delete(vararg entity: ParticipantEntity) = dao.delete(*entity)
    suspend fun delete(entities: List<ParticipantEntity>) = dao.delete(entities)
}