package com.example.mathapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mathapp.ui.theme.MathAppTheme
import com.example.mathapp.view.MainScreen
import com.example.mathapp.view.MathGameScreen
import com.example.mathapp.view.ScoreScreen
import com.example.mathapp.viewmodel.MathViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathAppTheme {
                val navController = rememberNavController()
                val mathViewModel: MathViewModel = viewModel()

                // Varmistetaan, että MyTestScreen näkyy
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Taustakuva, jos haluat sen
                    BackgroundImageWithOverlay(modifier = Modifier.fillMaxSize().zIndex(-1f))
                        NavHost(
                            navController,
                            startDestination = "main",
                            Modifier.fillMaxSize()
                        ) {
                            composable("main") { MainScreen(navController) }
                            composable("game/{level}") { navBackStackEntry ->
                                val level = navBackStackEntry.arguments?.getString("level")
                                MathGameScreen(level, navController, mathViewModel)
                            }
                            composable("score") { ScoreScreen(navController, mathViewModel) }
                        }
                    }
                }
            }
        }
    }


@Composable
fun BackgroundImageWithOverlay(modifier: Modifier) {
    Box(modifier = modifier) {
        // Taustakuva
        val image = painterResource(R.drawable.witchcraft_40) // Tarkista kuva
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(), // Täytä koko Box
            contentScale = ContentScale.Crop // Skaalaa kuva oikein
        )

        // Gradienttitaustaväri
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent, // Reuna (0%)
                            Color(0x80080808),  // Keskikohta (50% läpinäkyvyys)

                        ),
                        radius = 1000f // Gradientin pituus
                    )
                )
        )
    }
}


