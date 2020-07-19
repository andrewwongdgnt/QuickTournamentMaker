package com.dgnt.quickTournamentMaker.application

import android.app.Application

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton

class Application : Application(), KodeinAware {

    override val kodein: Kodein = Kodein {


//        var unitConversionService = ConversionService()
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
    }

}