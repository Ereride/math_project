package com.example.mathapp.ui.theme

import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.mathapp.R

// Värit
val LightPrimary = Color(0xFF6200EE)
val LightSecondary = Color(0xFF03DAC6)
val DarkPrimary = Color(0xFFBB86FC)
val DarkSecondary = Color(0xFF03DAC6)

val LightBackground = Color(0xFFFFFFFF)
val DarkBackground = Color(0xFF121212)

val BlodisanFontFamily = FontFamily(
    Font(R.font.blodisan)
)

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
    styleLevel: TextStyleLevel = TextStyleLevel.BODY,
    color: Color = MaterialTheme.colorScheme.onBackground
) {

    val textStyle = when (styleLevel) {
        TextStyleLevel.HEADLINE -> TextStyle(
            fontFamily = BlodisanFontFamily,
            fontSize = 44.sp,
            fontWeight = FontWeight.Normal,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.White,
                    Color.White,
                    Color.White,
                    Color(0xFFf2f0ef),
                    Color(0xFF780606)
                ),
                start = Offset(0f, 0f),
                end = Offset(0f, 160f)
            ),
            textAlign = TextAlign.Center
        )
        TextStyleLevel.SUBHEADLINE -> TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        TextStyleLevel.BODY -> TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        TextStyleLevel.CAPTION -> TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }

    Text(
        text = text,
        style = textStyle
    )
}
