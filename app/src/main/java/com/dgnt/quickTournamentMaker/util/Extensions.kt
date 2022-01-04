package com.dgnt.quickTournamentMaker.util

import java.io.OutputStream
import java.nio.charset.Charset


fun <E> MutableCollection<E>.update(collection: Collection<E>) {
    this.clear()
    this.addAll(collection)
}

fun <K, V> MutableMap<K, V>.update(map: Map<K, V>) {
    this.clear()
    this.putAll(map)
}

fun <E> Collection<E>.shuffledIf(condition: Boolean): Collection<E> {

    return if (condition) this.shuffled() else this
}

fun OutputStream.writeText(
    text: String,
    charset: Charset = Charsets.UTF_8
): Unit = write(text.toByteArray(charset))