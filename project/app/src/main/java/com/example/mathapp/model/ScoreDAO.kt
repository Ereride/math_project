package com.example.mathapp.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDAO {

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
    fun getTopThreeScoresPerLevel(): LiveData<List<ScoreBoard>>

    @Query("SELECT * FROM score_board WHERE points = :score AND level = :level")
    fun getScoresByPointsAndLevel(score: Int, level: Int): List<ScoreBoard>

    @Insert
    fun insertAll(vararg scoreBoard: ScoreBoard)

   @Query("DELETE FROM score_board")
   fun clearTable()
}
