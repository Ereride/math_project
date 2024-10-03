package com.example.mathapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mathapp.viewmodel.MathViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@Composable
fun MathGameScreen(level: String?, navController: NavController, mathViewModel: MathViewModel = viewModel()) {

    val currentProblem by mathViewModel.currentProblem.collectAsState()
    var playerAnswer by remember { mutableStateOf("") }
    var playerPoints by remember { mutableStateOf(0) }
    var feedbackMessage by remember { mutableStateOf("") }
    var questions by remember { mutableStateOf(0) }
    var showNewProblem by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }

    LaunchedEffect(level) {
        mathViewModel.generateNewProblem(level)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = currentProblem, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = playerAnswer,
            onValueChange = {playerAnswer = it},
            label = { Text("Vastauksesi") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val isCorrect = mathViewModel.checkAnswer(playerAnswer)
            if (isCorrect){
                playerPoints += 1
                questions += 1
                feedbackMessage= "Vastaus oikein"
            } else {
                feedbackMessage= "Vastaus on väärin :("
                questions += 1
            }
            showNewProblem = true
        }) {
            Text(("Tarkista vastaus"))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = feedbackMessage, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Pisteesi: $playerPoints", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = {
          navController.navigate("main")
        }) { Text("Palaa valikkoon") }
    }

    if (showNewProblem && questions < 10) {
        LaunchedEffect(Unit) {
            delay(2000)
            mathViewModel.generateNewProblem(level)
            playerAnswer= ""
            feedbackMessage= ""
            showNewProblem= false
        }
    }
    if (questions >= 10) {
        showMessage = true
    }
       if(showMessage)
       AlertDialog(
           onDismissRequest = {showMessage = false},
           title = { Text("Taso suoritettu") },
           text = { Text("Olen suorittanut tämän tason sait pisteitä $playerPoints/10 pistettä") },
           confirmButton = {
               Button(onClick = {
                   showMessage = false
                   navController.navigate("main")
               }) { Text("Palaa valikkoon") }
           }
       )
    }


