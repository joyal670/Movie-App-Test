package com.movieapp.model.movie_details

import android.os.Parcel
import android.os.Parcelable

data class Movy(
    val category_id: Int,
    val created_at: String,
    val id: Int,
    val movie_description: String,
    val movie_image: String,
    val movie_name: String,
    val movie_rating: Int,
    val updated_at: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(category_id)
        parcel.writeString(created_at)
        parcel.writeInt(id)
        parcel.writeString(movie_description)
        parcel.writeString(movie_image)
        parcel.writeString(movie_name)
        parcel.writeInt(movie_rating)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movy> {
        override fun createFromParcel(parcel: Parcel): Movy {
            return Movy(parcel)
        }

        override fun newArray(size: Int): Array<Movy?> {
            return arrayOfNulls(size)
        }
    }
}
