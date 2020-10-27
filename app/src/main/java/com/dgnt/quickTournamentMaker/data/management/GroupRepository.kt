package com.dgnt.quickTournamentMaker.data.management

class GroupRepository(private val dao: GroupDAO) {
    val groups = dao.getAllGroups()
    suspend fun insert(groupEntity:GroupEntity){
        dao.insertGroup(groupEntity)
    }
    suspend fun insert(groupEntities:List<GroupEntity>){
        dao.insertGroups(groupEntities)
    }
    suspend fun update(groupEntity:GroupEntity){
        dao.updateGroup(groupEntity)
    }
    suspend fun update(groupEntities:List<GroupEntity>){
        dao.updateGroups(groupEntities)
    }
    suspend fun delete(groupEntity: GroupEntity){
        dao.deleteGroup(groupEntity)
    }

}