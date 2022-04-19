package com.dgnt.quickTournamentMaker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dgnt.quickTournamentMaker.data.converter.DateConverter
import com.dgnt.quickTournamentMaker.data.converter.ProgressConverter
import com.dgnt.quickTournamentMaker.data.management.GroupDAO
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.data.management.PersonDAO
import com.dgnt.quickTournamentMaker.data.management.PersonEntity
import com.dgnt.quickTournamentMaker.data.search.SearchTermDAO
import com.dgnt.quickTournamentMaker.data.search.SearchTermEntity
import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.util.SimpleLogger
import java.util.concurrent.Executors


@Database(
    entities = [PersonEntity::class, GroupEntity::class, TournamentEntity::class, RoundEntity::class, MatchUpEntity::class, ParticipantEntity::class, SearchTermEntity::class],
    version = 2001, //Old one was 1002
)
@TypeConverters(DateConverter::class, ProgressConverter::class)
abstract class QTMDatabase : RoomDatabase() {
    abstract val personDAO: PersonDAO
    abstract val groupDAO: GroupDAO
    abstract val tournamentDAO: TournamentDAO
    abstract val roundDAO: RoundDAO
    abstract val matchUpDAO: MatchUpDAO
    abstract val participantDAO: ParticipantDAO
    abstract val searchTermDAO: SearchTermDAO

    companion object {

        @Volatile private var instance: QTMDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                QTMDatabase::class.java, "db"
            )
                .setQueryCallback({ sqlQuery, bindArgs ->
                    SimpleLogger.d(this,"SQL Query: $sqlQuery SQL Args: $bindArgs")
                }, Executors.newSingleThreadExecutor())
//                .addMigrations(MIGRATION_1002_2001)
                .build()

        private val MIGRATION_1002_2001: Migration = object : Migration(1002, 2001) {
            override fun migrate(database: SupportSQLiteDatabase) {

                val newUUID = """
                    lower(
                        hex(randomblob(4)) || '-' || hex(randomblob(2)) || '-' || '4' ||
                        substr(hex( randomblob(2)), 2) || '-' ||
                        substr('AB89', 1 + (abs(random()) % 4) , 1)  ||
                        substr(hex(randomblob(2)), 2) || '-' ||
                        hex(randomblob(6))
                        )
                """.trimMargin()

                database.beginTransaction()

                // migrate person table
                database.execSQL(
                    """
                    INSERT INTO personTableV2 (id, name, note, groupName)
                    SELECT 
                    $newUUID id,
                    name, note, groupName FROM personTable;      
                """.trimMargin()
                )
                // migrate group table
                database.execSQL(
                    """
                    INSERT INTO groupTableV2 (id, name, note, favourite)
                    SELECT 
                    $newUUID id,
                    name, note, favourite FROM groupTable;      
                """.trimMargin()
                )
                //migrate tournament table
                database.execSQL("ALTER TABLE tournamentTable ADD seedType TEXT;")
                database.execSQL("ALTER TABLE tournamentTable ADD progress TEXT;")
                //migrate matchUp table
                database.execSQL("ALTER TABLE matchUpTable ADD useTitle INTEGER;")
                database.execSQL("ALTER TABLE matchUpTable ADD name TEXT;")
                database.execSQL("ALTER TABLE matchUpTable ADD containsBye INTEGER;")
                database.execSQL("ALTER TABLE matchUpTable ADD isOpen INTEGER;")

                database.setTransactionSuccessful()
                database.endTransaction()
            }
        }
    }

}