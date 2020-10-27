package com.dgnt.quickTournamentMaker.data.management

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PersonDAO {

    @Query("SELECT * FROM $PERSON_TABLE")
    fun getAllPersons():LiveData<List<PersonEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPerson(personEntity:PersonEntity):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPersons(personEntities:List<PersonEntity>):List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updatePerson(personEntity:PersonEntity):Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updatePersons(personEntities:List<PersonEntity>):List<Long>

    @Delete
    suspend fun deletePerson(personEntity:PersonEntity)
}