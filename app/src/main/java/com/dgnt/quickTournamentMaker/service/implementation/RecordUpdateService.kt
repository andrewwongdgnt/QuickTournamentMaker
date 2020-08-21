package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.MatchUp
import com.dgnt.quickTournamentMaker.data.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.service.interfaces.IRecordUpdateService


class RecordUpdateService : IRecordUpdateService {
    override fun update(matchUp: MatchUp, previousMatchUpStatus: MatchUpStatus) {

        val participant1Record = matchUp.participant1.record
        val participant2Record = matchUp.participant2.record

        when (previousMatchUpStatus) {
            MatchUpStatus.DEFAULT -> {
                when (matchUp.status) {
                    MatchUpStatus.P1_WINNER -> {
                        participant1Record.wins++
                        participant2Record.losses++
                    }
                    MatchUpStatus.P2_WINNER -> {
                        participant1Record.losses++
                        participant2Record.wins++
                    }
                    MatchUpStatus.TIE -> {
                        participant1Record.ties++
                        participant2Record.ties++
                    }
                }
            }
            MatchUpStatus.P1_WINNER -> {
                when (matchUp.status) {
                    MatchUpStatus.DEFAULT -> {
                        participant1Record.wins--
                        participant2Record.losses--
                    }
                    MatchUpStatus.P2_WINNER -> {
                        participant1Record.wins--
                        participant1Record.losses++
                        participant2Record.wins++
                        participant2Record.losses--
                    }
                    MatchUpStatus.TIE -> {
                        participant1Record.wins--
                        participant1Record.ties++
                        participant2Record.losses--
                        participant2Record.ties++
                    }
                }
            }
            MatchUpStatus.P2_WINNER -> {
                when (matchUp.status) {
                    MatchUpStatus.DEFAULT -> {
                        participant1Record.losses--
                        participant2Record.wins--
                    }
                    MatchUpStatus.P1_WINNER -> {
                        participant1Record.wins++
                        participant1Record.losses--
                        participant2Record.wins--
                        participant2Record.losses++
                    }
                    MatchUpStatus.TIE -> {
                        participant1Record.losses--
                        participant1Record.ties++
                        participant2Record.wins--
                        participant2Record.ties++
                    }
                }
            }
            MatchUpStatus.TIE -> {
                when (matchUp.status) {
                    MatchUpStatus.DEFAULT -> {
                        participant1Record.ties--
                        participant2Record.ties--
                    }
                    MatchUpStatus.P1_WINNER -> {
                        participant1Record.wins++
                        participant1Record.ties--
                        participant2Record.losses++
                        participant2Record.ties--
                    }
                    MatchUpStatus.P2_WINNER -> {
                        participant1Record.losses++
                        participant1Record.ties--
                        participant2Record.wins++
                        participant2Record.ties--
                    }
                }
            }
        }

    }
}