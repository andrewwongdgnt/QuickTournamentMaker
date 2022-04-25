package com.dgnt.quickTournamentMaker.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
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
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentBuilderService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInformationCreatorService
import com.dgnt.quickTournamentMaker.util.SimpleLogger
import org.joda.time.LocalDateTime
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

        @Volatile
        private var instance: QTMDatabase? = null
        private val LOCK = Any()
        private lateinit var tournamentBuilderService: ITournamentBuilderService
        private lateinit var tournamentInformationCreatorService: ITournamentInformationCreatorService

        operator fun invoke(
            context: Context,
            tournamentBuilderService: ITournamentBuilderService,
            tournamentInformationCreatorService: ITournamentInformationCreatorService
        ) = instance ?: synchronized(LOCK) {
            instance ?: run {
                this.tournamentBuilderService = tournamentBuilderService
                this.tournamentInformationCreatorService = tournamentInformationCreatorService
                buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                QTMDatabase::class.java, "db"
            )
                .setQueryCallback({ sqlQuery, bindArgs ->
                    SimpleLogger.d(this, "SQL Query: $sqlQuery SQL Args: $bindArgs")
                }, Executors.newSingleThreadExecutor())
                .addMigrations(MIGRATION_1002_2001)
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
                    CREATE TABLE personTable_temp (
                        id TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        note TEXT NOT NULL,
                        groupName TEXT NOT NULL                        
                    );     
                """.trimMargin()
                )
                database.execSQL(
                    """
                    INSERT INTO personTable_temp (
                        id, 
                        name, 
                        note, 
                        groupName
                    )
                    SELECT 
                        $newUUID id,
                        name, 
                        note, 
                        groupName 
                    FROM personTable;      
                """.trimMargin()
                )
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_personTable_name ON personTable_temp (name)")
                database.execSQL("DROP TABLE personTable")
                database.execSQL("ALTER TABLE personTable_temp RENAME TO personTable")

                // migrate group table
                database.execSQL(
                    """
                    CREATE TABLE groupTable_temp (
                        id TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        note TEXT NOT NULL,
                        favourite INTEGER NOT NULL                        
                    );     
                """.trimMargin()
                )
                database.execSQL(
                    """
                    INSERT INTO groupTable_temp (
                        id, 
                        name, 
                        note, 
                        favourite
                    )
                    SELECT 
                        $newUUID id,
                        name, 
                        note, 
                        favourite 
                    FROM groupTable;      
                """.trimMargin()
                )
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_groupTable_name ON groupTable_temp (name)")
                database.execSQL("DROP TABLE groupTable")
                database.execSQL("ALTER TABLE groupTable_temp RENAME TO groupTable")

                //migrate tournament table
                database.execSQL(
                    """
                    CREATE TABLE tournamentTable_temp (
                        epoch INTEGER NOT NULL PRIMARY KEY,
                        lastModifiedTime INTEGER,
                        name TEXT NOT NULL,
                        note TEXT NOT NULL,
                        type TEXT NOT NULL,
                        rankingConfig TEXT NOT NULL,
                        seedType TEXT NOT NULL,                      
                        progress TEXT NOT NULL                      
                    );     
                """.trimMargin()
                )
                database.execSQL(
                    """
                    INSERT INTO tournamentTable_temp (
                        epoch, 
                        lastModifiedTime, 
                        name, 
                        note, 
                        type, 
                        rankingConfig, 
                        seedType, 
                        progress
                    )
                    SELECT 
                        epoch, 
                        lastModifiedTime, 
                        name, 
                        note, 
                        type, 
                        rankingConfig,
                        'CUSTOM' seedType,
                        '100/100' progress
                    FROM tournamentTable;      
                """.trimMargin()
                )
                database.execSQL("DROP TABLE tournamentTable")
                database.execSQL("ALTER TABLE tournamentTable_temp RENAME TO tournamentTable")
                //migrate round table
                database.execSQL(
                    """
                    CREATE TABLE roundTable_temp (
                        epoch INTEGER NOT NULL,
                        roundGroupIndex INTEGER NOT NULL,
                        roundIndex INTEGER NOT NULL,
                        name TEXT NOT NULL,
                        note TEXT NOT NULL,
                        color INTEGER NOT NULL,                                                                  
                        PRIMARY KEY (
                            epoch,
                            roundGroupIndex,
                            roundIndex
                        )
                    );     
                """.trimMargin()
                )
                database.execSQL(
                    """
                    INSERT INTO roundTable_temp (
                        epoch, 
                        roundGroupIndex,
                        roundIndex, 
                        name, 
                        note, 
                        color
                    )
                    SELECT 
                        epoch, 
                        roundGroupIndex, 
                        roundIndex, 
                        name,
                        note,
                        color
                    FROM roundTable;      
                """.trimMargin()
                )
                database.execSQL("DROP TABLE roundTable")
                database.execSQL("ALTER TABLE roundTable_temp RENAME TO roundTable")
                //migrate matchUp table
                database.execSQL(
                    """
                    CREATE TABLE matchUpTable_temp (
                        epoch INTEGER NOT NULL,
                        roundGroupIndex INTEGER NOT NULL,
                        roundIndex INTEGER NOT NULL,
                        matchUpIndex INTEGER NOT NULL,
                        useTitle INTEGER NOT NULL,
                        name TEXT NOT NULL,
                        note TEXT NOT NULL,
                        color INTEGER NOT NULL,
                        status TEXT NOT NULL,
                        containsBye INTEGER NOT NULL,                      
                        isOpen INTEGER NOT NULL,                                              
                        PRIMARY KEY (
                            epoch,
                            roundGroupIndex,
                            roundIndex,
                            matchUpIndex
                        )
                    );     
                """.trimMargin()
                )
                database.execSQL(
                    """
                    INSERT INTO matchUpTable_temp (
                        epoch, 
                        roundGroupIndex,
                        roundIndex, 
                        matchUpIndex, 
                        useTitle, 
                        name, 
                        note, 
                        color, 
                        status, 
                        containsBye, 
                        isOpen
                    )
                    SELECT 
                        epoch, 
                        roundGroupIndex, 
                        roundIndex, 
                        matchUpIndex, 
                        0 useTitle, 
                        '' name,
                        note,
                        color,
                        matchUpStatus,
                        0 containsBye,
                        1 isOpen
                    FROM matchUpTable;      
                """.trimMargin()
                )
                database.execSQL("DROP TABLE matchUpTable")
                database.execSQL("ALTER TABLE matchUpTable_temp RENAME TO matchUpTable")
                //migrate participant table
                database.execSQL(
                    """
                    CREATE TABLE participantTable_temp (
                        epoch INTEGER NOT NULL,
                        name TEXT NOT NULL,
                        seedIndex INTEGER NOT NULL,
                        displayName TEXT NOT NULL,
                        note TEXT NOT NULL,
                        type TEXT NOT NULL,
                        color INTEGER NOT NULL,                                            
                        PRIMARY KEY (
                            epoch,
                            name,
                            seedIndex
                        )
                    );     
                """.trimMargin()
                )
                database.execSQL(
                    """
                    INSERT INTO participantTable_temp (
                        epoch,
                        name,
                        seedIndex,
                        displayName,
                        note,
                        type,
                        color  
                    )
                    SELECT                         
                        epoch,
                        name,
                        seedIndex,
                        displayName,
                        note,
                        type,
                        color  
                    FROM participantTable;      
                """.trimMargin()
                )
                database.execSQL("DROP TABLE participantTable")
                database.execSQL("ALTER TABLE participantTable_temp RENAME TO participantTable")
                //create search term table
                database.execSQL(
                    """
                    CREATE TABLE searchTermTable (
                        term TEXT NOT NULL PRIMARY KEY,
                        count INTEGER NOT NULL
                    );     
                """.trimMargin()
                )

                deriveValues1002_2001(database)
                database.setTransactionSuccessful()
                database.endTransaction()
            }
        }

        private fun deriveValues1002_2001(database: SupportSQLiteDatabase) {
            val participantMap = mutableMapOf<Long, MutableList<Participant>>()
            database.query("SELECT epoch, name, seedIndex, type from participantTable ORDER BY seedIndex").use { cursor ->
                val epochIndex = cursor.getColumnIndex("epoch")
                val nameIndex = cursor.getColumnIndex("name")
                val seedIndexIndex = cursor.getColumnIndex("seedIndex")
                val typeIndex = cursor.getColumnIndex("type")
                while (cursor.moveToNext()) {
                    val epoch = cursor.getLong(epochIndex)
                    val model = Participant(
                        Person("", "", ""),
                        ParticipantType.valueOf(cursor.getString(typeIndex))
                    )
                    participantMap[epoch]?.add(model) ?: run {
                        participantMap[epoch] = mutableListOf(model)
                    }
                }
            }

            val matchUpMap = mutableMapOf<Long, MutableList<MatchUpEntity>>()
            database.query("SELECT epoch, roundGroupIndex, roundIndex, matchUpIndex, status from matchUpTable").use { cursor ->
                val epochIndex = cursor.getColumnIndex("epoch")
                val roundGroupIndexIndex = cursor.getColumnIndex("roundGroupIndex")
                val roundIndexIndex = cursor.getColumnIndex("roundIndex")
                val matchUpIndexIndex = cursor.getColumnIndex("matchUpIndex")
                val statusIndex = cursor.getColumnIndex("status")
                while (cursor.moveToNext()) {
                    val epoch = cursor.getLong(epochIndex)
                    val entity = MatchUpEntity(
                        LocalDateTime(epoch),
                        cursor.getInt(roundGroupIndexIndex),
                        cursor.getInt(roundIndexIndex),
                        cursor.getInt(matchUpIndexIndex),
                        false,
                        "",
                        "",
                        0,
                        MatchUpStatus.valueOf(cursor.getString(statusIndex)),
                        false,
                        false
                    )
                    matchUpMap[epoch]?.add(entity) ?: run {
                        matchUpMap[epoch] = mutableListOf(entity)
                    }
                }
            }

            val tournaments = mutableListOf<Pair<Long, Tournament>>()
            database.query("SELECT epoch, type from tournamentTable").use { cursor ->
                val epochIndex = cursor.getColumnIndex("epoch")
                val typeIndex = cursor.getColumnIndex("type")
                while (cursor.moveToNext()) {
                    val epoch = cursor.getLong(epochIndex)

                    val tournamentInfo = tournamentInformationCreatorService.create(
                        "fake",
                        mapOf(),
                        "",
                        TournamentType.valueOf(cursor.getString(typeIndex)),
                        SeedType.CUSTOM,
                        RankPriorityConfig.DEFAULT
                    )
                    participantMap[epoch]?.let { participants ->
                        matchUpMap[epoch]?.let { matchUps ->

                            tournamentBuilderService.build(tournamentInfo, participants).also { tournament ->
                                matchUps.forEach { entity ->
                                    tournament.matchUps.find { matchUp -> matchUp.roundGroupIndex == entity.roundGroupIndex && matchUp.roundIndex == entity.roundIndex && matchUp.matchUpIndex == entity.matchUpIndex }?.status = entity.status
                                    tournament.updateRound(entity.roundGroupIndex, entity.roundIndex, entity.matchUpIndex)
                                }
                                tournaments.add(epoch to tournament)
                            }
                        }
                    }
                }
            }

            tournaments.forEach { pair ->

                val epoch = pair.first
                val tournament = pair.second

                database.update(
                    "tournamentTable",
                    SQLiteDatabase.CONFLICT_ABORT,
                    ContentValues().apply {
                        put("progress", ProgressConverter().fromProgress(tournament.getProgress()))
                    },
                    "epoch = ?",
                    arrayOf(epoch)
                )

                tournament.matchUps.forEach { matchUp ->
                    database.update(
                        "matchUpTable",
                        SQLiteDatabase.CONFLICT_ABORT,
                        ContentValues().apply {
                            put("containsBye", matchUp.containsBye(true))
                            put("isOpen", matchUp.isOpen())
                        },
                        "epoch = ? AND roundGroupIndex = ? AND roundIndex = ? AND matchUpIndex = ?",
                        arrayOf(epoch, matchUp.roundGroupIndex, matchUp.roundIndex, matchUp.matchUpIndex)
                    )
                }

            }

        }
    }

}