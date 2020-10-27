package com.dgnt.quickTournamentMaker.data.management

class PersonRepository(private val dao: PersonDAO) {
    val persons = dao.getAllPersons()
    suspend fun insert(personEntity:PersonEntity){
        dao.insertPerson(personEntity)
    }
    suspend fun insert(personEntities:List<PersonEntity>){
        dao.insertPersons(personEntities)
    }
    suspend fun update(personEntity:PersonEntity){
        dao.updatePerson(personEntity)
    }
    suspend fun update(personEntities:List<PersonEntity>){
        dao.updatePersons(personEntities)
    }
    suspend fun delete(personEntity: PersonEntity){
        dao.deletePerson(personEntity)
    }

}