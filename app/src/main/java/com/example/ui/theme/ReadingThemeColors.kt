package com.example.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Shared theme color tokens for reading-focused screens (Quran, Duas, Azkar, Tasbih).
 */
data class ReadingThemeColors(
    val background: Color,
    val surface: Color,
    val border: Color,
    val arabicText: Color,
    val translationText: Color,
    val transliterationText: Color,
    val accent: Color,
    val name: String,
    val isDark: Boolean = false
)

typealias QuranReadingThemeColors = ReadingThemeColors

object ReadingThemes {
    val MadaniCrisp = ReadingThemeColors(
        background = CanvasMint,
        surface = SurfaceWhite,
        border = BorderTealGray,
        arabicText = DarkPine,
        translationText = Color(0xFF334E4A),
        transliterationText = DeepVibrantTeal,
        accent = DeepVibrantTeal,
        name = "Madani Crisp",
        isDark = false
    )

    val SepiaParchment = ReadingThemeColors(
        background = Color(0xFFF9F4E8),
        surface = Color(0xFFFFFDF5),
        border = Color(0xFFE8DCC2),
        arabicText = Color(0xFF2C221E),
        translationText = Color(0xFF5C4F48),
        transliterationText = Color(0xFF8C7355),
        accent = Color(0xFFB57E1A),
        name = "Sepia Parchment",
        isDark = false
    )

    val ObsidianNight = ReadingThemeColors(
        background = Color(0xFF0F1418),
        surface = Color(0xFF182026),
        border = Color(0xFF26333C),
        arabicText = Color(0xFFE2E8F0),
        translationText = Color(0xFF94A3B8),
        transliterationText = Color(0xFF38BDF8),
        accent = Color(0xFF10B981),
        name = "Obsidian Night",
        isDark = true
    )

    val allThemes = listOf(MadaniCrisp, SepiaParchment, ObsidianNight)

    fun fromColorScheme(
        colorScheme: androidx.compose.material3.ColorScheme,
        isDark: Boolean
    ): ReadingThemeColors {
        return ReadingThemeColors(
            background = colorScheme.background,
            surface = colorScheme.surface,
            border = colorScheme.outline,
            arabicText = colorScheme.onSurface,
            translationText = colorScheme.onSurfaceVariant,
            transliterationText = colorScheme.primary,
            accent = colorScheme.primary,
            name = if (isDark) "Obsidian Night" else "Madani Crisp",
            isDark = isDark
        )
    }

    fun getThemeByName(name: String): ReadingThemeColors {
        return when (name) {
            "Sepia Parchment" -> SepiaParchment
            "Obsidian Night" -> ObsidianNight
            else -> MadaniCrisp
        }
    }
}

/**
 * Shared horizontal row for selecting reading canvas themes.
 */
@Composable
fun ReadingThemePickerRow(
    selectedThemeName: String,
    onThemeSelect: (String) -> Unit,
    activeTheme: ReadingThemeColors,
    modifier: Modifier = Modifier
) {
    val themes = listOf(
        "Madani Crisp" to Color(0xFFF2FBF9),
        "Obsidian Night" to Color(0xFF0F1418)
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        themes.forEach { (name, color) ->
            val isSelected = selectedThemeName == name
            val isObsidian = name == "Obsidian Night"

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onThemeSelect(name) },
                shape = RoundedCornerShape(10.dp),
                color = color,
                border = BorderStroke(
                    if (isSelected) 2.dp else 1.dp,
                    if (isSelected) activeTheme.accent else activeTheme.border.copy(alpha = 0.5f)
                ),
                shadowElevation = if (isSelected) 2.dp else 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.split(" ").first(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isObsidian) Color.White else if (isSelected) activeTheme.accent else DarkPine
                        ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Full Section Component with Title, Subtitle, and Picker Row.
 */
@Composable
fun ReadingThemeSection(
    selectedThemeName: String,
    onThemeSelect: (String) -> Unit,
    activeTheme: ReadingThemeColors,
    modifier: Modifier = Modifier,
    title: String = "Reading Canvas Theme",
    subtitle: String = "Choose a palette tailored for comfort, night-time reading, or daytime clarity"
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = activeTheme.arabicText
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = activeTheme.translationText,
                    fontSize = 12.sp
                )
            )
        }

        ReadingThemePickerRow(
            selectedThemeName = selectedThemeName,
            onThemeSelect = onThemeSelect,
            activeTheme = activeTheme
        )
    }
}
