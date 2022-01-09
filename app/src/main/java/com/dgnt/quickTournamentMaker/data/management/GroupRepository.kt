package com.dgnt.quickTournamentMaker.data.management

import com.dgnt.quickTournamentMaker.data.base.BaseRepository

class GroupRepository(dao: GroupDAO) : BaseRepository<GroupEntity>(dao), IGroupRepository