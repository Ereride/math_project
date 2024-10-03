package com.example.mathapp.view

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ScoreScreen(navController: NavController) {
    Text(text = "Score")

    Button(onClick = {
        navController.navigate("main")
    }) { Text("Palaa valikkoon") }
}