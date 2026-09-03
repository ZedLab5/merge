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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.NoorDestination
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

// Custom Gradient Black Glassy & Fire Palette Constants
private val GlassDarkGradientStart = Color(0xFF1E252B)
private val GlassDarkGradientCenter = Color(0xFF14191D)
private val GlassDarkGradientEnd = Color(0xFF0C0F12)

private val FireOrange = Color(0xFFFF5722)
private val FireAmber = Color(0xFFF59E0B)
private val FireYellow = Color(0xFFFFCA28)
private val WarmCreamText = Color(0xFFFFF8E7)
private val MutedGlassText = Color(0xFF94A3B8)
private val GlassBorder = Color(0xFFFFFFFF).copy(alpha = 0.12f)
private val InnerGlassSurface = Color(0xFFFFFFFF).copy(alpha = 0.05f)

@Composable
fun UnifiedStreakHomeCard(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val streakData by viewModel.unifiedStreakData.collectAsStateWithLifecycle()
    val isArabic by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isLangArabic = isArabic.equals("Arabic", ignoreCase = true) || isArabic == "العربية"

    val completedDeeds = streakData.todayCompletedCount.coerceIn(0, 4)
    val percentage = ((completedDeeds / 4f) * 100).toInt()
    val progressFraction by animateFloatAsState(
        targetValue = (completedDeeds / 4f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600),
        label = "streakProgressFraction"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .clickable {
                viewModel.navigateTo(NoorDestination.STREAKS)
            }
            .testTag("unified_streak_home_card"),
        shape = RoundedCornerShape(26.dp),
        shadowElevation = 6.dp,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            GlassDarkGradientStart,
                            GlassDarkGradientCenter,
                            GlassDarkGradientEnd
                        )
                    )
                )
        ) {
            // Ambient Glassy Auras (Subtle Fire Glow at Top Left & Soft Frost at Bottom Right)
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopStart)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                FireOrange.copy(alpha = 0.12f),
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
                                Color(0xFF38BDF8).copy(alpha = 0.06f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                // =========================================================================
                // 1. HERO HEADER: Left (Fire Icon & Streak Title) + Right (% Progress Circle)
                // =========================================================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LEFT SIDE: Fire Icon (without circle) + Titles & Status
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp)
                    ) {
                        // Title Row with raw Fire Icon
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Raw Fire Icon with authentic flame color
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = "Fire Streak",
                                tint = FireOrange,
                                modifier = Modifier.size(24.dp)
                            )

                            Text(
                                text = if (isLangArabic) "سلسلة المواظبة" else "Devotion Streak",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    letterSpacing = 0.2.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Sub-headline: Streak continuous chain
                        Text(
                            text = if (streakData.currentStreak > 0) {
                                if (isLangArabic) "مواظبة متواصلة لمدة ${streakData.currentStreak} أيام" else "${streakData.currentStreak}-day continuous chain"
                            } else {
                                if (isLangArabic) "ابدأ مسيرة المواظبة اليوم" else "Start your daily devotion chain"
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = FireYellow,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Status Badge: Translucent Glass Pill
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (streakData.isTodayAnyCompleted) FireAmber.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.08f),
                            border = BorderStroke(
                                1.dp,
                                if (streakData.isTodayAnyCompleted) FireAmber.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.15f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = if (streakData.isTodayAnyCompleted) {
                                        if (isLangArabic) "نشط اليوم ✓" else "Active Today ✓"
                                    } else {
                                        if (isLangArabic) "بانتظار الإنجاز" else "Pending Today"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (streakData.isTodayAnyCompleted) WarmCreamText else MutedGlassText,
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
                            // Outer Track: Translucent white glass ring
                            drawCircle(
                                color = Color.White.copy(alpha = 0.12f),
                                style = Stroke(width = strokeWidth)
                            )
                            // Active Progress Arc: Glowing Fire Gradient Arc starting at 0%
                            if (progressFraction > 0f) {
                                drawArc(
                                    brush = Brush.sweepGradient(
                                        listOf(
                                            FireOrange,
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
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    lineHeight = 18.sp
                                )
                            )
                            Text(
                                text = if (isLangArabic) "إنجاز" else "DONE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (percentage > 0) FireYellow else MutedGlassText,
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
                // 2. CURRENT WEEK CHAIN (Inside the Glassy Card)
                // =========================================================================
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = InnerGlassSurface,
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp)
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
                                    color = MutedGlassText,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.6.sp
                                )
                            )
                            Text(
                                text = "$completedDeeds/4 ${if (isLangArabic) "طاعات" else "deeds"}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (completedDeeds > 0) FireYellow else MutedGlassText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.5.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

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
                                                    jewel.isCompleted -> FireAmber.copy(alpha = 0.25f)
                                                    jewel.isToday -> Color.White.copy(alpha = 0.12f)
                                                    else -> Color.White.copy(alpha = 0.05f)
                                                }
                                            )
                                            .border(
                                                width = if (jewel.isToday) 1.5.dp else 1.dp,
                                                color = when {
                                                    jewel.isCompleted -> FireAmber
                                                    jewel.isToday -> FireYellow
                                                    else -> Color.White.copy(alpha = 0.1f)
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
                                                    .background(FireYellow)
                                            )
                                        }
                                    }

                                    Text(
                                        text = jewel.label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (jewel.isToday) FontWeight.Bold else FontWeight.Medium,
                                            color = if (jewel.isToday) FireYellow else MutedGlassText,
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
                        color = MutedGlassText,
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
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.06f))
                        .clickable { viewModel.navigateTo(NoorDestination.STREAKS) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isLangArabic) "عرض تفاصيل السلسلة والأوسمة" else "View Full Streaks & Analytics",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = WarmCreamText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (isLangArabic) "فتح الصفحة" else "Open",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = FireYellow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Navigate to Streaks",
                            tint = FireYellow,
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

