package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.model.tournament.ExtendedTournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantType
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService

class LoadTournamentViewModel(
    tournamentRepository: ITournamentRepository,
    roundRepository: IRoundRepository,
    matchUpRepository: IMatchUpRepository,
    participantRepository: IParticipantRepository,
    private val rankingConfigService: IRankingConfigService,
) : ViewModel(), Observable {

    private val tournaments = tournamentRepository.getAll()
    private val rounds = roundRepository.getAll()
    private val matchUps = matchUpRepository.getAll()
    private val participants = participantRepository.getAll()

    val tournamentLiveData: LiveData<List<ExtendedTournamentInformation>> =
        object : MediatorLiveData<List<ExtendedTournamentInformation>>() {
            private var tournamentEntities: List<TournamentEntity>? = null
            private var roundEntities: List<RoundEntity>? = null
            private var matchUpEntities: List<MatchUpEntity>? = null
            private var participantEntities: List<ParticipantEntity>? = null

            init {
                addSource(tournaments) { tournaments ->
                    this.tournamentEntities = tournaments
                    roundEntities?.let { rounds ->
                        matchUpEntities?.let { matchUps ->
                            participantEntities?.let { participants ->
                                value = createTournament(tournaments, rounds, matchUps, participants)
                            }
                        }
                    }
                }
                addSource(rounds) { rounds ->
                    this.roundEntities = rounds
                    tournamentEntities?.let { tournaments ->
                        matchUpEntities?.let { matchUps ->
                            participantEntities?.let { participants ->
                                value = createTournament(tournaments, rounds, matchUps, participants)
                            }
                        }
                    }
                }
                addSource(matchUps) { matchUps ->
                    this.matchUpEntities = matchUps
                    tournamentEntities?.let { tournaments ->
                        roundEntities?.let { rounds ->
                            participantEntities?.let { participants ->
                                value = createTournament(tournaments, rounds, matchUps, participants)
                            }
                        }
                    }
                }
                addSource(participants) { participants ->
                    this.participantEntities = participants
                    tournamentEntities?.let { tournaments ->
                        roundEntities?.let { rounds ->
                            matchUpEntities?.let { matchUps ->
                                value = createTournament(tournaments, rounds, matchUps, participants)
                            }
                        }
                    }
                }
            }

            private fun createTournament(
                tournamentEntities: List<TournamentEntity>,
                roundEntities: List<RoundEntity>,
                matchUpEntities: List<MatchUpEntity>,
                participantEntities: List<ParticipantEntity>,
            ) =
                tournamentEntities.map {
                    ExtendedTournamentInformation(
                        TournamentInformation(
                            it.name,
                            it.note,
                            it.type,
                            it.seedType,
                            rankingConfigService.buildRankingFromString(it.rankingConfig),
                            it.epoch,
                            it.lastModifiedTime
                        ),
                        roundEntities.count { re -> re.epoch == it.epoch },
                        matchUpEntities.count { me -> me.epoch == it.epoch },
                        matchUpEntities.count { me -> me.epoch == it.epoch && me.containsBye },
                        participantEntities.filter { pe -> pe.type == ParticipantType.NORMAL }.count { pe -> pe.epoch == it.epoch }
                    )
                }
        }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


}