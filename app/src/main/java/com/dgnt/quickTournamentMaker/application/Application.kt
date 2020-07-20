package com.dgnt.quickTournamentMaker.application

import android.app.Application
import com.dgnt.quickTournamentMaker.service.implementation.EliminationSeedService
import com.dgnt.quickTournamentMaker.service.interfaces.ISeedService
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.provider


class Application() : Application(), DIAware {

    override val di = DI.lazy {
        bind<ISeedService>() with provider { EliminationSeedService() }

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