package com.example.mathapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mathapp.viewmodel.MathViewModel
import com.example.mathapp.viewmodel.ScoreViewModel
import androidx.compose.runtime.livedata.observeAsState // Ensure correct import
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.mathapp.R
import com.example.mathapp.ui.theme.CustomButton
import com.example.mathapp.ui.theme.CustomText
import com.example.mathapp.ui.theme.TextStyleLevel

@Composable
fun ScoreScreen(
    navController: NavController,
    mathViewModel: MathViewModel = viewModel(),
    scoreViewModel: ScoreViewModel = viewModel()
) {
    // Collecting scores as state
    val totalScore = mathViewModel.getTotalScore()
    val correctAnswers = mathViewModel.getScore()

    // Fetch top scores
    val topScores by scoreViewModel.topScores.observeAsState(emptyList()) // Observe top scores directly

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,

    ) {

        // Display top scores
        CustomText(text = "Top Scores", styleLevel = TextStyleLevel.HEADLINE)

        // Ryhmitellään pisteet tason mukaan
        val groupedScores = topScores.groupBy { it.level }

        // Käydään jokainen taso läpi
        groupedScores.forEach { (level, scores) ->
            Spacer(modifier = Modifier.height(16.dp))  // Väli jokaisen tason välillä

            // Näytetään tason otsikko
            CustomText(
                text = "Level $level",
                styleLevel = TextStyleLevel.SUBHEADLINE,
            )

            // Näytetään kolmen parhaan pisteet
            scores
                .sortedByDescending { it.points }
                .take(3)
                .forEach { score ->
                CustomText(text = "${score.points} points", styleLevel = TextStyleLevel.BODY)
            }

        }
        CustomButton(text = "Back to main", onClick = {
            navController.navigate("main")
            mathViewModel.resetGame()
        })

    }

    CustomButton(text = "Reset points",
        onClick = { scoreViewModel.deleteScores() },
        Modifier.padding(top = 20.dp)
    )
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
