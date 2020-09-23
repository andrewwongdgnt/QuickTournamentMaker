package com.dgnt.quickTournamentMaker.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dgnt.quickTournamentMaker.model.history.HistoricalFilters;
import com.dgnt.quickTournamentMaker.model.history.HistoricalMatchUp;
import com.dgnt.quickTournamentMaker.model.history.HistoricalRound;
import com.dgnt.quickTournamentMaker.model.history.HistoricalTournament;
import com.dgnt.quickTournamentMaker.model.management.Group;
import com.dgnt.quickTournamentMaker.model.management.Person;
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.RecordKeepingTournament;
import com.dgnt.quickTournamentMaker.model.tournament.Tournament;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1002;
    public static final String DATABASE_NAME = "db";
    private static final String PERSON_TABLE = "personTable";
    private static final String GROUP_TABLE = "groupTable";
    private static final String TOURNAMENT_TABLE = "tournamentTable";
    private static final String ROUND_TABLE = "roundTable";
    private static final String MATCH_UP_TABLE = "matchUpTable";
    private static final String PARTICIPANT_TABLE = "participantTable";

    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_FAVOURITE = "favourite";
    private static final String COLUMN_GROUP_NAME = "groupName";
    private static final String COLUMN_EPOCH = "epoch";
    private static final String COLUMN_LAST_MODIFIED_TIME = "lastModifiedTime";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_SEED_INDEX = "seedIndex";
    private static final String COLUMN_ROUND_GROUP_INDEX = "roundGroupIndex";
    private static final String COLUMN_ROUND_INDEX = "roundIndex";
    private static final String COLUMN_MATCH_UP_INDEX = "matchUpIndex";
    private static final String COLUMN_MATCH_UP_STATUS = "matchUpStatus";
    private static final String COLUMN_DISPLAY_NAME = "displayName";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_RANKING_CONFIG = "rankingConfig";

    //For PERSON_TABLE
    //name, note, groupName

    //FOR GROUP_TABLE
    //name, note, favourite

    //FOR TOURNAMENT_TABLE
    //epoch, lastModifiedTime, name, note, type, rankingConfig

    //FOR ROUND_TABLE
    //epoch, roundGroupIndex, roundIndex, name, note, color

    //FOR MATCH_UP_TABLE
    //epoch, roundGroupIndex, roundIndex, matchUpIndex, note, status, color

    //FOR PARTICIPANT_TABLE
    //epoch, name, seedIndex, displayName, note, type, color, wins, losses, ties

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        //Create Person Table
        String createPersonTableQuery = "CREATE TABLE " + PERSON_TABLE + "(" +
                COLUMN_NAME + " TEXT PRIMARY KEY, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_GROUP_NAME + " TEXT " +
                ")";

        db.execSQL(createPersonTableQuery);

        //Create Group Table
        String createGroupTableQuery = "CREATE TABLE " + GROUP_TABLE + "(" +
                COLUMN_NAME + " TEXT PRIMARY KEY, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_FAVOURITE + " INTEGER " +
                ")";

        db.execSQL(createGroupTableQuery);

        //Create Tournament Table
        String createTournamentTableQuery = "CREATE TABLE " + TOURNAMENT_TABLE + "(" +
                COLUMN_EPOCH + " INTEGER PRIMARY KEY, " +
                COLUMN_LAST_MODIFIED_TIME + " INTEGER, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_RANKING_CONFIG + " TEXT " +
                ")";

        db.execSQL(createTournamentTableQuery);

        //Create Round Table
        String createRoundTableQuery = "CREATE TABLE " + ROUND_TABLE + "(" +
                COLUMN_EPOCH + " INTEGER, " +
                COLUMN_ROUND_GROUP_INDEX + " INTEGER, " +
                COLUMN_ROUND_INDEX + " INTEGER, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_COLOR + " INTEGER, " +
                "PRIMARY KEY (" + COLUMN_EPOCH + ", " + COLUMN_ROUND_GROUP_INDEX + ", " + COLUMN_ROUND_INDEX + ")" +
                ")";

        db.execSQL(createRoundTableQuery);

        //Create Match Up Table
        String createMatchUpTableQuery = "CREATE TABLE " + MATCH_UP_TABLE + "(" +
                COLUMN_EPOCH + " INTEGER, " +
                COLUMN_ROUND_GROUP_INDEX + " INTEGER, " +
                COLUMN_ROUND_INDEX + " INTEGER, " +
                COLUMN_MATCH_UP_INDEX + " INTEGER, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_COLOR + " INTEGER, " +
                COLUMN_MATCH_UP_STATUS + " TEXT, " +
                "PRIMARY KEY (" + COLUMN_EPOCH + ", " + COLUMN_ROUND_GROUP_INDEX + ", " + COLUMN_ROUND_INDEX + ", " + COLUMN_MATCH_UP_INDEX + ")" +
                ")";

        db.execSQL(createMatchUpTableQuery);

        //Create Participant Table
        String createParticipantTableQuery = "CREATE TABLE " + PARTICIPANT_TABLE + "(" +
                COLUMN_EPOCH + " INTEGER, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SEED_INDEX + " INTEGER, " +
                COLUMN_DISPLAY_NAME + " TEXT, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_COLOR + " INTEGER, " +
                "PRIMARY KEY (" + COLUMN_EPOCH + ", " + COLUMN_NAME + ", " + COLUMN_SEED_INDEX + ")" +
                ")";

        db.execSQL(createParticipantTableQuery);

    }


    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        if (oldversion < 1001) {
            //This is the really really old stuff
            db.execSQL("DROP TABLE IF EXISTS pairs");
            db.execSQL("DROP TABLE IF EXISTS person");
            db.execSQL("DROP TABLE IF EXISTS players");
            db.execSQL("DROP TABLE IF EXISTS Tournaments");
            onCreate(db);
        } else {
            if (oldversion < 1002) {
                db.execSQL("ALTER TABLE " + TOURNAMENT_TABLE + " ADD COLUMN " + COLUMN_RANKING_CONFIG + " TEXT");
            }
        }
    }

    //------------------------------
    // Person Table
    //------------------------------

    public void addPersonAndGroup(final String name, final String note, final String groupKey) throws SQLException {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_GROUP_NAME, groupKey);

        db.insertOrThrow(PERSON_TABLE, null, values);

        values = new ContentValues();
        values.put(COLUMN_NAME, groupKey);
        values.put(COLUMN_NOTE, "");
        values.put(COLUMN_FAVOURITE, 0);

        try {
            db.insertOrThrow(GROUP_TABLE, null, values);
        } catch (SQLiteConstraintException e) {
            //do nothing
        }
        db.close();

    }

    public void updatePersonAndAddGroup(String oldName, String name, String note, String oldGroupKey, String groupKey) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_GROUP_NAME, groupKey);


        db.update(PERSON_TABLE, values, COLUMN_NAME + " = ?",
                new String[]{oldName});

        final long count = DatabaseUtils.queryNumEntries(db, PERSON_TABLE, COLUMN_GROUP_NAME + " = ?", new String[]{oldGroupKey});
        if (count == 0)
            db.delete(GROUP_TABLE, COLUMN_NAME + " = ?", new String[]{oldGroupKey});


        values = new ContentValues();
        values.put(COLUMN_NAME, groupKey);
        values.put(COLUMN_NOTE, "");
        values.put(COLUMN_FAVOURITE, 0);

        try {
            db.insertOrThrow(GROUP_TABLE, null, values);
        } catch (SQLiteConstraintException e) {
            //do nothing
        }
        db.close();
    }


    public void deletePersonsAndGroups(final Set<Person> personList, final Set<Group> groupList) {

        final Set<String> personNames = new HashSet<>();
        for (final Person person : personList) {
            personNames.add(person.getKey());
        }
        final Set<String> groupNames = new HashSet<>();
        for (final Group group : groupList) {
            int totalPersonsInSet = 0;
            for (int p = 0; p < group.getTotalPersons(); p++) {
                if (personNames.contains(group.getPersonAt(p).getKey())) {
                    totalPersonsInSet++;
                }
            }
            if (totalPersonsInSet == group.getTotalPersons())
                groupNames.add(group.getName());
        }


        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PERSON_TABLE, COLUMN_NAME + " IN (" + new String(new char[personNames.size() - 1]).replace("\0", "?,") + "?)", personNames.toArray(new String[personNames.size()]));
        if (groupNames.size() > 0)
            db.delete(GROUP_TABLE, COLUMN_NAME + " IN (" + new String(new char[groupNames.size() - 1]).replace("\0", "?,") + "?)", groupNames.toArray(new String[groupNames.size()]));

        db.close();
    }

    //------------------------------
    // Group Table
    //------------------------------


    public void updateGroup(String oldName, String name, String note, final boolean favourite) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues groupValues = new ContentValues();
        groupValues.put(COLUMN_NAME, name);
        groupValues.put(COLUMN_NOTE, note);
        groupValues.put(COLUMN_FAVOURITE, favourite ? 1 : 0);

        db.update(GROUP_TABLE, groupValues, COLUMN_NAME + " = ?",
                new String[]{oldName});

        ContentValues personValues = new ContentValues();
        personValues.put(COLUMN_GROUP_NAME, name);


        db.update(PERSON_TABLE, personValues, COLUMN_GROUP_NAME + " = ?",
                new String[]{oldName});

        db.close();
    }


    public void deleteGroups(final Set<Group> groupList) {

        final Set<String> personNames = new HashSet<>();
        final Set<String> groupNames = new HashSet<>();
        for (final Group group : groupList) {
            groupNames.add(group.getKey());
            for (int p = 0; p < group.getTotalPersons(); p++) {
                personNames.add(group.getPersonAt(p).getKey());
            }
        }

        SQLiteDatabase db = this.getWritableDatabase();
        if (personNames.size() > 0)
            db.delete(PERSON_TABLE, COLUMN_NAME + " IN (" + new String(new char[personNames.size() - 1]).replace("\0", "?,") + "?)", personNames.toArray(new String[personNames.size()]));
        db.delete(GROUP_TABLE, COLUMN_NAME + " IN (" + new String(new char[groupNames.size() - 1]).replace("\0", "?,") + "?)", groupNames.toArray(new String[groupNames.size()]));
        db.close();
    }

    public List<Group> getAllGroupsWithPersons() {
        final Map<String, List<Person>> groupToPersonListMap = new HashMap<>();

        final SQLiteDatabase db = this.getReadableDatabase();

        final Cursor personCursor = db.query(PERSON_TABLE, new String[]{
                        COLUMN_NAME,
                        COLUMN_NOTE,
                        COLUMN_GROUP_NAME,
                }, null,
                null, null, null, COLUMN_NAME);

        if (personCursor.moveToFirst()) {
            do {

                final String name = personCursor.getString(personCursor.getColumnIndex(COLUMN_NAME));
                final String note = personCursor.getString(personCursor.getColumnIndex(COLUMN_NOTE));
                final String groupKey = personCursor.getString(personCursor.getColumnIndex(COLUMN_GROUP_NAME));


                final Person person = new Person(name, note);
                if (groupToPersonListMap.get(groupKey) == null) {
                    groupToPersonListMap.put(groupKey, new ArrayList<Person>());
                }
                final List<Person> personList = groupToPersonListMap.get(groupKey);
                personList.add(person);

            } while (personCursor.moveToNext());
        }

        personCursor.close();

        final List<Group> groupList = new ArrayList<>();
        final Cursor groupCursor = db.query(GROUP_TABLE, new String[]{
                        COLUMN_NAME,
                        COLUMN_NOTE,
                        COLUMN_FAVOURITE,
                }, null,
                null, null, null, COLUMN_NAME);

        if (groupCursor.moveToFirst()) {
            do {

                final String name = groupCursor.getString(groupCursor.getColumnIndex(COLUMN_NAME));
                final String note = groupCursor.getString(groupCursor.getColumnIndex(COLUMN_NOTE));
                final boolean favourite = groupCursor.getInt(groupCursor.getColumnIndex(COLUMN_FAVOURITE)) == 1;


                final Group group = new Group(name, note, favourite, groupToPersonListMap.get(name));
                groupList.add(group);

            } while (groupCursor.moveToNext());
        }

        groupCursor.close();

        db.close();

        Collections.sort(groupList);
        return groupList;

    }

    //------------------------------
    // Tournament and Participant Table
    //------------------------------

    public void addTournament(final Tournament tournament) throws SQLException {
        final String rankingConfig = tournament instanceof RecordKeepingTournament ? ((RecordKeepingTournament) tournament).getRankingConfig() : "";
        addTournament(tournament.getCreationTimeInEpoch(), tournament.getLastModifiedTimeInEpoch(), tournament.getType(), tournament.getTitle(), tournament.getDescription(), TournamentUtil.buildRoundList(tournament), TournamentUtil.buildMatchUpList(tournament), tournament.getSeededParticipants(), rankingConfig);
    }

    public void addTournament(final long creationTimeInEpoch, final long lastModifiedTimeInEpoch, final Tournament.TournamentType tournamentType, final String name, final String description, final List<HistoricalRound> roundList, final List<HistoricalMatchUp> matchUpList, final List<Participant> participants, final String rankingConfig) throws SQLException {

        //delete a tournament with this creation time just in case it exists
        deleteTournament(creationTimeInEpoch);

        SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(COLUMN_EPOCH, creationTimeInEpoch);
        values.put(COLUMN_LAST_MODIFIED_TIME, lastModifiedTimeInEpoch);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_NOTE, description);
        values.put(COLUMN_TYPE, tournamentType.name());
        values.put(COLUMN_RANKING_CONFIG, rankingConfig == null ? "" : rankingConfig);

        db.insertOrThrow(TOURNAMENT_TABLE, null, values);

        for (final HistoricalRound historicalRound : roundList) {

            final ContentValues values_round = new ContentValues();
            values_round.put(COLUMN_EPOCH, creationTimeInEpoch);
            values_round.put(COLUMN_ROUND_GROUP_INDEX, historicalRound.getRoundGroupIndex());
            values_round.put(COLUMN_ROUND_INDEX, historicalRound.getRoundIndex());
            values_round.put(COLUMN_NAME, historicalRound.getTitle());
            values_round.put(COLUMN_NOTE, historicalRound.getNote());
            values_round.put(COLUMN_COLOR, historicalRound.getColor());

            db.insertOrThrow(ROUND_TABLE, null, values_round);

        }

        for (final HistoricalMatchUp historicalMatchUp : matchUpList) {

            final ContentValues values_matchUp = new ContentValues();
            values_matchUp.put(COLUMN_EPOCH, creationTimeInEpoch);
            values_matchUp.put(COLUMN_ROUND_GROUP_INDEX, historicalMatchUp.getRoundGroupIndex());
            values_matchUp.put(COLUMN_ROUND_INDEX, historicalMatchUp.getRoundIndex());
            values_matchUp.put(COLUMN_MATCH_UP_INDEX, historicalMatchUp.getMatchUpIndex());
            values_matchUp.put(COLUMN_NOTE, historicalMatchUp.getNote());
            values_matchUp.put(COLUMN_COLOR, historicalMatchUp.getColor());
            values_matchUp.put(COLUMN_MATCH_UP_STATUS, historicalMatchUp.getMatchUpStatus().name());

            db.insertOrThrow(MATCH_UP_TABLE, null, values_matchUp);

        }

        for (int seedIndex = 0; seedIndex < participants.size(); seedIndex++) {
            final Participant participant = participants.get(seedIndex);
            final ContentValues values_participant = new ContentValues();
            values_participant.put(COLUMN_EPOCH, creationTimeInEpoch);
            values_participant.put(COLUMN_NAME, participant.getName());
            values_participant.put(COLUMN_SEED_INDEX, seedIndex);
            values_participant.put(COLUMN_DISPLAY_NAME, participant.getDisplayName());
            values_participant.put(COLUMN_NOTE, participant.getNote());
            values_participant.put(COLUMN_TYPE, participant.isNull() ? Participant.ParticipantType.NULL.name() :
                    participant.isBye() ? Participant.ParticipantType.BYE.name() :
                            Participant.ParticipantType.NORMAL.name());
            values_participant.put(COLUMN_COLOR, participant.getColor());

            db.insertOrThrow(PARTICIPANT_TABLE, null, values_participant);
        }
        db.close();

    }

    public void updateTournament(final Tournament tournament) throws SQLException {
        final String rankingConfig = tournament instanceof RecordKeepingTournament ? ((RecordKeepingTournament) tournament).getRankingConfig() : "";
        updateTournament(tournament.getCreationTimeInEpoch(), tournament.getLastModifiedTimeInEpoch(), tournament.getType(), tournament.getTitle(), tournament.getDescription(), TournamentUtil.buildRoundList(tournament), TournamentUtil.buildMatchUpList(tournament), tournament.getSeededParticipants(), rankingConfig);
    }

    public void updateTournament(final long creationTimeInEpoch, final long lastModifiedTimeInEpoch, final Tournament.TournamentType tournamentType, final String name, final String description, final List<HistoricalRound> roundList, final List<HistoricalMatchUp> matchUpList, final List<Participant> participants, final String rankingConfig) throws SQLException {

        SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(COLUMN_LAST_MODIFIED_TIME, lastModifiedTimeInEpoch);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_NOTE, description);
        values.put(COLUMN_TYPE, tournamentType.name());
        values.put(COLUMN_RANKING_CONFIG, rankingConfig == null ? "" : rankingConfig);

        db.update(TOURNAMENT_TABLE, values, COLUMN_EPOCH + " = ?",
                new String[]{String.valueOf(creationTimeInEpoch)});

        for (final HistoricalRound historicalRound : roundList) {

            final ContentValues values_round = new ContentValues();
            values_round.put(COLUMN_NAME, historicalRound.getTitle());
            values_round.put(COLUMN_NOTE, historicalRound.getNote());
            values_round.put(COLUMN_COLOR, historicalRound.getColor());

            db.update(ROUND_TABLE, values_round, COLUMN_EPOCH + " = ? AND " + COLUMN_ROUND_GROUP_INDEX + " = ? AND " + COLUMN_ROUND_INDEX + " = ?",
                    new String[]{String.valueOf(creationTimeInEpoch), String.valueOf(historicalRound.getRoundGroupIndex()), String.valueOf(historicalRound.getRoundIndex())});

        }

        for (final HistoricalMatchUp historicalMatchUp : matchUpList) {

            final ContentValues values_matchUp = new ContentValues();
            values_matchUp.put(COLUMN_NOTE, historicalMatchUp.getNote());
            values_matchUp.put(COLUMN_COLOR, historicalMatchUp.getColor());
            values_matchUp.put(COLUMN_MATCH_UP_STATUS, historicalMatchUp.getMatchUpStatus().name());

            db.update(MATCH_UP_TABLE, values_matchUp, COLUMN_EPOCH + " = ? AND " + COLUMN_ROUND_GROUP_INDEX + " = ? AND " + COLUMN_ROUND_INDEX + " = ? AND " + COLUMN_MATCH_UP_INDEX + " = ?",
                    new String[]{String.valueOf(creationTimeInEpoch), String.valueOf(historicalMatchUp.getRoundGroupIndex()), String.valueOf(historicalMatchUp.getRoundIndex()), String.valueOf(historicalMatchUp.getMatchUpIndex())});

        }

        for (int seedIndex = 0; seedIndex < participants.size(); seedIndex++) {
            final Participant participant = participants.get(seedIndex);
            final ContentValues values_participant = new ContentValues();
            values_participant.put(COLUMN_SEED_INDEX, seedIndex);
            values_participant.put(COLUMN_DISPLAY_NAME, participant.getDisplayName());
            values_participant.put(COLUMN_NOTE, participant.getNote());
            values_participant.put(COLUMN_COLOR, participant.getColor());

            db.update(PARTICIPANT_TABLE, values_participant, COLUMN_EPOCH + " = ? AND " + COLUMN_NAME + " = ? AND " + COLUMN_SEED_INDEX + " = ?",
                    new String[]{String.valueOf(creationTimeInEpoch), participant.getName(), String.valueOf(seedIndex)});
        }
        db.close();

    }

    public void deleteTournament(final long creationTimeInEpoch) {

        SQLiteDatabase db = this.getWritableDatabase();
        final String[] whereArgs = new String[]{String.valueOf(creationTimeInEpoch)};
        db.delete(TOURNAMENT_TABLE, COLUMN_EPOCH + " = ?", whereArgs);
        db.delete(ROUND_TABLE, COLUMN_EPOCH + " = ?", whereArgs);
        db.delete(MATCH_UP_TABLE, COLUMN_EPOCH + " = ?", whereArgs);
        db.delete(PARTICIPANT_TABLE, COLUMN_EPOCH + " = ?", whereArgs);

        db.close();
    }

    public List<HistoricalTournament> getAllTournaments(final HistoricalTournament.Sort sort, final String searchTerm, final HistoricalFilters historicalFilters) {


        final SQLiteDatabase db = this.getReadableDatabase();

        final Map<Long, List<Participant>> epochToParticipantList = new HashMap<>();

        final Cursor participantCursor = db.query(PARTICIPANT_TABLE, new String[]{
                        COLUMN_EPOCH,
                        COLUMN_NAME,
                        COLUMN_SEED_INDEX,
                        COLUMN_DISPLAY_NAME,
                        COLUMN_NOTE,
                        COLUMN_TYPE,
                        COLUMN_COLOR,
                }, null,
                null, null, null, COLUMN_SEED_INDEX + " ASC");

        if (participantCursor.moveToFirst()) {
            do {
                try {
                    final long epoch = participantCursor.getLong(participantCursor.getColumnIndex(COLUMN_EPOCH));
                    final String name = participantCursor.getString(participantCursor.getColumnIndex(COLUMN_NAME));
                    final String displayName = participantCursor.getString(participantCursor.getColumnIndex(COLUMN_DISPLAY_NAME));
                    final String note = participantCursor.getString(participantCursor.getColumnIndex(COLUMN_NOTE));
                    final Participant.ParticipantType type = Participant.ParticipantType.valueOf(participantCursor.getString(participantCursor.getColumnIndex(COLUMN_TYPE)));
                    final int color = participantCursor.getInt(participantCursor.getColumnIndex(COLUMN_COLOR));

                    final Participant participant = new Participant(new Person(name, note), type);
                    participant.setDisplayName(displayName);
                    participant.setColor(color);

                    if (epochToParticipantList.get(epoch) == null) {
                        epochToParticipantList.put(epoch, new ArrayList<Participant>());
                    }
                    final List<Participant> participants = epochToParticipantList.get(epoch);
                    participants.add(participant);
                } catch (Exception e) {

                }
            } while (participantCursor.moveToNext());
        }

        participantCursor.close();

        final Map<Long, List<HistoricalRound>> epochToRoundList = new HashMap<>();

        final Cursor roundCursor = db.query(ROUND_TABLE, new String[]{
                        COLUMN_EPOCH,
                        COLUMN_ROUND_GROUP_INDEX,
                        COLUMN_ROUND_INDEX,
                        COLUMN_NAME,
                        COLUMN_COLOR,
                        COLUMN_NOTE,
                }, null,
                null, null, null, null);

        if (roundCursor.moveToFirst()) {
            do {
                try {
                    final long epoch = roundCursor.getLong(roundCursor.getColumnIndex(COLUMN_EPOCH));
                    final int roundGroupIndex = roundCursor.getInt(roundCursor.getColumnIndex(COLUMN_ROUND_GROUP_INDEX));
                    final int roundIndex = roundCursor.getInt(roundCursor.getColumnIndex(COLUMN_ROUND_INDEX));
                    final String title = roundCursor.getString(roundCursor.getColumnIndex(COLUMN_NAME));
                    final String note = roundCursor.getString(roundCursor.getColumnIndex(COLUMN_NOTE));
                    final int color = roundCursor.getInt(roundCursor.getColumnIndex(COLUMN_COLOR));


                    final HistoricalRound historicalRound = new HistoricalRound(roundGroupIndex, roundIndex, title, note, color);


                    if (epochToRoundList.get(epoch) == null) {
                        epochToRoundList.put(epoch, new ArrayList<HistoricalRound>());
                    }
                    final List<HistoricalRound> historicalRoundList = epochToRoundList.get(epoch);
                    historicalRoundList.add(historicalRound);
                } catch (Exception e) {

                }
            } while (roundCursor.moveToNext());
        }

        roundCursor.close();

        final Map<Long, List<HistoricalMatchUp>> epochToMatchUpList = new HashMap<>();

        final Cursor matchUpCursor = db.query(MATCH_UP_TABLE, new String[]{
                        COLUMN_EPOCH,
                        COLUMN_ROUND_GROUP_INDEX,
                        COLUMN_ROUND_INDEX,
                        COLUMN_MATCH_UP_INDEX,
                        COLUMN_COLOR,
                        COLUMN_NOTE,
                        COLUMN_MATCH_UP_STATUS
                }, null,
                null, null, null, null);

        if (matchUpCursor.moveToFirst()) {
            do {
                try {
                    final long epoch = matchUpCursor.getLong(matchUpCursor.getColumnIndex(COLUMN_EPOCH));
                    final int roundGroupIndex = matchUpCursor.getInt(matchUpCursor.getColumnIndex(COLUMN_ROUND_GROUP_INDEX));
                    final int roundIndex = matchUpCursor.getInt(matchUpCursor.getColumnIndex(COLUMN_ROUND_INDEX));
                    final int matchUpIndex = matchUpCursor.getInt(matchUpCursor.getColumnIndex(COLUMN_MATCH_UP_INDEX));
                    final String note = matchUpCursor.getString(matchUpCursor.getColumnIndex(COLUMN_NOTE));
                    final int color = matchUpCursor.getInt(matchUpCursor.getColumnIndex(COLUMN_COLOR));
                    final MatchUp.MatchUpStatus status = MatchUp.MatchUpStatus.valueOf(matchUpCursor.getString(matchUpCursor.getColumnIndex(COLUMN_MATCH_UP_STATUS)));


                    final HistoricalMatchUp historicalMatchUp = new HistoricalMatchUp(roundGroupIndex, roundIndex, matchUpIndex, note, color, status);


                    if (epochToMatchUpList.get(epoch) == null) {
                        epochToMatchUpList.put(epoch, new ArrayList<HistoricalMatchUp>());
                    }
                    final List<HistoricalMatchUp> historicalMatchUpList = epochToMatchUpList.get(epoch);
                    historicalMatchUpList.add(historicalMatchUp);
                } catch (Exception e) {

                }
            } while (matchUpCursor.moveToNext());
        }

        matchUpCursor.close();

        final List<HistoricalTournament> historicalTournamentList = new ArrayList<>();

        final String orderBy;
        switch (sort) {
            case CREATION_DATE_NEWEST:
                orderBy = COLUMN_EPOCH + " DESC";
                break;
            case CREATION_DATE_OLDEST:
                orderBy = COLUMN_EPOCH + " ASC";
                break;
            case LAST_MODIFIED_DATE_NEWEST:
                orderBy = COLUMN_LAST_MODIFIED_TIME + " DESC";
                break;
            case LAST_MODIFIED_DATE_OLDEST:
                orderBy = COLUMN_LAST_MODIFIED_TIME + " ASC";
                break;
            case NAME:
                orderBy = COLUMN_NAME + " ASC";
                break;
            case NAME_REVERSED:
                orderBy = COLUMN_NAME + " DESC";
                break;
            case TOURNAMENT_TYPE:
                orderBy = "CASE"
                        + " WHEN " + COLUMN_TYPE + " = '" + Tournament.TournamentType.ELIMINATION.name() + "' THEN 0"
                        + " WHEN " + COLUMN_TYPE + " = '" + Tournament.TournamentType.DOUBLE_ELIMINATION.name() + "' THEN 1"
                        + " WHEN " + COLUMN_TYPE + " = '" + Tournament.TournamentType.ROUND_ROBIN.name() + "' THEN 2"
                        + " WHEN " + COLUMN_TYPE + " = '" + Tournament.TournamentType.SURVIVAL.name() + "' THEN 3"
                        + " WHEN " + COLUMN_TYPE + " = '" + Tournament.TournamentType.SWISS.name() + "' THEN 4"
                        + " END";
                break;
            case TOURNAMENT_TYPE_REVERSED:
            default:
                orderBy = "CASE"
                        + " WHEN " + COLUMN_TYPE + " = '" + Tournament.TournamentType.SWISS.name() + "' THEN 0"
                        + " WHEN " + COLUMN_TYPE + " = '" + Tournament.TournamentType.SURVIVAL.name() + "' THEN 1"
                        + " WHEN " + COLUMN_TYPE + " = '" + Tournament.TournamentType.ROUND_ROBIN.name() + "' THEN 2"
                        + " WHEN " + COLUMN_TYPE + " = '" + Tournament.TournamentType.DOUBLE_ELIMINATION.name() + "' THEN 3"
                        + " WHEN " + COLUMN_TYPE + " = '" + Tournament.TournamentType.ELIMINATION.name() + "' THEN 4"
                        + " END";
                break;

        }

        final String whereClauseForSearchTerm = searchTerm == null || searchTerm.trim().length() == 0 ? "" : " AND (" + COLUMN_NAME + " LIKE ? OR " + COLUMN_NOTE + " LIKE ? COLLATE NOCASE)";
        final List<String> whereClauseForTypeFilters_arr = new ArrayList<>();
        if (historicalFilters.isFilterOnElimination())
            whereClauseForTypeFilters_arr.add(COLUMN_TYPE + " = ?");
        if (historicalFilters.isFilterOnDoubleElimination())
            whereClauseForTypeFilters_arr.add(COLUMN_TYPE + " = ?");
        if (historicalFilters.isFilterOnRoundRobin())
            whereClauseForTypeFilters_arr.add(COLUMN_TYPE + " = ?");
        if (historicalFilters.isFilterOnSwiss())
            whereClauseForTypeFilters_arr.add(COLUMN_TYPE + " = ?");
        if (historicalFilters.isFilterOnSurvival())
            whereClauseForTypeFilters_arr.add(COLUMN_TYPE + " = ?");

        final String whereClauseForFilters = (whereClauseForTypeFilters_arr.size() > 0 ? " AND (" + StringUtils.join(whereClauseForTypeFilters_arr, " OR ") + ")" : "")
                + (historicalFilters.getEarliestCreatedEpoch() >= 0 ? " AND " + COLUMN_EPOCH + " >= ?" : "")
                + (historicalFilters.getLatestCreatedEpoch() >= 0 ? " AND " + COLUMN_EPOCH + " <= ?" : "")
                + (historicalFilters.getEarliestModifiedEpoch() >= 0 ? " AND " + COLUMN_LAST_MODIFIED_TIME + " >= ?" : "")
                + (historicalFilters.getLatestModifiedEpoch() >= 0 ? " AND " + COLUMN_LAST_MODIFIED_TIME + " <= ?" : "");

        final String whereClause = "1 = 1" + whereClauseForSearchTerm + whereClauseForFilters;

        final List<String> whereArgs = new ArrayList<>();
        if (searchTerm != null && searchTerm.trim().length() > 0) {
            whereArgs.add("%" + searchTerm + "%");
            whereArgs.add("%" + searchTerm + "%");
        }
        if (historicalFilters.isFilterOnElimination())
            whereArgs.add("" + Tournament.TournamentType.ELIMINATION.name() + "");
        if (historicalFilters.isFilterOnDoubleElimination())
            whereArgs.add("" + Tournament.TournamentType.DOUBLE_ELIMINATION.name() + "");
        if (historicalFilters.isFilterOnRoundRobin())
            whereArgs.add("" + Tournament.TournamentType.ROUND_ROBIN.name() + "");
        if (historicalFilters.isFilterOnSwiss())
            whereArgs.add("" + Tournament.TournamentType.SWISS.name() + "");
        if (historicalFilters.isFilterOnSurvival())
            whereArgs.add("" + Tournament.TournamentType.SURVIVAL.name() + "");

        if (historicalFilters.getEarliestCreatedEpoch() >= 0)
            whereArgs.add(String.valueOf(historicalFilters.getEarliestCreatedEpoch()));
        if (historicalFilters.getLatestCreatedEpoch() >= 0)
            whereArgs.add(String.valueOf(historicalFilters.getLatestCreatedEpoch()));

        if (historicalFilters.getEarliestModifiedEpoch() >= 0)
            whereArgs.add(String.valueOf(historicalFilters.getEarliestModifiedEpoch()));
        if (historicalFilters.getLatestModifiedEpoch() >= 0)
            whereArgs.add(String.valueOf(historicalFilters.getLatestModifiedEpoch()));

        final String[] whereArgs_final = whereArgs.size() > 0 ? whereArgs.toArray(new String[whereArgs.size()]) : null;

        final Cursor tournamentCursor = db.query(TOURNAMENT_TABLE, new String[]{
                        COLUMN_EPOCH,
                        COLUMN_LAST_MODIFIED_TIME,
                        COLUMN_NAME,
                        COLUMN_NOTE,
                        COLUMN_TYPE,
                        COLUMN_RANKING_CONFIG
                }, whereClause,
                whereArgs_final, null, null, orderBy);

        if (tournamentCursor.moveToFirst()) {
            do {
                try {

                    final long creationTimeInEpoch = tournamentCursor.getLong(tournamentCursor.getColumnIndex(COLUMN_EPOCH));
                    final long lastModifiedTimeInEpoch = tournamentCursor.getLong(tournamentCursor.getColumnIndex(COLUMN_LAST_MODIFIED_TIME));
                    final String name = tournamentCursor.getString(tournamentCursor.getColumnIndex(COLUMN_NAME));
                    final String note = tournamentCursor.getString(tournamentCursor.getColumnIndex(COLUMN_NOTE));
                    final Tournament.TournamentType type = Tournament.TournamentType.valueOf(tournamentCursor.getString(tournamentCursor.getColumnIndex(COLUMN_TYPE)));
                    final String rankingConfig = tournamentCursor.getString(tournamentCursor.getColumnIndex(COLUMN_RANKING_CONFIG));

                    final List<Participant> participants = epochToParticipantList.get(creationTimeInEpoch);
                    final List<HistoricalRound> roundList = epochToRoundList.get(creationTimeInEpoch);
                    final List<HistoricalMatchUp> matchUpList = epochToMatchUpList.get(creationTimeInEpoch);

                    final HistoricalTournament historicalTournament = new HistoricalTournament(creationTimeInEpoch, lastModifiedTimeInEpoch, name, note, type, rankingConfig, participants, roundList, matchUpList);

                    final int normalParticipantCount = TournamentUtil.getNormalParticipantCount(participants);

                    if ((historicalFilters.getMinParticipants() < 0 || normalParticipantCount >= historicalFilters.getMinParticipants())
                            &&
                            (historicalFilters.getMaxParticipants() < 0 || normalParticipantCount <= historicalFilters.getMaxParticipants()))
                        historicalTournamentList.add(historicalTournament);
                } catch (Exception e) {

                }
            } while (tournamentCursor.moveToNext());
        }

        tournamentCursor.close();

        if (searchTerm != null && searchTerm.trim().length() > 0) {
            Collections.sort(historicalTournamentList, new Comparator<HistoricalTournament>() {
                @Override
                public int compare(final HistoricalTournament lhs, final HistoricalTournament rhs) {

                    final int leftIndexOfSearchTerm_name = lhs.getName().indexOf(searchTerm);
                    final int rightIndexOfSearchTerm_name = rhs.getName().indexOf(searchTerm);

                    // if the search term appears in the title, then give a higher relevance rank the closer the search term is to the beginning (IE smaller index)
                    if (leftIndexOfSearchTerm_name >= 0 && rightIndexOfSearchTerm_name >= 0 && rightIndexOfSearchTerm_name - leftIndexOfSearchTerm_name != 0)
                        return leftIndexOfSearchTerm_name - rightIndexOfSearchTerm_name;

                    // otherwise, assign weights
                    final int leftWeight_distinctWords = getWeight(lhs, true);
                    final int leftWeight_sequence = getWeight(lhs, false);
                    final int rightWeight_distinctWords = getWeight(rhs, true);
                    final int rightWeight_sequence = getWeight(rhs, false);

                    // if the search term appears as a distinct word, then its relevance ranking is always higher than those that are not
                    if (leftWeight_distinctWords > 0 && rightWeight_distinctWords == 0)
                        return -1;
                    else if (leftWeight_distinctWords == 0 && rightWeight_distinctWords > 0)
                        return 1;
                    else if (leftWeight_distinctWords > 0 && rightWeight_distinctWords > 0 && rightWeight_distinctWords - leftWeight_distinctWords != 0)
                        return rightWeight_distinctWords - leftWeight_distinctWords;
                    else if (rightWeight_sequence - leftWeight_sequence != 0)
                        return rightWeight_sequence - leftWeight_sequence;

                        // if above results in a draw, then resort to sorting by alphabetical order, starting with title, then description
                    else if (lhs.getName().compareTo(rhs.getName()) != 0)
                        return lhs.getName().compareTo(rhs.getName());
                    else
                        return lhs.getNote().compareTo(rhs.getNote());
                }

                private int getWeight(final HistoricalTournament historicalTournament, final boolean isDistinctWord) {

                    return 3 * getCountOfSearchTerm(historicalTournament.getName(), isDistinctWord) + getCountOfSearchTerm(historicalTournament.getNote(), isDistinctWord);
                }

                private int getCountOfSearchTerm(final String str, final boolean isDistinctWord) {

                    if (isDistinctWord) {
                        final String[] splitStr = str.split("\\s+");

                        int count = 0;
                        for (final String word : splitStr) {
                            if (word.equals(searchTerm)) {
                                count++;
                            }
                        }
                        return count;
                    } else {

                        return StringUtils.countMatches(str, searchTerm);
                    }
                }
            });
        }

        return historicalTournamentList;
    }


}