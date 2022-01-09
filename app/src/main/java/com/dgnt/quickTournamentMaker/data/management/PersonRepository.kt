package com.dgnt.quickTournamentMaker.data.management

import com.dgnt.quickTournamentMaker.data.base.BaseRepository

class PersonRepository(private val dao: PersonDAO) : BaseRepository<PersonEntity>(dao), IPersonRepository {
    override suspend fun updateGroup(oldGroupNames: List<String>, groupName: String) = dao.updateGroup(oldGroupNames, groupName)
    override suspend fun deleteViaGroup(groupNames: List<String>) = dao.deleteViaGroup(groupNames)
}