package com.mis.route.islami.ui.home.fragments.quran.model

import android.os.Parcel
import android.os.Parcelable

data class Surah(
    val surahName: String,
    val verseCount: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(surahName)
        parcel.writeInt(verseCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Surah> {
        override fun createFromParcel(parcel: Parcel): Surah {
            return Surah(parcel)
        }

        override fun newArray(size: Int): Array<Surah?> {
            return arrayOfNulls(size)
        }
    }
}