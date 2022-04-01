package com.dgnt.quickTournamentMaker.data.search

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickTournamentMaker.data.base.BaseDAO

@Dao
interface SearchTermDAO : BaseDAO<SearchTermEntity> {

    @Query("SELECT * FROM $SEARCH_TERM_TABLE ORDER BY count DESC, term")
    override fun getAll(): LiveData<List<SearchTermEntity>>

    @Query("DELETE FROM $SEARCH_TERM_TABLE WHERE term = :term")
    suspend fun delete(term: String)

}