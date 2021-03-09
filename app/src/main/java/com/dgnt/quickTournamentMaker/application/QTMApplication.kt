package com.dgnt.quickTournamentMaker.application

import android.app.Application
import android.preference.PreferenceManager
import com.dgnt.quickTournamentMaker.data.QTMDatabase
import com.dgnt.quickTournamentMaker.data.management.*
import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.*
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService
import com.dgnt.quickTournamentMaker.service.interfaces.ISelectedPersonsService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInformationCreatorService
import com.dgnt.quickTournamentMaker.ui.main.home.HomeViewModelFactory
import com.dgnt.quickTournamentMaker.ui.main.management.*
import com.dgnt.quickTournamentMaker.ui.tournament.ParticipantEditorViewModelFactory
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentEditorViewModelFactory
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentViewModelFactory
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule


class QTMApplication() : Application(), DIAware {


    override val di = DI.lazy {

        import(androidXModule(this@QTMApplication))
        //Database
        bind() from singleton { QTMDatabase(instance()) }

        //DAO
        bind() from singleton { instance<QTMDatabase>().personDAO }
        bind() from singleton { instance<QTMDatabase>().groupDAO }

        //Repo
        bind<IPersonRepository>() with singleton { PersonRepository(instance()) }
        bind<IGroupRepository>() with singleton { GroupRepository(instance()) }
        bind<IMatchUpRepository>() with singleton { MatchUpRepository(instance()) }
        bind<IParticipantRepository>() with singleton { ParticipantRepository(instance()) }
        bind<IRoundRepository>() with singleton { RoundRepository(instance()) }
        bind<ITournamentRepository>() with singleton { TournamentRepository(instance()) }

        //ViewModelFactory
        bind() from provider { HomeViewModelFactory(instance(), instance(), instance(), instance(), instance()) }
        bind() from provider { ManagementViewModelFactory(instance(), instance()) }
        bind() from provider { GroupDeleteViewModelFactory(instance(), instance()) }
        bind() from provider { GroupEditorViewModelFactory(instance(), instance()) }
        bind() from provider { MovePersonsViewModelFactory(instance()) }
        bind() from provider { PersonEditorViewModelFactory(instance(), instance()) }
        bind() from provider { TournamentViewModelFactory() }
        bind() from provider { TournamentEditorViewModelFactory() }
        bind() from provider { ParticipantEditorViewModelFactory() }

        //Service
        bind<IRankingConfigService>() with singleton { RankingConfigService() }
        bind<IPreferenceService>() with singleton { PreferenceService(PreferenceManager.getDefaultSharedPreferences(instance()), instance()) }
        bind<ITournamentInformationCreatorService>() with singleton { TournamentInformationCreatorService() }
        bind<ISelectedPersonsService>() with singleton { SelectedPersonsService() }


    }




}