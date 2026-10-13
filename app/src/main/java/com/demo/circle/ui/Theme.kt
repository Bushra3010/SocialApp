package com.demo.circle.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Brand = Color(0xFFE1306C)
val BrandPurple = Color(0xFF7B2FF7)
val BrandOrange = Color(0xFFFF7A18)
val Muted = Color(0xFF8E8E93)
val Divider = Color(0xFFEDEDED)
val Surface2 = Color(0xFFF5F5F7)

val BrandGradient = Brush.linearGradient(listOf(BrandOrange, Brand, BrandPurple))

@Composable
fun CircleTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Brand,
            secondary = BrandPurple,
            background = Color.White,
            surface = Color.White,
            onSurface = Color(0xFF111111),
        ),
        content = content,
    )
}
