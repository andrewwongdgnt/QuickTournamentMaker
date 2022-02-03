package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.model.loadTournament.Sort
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService
import kotlinx.coroutines.launch

class LoadTournamentViewModel(
    private val tournamentRepository: ITournamentRepository,
    roundRepository: IRoundRepository,
    matchUpRepository: IMatchUpRepository,
    participantRepository: IParticipantRepository,
    rankingConfigService: IRankingConfigService,
    private val preferenceService: IPreferenceService
) : ViewModel(), Observable {

    private val tournaments = tournamentRepository.getAll()
    private val rounds = roundRepository.getAll()
    private val matchUps = matchUpRepository.getAll()
    private val participants = participantRepository.getAll()

    val tournamentLiveData: LiveData<List<RestoredTournamentInformation>> =
        object : MediatorLiveData<List<RestoredTournamentInformation>>() {
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

                    val filteredRoundEntities = roundEntities.filter { re -> re.epoch == it.epoch }
                    val filteredMatchUpEntities = matchUpEntities.filter { me -> me.epoch == it.epoch }
                    val filteredMatchUpEntitiesWithByes = filteredMatchUpEntities.filter { me -> me.containsBye }
                    val filteredParticipantEntities = participantEntities.filter { pe -> pe.epoch == it.epoch }

                    RestoredTournamentInformation(
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
                            filteredRoundEntities.size,
                            filteredMatchUpEntities.size,
                            filteredMatchUpEntitiesWithByes.size,
                            filteredParticipantEntities.count { pe -> pe.type == ParticipantType.NORMAL }
                        ),
                        FoundationalTournamentEntities(
                            filteredRoundEntities,
                            filteredMatchUpEntities,
                            filteredParticipantEntities
                        )
                    )
                }
        }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun updateTournament(tournamentInformation: TournamentInformation) = viewModelScope.launch {
        tournaments.value?.run {
            find { it.epoch.isEqual(tournamentInformation.creationDate) }?.let { tournamentEntity ->
                tournamentRepository.upsert(
                    tournamentEntity.clone(
                        name = tournamentInformation.title,
                        note = tournamentInformation.description,
                    )
                )
            }
        }
    }

    fun updateSortPreference(sort: Sort) {
        preferenceService.setSort(sort)
    }

    fun getSortPreference() =
        preferenceService.getSort()


}