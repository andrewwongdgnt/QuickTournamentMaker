package com.dgnt.quickTournamentMaker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dgnt.quickTournamentMaker.data.converter.DateConverter
import com.dgnt.quickTournamentMaker.data.management.GroupDAO
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.data.management.PersonDAO
import com.dgnt.quickTournamentMaker.data.management.PersonEntity
import com.dgnt.quickTournamentMaker.data.tournament.*

@Database(
    entities = [PersonEntity::class, GroupEntity::class, TournamentEntity::class, RoundEntity::class, MatchUpEntity::class, ParticipantEntity::class],
    version = 2001 //Old one was 1002
)
@TypeConverters(DateConverter::class)
abstract class QTMDatabase : RoomDatabase() {
    abstract val personDAO: PersonDAO
    abstract val groupDAO: GroupDAO
    abstract val tournamentDAO: TournamentDAO
    abstract val roundDAO: RoundDAO
    abstract val matchUpDAO: MatchUpDAO
    abstract val participantDAO: ParticipantDAO

    companion object {

        @Volatile private var instance: QTMDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                QTMDatabase::class.java, "db")
                .build()
    }
}