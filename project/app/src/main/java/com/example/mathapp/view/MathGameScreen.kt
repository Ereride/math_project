package com.example.mathapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mathapp.ui.theme.CustomButton
import com.example.mathapp.ui.theme.CustomText
import com.example.mathapp.ui.theme.TextStyleLevel
import com.example.mathapp.viewmodel.MathViewModel
import com.example.mathapp.viewmodel.ScoreViewModel
import kotlinx.coroutines.delay
@Composable
fun MathGameScreen(
    level: String?,
    navController: NavController,
    mathViewModel: MathViewModel = viewModel(),
    scoreViewModel: ScoreViewModel = viewModel()
) {
    val currentProblem by mathViewModel.currentProblem.collectAsState()
    var playerAnswer by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    var showNewProblem by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = level) {
        if (level != null) {
            mathViewModel.startNewLevel() // Call startNewLevel instead of resetGame
            generateNewProblem(level, mathViewModel)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp,start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CustomText(text = currentProblem, styleLevel = TextStyleLevel.SUBHEADLINE)

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = playerAnswer,
            onValueChange = { playerAnswer = it },
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomButton(text = "Check answer",onClick = {
            if (playerAnswer.isNotEmpty()) {
                val isCorrect = mathViewModel.checkAnswer(playerAnswer)
                feedbackMessage = if (isCorrect) "Correct!" else "That was wrong..."
                showNewProblem = true
            } else {
                feedbackMessage = "Give answer"
            }
        })

        Spacer(modifier = Modifier.height(8.dp))

        CustomText(text = feedbackMessage, styleLevel = TextStyleLevel.SUBHEADLINE)

        Spacer(modifier = Modifier.height(24.dp))

        CustomButton(text = "Back to main", onClick = {
            navController.navigate("main")
            mathViewModel.resetGame()
        })
    }

    if (showNewProblem && mathViewModel.getQuestionCount() < 10) {
        LaunchedEffect(Unit) {
            delay(2000)
            generateNewProblem(level, mathViewModel)
            playerAnswer = ""
            feedbackMessage = ""
            showNewProblem = false
        }
    }

    if (mathViewModel.getQuestionCount() >= 10) {
        showMessage = true
    }

    if (showMessage) {
        val scoreToSave = mathViewModel.getTotalScore() // Get the total score from your game logic
        AlertDialog(
            onDismissRequest = { showMessage = false },
            title = { Text("Level Completed") },
            text = { Text("You have completed this level. You got ${mathViewModel.getScore()}/10 answers correct, with a total score of $scoreToSave/200 for this level.") },
            confirmButton = {
                CustomButton(text = "Back to main", onClick = {
                    scoreViewModel.insertScore(scoreToSave, level?.toInt() ?: 1) // Pass the score and level
                    navController.navigate("main")
                    mathViewModel.resetGame()
                })
            }
        )
    }
}

private fun generateNewProblem(level: String?, mathViewModel: MathViewModel) {
    when (level) {
        "1" -> mathViewModel.generate1NewProblem()
        "2" -> mathViewModel.generate2NewProblem()
        "3" -> mathViewModel.generate3NewProblem()
    }
}
