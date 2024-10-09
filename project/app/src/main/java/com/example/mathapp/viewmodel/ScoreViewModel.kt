package com.example.mathapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mathapp.model.AppDatabase
import com.example.mathapp.model.ScoreBoard
import com.example.mathapp.model.ScoreDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ViewModel for managing score-related data and operations.
class ScoreViewModel(application: Application) : AndroidViewModel(application) {
    // Get a reference to the ScoreDAO from the database.
    private val scoreDao: ScoreDAO = AppDatabase.getDatabase(application).scoreDao()

    // Expose top scores as LiveData for UI observation.
    val topScores: LiveData<List<ScoreBoard>> = scoreDao.getTopThreeScoresPerLevel()

    // Function to insert a new score into the database.
    fun insertScore(score: Int, level: Int) {
        Log.d("ScoreViewModel", "Inserting score: $score for level: $level")
        // Create a new ScoreBoard entry, assuming UID will be auto-generated.
        val scoreEntry = ScoreBoard(uid = 0, level = level, points = score)
        // Launch a coroutine in the IO dispatcher for database operations.
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Insert the score entry into the database.
                scoreDao.insertAll(scoreEntry)
                Log.d("ScoreViewModel", "Score inserted successfully")
            } catch (e: Exception) {
                // Log any errors encountered during the insertion.
                Log.e("ScoreViewModel", "Error inserting score: ${e.message}")
            }
        }
    }

    // Function to delete all scores from the database.
    fun deleteScores() {
        // Launch a coroutine in the IO dispatcher for database operations.
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Clear the scores table.
                scoreDao.clearTable() // Calls clearTable() method to empty the table.
                Log.d("ScoreViewModel", "All scores deleted successfully")
            } catch (e: Exception) {
                // Log any errors encountered during the deletion.
                Log.e("ScoreViewModel", "Error deleting all scores: ${e.message}")
            }
        }
    }
}
