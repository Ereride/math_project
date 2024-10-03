package com.example.mathapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mathapp.ui.theme.MathAppTheme
import com.example.mathapp.view.MainScreen
import com.example.mathapp.view.MathGameScreen
import com.example.mathapp.view.ScoreScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathAppTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController, startDestination = "main", Modifier.padding(innerPadding)) {
                        composable("main") { MainScreen(navController) }
                        composable("game/{level}") {navBackStackEntry ->
                            val level = navBackStackEntry.arguments?.getString("level")
                            MathGameScreen(level, navController)
                        }
                        composable("score") { ScoreScreen(navController) }
                    }
                }
            }
        }
    }
}
