package com.dgnt.quickTournamentMaker.application

import android.app.Application
import androidx.preference.PreferenceManager
import com.dgnt.quickTournamentMaker.data.QTMDatabase
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.IGroupRepository
import com.dgnt.quickTournamentMaker.data.management.IPersonRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.data.search.ISearchTermRepository
import com.dgnt.quickTournamentMaker.data.search.SearchTermRepository
import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.data.TournamentTypeServices
import com.dgnt.quickTournamentMaker.service.implementation.*
import com.dgnt.quickTournamentMaker.service.interfaces.*
import com.dgnt.quickTournamentMaker.ui.customSeed.CustomSeedViewModelFactory
import com.dgnt.quickTournamentMaker.ui.main.home.HomeViewModelFactory
import com.dgnt.quickTournamentMaker.ui.main.loadTournament.LoadTournamentFilterOptionsViewModelFactory
import com.dgnt.quickTournamentMaker.ui.main.loadTournament.LoadTournamentViewModelFactory
import com.dgnt.quickTournamentMaker.ui.main.management.*
import com.dgnt.quickTournamentMaker.ui.tournament.*
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule


class QTMApplication : Application(), DIAware {

    companion object {
        private const val RECORD_TAG = "RECORD_TAG"
        private const val POWER_OF_2_TAG = "POWER_OF_2_TAG"
        private const val EVEN_NUMBER_TAG = "EVEN_NUMBER_TAG"
        private const val NULL_POPULATE_TAG = "NULL_POPULATE_TAG"
        private const val ONE_WINNER_TAG = "ONE_WINNER_TAG"
        private const val TIE_TAG = "TIE_TAG"
        private const val PROGRESS_VIA_MATCH_UPS_TAG = "PROGRESS_VIA_MATCH_UPS_TAG"
        private const val PROGRESS_FOR_SURVIVAL_TAG = "PROGRESS_FOR_SURVIVAL_TAG"
    }

