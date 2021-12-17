package com.rexoit.mywallpaper.model

import android.os.Parcel
import android.os.Parcelable

data class WallpaperModel(val imageName: String? = null, val imageLink: String? = null) :
	Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString(),
		parcel.readString()
	) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(imageName)
		parcel.writeString(imageLink)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<WallpaperModel> {
		override fun createFromParcel(parcel: Parcel): WallpaperModel {
			return WallpaperModel(parcel)
		}

		override fun newArray(size: Int): Array<WallpaperModel?> {
			return arrayOfNulls(size)
		}
	}
}