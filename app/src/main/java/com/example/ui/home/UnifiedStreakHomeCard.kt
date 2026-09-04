package com.example.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.NoorDestination
import com.example.ui.theme.BorderTealGray
import com.example.ui.theme.BorderTealLight
import com.example.ui.theme.CanvasMint
import com.example.ui.theme.DarkPine
import com.example.ui.theme.DeepVibrantTeal
import com.example.ui.theme.MetallicGold
import com.example.ui.theme.ReadingThemeColors
import com.example.ui.theme.ReadingThemes
import com.example.ui.theme.SlateTealMuted
import com.example.ui.theme.SoftTealTint
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

// Fire accent colors (used for active flame highlights across both light and dark themes)
private val FireOrange = Color(0xFFFF5722)
private val FireAmber = Color(0xFFF59E0B)
private val FireYellow = Color(0xFFFFCA28)

@Composable
fun UnifiedStreakHomeCard(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    customThemeColors: ReadingThemeColors? = null
) {
    val streakData by viewModel.unifiedStreakData.collectAsStateWithLifecycle()
    val isArabic by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isLangArabic = isArabic.equals("Arabic", ignoreCase = true) || isArabic == "العربية"

    val readingThemeName by viewModel.sharedReadingTheme.collectAsStateWithLifecycle()
    val themeColors = customThemeColors ?: remember(readingThemeName) { ReadingThemes.getThemeByName(readingThemeName) }
    val isDark = themeColors.isDark

    val completedDeeds = streakData.todayCompletedCount.coerceIn(0, 4)
    val percentage = ((completedDeeds / 4f) * 100).toInt()
    val progressFraction by animateFloatAsState(
        targetValue = (completedDeeds / 4f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600),
        label = "streakProgressFraction"
    )

    // Dynamic Theme Tokens
    val cardBackground = themeColors.surface
    val cardBorder = if (isDark) themeColors.border else BorderTealGray
    val titleColor = if (isDark) themeColors.arabicText else DarkPine
    val subtitleColor = if (isDark) FireAmber else DeepVibrantTeal
    val descriptionColor = if (isDark) themeColors.translationText else SlateTealMuted
    val innerSurfaceColor = if (isDark) themeColors.background else Color(0xFFF7FAF9)
    val innerBorderColor = if (isDark) themeColors.border else Color(0xFFDFEBE5)
    val trackColor = if (isDark) themeColors.border else Color(0xFFE2EBE6)
    val ctaBackground = if (isDark) themeColors.border.copy(alpha = 0.35f) else SoftTealTint
    val ctaBorder = if (isDark) themeColors.border else BorderTealLight
    val ctaTextColor = if (isDark) themeColors.arabicText else DarkPine
    val ctaActionColor = if (isDark) themeColors.accent else DeepVibrantTeal

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                viewModel.navigateTo(NoorDestination.STREAKS)
            }
            .testTag("unified_streak_home_card"),
        shape = RoundedCornerShape(24.dp),
        color = cardBackground,
        shadowElevation = 0.dp,
        border = BorderStroke(1.2.dp, cardBorder)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Subtle ambient warm/teal glow aura matching the theme
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.TopStart)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                (if (isDark) FireOrange.copy(alpha = 0.08f) else FireOrange.copy(alpha = 0.05f)),
                                Color.Transparent
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.BottomEnd)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                (if (isDark) themeColors.accent.copy(alpha = 0.06f) else DeepVibrantTeal.copy(alpha = 0.04f)),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 18.dp)
            ) {
                // =========================================================================
                // 1. HERO HEADER: Left (Fire Icon & Streak Title) + Right (% Progress Gauge)
                // =========================================================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LEFT SIDE: Fire Icon + Titles & Status
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp)
                    ) {
                        // Title Row with Fire Icon
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isDark) themeColors.background else Color(0xFFFFF4EB))
                                    .border(
                                        1.dp,
                                        if (isDark) themeColors.border else Color(0xFFFFD8BF),
                                        RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocalFireDepartment,
                                    contentDescription = "Fire Streak",
                                    tint = FireOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Text(
                                text = if (isLangArabic) "سلسلة المواظبة" else "Devotion Streak",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = titleColor,
                                    fontSize = 17.sp,
                                    letterSpacing = 0.1.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        // Sub-headline: Streak continuous chain
                        Text(
                            text = if (streakData.currentStreak > 0) {
                                if (isLangArabic) "مواظبة متواصلة لمدة ${streakData.currentStreak} أيام" else "${streakData.currentStreak}-day continuous chain"
                            } else {
                                if (isLangArabic) "ابدأ مسيرة المواظبة اليوم" else "Start your daily devotion chain"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = subtitleColor,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Spacer(modifier = Modifier.height(7.dp))

                        // Status Badge: Clean Theme Pill
                        val isAnyCompleted = streakData.isTodayAnyCompleted
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when {
                                isDark && isAnyCompleted -> themeColors.accent.copy(alpha = 0.15f)
                                isDark -> themeColors.border.copy(alpha = 0.4f)
                                isAnyCompleted -> SoftTealTint
                                else -> Color(0xFFF1F5F4)
                            },
                            border = BorderStroke(
                                1.dp,
                                when {
                                    isDark && isAnyCompleted -> themeColors.accent.copy(alpha = 0.4f)
                                    isDark -> themeColors.border
                                    isAnyCompleted -> DeepVibrantTeal.copy(alpha = 0.35f)
                                    else -> Color(0xFFDFEBE5)
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isAnyCompleted) {
                                                if (isDark) themeColors.accent else DeepVibrantTeal
                                            } else {
                                                if (isDark) themeColors.translationText else SlateTealMuted
                                            }
                                        )
                                )
                                Text(
                                    text = if (isAnyCompleted) {
                                        if (isLangArabic) "نشط اليوم ✓" else "Active Today ✓"
                                    } else {
                                        if (isLangArabic) "بانتظار الإنجاز" else "Pending Today"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isAnyCompleted) {
                                            if (isDark) themeColors.arabicText else DeepVibrantTeal
                                        } else {
                                            if (isDark) themeColors.translationText else SlateTealMuted
                                        },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    // RIGHT SIDE: Circular Progress Gauge showing % of tasks done (0% to 100%)
                    Box(
                        modifier = Modifier.size(72.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(72.dp)) {
                            val strokeWidth = 5.5.dp.toPx()
                            // Outer Track
                            drawCircle(
                                color = trackColor,
                                style = Stroke(width = strokeWidth)
                            )
                            // Active Progress Arc
                            if (progressFraction > 0f) {
                                drawArc(
                                    brush = Brush.sweepGradient(
                                        listOf(
                                            FireOrange,
                                            FireAmber,
                                            FireYellow,
                                            FireOrange
                                        )
                                    ),
                                    startAngle = -90f,
                                    sweepAngle = 360f * progressFraction,
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }
                        }

                        // Center Display: Percentage (%) starting from 0%
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "$percentage%",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = titleColor,
                                    fontSize = 16.5.sp,
                                    lineHeight = 18.sp
                                )
                            )
                            Text(
                                text = if (isLangArabic) "إنجاز" else "DONE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (percentage > 0) FireAmber else descriptionColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 8.5.sp,
                                    letterSpacing = 0.6.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // =========================================================================
                // 2. CURRENT WEEK CHAIN (Theme-Adaptive Inner Card)
                // =========================================================================
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = innerSurfaceColor,
                    border = BorderStroke(1.dp, innerBorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 13.dp, vertical = 11.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isLangArabic) "مسار الأسبوع الحالي" else "CURRENT WEEK CHAIN",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = descriptionColor,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "$completedDeeds/4 ${if (isLangArabic) "طاعات" else "deeds"}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (completedDeeds > 0) subtitleColor else descriptionColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.5.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 7 Day Status Indicators (M T W T F S S)
                        val today = LocalDate.now()
                        val currentDayOfWeek = today.dayOfWeek.value // 1 (Mon) .. 7 (Sun)
                        val daysOfWeek = (1..7).map { dayNum ->
                            val date = today.minusDays((currentDayOfWeek - dayNum).toLong())
                            val isToday = dayNum == currentDayOfWeek
                            val isPast = dayNum < currentDayOfWeek
                            val isFuture = dayNum > currentDayOfWeek
                            val dayLabel = date.dayOfWeek.getDisplayName(
                                TextStyle.NARROW,
                                if (isLangArabic) Locale("ar") else Locale.ENGLISH
                            )

                            val isCompleted = if (isToday) {
                                streakData.isTodayAnyCompleted
                            } else if (isPast) {
                                val daysAgo = currentDayOfWeek - dayNum
                                daysAgo < streakData.currentStreak
                            } else false

                            DayJewelInfo(
                                label = dayLabel,
                                isToday = isToday,
                                isCompleted = isCompleted,
                                isFuture = isFuture
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            daysOfWeek.forEach { jewel ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    jewel.isCompleted -> if (isDark) FireAmber.copy(alpha = 0.2f) else Color(0xFFFFF4EB)
                                                    jewel.isToday -> themeColors.surface
                                                    else -> themeColors.surface.copy(alpha = 0.5f)
                                                }
                                            )
                                            .border(
                                                width = if (jewel.isToday) 1.5.dp else 1.dp,
                                                color = when {
                                                    jewel.isCompleted -> if (isDark) FireAmber else Color(0xFFFFD8BF)
                                                    jewel.isToday -> if (isDark) themeColors.accent else DeepVibrantTeal
                                                    else -> if (isDark) themeColors.border else Color(0xFFE2EBE6)
                                                },
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (jewel.isCompleted) {
                                            Icon(
                                                imageVector = Icons.Filled.LocalFireDepartment,
                                                contentDescription = "Completed",
                                                tint = FireOrange,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        } else if (jewel.isToday) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isDark) themeColors.accent else DeepVibrantTeal)
                                            )
                                        }
                                    }

                                    Text(
                                        text = jewel.label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (jewel.isToday) FontWeight.Bold else FontWeight.Medium,
                                            color = if (jewel.isToday) titleColor else descriptionColor,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // =========================================================================
                // 3. EXPLANATION TEXT & LINK TO DEDICATED STREAKS PAGE
                // =========================================================================
                Text(
                    text = if (isLangArabic) {
                        "أنجز طاعاتك اليومية الأربع (الصلاة، القرآن، الأذكار، والتسبيح) للحفاظ على شعلة المواظبة وتنمية أثرك الروحي."
                    } else {
                        "Complete your 4 daily devotions (Salat, Quran, Azkar, Tasbih) to keep your spiritual streak flame alive."
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = descriptionColor,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Redirect Link to Dedicated Streaks Page
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ctaBackground)
                        .border(1.dp, ctaBorder, RoundedCornerShape(12.dp))
                        .clickable { viewModel.navigateTo(NoorDestination.STREAKS) }
                        .padding(horizontal = 12.dp, vertical = 9.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isLangArabic) "عرض تفاصيل السلسلة والأوسمة" else "View Full Streaks & Analytics",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = ctaTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (isLangArabic) "فتح الصفحة" else "Open",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ctaActionColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Navigate to Streaks",
                            tint = ctaActionColor,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

private data class DayJewelInfo(
    val label: String,
    val isToday: Boolean,
    val isCompleted: Boolean,
    val isFuture: Boolean
)


