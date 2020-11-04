package com.dgnt.quickTournamentMaker.data.tournament

class RoundRepository(private val dao: RoundDAO) {

    companion object {
        @Volatile private var instance: RoundRepository? = null

        fun getInstance(dao: RoundDAO) =
            instance ?: synchronized(this) {
                instance ?: RoundRepository(dao).also { instance = it }
            }
    }


    fun getALL(epoch: Long) = dao.getAll(epoch)
    suspend fun insert(vararg entity: RoundEntity) = dao.insert(*entity)
    suspend fun insert(entities: List<RoundEntity>) = dao.insert(entities)
    suspend fun update(vararg entity: RoundEntity) = dao.update(*entity)
    suspend fun update(entities: List<RoundEntity>) = dao.update(entities)
    suspend fun delete(vararg entity: RoundEntity) = dao.delete(*entity)
    suspend fun delete(entities: List<RoundEntity>) = dao.delete(entities)
}