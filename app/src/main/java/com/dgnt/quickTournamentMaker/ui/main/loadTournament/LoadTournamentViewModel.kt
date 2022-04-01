package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import androidx.databinding.Observable
import androidx.lifecycle.*
import com.dgnt.quickTournamentMaker.data.search.ISearchTermRepository
import com.dgnt.quickTournamentMaker.data.search.SearchTermEntity
import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentFilterService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentSortService
import com.dgnt.quickTournamentMaker.util.Event
import kotlinx.coroutines.launch

class LoadTournamentViewModel(
    private val tournamentRepository: ITournamentRepository,
    private val roundRepository: IRoundRepository,
    private val matchUpRepository: IMatchUpRepository,
    private val participantRepository: IParticipantRepository,
    private val searchTermRepository: ISearchTermRepository,
    private val rankingConfigService: IRankingConfigService,
    private val preferenceService: IPreferenceService,
    private val tournamentFilterService: ITournamentFilterService,
    private val tournamentSortService: ITournamentSortService
) : ViewModel(), Observable, IPreferenceService by preferenceService, ITournamentFilterService by tournamentFilterService, ITournamentSortService by tournamentSortService {

    val messageEvent = MutableLiveData<Event<String>>()

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
                    val filteredParticipantEntities = participantEntities.filter { pe -> pe.epoch == it.epoch }

                    val filteredOpenMatchUpEntities = filteredMatchUpEntities.filter { me -> me.isOpen }
                    val filteredMatchUpEntitiesWithByes = filteredMatchUpEntities.filter { me -> me.containsBye }

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
                            filteredOpenMatchUpEntities.size,
                            filteredMatchUpEntitiesWithByes.size,
                            filteredParticipantEntities.count { pe -> pe.type == ParticipantType.NORMAL },
                            it.progress
                        ),
                        FoundationalTournamentEntities(
                            filteredRoundEntities,
                            filteredMatchUpEntities,
                            filteredParticipantEntities
                        )
                    )
                }
        }

    val searchTerms = searchTermRepository.getAll()

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

    fun delete(tournaments: Set<RestoredTournamentInformation>, successMsg: String) = viewModelScope.launch {
        tournamentRepository.delete(tournaments.map { it.toTournamentEntity(rankingConfigService) })
        roundRepository.delete(tournaments.flatMap { it.foundationalTournamentEntities.roundEntities })
        matchUpRepository.delete(tournaments.flatMap { it.foundationalTournamentEntities.matchUpEntities })
        participantRepository.delete(tournaments.flatMap { it.foundationalTournamentEntities.participantEntities })
        messageEvent.value = Event(successMsg)
    }

    fun clearSearchTerm(term: String) = viewModelScope.launch {
        searchTermRepository.delete(term)
    }

    fun addSearchTerm(termPair: Pair<String, Int>) = viewModelScope.launch {
        searchTermRepository.upsert(SearchTermEntity(termPair.first, termPair.second + 1))
    }

}