    override val di = DI.lazy {

        import(androidXModule(this@QTMApplication))
        //Database
        bind() from singleton { QTMDatabase(instance(), instance(), instance()) }

        //DAO
        bind() from singleton { instance<QTMDatabase>().personDAO }
        bind() from singleton { instance<QTMDatabase>().groupDAO }
        bind() from singleton { instance<QTMDatabase>().tournamentDAO }
        bind() from singleton { instance<QTMDatabase>().roundDAO }
        bind() from singleton { instance<QTMDatabase>().matchUpDAO }
        bind() from singleton { instance<QTMDatabase>().participantDAO }
        bind() from singleton { instance<QTMDatabase>().searchTermDAO }

        //Repo
        bind<IPersonRepository>() with singleton { PersonRepository(instance()) }
        bind<IGroupRepository>() with singleton { GroupRepository(instance()) }
        bind<IMatchUpRepository>() with singleton { MatchUpRepository(instance()) }
        bind<IParticipantRepository>() with singleton { ParticipantRepository(instance()) }
        bind<IRoundRepository>() with singleton { RoundRepository(instance()) }
        bind<ITournamentRepository>() with singleton { TournamentRepository(instance()) }
        bind<ISearchTermRepository>() with singleton { SearchTermRepository(instance()) }

        //ViewModelFactory
        bind() from provider {
            HomeViewModelFactory(
                instance(), instance(), instance(), instance(), instance(), mapOf(
                    TournamentType.ELIMINATION to instance(POWER_OF_2_TAG),
                    TournamentType.DOUBLE_ELIMINATION to instance(POWER_OF_2_TAG),
                    TournamentType.ROUND_ROBIN to instance(EVEN_NUMBER_TAG),
                    TournamentType.SWISS to instance(EVEN_NUMBER_TAG),
                    TournamentType.SURVIVAL to instance(NULL_POPULATE_TAG),
                )
            )
        }
        bind() from provider { ManagementViewModelFactory(instance(), instance()) }
        bind() from provider { GroupDeleteViewModelFactory(instance(), instance()) }
        bind() from provider { GroupEditorViewModelFactory(instance(), instance()) }
        bind() from provider { MovePersonsViewModelFactory(instance()) }
        bind() from provider { PersonEditorViewModelFactory(instance(), instance()) }
        bind() from provider { TournamentViewModelFactory(instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance()) }
        bind() from provider { MoreInfoViewModelFactory() }
        bind() from provider { RebuildTournamentViewModelFactory(instance(), instance(), mapOf(
            TournamentType.ELIMINATION to instance(POWER_OF_2_TAG),
            TournamentType.DOUBLE_ELIMINATION to instance(POWER_OF_2_TAG),
            TournamentType.ROUND_ROBIN to instance(EVEN_NUMBER_TAG),
            TournamentType.SWISS to instance(EVEN_NUMBER_TAG),
            TournamentType.SURVIVAL to instance(NULL_POPULATE_TAG),
        )
        ) }
        bind() from provider { ParticipantEditorViewModelFactory() }
        bind() from provider { MatchUpEditorViewModelFactory(instance()) }
        bind() from provider { RoundEditorViewModelFactory() }
        bind() from provider { CustomSeedViewModelFactory(instance()) }
        bind() from provider { LoadTournamentViewModelFactory(instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance()) }
        bind() from provider { LoadTournamentFilterOptionsViewModelFactory(instance()) }

        //Service
        bind<IRankingConfigService>() with singleton { RankingConfigService() }
        bind<IPreferenceService>() with singleton { PreferenceService(PreferenceManager.getDefaultSharedPreferences(instance()), instance()) }
        bind<ITournamentInformationCreatorService>() with singleton { TournamentInformationCreatorService() }
        bind<ISelectedPersonsService>() with singleton { SelectedPersonsService() }
        bind<IParticipantService>() with singleton { ParticipantService() }

        bind<IRoundGeneratorService>(TournamentType.ELIMINATION) with singleton { EliminationRoundGeneratorService(instance()) }
        bind<IRoundGeneratorService>(TournamentType.DOUBLE_ELIMINATION) with singleton { DoubleEliminationRoundGeneratorService(instance(TournamentType.ELIMINATION)) }
        bind<IRoundGeneratorService>(TournamentType.ROUND_ROBIN) with singleton { RoundRobinRoundGeneratorService(instance()) }
        bind<IRoundGeneratorService>(TournamentType.SWISS) with singleton { SwissRoundGeneratorService(instance()) }
        bind<IRoundGeneratorService>(TournamentType.SURVIVAL) with singleton { SurvivalRoundGeneratorService(instance()) }

        bind<IRoundUpdateService>(TournamentType.ELIMINATION) with singleton { EliminationRoundUpdateService() }
        bind<IRoundUpdateService>(TournamentType.DOUBLE_ELIMINATION) with singleton { DoubleEliminationRoundUpdateService(instance(TournamentType.ELIMINATION)) }
        bind<IRoundUpdateService>(TournamentType.ROUND_ROBIN) with singleton { EmptyRoundUpdateService() }
        bind<IRoundUpdateService>(TournamentType.SWISS) with singleton { SwissRoundUpdateService(instance(RECORD_TAG)) }
        bind<IRoundUpdateService>(TournamentType.SURVIVAL) with singleton { SurvivalRoundUpdateService() }

        bind<IEliminationRankingHelperService>() with singleton { EliminationRankingHelperService() }
        bind<IRankingService>(TournamentType.ELIMINATION) with singleton { EliminationRankingService(instance()) }
        bind<IRankingService>(TournamentType.DOUBLE_ELIMINATION) with singleton { DoubleEliminationRankingService(instance()) }
        bind<IRankingService>(RECORD_TAG) with singleton { RecordRankingService() }
        bind<IRankingService>(TournamentType.SURVIVAL) with singleton { SurvivalRankingService() }

        bind<ISeedService>(POWER_OF_2_TAG) with singleton { PowerOf2SeedService() }
        bind<ISeedService>(EVEN_NUMBER_TAG) with singleton { EvenNumberSeedService() }
        bind<ISeedService>(NULL_POPULATE_TAG) with singleton { NullPopulateSeedService() }

        bind<IMatchUpStatusTransformService>(ONE_WINNER_TAG) with singleton { OneWinnerMatchUpStatusTransformService() }
        bind<IMatchUpStatusTransformService>(TIE_TAG) with singleton { TieMatchUpStatusTransformService() }

        bind<ITournamentBuilderService>() with singleton {
            TournamentBuilderService(
                mapOf(
                    TournamentType.ELIMINATION to TournamentTypeServices(
                        roundGeneratorService = instance(TournamentType.ELIMINATION),
                        roundUpdateService = instance(TournamentType.ELIMINATION),
                        rankingService = instance(TournamentType.ELIMINATION),
                        matchUpStatusTransformService = instance(ONE_WINNER_TAG),
                        progressCalculatorService = instance(PROGRESS_VIA_MATCH_UPS_TAG),
                    ),
                    TournamentType.DOUBLE_ELIMINATION to TournamentTypeServices(
                        roundGeneratorService = instance(TournamentType.DOUBLE_ELIMINATION),
                        roundUpdateService = instance(TournamentType.DOUBLE_ELIMINATION),
                        rankingService = instance(TournamentType.DOUBLE_ELIMINATION),
                        matchUpStatusTransformService = instance(ONE_WINNER_TAG),
                        progressCalculatorService = instance(PROGRESS_VIA_MATCH_UPS_TAG),
                    ),
                    TournamentType.ROUND_ROBIN to TournamentTypeServices(
                        roundGeneratorService = instance(TournamentType.ROUND_ROBIN),
                        roundUpdateService = instance(TournamentType.ROUND_ROBIN),
                        rankingService = instance(RECORD_TAG),
                        matchUpStatusTransformService = instance(TIE_TAG),
                        progressCalculatorService = instance(PROGRESS_VIA_MATCH_UPS_TAG),
                    ),
                    TournamentType.SWISS to TournamentTypeServices(
                        roundGeneratorService = instance(TournamentType.SWISS),
                        roundUpdateService = instance(TournamentType.SWISS),
                        rankingService = instance(RECORD_TAG),
                        matchUpStatusTransformService = instance(TIE_TAG),
                        progressCalculatorService = instance(PROGRESS_VIA_MATCH_UPS_TAG),
                    ),
                    TournamentType.SURVIVAL to TournamentTypeServices(
                        roundGeneratorService = instance(TournamentType.SURVIVAL),
                        roundUpdateService = instance(TournamentType.SURVIVAL),
                        rankingService = instance(TournamentType.SURVIVAL),
                        matchUpStatusTransformService = instance(ONE_WINNER_TAG),
                        progressCalculatorService = instance(PROGRESS_FOR_SURVIVAL_TAG),
                    )
                ),
                instance()
            )
        }

        bind<ICreateDefaultTitleService>() with singleton { CreateDefaultTitleService() }

        bind<ICustomSeedManagementService>() with singleton { CustomSeedManagementService(instance()) }
        bind<IByeStatusResolverService>() with singleton { ByeStatusResolverService() }
        bind<ITournamentInitiatorService>() with singleton { TournamentInitiatorService(instance()) }
        bind<ITournamentDataTransformerService>() with singleton { TournamentDataTransformerService(instance()) }
        bind<ITournamentRestoreService>() with singleton { TournamentRestoreService() }
        bind<ITournamentFilterService>() with singleton { TournamentFilterViaSharedPreferenceService(instance()) }
        bind<ITournamentSortService>() with singleton { TournamentSortViaSharedPreferenceService(instance()) }

        bind<IProgressCalculatorService>(PROGRESS_VIA_MATCH_UPS_TAG) with singleton { ProgressCalculatorViaMatchUpsService() }
        bind<IProgressCalculatorService>(PROGRESS_FOR_SURVIVAL_TAG) with singleton { ProgressCalculatorForSurvivalService() }
    }


}