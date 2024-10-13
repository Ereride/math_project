package com.example.mathapp.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mathapp.R
import com.example.mathapp.ui.theme.CustomButton
import com.example.mathapp.ui.theme.CustomText
import com.example.mathapp.ui.theme.TextStyleLevel
import com.example.mathapp.viewmodel.MathViewModel
import com.example.mathapp.viewmodel.ScoreViewModel
import kotlinx.coroutines.delay

@Composable
fun MathGameScreen(
    level: String?, // Game level passed as an argument
    navController: NavController, // Navigation controller to navigate between screens
    mathViewModel: MathViewModel = viewModel(), // ViewModel for math logic
    scoreViewModel: ScoreViewModel = viewModel() // ViewModel for score management
) {
    val currentProblem by mathViewModel.currentProblem.collectAsState()
    var playerAnswer by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    var showNewProblem by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    var showInvalidLevelAlert by remember { mutableStateOf(false) }

    // Effect to start a new level and generate a problem
    LaunchedEffect(key1 = level) {
        if (level != null) {
            mathViewModel.startNewLevel() // Start the new level
            try {
                Log.d("MathGameScreen", "Starting new level: $level")
                generateNewProblem(level, mathViewModel)
                focusRequester.requestFocus() // Request focus for the text field
            } catch (e: IllegalArgumentException) {
                showInvalidLevelAlert = true
                Log.e("MathGameScreen", "Invalid level: $level", e)
            }
        }
    }

    // Alert dialog for invalid level input
    if (showInvalidLevelAlert) {
        AlertDialog(
            onDismissRequest = { showInvalidLevelAlert = false },
            title = { Text(stringResource(R.string.error)) },
            text = { Text(stringResource(R.string.invalid_level))},
            confirmButton = {
                CustomButton(text = stringResource(R.string.try_again), onClick = {
                    showInvalidLevelAlert = false
                    mathViewModel.startNewLevel()
                })
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display the current math problem
            CustomText(text = currentProblem, styleLevel = TextStyleLevel.SUBHEADLINE)

            Spacer(modifier = Modifier.height(24.dp))

            // TextField for player's answer input
            TextField(
                value = playerAnswer,
                onValueChange = { playerAnswer = it },
                modifier = Modifier.focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Prepare messages for feedback
            val provideAnswerMessage = stringResource(R.string.please_provide_answer)
            val answerMustBeNumericMessage = stringResource(R.string.answer_must_be_numeric)
            val errorCheckingAnswerMessage = stringResource(R.string.error_checking_answer)
            val correctAnswerMessage = stringResource(R.string.correct_answer)
            val wrongAnswerMessage = stringResource(R.string.wrong_answer)

            // Button to check the answer
            CustomButton(text = stringResource(R.string.check_answer), onClick = {
                // Validate answer input
                when {
                    playerAnswer.isEmpty() -> {
                        feedbackMessage = provideAnswerMessage
                        Log.d("MathGameScreen", "Player did not provide an answer")
                    }
                    !isNumeric(playerAnswer) -> {
                        feedbackMessage = answerMustBeNumericMessage
                        Log.d("MathGameScreen", "Player answer is not numeric: $playerAnswer")
                    }
                    else -> {
                        try {
                            val isCorrect = mathViewModel.checkAnswer(playerAnswer)
                            feedbackMessage = if (isCorrect) correctAnswerMessage else wrongAnswerMessage
                            Log.d("MathGameScreen", "Player answer: $playerAnswer, Correct? $isCorrect")
                            val currentScore = mathViewModel.getTotalScore()
                            Log.d("MathGameScreen", "Current score: $currentScore")
                            showNewProblem = true
                        } catch (e: Exception) {
                            feedbackMessage = errorCheckingAnswerMessage
                            Log.e("MathGameScreen", "Error checking answer: ${e.message}", e)
                        }
                    }
                }
            })

            Spacer(modifier = Modifier.height(8.dp))

            // Display feedback message to the player
            CustomText(text = feedbackMessage, styleLevel = TextStyleLevel.SUBHEADLINE)

            Spacer(modifier = Modifier.height(24.dp))

            // Button to navigate back to the main screen
            CustomButton(text = stringResource(R.string.back_to_main), onClick = {
                navController.navigate("main")
                mathViewModel.resetGame()
            })
        }

        val image = painterResource(R.drawable.__cute_ghost_with_pencil_design)
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .padding(start = 20.dp, bottom = 20.dp)
        )
    }

    // Generate a new problem after a short delay if conditions are met
    if (showNewProblem && mathViewModel.getQuestionCount() < 10) {
        LaunchedEffect(Unit) {
            delay(1500)
            generateNewProblem(level, mathViewModel)
            playerAnswer = ""
            feedbackMessage = ""
            showNewProblem = false
        }
    }

    // Show completion message if the player has answered enough questions
    if (mathViewModel.getQuestionCount() >= 10) {
        showMessage = true
        Log.d("MathGameScreen", "Level completed, total questions: ${mathViewModel.getQuestionCount()}")
    }

    // Display an alert dialog when the level is completed
    if (showMessage) {
        val scoreToSave = mathViewModel.getTotalScore()
        Log.d("MathGameScreen", "Final score: $scoreToSave")
        val totalQuestionsAnswered = mathViewModel.getScore()

        val completedLevelText = stringResource(
            R.string.level_completed_message,
            totalQuestionsAnswered,
        )

        val feedbackText = when {
            scoreToSave <= 49 -> stringResource(R.string.zombie_message, scoreToSave)
            scoreToSave in 50..99 -> stringResource(R.string.witch_message, scoreToSave)
            scoreToSave in 100..149 -> stringResource(R.string.vampire_message, scoreToSave)
            scoreToSave in 150..174 -> stringResource(R.string.werewolf_message, scoreToSave)
            scoreToSave in 175..189 -> stringResource(R.string.ghost_message, scoreToSave)
            scoreToSave in 190..200 -> stringResource(R.string.pumpkin_master_message, scoreToSave)
            else -> stringResource(R.string.unknown_score_message)
        }

        AlertDialog(
            onDismissRequest = { showMessage = false },
            title = { Text(stringResource(R.string.level_completed_title)) },
            text = {
                Text(
                    text = "$completedLevelText\n$feedbackText"
                )
            },
            confirmButton = {
                CustomButton(text = stringResource(R.string.back_to_main), onClick = {
                    showMessage = false
                    scoreViewModel.insertScore(scoreToSave, level?.toInt() ?: 1)
                    navController.navigate("main")
                    mathViewModel.resetGame()
                })
            }
        )
    }
}

// Function to generate a new math problem based on the leve
private fun generateNewProblem(level: String?, mathViewModel: MathViewModel) {
    when (level) {
        "1" -> mathViewModel.generate1NewProblem()
        "2" -> mathViewModel.generate2NewProblem()
        "3" -> mathViewModel.generate3NewProblem()
        else -> {
            Log.w("MathGameScreen", "Invalid level: $level. Falling back to level 1.")
            mathViewModel.generate1NewProblem()
        }
    }
}

// Utility function to check if a string is numeric
private fun isNumeric(str: String): Boolean {
    return str.toDoubleOrNull() != null
}
