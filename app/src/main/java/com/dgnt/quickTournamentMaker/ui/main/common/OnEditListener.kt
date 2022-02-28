package com.dgnt.quickTournamentMaker.ui.main.common

import android.os.Parcel
import android.os.Parcelable

interface OnEditListener<T> : Parcelable {

    /**
     * handles the edit
     *
     * @param editedValue the new edited value
     */
    fun onEdit(editedValue: T)

    override fun describeContents() = 0

    override fun writeToParcel(p0: Parcel?, p1: Int) {

    }
}