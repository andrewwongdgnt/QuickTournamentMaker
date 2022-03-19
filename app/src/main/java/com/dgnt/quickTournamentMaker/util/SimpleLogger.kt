package com.dgnt.quickTournamentMaker.util

import android.util.Log

object SimpleLogger {

    fun d(tag: Any, text: String) = Log.d(tag::class.simpleName, text)

    fun e(tag: Any, text: String, thr: Throwable? = null) = Log.e(tag::class.simpleName, text, thr)
}