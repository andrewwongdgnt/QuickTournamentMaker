package com.dgnt.quickTournamentMaker.data.management

import com.dgnt.quickTournamentMaker.data.base.BaseRepository

class GroupRepository(private val dao: GroupDAO) : BaseRepository<GroupEntity>(dao), IGroupRepository {
    override fun getAll() = dao.getAll()
}