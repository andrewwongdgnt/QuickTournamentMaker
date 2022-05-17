package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickTournamentMaker.data.tournament.IMatchUpRepository
import com.dgnt.quickTournamentMaker.data.tournament.IParticipantRepository
import com.dgnt.quickTournamentMaker.data.tournament.IRoundRepository
import com.dgnt.quickTournamentMaker.data.tournament.ITournamentRepository
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentBuilderService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentDataTransformerService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInitiatorService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentRestoreService
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime

class TournamentViewModel(
    private val tournamentBuilderService: ITournamentBuilderService,
    private val tournamentInitiatorService: ITournamentInitiatorService,
    private val tournamentRestoreService: ITournamentRestoreService,
    private val tournamentDataTransformerService: ITournamentDataTransformerService,
    private val tournamentRepository: ITournamentRepository,
    private val roundRepository: IRoundRepository,
    private val matchUpRepository: IMatchUpRepository,
    private val participantRepository: IParticipantRepository,
) : ViewModel(), Observable {

    var rootPropertiesAreSet = false

    @Bindable
    val title = MutableLiveData<String>()

    @Bindable
    val description = MutableLiveData<String>()

    @Bindable
    val tournament = MutableLiveData<Tournament>()

    @Bindable
    val hasChanges = MutableLiveData<Boolean>()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(
        tournamentInformation: TournamentInformation,
        orderedParticipants: List<Participant>,
        defaultRoundGroupTitleFunc: (Int) -> String,
        defaultRoundTitleFunc: (Int) -> String,
        defaultMatchUpTitleFunc: (Int, Participant, Participant) -> String,
        foundationalTournamentEntities: FoundationalTournamentEntities?
    ) {
        title.value = tournamentInformation.title
        description.value = tournamentInformation.description

        tournamentBuilderService.build(
            tournamentInformation,
            orderedParticipants,
            defaultRoundGroupTitleFunc,
            defaultRoundTitleFunc,
            defaultMatchUpTitleFunc
        ).let {
            tournamentInitiatorService.initiate(it)
            foundationalTournamentEntities?.let { foundationalTournamentEntities ->
                tournamentRestoreService.restore(it, foundationalTournamentEntities)
            }
            this.tournament.value = it
        }

    }

    fun updateTournament(matchUp: MatchUp, participantPosition: ParticipantPosition) {
        tournament.value?.let {
            matchUp.status = it.matchUpStatusTransformService.transform(matchUp.status, participantPosition)
            it.updateRound(matchUp.roundGroupIndex, matchUp.roundIndex, matchUp.matchUpIndex)
        }
    }

    fun transformTournament() =
        tournament.value?.let { tournamentDataTransformerService.transform(it) }

    fun saveTournament(duplicate: Boolean) = viewModelScope.launch {
        tournament.value?.let { tournament ->

            val now = LocalDateTime.now()

            val id = tournament.tournamentInformation.run {
                if (duplicate)
                    creationDate = now

                lastModifiedDate = now

                creationDate
            }

            tournamentRepository.upsert(tournament.toEntity())

            tournament.rounds.map { it.toEntity(id) }.let { entities ->
                roundRepository.upsert(entities)
            }

            tournament.matchUps.map { it.toEntity(id) }.let { entities ->
                matchUpRepository.upsert(entities)
            }

            tournament.orderedParticipants.mapIndexed { index, participant -> participant.toEntity(id, index) }.let { entities ->
                participantRepository.upsert(entities)
            }

        }
    }
}