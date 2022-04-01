package com.dgnt.quickTournamentMaker.data.search

import com.dgnt.quickTournamentMaker.data.base.BaseRepository

class SearchTermRepository(private val dao: SearchTermDAO) : BaseRepository<SearchTermEntity>(dao), ISearchTermRepository {
    override suspend fun delete(term: String) = dao.delete(term)
}