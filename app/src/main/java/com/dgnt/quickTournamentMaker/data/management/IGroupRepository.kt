package com.dgnt.quickTournamentMaker.data.management

import androidx.lifecycle.LiveData
import com.dgnt.quickTournamentMaker.data.base.IRepository

interface IGroupRepository : IRepository<GroupEntity> {
    fun getAll(): LiveData<List<GroupEntity>>
}