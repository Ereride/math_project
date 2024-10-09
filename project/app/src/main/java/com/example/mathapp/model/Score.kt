package com.example.mathapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Data class representing a score entry in the scoreboard.
@Entity(tableName = "score_board")
data class ScoreBoard(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val level: Int,
    val points: Int,
    val newColumn: String? = null // Optional new column added for additional data or functionality.
)
