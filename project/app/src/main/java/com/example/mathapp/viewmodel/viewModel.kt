package com.example.mathapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.ceil
import kotlin.random.Random

class MathViewModel : ViewModel() {
    private val _currentProblem = MutableStateFlow("0 + 0")
    val currentProblem: StateFlow<String> = _currentProblem

    private var correctAnswer: Int = 0
    private var questionsAnswered = 0
    private var correctAnswersCount = 0
    private var startTime: Long = 0L
    private var totalScore: Int = 0

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _correctAnswersCount = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswersCount

    fun generate1NewProblem() {
        startTime = System.currentTimeMillis()
        val maxNumber = 20
        val maxNumberForMultiplication = 10

        val number1 = Random.nextInt(0, maxNumber + 1)
        val number2 = Random.nextInt(0, maxNumber + 1)
        val number3 = Random.nextInt(0, maxNumberForMultiplication + 1)
        val number4 = Random.nextInt(0, maxNumberForMultiplication + 1)

        val operation = listOf("+", "-", "*").random()

        _currentProblem.value = when(operation) {
            "+" -> "$number1 + $number2"
            "-" -> "$number1 - $number2"
            "*" -> "$number3 * $number4"
            else ->  "$number1 + $number2"
        }

        correctAnswer = when(operation) {
            "+" -> number1 + number2
            "-" -> number1 - number2
            "*" -> number3 * number4
            else -> number1 + number2
        }
    }

    fun generate2NewProblem() {
        startTime = System.currentTimeMillis()
        val maxNumber = 40
        val maxNumberForMultiplication = 20

        val number1 = Random.nextInt(0, maxNumber + 1)
        val number2 = Random.nextInt(0, maxNumber + 1)
        val number3 = Random.nextInt(0, maxNumberForMultiplication + 1)
        val number4 = Random.nextInt(0, maxNumberForMultiplication + 1)

        val operation = listOf("+", "-", "*", "m", "p").random()

        _currentProblem.value = when(operation) {
            "+" -> "$number1 + $number3 + $number2"
            "-" -> "$number1 - $number3 - $number2"
            "*" -> "Calculate area: \nlength is $number3\nwidth is $number4"
            "m" -> "$number1 * $number2 - $number3"
            "p" -> "$number1 * $number2 + $number3"
            else ->  "$number1 + $number2"
        }

        correctAnswer = when(operation) {
            "+" -> number1 + number3 + number2
            "-" -> number1 - number3 - number2
            "*" -> number3 * number4
            "m" -> number1 * number2 - number3
            "p" -> number1 * number2 + number3
            else -> number1 + number2 - number3
        }
        Log.d("MathGame", "Oikea vastaus (Level 2): $correctAnswer")
    }

    fun generate3NewProblem() {
        startTime = System.currentTimeMillis()
        val maxNumber = 10
        val maxNumberForTriangle = 20
        val maxNumberForProcentage1 = 100
        val maxNumberForProcentage2 = 200

        val number1 = Random.nextInt(1, maxNumber / 2 + 1) * 2
        val number2 = Random.nextInt(0, maxNumber / 2 + 1) * 2
        val number3 = Random.nextInt(10, 30 / 2) * 2 / number1 // Ensure division yields double

        val number4 = Random.nextInt(1, maxNumberForTriangle / 2 + 1) * 2
        val number5 = Random.nextInt(1, maxNumberForTriangle / 2 + 1) * 2

        val number6 = Random.nextInt(0, maxNumberForProcentage1 / 10 + 1) * 10
        val number7 = Random.nextInt(10, maxNumberForProcentage2 / 10 + 1) * 10

        val operation = listOf("equation1", "equation2", "triangle", "%").random()

        _currentProblem.value = when (operation) {
            "equation1" -> "Solve x:\n $number1 x + $number2 = $number3"
            "equation2" -> "Solve x:\n $number1 + $number2 - x = $number3"
            "triangle" -> "Calculate area of triangle\n base is $number5\n height is $number4"
            "%" -> "What is $number6% of $number7?"
            else -> "Solve x:\n $number1 + $number2 = $number3"
        }

        correctAnswer = when (operation) {
            "equation1" -> if (number1 != 0) ceil((number3 - number2) / number1.toDouble()).toInt() else 0 // Round up
            "equation2" -> ceil((number1 + number2 - number3).toDouble()).toInt() // Round up
            "triangle" -> ceil((number5 * number4) / 2.0).toInt() // Round up
            "%" -> ceil((number7 * number6 / 100.0)).toInt() // Round up
            else -> ceil((number3 - number2) / number1.toDouble()).toInt() // Round up
        }
        Log.d("MathGame", "Correct!: $correctAnswer")
    }

    fun calculateScore(timeTaken: Long, isCorrect: Boolean): Int {
        val baseScore: Int = if (isCorrect) 1 else 0 // This is of type Int
        val timeBonus: Int = if (isCorrect) {
            val bonus: Int = 20 - (timeTaken / 1000).toInt() // This also resolves to an Int
            bonus.coerceAtLeast(0) // This returns an Int
        } else 0 // This is also an Int

        return baseScore + timeBonus // Ensure both are Int
    }


    fun checkAnswer(answer: String): Boolean {
        val timeTaken = System.currentTimeMillis() - startTime
        val isCorrect = answer.toIntOrNull() == correctAnswer

        val score = calculateScore(timeTaken, isCorrect)
        totalScore += score
        _score.value = totalScore  // Update the score flow

        if (isCorrect) {
            correctAnswersCount += 1
            _correctAnswersCount.value = correctAnswersCount  // Update correct answers count flow
        }
        questionsAnswered += 1

        return isCorrect
    }

    fun getTotalScore(): Int {
        return totalScore
    }

    fun getScore(): Int {
        return correctAnswersCount
    }

    fun getQuestionCount(): Int {
        return  questionsAnswered
    }

    fun resetGame() {
        questionsAnswered = 0
        correctAnswersCount = 0
        totalScore = 0 // Reset total score
        _score.value = 0 // Reset the score flow
        _correctAnswersCount.value = 0 // Reset correct answers count flow
        _currentProblem.value = "0 + 0" // Reset the current problem
    }

    fun startNewLevel() {
        resetGame() // Reset game state for a new level
        // Any additional logic for starting a new level can go here
    }
}