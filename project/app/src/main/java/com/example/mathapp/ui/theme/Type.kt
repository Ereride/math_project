package com.example.mathapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

// Värit
val LightPrimary = Color(0xFF6200EE)
val LightSecondary = Color(0xFF03DAC6)
val DarkPrimary = Color(0xFFBB86FC)
val DarkSecondary = Color(0xFF03DAC6)

val LightBackground = Color(0xFFFFFFFF)
val DarkBackground = Color(0xFF121212)

// Typografia
val AppTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)

enum class TextStyleLevel {
    HEADLINE,
    SUBHEADLINE,
    BODY,
    CAPTION
}

@Composable
fun CustomText(
    text: String,
    styleLevel: TextStyleLevel = TextStyleLevel.BODY, // Oletuksena body-teksti
    color: Color = MaterialTheme.colorScheme.onBackground // Oletusväri teemasta
) {
    // Määritetään erilaiset tekstityylit
    val textStyle = when (styleLevel) {
        TextStyleLevel.HEADLINE -> TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = 36.sp, // Otsikon koko
            fontWeight = FontWeight.Bold, // Otsikon vahvuus
            color = Color.White,
            textAlign = TextAlign.Center
        )
        TextStyleLevel.SUBHEADLINE -> TextStyle(
            fontSize = 28.sp, // Alaotsikon koko
            fontWeight = FontWeight.Medium, // Alaotsikon vahvuus
            color = Color.White,
            textAlign = TextAlign.Center
        )
        TextStyleLevel.BODY -> TextStyle(
            fontSize = 20.sp, // Normaalin leipätekstin koko
            fontWeight = FontWeight.SemiBold, // Normaalin leipätekstin vahvuus
            color = Color.Black
        )
        TextStyleLevel.CAPTION -> TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp, // Pienemmän tekstin koko
            fontWeight = FontWeight.SemiBold,// Pienemmän tekstin vahvuus
            color = color,
            textAlign = TextAlign.Center
        )
    }

    Text(
        text = text,
        style = textStyle

    )
}