package com.dgnt.quickTournamentMaker.data.search

import com.dgnt.quickTournamentMaker.data.base.IRepository

interface ISearchTermRepository : IRepository<SearchTermEntity> {
    suspend fun delete(term: String)
}