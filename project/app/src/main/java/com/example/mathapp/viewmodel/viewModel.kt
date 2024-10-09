package com.example.mathapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.ceil
import kotlin.random.Random

// ViewModel for handling math problems and scoring in the app.
class MathViewModel : ViewModel() {
    // StateFlow to hold the current math problem.
    private val _currentProblem = MutableStateFlow("0 + 0")
    val currentProblem: StateFlow<String> = _currentProblem

    // Variables to track the correct answer, counts of answered questions, and scores.
    private var correctAnswer: Int = 0
    private var questionsAnswered = 0
    private var correctAnswersCount = 0
    private var startTime: Long = 0L
    private var totalScore: Int = 0

    // StateFlow to hold the current score and correct answers count.
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _correctAnswersCount = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswersCount

    // Generate a new math problem for level 1.
    fun generate1NewProblem() {
        startTime = System.currentTimeMillis() // Start the timer for score calculation.
        val maxNumber = 20
        val maxNumberForMultiplication = 10

        // Randomly generate two numbers for addition/subtraction and two for multiplication.
        val number1 = Random.nextInt(0, maxNumber + 1)
        val number2 = Random.nextInt(0, maxNumber + 1)
        val number3 = Random.nextInt(0, maxNumberForMultiplication + 1)
        val number4 = Random.nextInt(0, maxNumberForMultiplication + 1)

        // Randomly select an operation.
        val operation = listOf("+", "-", "*").random()

        // Set the current problem based on the selected operation.
        _currentProblem.value = when (operation) {
            "+" -> "$number1 + $number2"
            "-" -> "$number1 - $number2"
            "*" -> "$number3 * $number4"
            else -> "$number1 + $number2" // Default case
        }

        // Calculate the correct answer based on the selected operation.
        correctAnswer = when (operation) {
            "+" -> number1 + number2
            "-" -> number1 - number2
            "*" -> number3 * number4
            else -> number1 + number2 // Default case
        }
    }

    // Generate a new math problem for level 2.
    fun generate2NewProblem() {
        startTime = System.currentTimeMillis() // Start the timer for score calculation.
        val maxNumber = 30
        val maxNumberForMultiplication = 20

        // Randomly generate numbers for the problem.
        val number1 = Random.nextInt(0, maxNumber + 1)
        val number2 = Random.nextInt(0, maxNumber + 1)
        val number3 = Random.nextInt(0, maxNumberForMultiplication + 1)
        val number4 = Random.nextInt(0, maxNumberForMultiplication + 1)

        // Randomly select an operation.
        val operation = listOf("+", "-", "*", "m", "p").random()

        // Set the current problem based on the selected operation.
        _currentProblem.value = when (operation) {
            "+" -> "$number1 + $number3 + $number2"
            "-" -> "$number1 - $number3 - $number2"
            "*" -> "Calculate area: \nlength is $number3\nwidth is $number4"
            "m" -> "$number1 * $number2 - $number3"
            "p" -> "$number1 * $number2 + $number3"
            else -> "$number1 + $number2" // Default case
        }

        // Calculate the correct answer based on the selected operation.
        correctAnswer = when (operation) {
            "+" -> number1 + number3 + number2
            "-" -> number1 - number3 - number2
            "*" -> number3 * number4
            "m" -> number1 * number2 - number3
            "p" -> number1 * number2 + number3
            else -> number1 + number2 - number3 // Default case
        }
        Log.d("MathGame", "Correct answer (Level 2): $correctAnswer") // Debug log for correct answer
    }

