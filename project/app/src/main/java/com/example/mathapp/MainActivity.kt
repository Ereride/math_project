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

// Main activity for the math application, extending ComponentActivity
class MainActivity : ComponentActivity() {
    // Called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables edge-to-edge display for a full-screen experience
        enableEdgeToEdge()

        // Sets the content of the activity using Jetpack Compose
        setContent {
            // Apply the custom theme for the app
            MathAppTheme {
                // Create a NavController for managing navigation within the app
                val navController = rememberNavController()
                // Create a ViewModel instance for the math game
                val mathViewModel: MathViewModel = viewModel()

                // Create a Scaffold to provide basic Material Design layout structure
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Background image with overlay to provide a visual backdrop
                    BackgroundImageWithOverlay(modifier = Modifier.fillMaxSize().zIndex(-1f))

                    // Navigation host for managing different screens in the app
                    NavHost(
                        navController,
                        startDestination = "main", // Start with the main screen
                        Modifier.fillMaxSize() // Fill the entire available space
                    ) {
                        // Define the main screen of the app
                        composable("main") { MainScreen(navController) }

                        // Define the game screen, passing the level as an argument
                        composable("game/{level}") { navBackStackEntry ->
                            val level = navBackStackEntry.arguments?.getString("level") // Get level argument from nav back stack
                            MathGameScreen(level, navController, mathViewModel) // Call the game screen
                        }

                        // Define the score screen
                        composable("score") { ScoreScreen(navController) }
                    }
                }
            }
        }
    }
}

// Composable function to display a background image with a gradient overlay
@Composable
fun BackgroundImageWithOverlay(modifier: Modifier) {
    Box(modifier = modifier) {
        // Load the background image resource
        val image = painterResource(R.drawable.witchcraft_40) // Ensure the image resource exists
        Image(
            painter = image, // Set the image painter
            contentDescription = null, // No content description for decorative images
            modifier = Modifier.fillMaxSize(), // Image fills the entire Box
            contentScale = ContentScale.Crop // Crop the image to fill the Box while maintaining aspect ratio
        )

        // Overlay with a radial gradient background
        Box(
            modifier = Modifier
                .fillMaxSize() // Fill the entire Box
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent, // Transparent at the edges
                            Color(0x80080808)  // Semi-transparent black in the center
                        ),
                        radius = 1000f // The radius of the gradient
                    )
                )
        )
    }
}
