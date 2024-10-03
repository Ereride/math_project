package com.example.mathapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class MathViewModel : ViewModel() {
    private val _currentProblem = MutableStateFlow("0 + 0")
    val currentProblem: StateFlow<String> = _currentProblem

    private var correctAnswer: Int = 0

    fun generateNewProblem(level: String?) {
        val maxNumber = when (level) {
            "1" -> 20
            "2" -> 50
            "3" -> 100
            else -> 100
        }

        val number1 = Random.nextInt(0, maxNumber + 1)
        val number2 = Random.nextInt(0, maxNumber + 1)

        val operation = if (Random.nextBoolean()) {
            "+"
        } else {
            "-"
        }

        _currentProblem.value = "$number1 $operation $number2"

        correctAnswer = if (operation == "+"){
            number1 + number2
        } else {
            number1 - number2
        }
    }

    fun checkAnswer(answer: String): Boolean {
        return answer.toIntOrNull() == correctAnswer
    }
}