    // Generate a new math problem for level 3.
    fun generate3NewProblem() {
        startTime = System.currentTimeMillis() // Start the timer for score calculation.
        val maxNumber = 10
        val maxNumberForTriangle = 20
        val maxNumberForPercentage1 = 100
        val maxNumberForPercentage2 = 200

        // Randomly generate numbers for equations and calculations.
        val number1 = Random.nextInt(1, maxNumber / 2 + 1) * 2
        val number2 = Random.nextInt(0, maxNumber / 2 + 1) * 2
        val number3 = Random.nextInt(10, 30 / 2) * 2 / number1 // Ensure division yields a double

        val number4 = Random.nextInt(1, maxNumberForTriangle / 2 + 1) * 2
        val number5 = Random.nextInt(1, maxNumberForTriangle / 2 + 1) * 2

        val number6 = Random.nextInt(0, maxNumberForPercentage1 / 10 + 1) * 10
        val number7 = Random.nextInt(10, maxNumberForPercentage2 / 10 + 1) * 10

        // Randomly select an operation for the problem.
        val operation = listOf("equation1", "equation2", "triangle", "%").random()

        // Set the current problem based on the selected operation.
        _currentProblem.value = when (operation) {
            "equation1" -> "Solve x:\n $number1 x + $number2 = $number3"
            "equation2" -> "Solve x:\n $number1 + $number2 - x = $number3"
            "triangle" -> "Calculate area of triangle\n base is $number5\n height is $number4"
            "%" -> "What is $number6% of $number7?"
            else -> "Solve x:\n $number1 + $number2 = $number3" // Default case
        }

        // Calculate the correct answer based on the selected operation.
        correctAnswer = when (operation) {
            "equation1" -> if (number1 != 0) ceil((number3 - number2) / number1.toDouble()).toInt() else 0 // Round up
            "equation2" -> ceil((number1 + number2 - number3).toDouble()).toInt() // Round up
            "triangle" -> ceil((number5 * number4) / 2.0).toInt() // Round up
            "%" -> ceil((number7 * number6 / 100.0)).toInt() // Round up
            else -> ceil((number3 - number2) / number1.toDouble()).toInt() // Round up
        }
        Log.d("MathGame", "Correct answer!: $correctAnswer") // Debug log for correct answer
    }

    // Calculate score based on time taken and whether the answer was correct.
    fun calculateScore(timeTaken: Long, isCorrect: Boolean): Int {
        val baseScore: Int = if (isCorrect) 1 else 0 // Base score is 1 if correct, otherwise 0
        val timeBonus: Int = if (isCorrect) {
            val bonus: Int = 20 - (timeTaken / 1000).toInt() // Calculate time bonus
            bonus.coerceAtLeast(0) // Ensure bonus is not negative
        } else 0 // No bonus if answer is incorrect

        return baseScore + timeBonus // Return total score (base + time bonus)
    }

    // Check the provided answer against the correct answer and update scores.
    fun checkAnswer(answer: String): Boolean {
        val timeTaken = System.currentTimeMillis() - startTime // Calculate time taken to answer
        val userAnswer = answer.toIntOrNull()

        // Check if the input is valid (not null and within a reasonable range)
        if (userAnswer == null) {
            Log.e("MathGame", "Invalid input: '$answer'. Input must be a number.")
            return false // Invalid input, treat as incorrect
        }

        val isCorrect = userAnswer == correctAnswer // Check if the answer is correct

        // Calculate and update the score based on the answer.
        val score = calculateScore(timeTaken, isCorrect)
        totalScore += score
        _score.value = totalScore  // Update the score flow

        // Update correct answers count if the answer is correct.
        if (isCorrect) {
            correctAnswersCount += 1
            _correctAnswersCount.value = correctAnswersCount  // Update correct answers count flow
        }
        questionsAnswered += 1 // Increment the total questions answered

        return isCorrect // Return whether the answer was correct
    }

// Get the total score accumulated in the game.
    fun getTotalScore(): Int {
        return totalScore
    }

    // Get the count of correct answers.
    fun getScore(): Int {
        return correctAnswersCount
    }

    // Get the count of questions answered.
    fun getQuestionCount(): Int {
        return  questionsAnswered
    }

    // Reset the game state for a new session.
    fun resetGame() {
        questionsAnswered = 0 // Reset questions answered count
        correctAnswersCount = 0 // Reset correct answers count
        totalScore = 0 // Reset total score
        _score.value = 0 // Reset the score flow
        _correctAnswersCount.value = 0 // Reset correct answers count flow
        _currentProblem.value = "0 + 0" // Reset the current problem to a default state
    }

    // Start a new level, resetting the game state.
    fun startNewLevel() {
        resetGame() // Reset game state for a new level
        // Any additional logic for starting a new level can go here
    }
}
