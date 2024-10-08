package com.example.mathapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_board")
data class ScoreBoard(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val level: Int,
    val points: Int,
    val newColumn: String? = null // New column added
)
