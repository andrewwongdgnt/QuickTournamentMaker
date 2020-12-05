package com.dgnt.quickTournamentMaker.application

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.dgnt.quickTournamentMaker.service.implementation.DefaultPreferenceService
import com.dgnt.quickTournamentMaker.service.implementation.DefaultRankingConfigService
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService
import org.kodein.di.*


class QTMApplication() : Application(), DIAware {



    override val di = DI.lazy {


        bind<IRankingConfigService>() with provider { DefaultRankingConfigService() }
        bind<IPreferenceService>() with provider { DefaultPreferenceService(PreferenceManager.getDefaultSharedPreferences(applicationContext), instance()) }
       //  bind<IRoundGeneratorService>() with provider { EliminationRoundGeneratorService() }

    }


    //override val kodein = DIAware.lazy {


    //var eliminationSeedService = ISeedService()
//
//        bind<ConversionService>() with singleton {
//            unitConversionService
//        }
//        bind<CalculatorService>() with singleton {
//            CalculatorService(unitConversionService)
//        }
//        bind<UnitGeneratorService>() with singleton {
//            UnitGeneratorService()
//        }
//}

}