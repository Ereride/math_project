package com.example.mathapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // Import layout components
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState // To collect state from ViewModel
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mathapp.R
import com.example.mathapp.ui.theme.CustomButton // Custom button for UI
import com.example.mathapp.viewmodel.ApiViewModel // ViewModel for API data
import com.example.mathapp.ui.theme.CustomText // Custom text for UI
import com.example.mathapp.ui.theme.TextStyleLevel
import androidx.compose.material3.AlertDialog // Alert dialog for information
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    navController: NavController, // Navigation controller for screen transitions
    apiViewModel: ApiViewModel = viewModel() // ViewModel to handle API calls
) {
    // Collecting state from the ViewModel to use in the UI
    val numberFact by apiViewModel.numberFact.collectAsState() // Collect trivia text
    val hasError by apiViewModel.hasError.collectAsState() // Check for errors
    val showDialog = remember { mutableStateOf(false) } // State to control dialog visibility

    // Fetch trivia when the composable is launched
    LaunchedEffect(Unit) {
        apiViewModel.fetchRandomTrivia() // Fetch trivia data from API
    }

    // Outer Box to stack the IconButton and other UI elements
    Box(modifier = Modifier.fillMaxSize()) {

        // Column to hold the main content of the screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, start = 40.dp, end = 40.dp), // Padding around the content
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center the column vertically
        ) {
            // Custom Text for title
            CustomText(
                text = stringResource(id = R.string.math_challenge),
                styleLevel = TextStyleLevel.HEADLINE // Title style
            )
            Spacer(modifier = Modifier.height(16.dp)) // Space below the title

            // Custom Text for trivia
            CustomText(
                text = numberFact.text,
                styleLevel = TextStyleLevel.CAPTION // Style for trivia text
            )

            // Show button to fetch new trivia if there was an error
            if (hasError) {
                Spacer(modifier = Modifier.height(16.dp))
                CustomButton(text = stringResource(R.string.fetch_new_trivia), onClick = { apiViewModel.fetchRandomTrivia() })
            } else {
                Spacer(modifier = Modifier.height(16.dp)) // Space if no error
            }

            Spacer(modifier = Modifier.height(24.dp)) // Space before level buttons

            // Custom buttons for game levels
            CustomButton(text = stringResource(R.string.level1), onClick = { navController.navigate("game/1") }) // Navigate to level 1
            Spacer(modifier = Modifier.height(8.dp)) // Space between buttons
            CustomButton(text = stringResource(R.string.level2), onClick = { navController.navigate("game/2") }) // Navigate to level 2
            Spacer(modifier = Modifier.height(8.dp)) // Space between buttons
            CustomButton(text = stringResource(R.string.level3), onClick = { navController.navigate("game/3") }) // Navigate to level 3
            Spacer(modifier = Modifier.height(8.dp)) // Space between buttons
            CustomButton(text = stringResource(R.string.score), onClick = { navController.navigate("score") }) // Navigate to score screen

            Spacer(modifier = Modifier.height(24.dp)) // Space below level buttons
        }

        // IconButton in the top right corner for help or info
        Image(
            painter = painterResource(R.drawable.guidebook), // Load the image resource
            contentDescription = null, // No content description for the image
            modifier = Modifier
                .size(80.dp) // Set size for the image
                .align(Alignment.TopEnd) // Align in the top end corner
                .clickable {
                    showDialog.value = true // Show the dialog when clicked
                }
                .padding(20.dp) // Add padding around the image
        )

        // AlertDialog to show the level guide when icon is clicked
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false }, // Dismiss the dialog when clicked outside
                title = { Text( stringResource(R.string.level_guide)) }, // Dialog title
                text = {
                    Text(
                        text = stringResource(id = R.string.level_1_instructions) + "\n\n" +
                                stringResource(id = R.string.level_2_instructions) + "\n\n" +
                                stringResource(id = R.string.level_3_instructions) + "\n\n" +
                                stringResource(id = R.string.good_luck) // Instructions for each level
                    )
                },
                confirmButton = {
                    CustomButton(text = stringResource(R.string.back_to_main), onClick = {
                        showDialog.value = false // Close the dialog
                        navController.navigate("main") // Navigate back to the main screen
                    })
                }
            )
        }

        // Background image for visual appeal
        val image = painterResource(R.drawable.spider) // Load the background image resource
        Image(
            painter = image,
            contentDescription = null, // No content description for the image
            modifier = Modifier
                .size(200.dp) // Set size for the background image
                .align(Alignment.BottomStart) // Align the image at the bottom start corner
                .padding(start = 50.dp) // Add padding to the left
        )
    }
}
