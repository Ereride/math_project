package com.example.mathapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mathapp.ui.theme.CustomButton // Käytä mukautettua painiketta
import com.example.mathapp.viewmodel.ApiViewModel
import com.example.mathapp.viewmodel.ScoreViewModel
import com.example.mathapp.ui.theme.CustomText
import com.example.mathapp.ui.theme.TextStyleLevel

@Composable
fun MainScreen(
    navController: NavController,
    apiViewModel: ApiViewModel = viewModel()
) {
    // Collect the state from the ViewModel
    val numberFact by apiViewModel.numberFact.collectAsState()
    val hasError by apiViewModel.hasError.collectAsState()

    LaunchedEffect(Unit) {
        apiViewModel.fetchRandomTrivia() // Kutsutaan triviaa hakevaa funktiota
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp, start = 40.dp, end = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Käytä teemasta haettua otsikon tyyliä
        CustomText(
            text = "Math Challenge",
            styleLevel = TextStyleLevel.HEADLINE // Otsikkotasoinen teksti
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Käytä teemasta haettua tekstin tyyliä
       CustomText(
            text = numberFact.text,
           styleLevel = TextStyleLevel.CAPTION
        )

        if (hasError) {
            CustomButton(text = "Fetch new trivia", onClick = { apiViewModel.fetchRandomTrivia()}
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

        // Käytä mukautettuja painikkeita jokaisessa tasovalinnassa
        CustomButton(text = "Level 1", onClick = { navController.navigate("game/1") })
        Spacer(modifier = Modifier.height(8.dp))
        CustomButton(text = "Level 2", onClick = { navController.navigate("game/2") })
        Spacer(modifier = Modifier.height(8.dp))
        CustomButton(text = "Level 3", onClick = { navController.navigate("game/3") })
        Spacer(modifier = Modifier.height(8.dp))
        CustomButton(text = "Score", onClick = { navController.navigate("score") })

        Spacer(modifier = Modifier.height(24.dp))

    }
}
