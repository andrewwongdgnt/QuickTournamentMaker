package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.model.tournament.Record
import com.dgnt.quickTournamentMaker.service.implementation.RecordUpdateService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RecordUpdateServiceTest {

    private val sut = RecordUpdateService()
    private lateinit var matchUp: MatchUp
    private lateinit var participant1Record: Record
    private lateinit var participant2Record: Record

    @Before
    fun setUp() {

        matchUp = MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, "").apply {
            participant1.record = Record(1, 1, 1)
            participant2.record = Record(2, 2, 2)
        }

        participant1Record = Data.ANDREW.record
        participant2Record = Data.KYRA.record

    }

    @Test
    fun testDefaultToP1Winner() {
        matchUp.status = MatchUpStatus.P1_WINNER
        sut.update(matchUp, MatchUpStatus.DEFAULT)
        Assert.assertEquals(Record(2, 1, 1), participant1Record)
        Assert.assertEquals(Record(2, 3, 2), participant2Record)
    }

    @Test
    fun testDefaultToP2Winner() {
        matchUp.status = MatchUpStatus.P2_WINNER
        sut.update(matchUp, MatchUpStatus.DEFAULT)
        Assert.assertEquals(Record(1, 2, 1), participant1Record)
        Assert.assertEquals(Record(3, 2, 2), participant2Record)
    }

    @Test
    fun testDefaultToTie() {
        matchUp.status = MatchUpStatus.TIE
        sut.update(matchUp, MatchUpStatus.DEFAULT)
        Assert.assertEquals(Record(1, 1, 2), participant1Record)
        Assert.assertEquals(Record(2, 2, 3), participant2Record)
    }

    @Test
    fun testP1WinnerToDefault() {
        matchUp.status = MatchUpStatus.DEFAULT
        sut.update(matchUp, MatchUpStatus.P1_WINNER)
        Assert.assertEquals(Record(0, 1, 1), participant1Record)
        Assert.assertEquals(Record(2, 1, 2), participant2Record)
    }

    @Test
    fun testP1WinnerToP2Winner() {
        matchUp.status = MatchUpStatus.P2_WINNER
        sut.update(matchUp, MatchUpStatus.P1_WINNER)
        Assert.assertEquals(Record(0, 2, 1), participant1Record)
        Assert.assertEquals(Record(3, 1, 2), participant2Record)
    }

    @Test
    fun testP1WinnerToTie() {
        matchUp.status = MatchUpStatus.TIE
        sut.update(matchUp, MatchUpStatus.P1_WINNER)
        Assert.assertEquals(Record(0, 1, 2), participant1Record)
        Assert.assertEquals(Record(2, 1, 3), participant2Record)
    }

    @Test
    fun testP2WinnerToDefault() {
        matchUp.status = MatchUpStatus.DEFAULT
        sut.update(matchUp, MatchUpStatus.P2_WINNER)
        Assert.assertEquals(Record(1, 0, 1), participant1Record)
        Assert.assertEquals(Record(1, 2, 2), participant2Record)
    }

    @Test
    fun testP2WinnerToP1Winner() {
        matchUp.status = MatchUpStatus.P1_WINNER
        sut.update(matchUp, MatchUpStatus.P2_WINNER)
        Assert.assertEquals(Record(2, 0, 1), participant1Record)
        Assert.assertEquals(Record(1, 3, 2), participant2Record)
    }

    @Test
    fun testP2WinnerToTie() {
        matchUp.status = MatchUpStatus.TIE
        sut.update(matchUp, MatchUpStatus.P2_WINNER)
        Assert.assertEquals(Record(1, 0, 2), participant1Record)
        Assert.assertEquals(Record(1, 2, 3), participant2Record)
    }

    @Test
    fun testTieToDefault() {
        matchUp.status = MatchUpStatus.DEFAULT
        sut.update(matchUp, MatchUpStatus.TIE)
        Assert.assertEquals(Record(1, 1, 0), participant1Record)
        Assert.assertEquals(Record(2, 2, 1), participant2Record)
    }

    @Test
    fun testTieToP1Winner() {
        matchUp.status = MatchUpStatus.P1_WINNER
        sut.update(matchUp, MatchUpStatus.TIE)
        Assert.assertEquals(Record(2, 1, 0), participant1Record)
        Assert.assertEquals(Record(2, 3, 1), participant2Record)
    }

    @Test
    fun testTieToP2Winner() {
        matchUp.status = MatchUpStatus.P2_WINNER
        sut.update(matchUp, MatchUpStatus.TIE)
        Assert.assertEquals(Record(1, 2, 0), participant1Record)
        Assert.assertEquals(Record(3, 2, 1), participant2Record)
    }

}