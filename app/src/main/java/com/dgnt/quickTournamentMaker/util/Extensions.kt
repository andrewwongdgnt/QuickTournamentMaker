package com.dgnt.quickTournamentMaker.util

import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*


fun <E> MutableCollection<E>.update(collection: Collection<E>) {
    this.clear()
    this.addAll(collection)
}

fun <K, V> MutableMap<K, V>.update(map: Map<K, V>) {
    this.clear()
    this.putAll(map)
}

fun <E> Collection<E>.shuffledIf(condition: Boolean) = if (condition) this.shuffled() else this

fun OutputStream.writeText(
    text: String,
    charset: Charset = Charsets.UTF_8
) = write(text.toByteArray(charset))

fun String?.toIntOr(default: Int) = this?.toIntOrNull() ?: default

fun String.containsCaseInsensitive(compared: String) = this.lowercase(Locale.getDefault()).contains(compared.lowercase(Locale.getDefault()))

fun TextView.highlight(input: String, color: Int, caseSensitive: Boolean = false) {

    val searchText = if (caseSensitive) input else input.lowercase(Locale.getDefault())

    if (searchText.isBlank())
        return
    //Get the text from text view and create a spannable string
    val spannableString = SpannableString(text)
    //Get the previous spans and remove them
    val backgroundSpans = spannableString.getSpans(0, spannableString.length, BackgroundColorSpan::class.java)
    for (span in backgroundSpans) {
        spannableString.removeSpan(span)
    }

    val adjustedSpannableString = if (caseSensitive) spannableString.toString() else spannableString.toString().lowercase(Locale.getDefault())
    //Search for all occurrences of the keyword in the string
    var indexOfKeyword = adjustedSpannableString.indexOf(searchText)
    while (indexOfKeyword >= 0) {
        //Create a background color span on the keyword
        spannableString.setSpan(BackgroundColorSpan(color), indexOfKeyword, indexOfKeyword + searchText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        //Get the next index of the keyword
        indexOfKeyword = adjustedSpannableString.indexOf(searchText, indexOfKeyword + searchText.length)
    }

    //Set the final text on TextView
    text = spannableString
}

fun View.getAllViews(): List<View> {
    if (this !is ViewGroup || childCount == 0) return listOf(this)

    return children
        .toList()
        .flatMap { it.getAllViews() }
        .plus(this as View)
}