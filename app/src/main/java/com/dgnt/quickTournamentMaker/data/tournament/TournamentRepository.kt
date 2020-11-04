package com.dgnt.quickTournamentMaker.data.tournament

class TournamentRepository(private val dao: TournamentDAO) {

    companion object {
        @Volatile private var instance: TournamentRepository? = null

        fun getInstance(dao: TournamentDAO) =
            instance ?: synchronized(this) {
                instance ?: TournamentRepository(dao).also { instance = it }
            }
    }


    fun getALL(epoch: Long) = dao.getAll(epoch)
    suspend fun insert(vararg entity: TournamentEntity) = dao.insert(*entity)
    suspend fun insert(entities: List<TournamentEntity>) = dao.insert(entities)
    suspend fun update(vararg entity: TournamentEntity) = dao.update(*entity)
    suspend fun update(entities: List<TournamentEntity>) = dao.update(entities)
    suspend fun delete(vararg entity: TournamentEntity) = dao.delete(*entity)
    suspend fun delete(entities: List<TournamentEntity>) = dao.delete(entities)
}