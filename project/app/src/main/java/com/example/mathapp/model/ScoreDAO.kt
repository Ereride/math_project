package com.example.mathapp.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// Data Access Object (DAO) interface for accessing the score_board table in the database.
@Dao
interface ScoreDAO {

    // Query to retrieve the top three scores for each level (1, 2, and 3) from the score_board table.
    @Query("""
        SELECT * FROM score_board WHERE uid IN (
            SELECT uid FROM score_board WHERE level = 1 ORDER BY points DESC LIMIT 3
        )
        UNION ALL
        SELECT * FROM score_board WHERE uid IN (
            SELECT uid FROM score_board WHERE level = 2 ORDER BY points DESC LIMIT 3
        )
        UNION ALL
        SELECT * FROM score_board WHERE uid IN (
            SELECT uid FROM score_board WHERE level = 3 ORDER BY points DESC LIMIT 3
        )
    """)
    fun getTopThreeScoresPerLevel(): LiveData<List<ScoreBoard>> // Returns a LiveData list of top three scores per level.

    // Query to retrieve scores that match a specific score and level from the score_board table.
    @Query("SELECT * FROM score_board WHERE points = :score AND level = :level")
    fun getScoresByPointsAndLevel(score: Int, level: Int): List<ScoreBoard> // Returns a list of ScoreBoard entries that match the score and level.

    // Insert multiple score entries into the score_board table.
    @Insert
    fun insertAll(vararg scoreBoard: ScoreBoard) // Accepts a variable number of ScoreBoard objects to insert.

    // Query to delete all entries from the score_board table.
    @Query("DELETE FROM score_board")
    fun clearTable() // Removes all entries from the score_board table.
}
