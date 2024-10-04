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

    fun generate1NewProblem() {
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
        val maxNumber = 40
        val maxNumberForMultiplication = 20

        val number1 = Random.nextInt(0, maxNumber + 1)
        val number2 = Random.nextInt(0, maxNumber + 1)
        val number3 = Random.nextInt(0, maxNumberForMultiplication + 1)
        val number4 = Random.nextInt(0, maxNumberForMultiplication + 1)

        val operation = listOf("+", "-", "*", "%", "/").random()

        _currentProblem.value = when(operation) {
            "+" -> "$number1 + $number3 + $number2"
            "-" -> "$number1 - $number3 - $number2"
            "*" -> "Laske pinta-ala kun pituus on $number3 ja leveys on $number4"
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
            else -> number1 + number2
        }
        Log.d("MathGame", "Oikea vastaus (Level 2): $correctAnswer")
    }

    fun generate3NewProblem() {
        val maxNumber = 10
        val maxNumberForTriangle = 20
        val maxNumberForProcentage1 = 200
        val maxNumberForProcentage2 = 100

        val number1 = Random.nextInt(1, maxNumber / 2 + 1) * 2
        val number2 = Random.nextInt(0, maxNumber + 1)
        val number3 = number1 + number2 + Random.nextInt(1, 5)

        val number4 = Random.nextInt(1, maxNumberForTriangle / 2 + 1) * 2
        val number5 = Random.nextInt(1, maxNumberForTriangle / 2 + 1) * 2

        val number6 = Random.nextInt(0, maxNumberForProcentage1 /10 + 1) * 10
        val number7 = Random.nextInt(0, maxNumberForProcentage2 /10 + 1) * 10

        val operation = listOf("equation1","equation2","triangle","%").random()

        _currentProblem.value = when (operation) {
            "equation1" -> "Ratkaise x  $number1 x + $number2 = $number3"
            "equation2" -> "Ratkaise x  $number1 + $number2 - x = $number3"
            "triangle" -> "Laske kolmion pinta-ala, kun pohja on $number5 ja korkeus $number4"
            "%" -> "Paljon on $number6 % luvusta $number7"
            else -> "Ratkaise x $number1 + $number2 = $number3"
        }

        correctAnswer = when (operation) {
            "equation1" -> if (number1 != 0) (number3 - number2) / number1 else 0
            "equation2" -> number1 + number2 - number3
            "triangle" -> (number5 * number4) / 2
            "%" -> number7 * number6 / 100
            else -> (number3 - number2) / number1
        }
        Log.d("MathGame", "Oikea vastaus: $correctAnswer")
    }

    fun checkAnswer(answer: String): Boolean {
        val isCorrect = answer.toIntOrNull() == correctAnswer
        if (isCorrect) {
            correctAnswersCount += 1
        }
        questionsAnswered += 1
        return isCorrect
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
    }
}