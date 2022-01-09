package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.data.tournament.*
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

    val tournamentLiveData: LiveData<List<Pair<TournamentInformation, Triple<Int, Int, Int>>>> =
        object : MediatorLiveData<List<Pair<TournamentInformation, Triple<Int, Int, Int>>>>() {
            private var tournamentEntities: List<TournamentEntity>? = null
            private var roundEntities: List<RoundEntity>? = null
            private var matchUpEntities: List<MatchUpEntity>? = null
            private var participantEntities: List<ParticipantEntity>? = null

            init {
                addSource(tournaments) { tournaments ->
                    this.tournamentEntities = tournaments
                    roundEntities?.let { roundEntities ->
                        matchUpEntities?.let { matchUpEntities ->
                            participantEntities?.let { participantEntities ->
                                value = tournaments.map {
                                    TournamentInformation(
                                        it.name,
                                        it.note,
                                        it.type,
                                        it.seedType,
                                        rankingConfigService.buildRankingFromString(it.rankingConfig),
                                        it.epoch,
                                        it.lastModifiedTime
                                    ) to
                                            Triple(
                                                roundEntities.count { re -> re.epoch == it.epoch },
                                                matchUpEntities.count { me -> me.epoch == it.epoch },
                                                participantEntities.count { pe -> pe.epoch == it.epoch }
                                            )
                                }

                            }

                        }

                    }
                }

            }
        }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


}