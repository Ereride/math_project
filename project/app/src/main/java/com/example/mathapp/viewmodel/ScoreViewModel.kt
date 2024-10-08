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

class ScoreViewModel(application: Application) : AndroidViewModel(application) {
    private val scoreDao: ScoreDAO = AppDatabase.getDatabase(application).scoreDao()

    // Expose top scores as LiveData
    val topScores: LiveData<List<ScoreBoard>> = scoreDao.getTopThreeScoresPerLevel()

    fun insertScore(score: Int, level: Int) {
        Log.d("ScoreViewModel", "Inserting score: $score for level: $level")
        val scoreEntry = ScoreBoard(uid = 0, level = level, points = score) // Assuming UID will be auto-generated
        viewModelScope.launch(Dispatchers.IO) {
            try {
                scoreDao.insertAll(scoreEntry)
                Log.d("ScoreViewModel", "Score inserted successfully")
            } catch (e: Exception) {
                Log.e("ScoreViewModel", "Error inserting score: ${e.message}")
            }
        }
    }

    fun deleteScores() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                scoreDao.clearTable()  // Kutsutaan clearTable() metodia taulun tyhjentämiseksi
                Log.d("ScoreViewModel", "All scores deleted successfully")
            } catch (e: Exception) {
                Log.e("ScoreViewModel", "Error deleting all scores: ${e.message}")
            }
        }
    }
}
