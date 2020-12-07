package com.dgnt.quickTournamentMaker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dgnt.quickTournamentMaker.data.management.GroupDAO
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.data.management.PersonDAO
import com.dgnt.quickTournamentMaker.data.management.PersonEntity

@Database(
    entities = [PersonEntity::class, GroupEntity::class],
    version = 2001 //Old one was 1002
)
abstract class QTMDatabase : RoomDatabase() {
    abstract val personDAO: PersonDAO
    abstract val groupDAO: GroupDAO

    companion object {
//        @Volatile
//        private var INSTANCE: QTMDatabase? = null
//        fun getInstance(context: Context): QTMDatabase {
//            synchronized(this) {
//                var instance = INSTANCE
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        QTMDatabase::class.java,
//                        "db" // same as old one
//                    ).build()
//                }
//                return instance
//            }
//        }

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