package com.alizaidi.trendingrepos.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "owner_table")
@Parcelize
data class Owner(
    @PrimaryKey
    val id: Int,
    val login: String,
    val avatar_url: String
): Parcelable