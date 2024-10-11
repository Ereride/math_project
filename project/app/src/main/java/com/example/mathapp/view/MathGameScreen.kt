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
    // Collect current problem state from the ViewModel
    val currentProblem by mathViewModel.currentProblem.collectAsState()
    var playerAnswer by remember { mutableStateOf("") } // Holds player's answer
    var feedbackMessage by remember { mutableStateOf("") } // Message for feedback
    var showNewProblem by remember { mutableStateOf(false) } // State to show new problem
    var showMessage by remember { mutableStateOf(false) } // State to show completion message

    val focusRequester = remember { FocusRequester() } // Requester to handle focus on the text field
    var showInvalidLevelAlert by remember { mutableStateOf(false) }

    // Effect to start a new level and generate a problem
    LaunchedEffect(key1 = level) {
        if (level != null) {
            mathViewModel.startNewLevel() // Start the new level
            try {
                generateNewProblem(level, mathViewModel)
                focusRequester.requestFocus() // Request focus for the text field
            } catch (e: IllegalArgumentException) {
                // Handle the invalid level case by showing the alert
                showInvalidLevelAlert = true
            }
        }
    }

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
        modifier = Modifier.fillMaxSize() // Full size for the Box layout
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Fill the available size
                .padding(top = 40.dp, start = 16.dp, end = 16.dp), // Padding for the column
            horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally
            verticalArrangement = Arrangement.Center // Center content vertically
        ) {
            CustomText(text = currentProblem, styleLevel = TextStyleLevel.SUBHEADLINE) // Display the current problem

            Spacer(modifier = Modifier.height(24.dp)) // Space between problem and answer input

            TextField(
                value = playerAnswer, // Bind player answer
                onValueChange = { playerAnswer = it }, // Update player answer on change
                modifier = Modifier.focusRequester(focusRequester), // Set focus requester
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) // Numeric keyboard
            )

            Spacer(modifier = Modifier.height(24.dp)) // Space between input and check button

            val provideAnswerMessage = stringResource(R.string.please_provide_answer)
            val answerMustBeNumericMessage = stringResource(R.string.answer_must_be_numeric)
            val errorCheckingAnswerMessage = stringResource(R.string.error_checking_answer)
            val correctAnswerMessage = stringResource(R.string.correct_answer)
            val wrongAnswerMessage = stringResource(R.string.wrong_answer)

            CustomButton(text = stringResource(R.string.check_answer), onClick = {
                // Validate answer input
                when {
                    playerAnswer.isEmpty() -> {
                        feedbackMessage = provideAnswerMessage // Prompt to give an answer if empty
                    }
                    !isNumeric(playerAnswer) -> {
                        feedbackMessage = answerMustBeNumericMessage // Ensure the answer is numeric
                    }
                    else -> {
                        // Attempt to check the answer and handle any potential errors
                        try {
                            val isCorrect = mathViewModel.checkAnswer(playerAnswer) // Check if the answer is correct
                            feedbackMessage = if (isCorrect) correctAnswerMessage else wrongAnswerMessage
                        } catch (e: Exception) {
                            feedbackMessage = errorCheckingAnswerMessage// Handle error during answer check
                            // Log the error if needed
                        }
                    }
                }
            })

            Spacer(modifier = Modifier.height(8.dp)) // Space between check button and feedback message

            CustomText(text = feedbackMessage, styleLevel = TextStyleLevel.SUBHEADLINE) // Display feedback message

            Spacer(modifier = Modifier.height(24.dp)) // Space before the back button

            CustomButton(text = stringResource(R.string.back_to_main), onClick = {
                navController.navigate("main") // Navigate back to the main screen
                mathViewModel.resetGame() // Reset the game state in the MathViewModel
            })
        }

        // Load and display an image at the bottom end
        val image = painterResource(R.drawable.__cute_ghost_with_pencil_design) // Load the image resource
        Image(
            painter = image,
            contentDescription = null, // No content description
            modifier = Modifier
                .size(200.dp) // Set size for the image
                .align(Alignment.BottomEnd) // Align image to the bottom end
                .padding(start = 20.dp, bottom = 20.dp) // Padding for the image
        )
    }

    // Show new problem after a delay if the question count is less than 10
    if (showNewProblem && mathViewModel.getQuestionCount() < 10) {
        LaunchedEffect(Unit) {
            delay(2000) // Delay for 2 seconds
            generateNewProblem(level, mathViewModel) // Generate a new problem
            playerAnswer = "" // Clear the answer field
            feedbackMessage = "" // Clear the feedback message
            showNewProblem = false // Reset the showNewProblem flag
        }
    }

    // Check if the question count has reached 10 to show the completion message
    if (mathViewModel.getQuestionCount() >= 10) {
        showMessage = true // Set flag to show completion message
    }

    // Show completion dialog if showMessage is true
    if (showMessage) {
        val scoreToSave = mathViewModel.getTotalScore() // Get the total score
        val feedbackMessage = when {
            scoreToSave <= 49 -> stringResource(R.string.zombie_message, scoreToSave)
            scoreToSave in 50..99 -> stringResource(R.string.witch_message, scoreToSave)
            scoreToSave in 100..149 -> stringResource(R.string.vampire_message, scoreToSave)
            scoreToSave in 150..174 -> stringResource(R.string.werewolf_message, scoreToSave)
            scoreToSave in 175..189 -> stringResource(R.string.ghost_message, scoreToSave)
            scoreToSave in 190..200 -> stringResource(R.string.pumpkin_master_message, scoreToSave)
            else -> stringResource(R.string.unknown_score_message)
        }

        // Show an AlertDialog for level completion
        AlertDialog(
            onDismissRequest = { showMessage = false }, // Dismiss dialog on request
            title = { Text(stringResource(R.string.level_completed_title)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.level_completed_message,
                        mathViewModel.getScore(),
                        feedbackMessage
                    )
                )
            }, // Dialog content
            confirmButton = {
                CustomButton(text = stringResource(R.string.back_to_main), onClick = {
                    showMessage = false // Close the dialog
                    scoreViewModel.insertScore(scoreToSave, level?.toInt() ?: 1) // Save score and level
                    navController.navigate("main") // Navigate back to the main screen
                    mathViewModel.resetGame() // Reset the game
                })
            }
        )
    }
}

// Function to generate a new math problem based on the level
private fun generateNewProblem(level: String?, mathViewModel: MathViewModel) {
    when (level) {
        "1" -> mathViewModel.generate1NewProblem() // Generate problem for level 1
        "2" -> mathViewModel.generate2NewProblem() // Generate problem for level 2
        "3" -> mathViewModel.generate3NewProblem() // Generate problem for level 3
        else -> {
            // Handle unexpected level value
            Log.w("MathGameScreen", "Invalid level: $level. Falling back to level 1.")
            mathViewModel.generate1NewProblem()
        }
    }
}

// Function to check if the answer is numeric
private fun isNumeric(str: String): Boolean {
    return str.toDoubleOrNull() != null // Check if the string can be converted to a Double
}
