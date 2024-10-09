package com.example.mathapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mathapp.viewmodel.MathViewModel
import com.example.mathapp.viewmodel.ScoreViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.mathapp.R
import com.example.mathapp.ui.theme.CustomButton
import com.example.mathapp.ui.theme.CustomText
import com.example.mathapp.ui.theme.TextStyleLevel

@Composable
fun ScoreScreen(
    navController: NavController, // Navigation controller for transitioning screens
    mathViewModel: MathViewModel = viewModel(), // ViewModel for math logic
    scoreViewModel: ScoreViewModel = viewModel() // ViewModel for score management
) {

    // Observe the top scores from the ScoreViewModel
    val topScores by scoreViewModel.topScores.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize() // Fills the entire available space
            .padding(bottom = 80.dp), // Adds padding to the bottom
        horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally
        verticalArrangement = Arrangement.Bottom, // Arrange content towards the bottom
    ) {

        CustomText(text = "Top Scores", styleLevel = TextStyleLevel.HEADLINE) // Display title

        // Group scores by level
        val groupedScores = topScores.groupBy { it.level }

        // Iterate through each group of scores
        groupedScores.forEach { (level, scores) ->
            Spacer(modifier = Modifier.height(16.dp)) // Add space between levels

            // Display the level title
            CustomText(
                text = "Level $level",
                styleLevel = TextStyleLevel.SUBHEADLINE,
            )

            // Sort scores for the current level and take the top 3
            scores
                .sortedByDescending { it.points } // Sort scores in descending order based on points
                .take(3) // Take only the top 3 scores
                .forEach { score ->
                    // Display each top score
                    CustomText(text = "${score.points} points", styleLevel = TextStyleLevel.BODY)
                }
        }

        // Button to navigate back to the main screen
        CustomButton(text = stringResource(R.string.back_to_main), onClick = {
            navController.navigate("main") // Navigate back to the main screen
            mathViewModel.resetGame() // Reset the game state in the MathViewModel
        })
    }

    // Button to reset all scores
    CustomButton(text = "Reset points",
        onClick = { scoreViewModel.deleteScores() }, // Calls the method to delete scores
        Modifier.padding(top = 20.dp)
    )

    // Box layout to overlay an image on the score screen
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val image = painterResource(R.drawable.claws)
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .size(340.dp)
                .align(Alignment.TopEnd)
                .padding(top = 40.dp)
        )
    }
}
