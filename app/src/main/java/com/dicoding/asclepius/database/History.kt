package com.dicoding.asclepius.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "history")
@Parcelize
data class History(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "imageUri")
    var uri: String,

    @ColumnInfo(name = "label")
    var label: String? = null,

    @ColumnInfo(name = "confidence")
    var confidence: Float = 0.0F,

    @ColumnInfo(name = "dateGenerate")
    val dateGenerate: String? = null
) : Parcelable
