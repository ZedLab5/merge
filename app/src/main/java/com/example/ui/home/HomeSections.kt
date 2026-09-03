package com.example.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.localization.tr
import com.example.data.model.PrayerTime
import com.example.data.model.QuickAccessTool
import com.example.data.quran.DuaData
import com.example.data.quran.KhatmaEngine
import com.example.data.quran.KhatmaPaceStatus
import com.example.data.quran.QuranData
import com.example.ui.MainViewModel
import com.example.ui.NoorDestination
import com.example.ui.SalatTab
import com.example.ui.components.AudioWaveformIndicator
import com.example.ui.quran.AmiriQuranFontFamily
import com.example.ui.theme.BorderTealGray
import com.example.ui.theme.BorderTealLight
import com.example.ui.theme.DarkPine
import com.example.ui.theme.DeepVibrantTeal
import com.example.ui.theme.GoldBadgeBg
import com.example.ui.theme.MetallicGold
import com.example.ui.theme.PrimaryTealGradient
import com.example.ui.theme.SlateTealMuted
import com.example.ui.theme.SoftTealTint
import com.example.ui.theme.SurfaceWhite

// Accent Colors: Exact Gradient between 099382 and 13A795 & Tasteful Gold Highlights
private val NoorTealStart = Color(0xFF099382)
private val NoorTealEnd = Color(0xFF13A795)
val NoorAccentGradient = Brush.linearGradient(listOf(NoorTealStart, NoorTealEnd))

// Prayer Tracker Diagonal Gradient (Top-Left to Bottom-Right, 3 color stops)
val PrayerTrackerDiagonalGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF0FA895), // Bright teal-green start
        Color(0xFF14C4A8), // Lighter teal-green middle
        Color(0xFF0FA895)  // Bright teal-green end (subtle sheen effect)
    ),
    start = Offset.Zero,
    end = Offset.Infinite
)

// Subtle Warm Islamic Gold / Yellow Accents (Tasteful touches)
val NoorGoldAccent = Color(0xFFD4A340)
val NoorGoldLight = Color(0xFFE8BA5A)
val NoorGoldSoft = Color(0xFFFAF3E6)
val NoorGoldBorder = Color(0xFFE8D4A8)
val NoorGoldGradient = Brush.linearGradient(listOf(Color(0xFFE5B958), Color(0xFFC8932A)))
val NoorBorderGradient = Brush.linearGradient(listOf(NoorTealStart, NoorGoldAccent, NoorTealEnd))

private val NoorDarkPine = Color(0xFF10261F)
private val NoorSageSlate = Color(0xFF5A756C)
private val NoorCardBorder = Color(0xFFE2EBE6)
private val NoorSurfaceSoft = Color(0xFFF6FAF8)
private val NoorNightCanopy = Color(0xFF091F19)
private val NoorNightCanopyMid = Color(0xFF103328)

// Soft green palette for refined, consistent spiritual cards
val NoorSoftGreenBorder = Color(0xFFCCE4DC)
val NoorSoftGreenBg = Color(0xFFF2F8F5)
val NoorSoftGreenBadgeBg = Color(0xFFE0F3ED)

// ============================================================
// REUSABLE UNIFIED CARD STRUCTURE: (ICON + HEADING + SUBHEADING)
// ============================================================

@Composable
fun NoorSectionContainer(
    icon: ImageVector? = null,
    customIcon: (@Composable () -> Unit)? = null,
    title: String,
    subtitle: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    containerShape: RoundedCornerShape = RoundedCornerShape(24.dp),
    backgroundBrush: Brush? = null,
    backgroundColor: Color = Color.White,
    borderBrush: Brush? = null,
    borderWidth: Dp = 0.dp,
    hasBorder: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(18.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(containerShape)
            .then(
                if (backgroundBrush != null) {
                    Modifier.background(backgroundBrush)
                } else {
                    Modifier.background(backgroundColor)
                }
            )
            .then(
                if (hasBorder) {
                    if (borderBrush != null) {
                        Modifier.border(if (borderWidth.value > 0f) borderWidth else 1.dp, borderBrush, containerShape)
                    } else {
                        Modifier.border(if (borderWidth.value > 0f) borderWidth else 1.dp, NoorSoftGreenBorder, containerShape)
                    }
                } else {
                    Modifier
                }
            )
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Standard Header: Leading Badge + Title & Subtitle + Optional Trailing Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f, fill = false),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Leading Icon Badge with clean minimalist styling matching settings
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .background(NoorSoftGreenBg),
                            contentAlignment = Alignment.Center
                        ) {
                            if (customIcon != null) {
                                customIcon()
                            } else if (icon != null) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = NoorTealStart,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NoorDarkPine,
                                    letterSpacing = (-0.2).sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = NoorSageSlate
                                )
                            )
                        }
                    }

                    if (actionLabel != null) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NoorGoldSoft,
                            modifier = Modifier.clickable { onActionClick?.invoke() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = actionLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NoorGoldAccent
                                    )
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = NoorGoldAccent,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Body Content
                content()
            }
    }
}

// ============================================================
// CUSTOM MINIMAL ISLAMIC VECTOR ICONS (Clean & Refined)
// ============================================================

@Composable
fun IslamicIconAzkar(
    modifier: Modifier = Modifier,
    tint: Color = NoorTealStart
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h * 0.42f
        val r = w * 0.30f
        val strokeW = 1.6.dp.toPx()

        // Rosary Bead Loop
        val beadCount = 8
        for (i in 0 until beadCount) {
            val angle = Math.toRadians((i * 360.0 / beadCount) - 90.0)
            val bx = (cx + r * Math.cos(angle)).toFloat()
            val by = (cy + r * Math.sin(angle)).toFloat()
            drawCircle(color = tint, radius = 2.0.dp.toPx(), center = Offset(bx, by))
        }

        // Hanging Minaret Tassel
        drawLine(tint, Offset(cx, cy + r), Offset(cx, cy + r + h * 0.22f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawCircle(color = NoorGoldAccent, radius = 1.8.dp.toPx(), center = Offset(cx, cy + r + h * 0.26f))
    }
}

@Composable
fun IslamicIconQuranAudio(
    modifier: Modifier = Modifier,
    tint: Color = NoorTealStart
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = 1.6.dp.toPx()

        // Headphone Arc
        val arcTop = h * 0.18f
        val arcBottom = h * 0.62f
        val leftX = w * 0.22f
        val rightX = w * 0.78f

        val path = Path().apply {
            moveTo(leftX, arcBottom)
            cubicTo(leftX, arcTop, rightX, arcTop, rightX, arcBottom)
        }
        drawPath(path, color = tint, style = Stroke(width = strokeW, cap = StrokeCap.Round))

        // Left & Right Minimal Ear Cushions
        drawRoundRect(
            color = tint,
            topLeft = Offset(leftX - 2.5.dp.toPx(), arcBottom - 2.dp.toPx()),
            size = Size(5.dp.toPx(), 9.dp.toPx()),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
        )
        drawRoundRect(
            color = tint,
            topLeft = Offset(rightX - 2.5.dp.toPx(), arcBottom - 2.dp.toPx()),
            size = Size(5.dp.toPx(), 9.dp.toPx()),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
        )

        // Center Minimal Wave Bars
        val cx = w / 2f
        drawLine(tint, Offset(cx - 3.dp.toPx(), h * 0.52f), Offset(cx - 3.dp.toPx(), h * 0.76f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawLine(NoorGoldAccent, Offset(cx, h * 0.44f), Offset(cx, h * 0.84f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawLine(tint, Offset(cx + 3.dp.toPx(), h * 0.52f), Offset(cx + 3.dp.toPx(), h * 0.76f), strokeWidth = strokeW, cap = StrokeCap.Round)
    }
}

@Composable
fun IslamicIconTasbeeh(
    modifier: Modifier = Modifier,
    tint: Color = NoorTealStart
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val strokeW = 1.6.dp.toPx()

        // Outer Ring
        drawCircle(
            color = tint,
            radius = w * 0.34f,
            center = Offset(cx, cy),
            style = Stroke(width = strokeW)
        )

        // Inner Counter Dial & Notch
        drawCircle(
            color = tint.copy(alpha = 0.15f),
            radius = w * 0.18f,
            center = Offset(cx, cy),
            style = Fill
        )
        drawCircle(
            color = NoorGoldAccent,
            radius = 2.dp.toPx(),
            center = Offset(cx, cy)
        )

        // Top Clicker Button
        drawLine(
            color = NoorGoldAccent,
            start = Offset(cx, h * 0.08f),
            end = Offset(cx, h * 0.16f),
            strokeWidth = strokeW * 1.2f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun IslamicIconDua(
    modifier: Modifier = Modifier,
    tint: Color = NoorTealStart
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = 1.6.dp.toPx()

        // Left Hand
        val leftHand = Path().apply {
            moveTo(w * 0.46f, h * 0.80f)
            lineTo(w * 0.22f, h * 0.65f)
            cubicTo(w * 0.16f, h * 0.46f, w * 0.26f, h * 0.26f, w * 0.44f, h * 0.24f)
            lineTo(w * 0.46f, h * 0.80f)
        }
        drawPath(leftHand, color = tint, style = Stroke(width = strokeW, cap = StrokeCap.Round))

        // Right Hand
        val rightHand = Path().apply {
            moveTo(w * 0.54f, h * 0.80f)
            lineTo(w * 0.78f, h * 0.65f)
            cubicTo(w * 0.84f, h * 0.46f, w * 0.74f, h * 0.26f, w * 0.56f, h * 0.24f)
            lineTo(w * 0.54f, h * 0.80f)
        }
        drawPath(rightHand, color = tint, style = Stroke(width = strokeW, cap = StrokeCap.Round))
    }
}

@Composable
fun IslamicIconTask(
    modifier: Modifier = Modifier,
    tint: Color = NoorGoldAccent
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val radius = w * 0.36f
        val path = Path()

        // 8-Point Islamic Star
        val points = 16
        for (i in 0 until points) {
            val r = if (i % 2 == 0) radius else radius * 0.65f
            val angle = Math.toRadians((i * 360.0 / points) - 90.0)
            val x = (cx + r * Math.cos(angle)).toFloat()
            val y = (cy + r * Math.sin(angle)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        drawPath(path, color = tint, style = Stroke(width = 1.6.dp.toPx()))

        // Inner Check Mark
        val checkPath = Path().apply {
            moveTo(w * 0.36f, cy)
            lineTo(w * 0.47f, cy + 3.dp.toPx())
            lineTo(w * 0.64f, cy - 3.5.dp.toPx())
        }
        drawPath(checkPath, color = tint, style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round))
    }
}

@Composable
fun IslamicIconSalat(
    modifier: Modifier = Modifier,
    tint: Color = NoorTealStart
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = 1.6.dp.toPx()

        // Mihrab Arch
        val path = Path().apply {
            moveTo(w * 0.24f, h * 0.84f)
            lineTo(w * 0.24f, h * 0.46f)
            cubicTo(w * 0.24f, h * 0.24f, w * 0.5f, h * 0.16f, w * 0.5f, h * 0.14f)
            cubicTo(w * 0.5f, h * 0.16f, w * 0.76f, h * 0.24f, w * 0.76f, h * 0.46f)
            lineTo(w * 0.76f, h * 0.84f)
        }
        drawPath(path, color = tint, style = Stroke(width = strokeW, cap = StrokeCap.Round))

        // Floor Base & Mihrab Lamp
        drawLine(tint, Offset(w * 0.16f, h * 0.84f), Offset(w * 0.84f, h * 0.84f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawCircle(color = NoorGoldAccent, radius = 2.dp.toPx(), center = Offset(w * 0.5f, h * 0.44f))
        drawLine(tint, Offset(w * 0.5f, h * 0.24f), Offset(w * 0.5f, h * 0.42f), strokeWidth = 1.2.dp.toPx())
    }
}

@Composable
fun IslamicIconQibla(
    modifier: Modifier = Modifier,
    tint: Color = NoorTealStart
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val strokeW = 1.6.dp.toPx()

        // Outer Compass Ring
        drawCircle(
            color = tint,
            radius = w * 0.34f,
            center = Offset(cx, cy),
            style = Stroke(width = strokeW)
        )

        // North Pointer with Gold Tip
        val needlePath = Path().apply {
            moveTo(cx, h * 0.20f)
            lineTo(cx - 3.5.dp.toPx(), cy + 2.dp.toPx())
            lineTo(cx, cy)
            lineTo(cx + 3.5.dp.toPx(), cy + 2.dp.toPx())
            close()
        }
        drawPath(needlePath, color = NoorGoldAccent)

        // Kaaba Cube Base
        drawRect(
            color = tint,
            topLeft = Offset(cx - 3.5.dp.toPx(), cy + 3.dp.toPx()),
            size = Size(7.dp.toPx(), 6.5.dp.toPx())
        )
    }
}

@Composable
fun IslamicIconMushaf(
    modifier: Modifier = Modifier,
    tint: Color = NoorTealStart
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val strokeW = 1.6.dp.toPx()

        // Open Holy Book (Mushaf)
        val leftPage = Path().apply {
            moveTo(cx, h * 0.50f)
            cubicTo(w * 0.36f, h * 0.46f, w * 0.22f, h * 0.36f, w * 0.18f, h * 0.32f)
            lineTo(w * 0.18f, h * 0.66f)
            cubicTo(w * 0.22f, h * 0.70f, w * 0.36f, h * 0.80f, cx, h * 0.84f)
        }
        drawPath(leftPage, color = tint, style = Stroke(width = strokeW, cap = StrokeCap.Round))

        val rightPage = Path().apply {
            moveTo(cx, h * 0.50f)
            cubicTo(w * 0.64f, h * 0.46f, w * 0.78f, h * 0.36f, w * 0.82f, h * 0.32f)
            lineTo(w * 0.82f, h * 0.66f)
            cubicTo(w * 0.78f, h * 0.70f, w * 0.64f, h * 0.80f, cx, h * 0.84f)
        }
        drawPath(rightPage, color = tint, style = Stroke(width = strokeW, cap = StrokeCap.Round))

        // Center Spine & Gold Ribbon
        drawLine(tint, Offset(cx, h * 0.50f), Offset(cx, h * 0.84f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawLine(NoorGoldAccent, Offset(cx, h * 0.50f), Offset(cx + 2.dp.toPx(), h * 0.92f), strokeWidth = 1.4.dp.toPx(), cap = StrokeCap.Round)
    }
}

// ============================================================
// 1. STANDALONE ATMOSPHERIC MOSQUE HERO CARD (Luxury Islamic Aesthetic)
// ============================================================

// ============================================================
// 1. STANDALONE USER PROFILE ROW & HERO PRAYER CARD
// ============================================================

@Composable
fun UserProfileRow(
    viewModel: MainViewModel,
    userName: String,
    location: String,
    modifier: Modifier = Modifier
) {
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isArabic = appLanguage.equals("Arabic", ignoreCase = true) ||
            appLanguage == "العربية" ||
            appLanguage.startsWith("ar", ignoreCase = true)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable { viewModel.navigateTo(NoorDestination.PROFILE) }
                .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.5.dp, NoorGoldAccent, CircleShape)
                    .padding(2.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_user_avatar),
                    contentDescription = tr("home_user_avatar", viewModel),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Column {
                Text(
                    text = tr("home_header_greeting", viewModel),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = NoorGoldAccent
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = if (userName.isBlank() || userName == "Guest Mode") tr("home_header_guest", viewModel) else userName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = NoorDarkPine
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = NoorGoldAccent,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = NoorTealStart,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = location.ifBlank { tr("home_header_default_location", viewModel) },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = NoorSageSlate
                        )
                    )
                }
            }
        }

        // Two small circular icon buttons on the right (Customize, Settings)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { viewModel.openCustomizeHomeSheet() },
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, NoorSoftGreenBorder)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.DashboardCustomize,
                        contentDescription = if (isArabic) "تخصيص" else "Customize",
                        tint = NoorTealStart,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { viewModel.openSettingsModal() },
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, NoorSoftGreenBorder)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = if (isArabic) "الإعدادات" else "Settings",
                        tint = NoorTealStart,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HeroPrayerCard(
    viewModel: MainViewModel,
    nextPrayerName: String,
    nextPrayerTime: String,
    prayers: List<PrayerTime> = emptyList(),
    modifier: Modifier = Modifier
) {
    val countdown by viewModel.nextPrayerCountdown.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isArabic = appLanguage.equals("Arabic", ignoreCase = true) ||
            appLanguage == "العربية" ||
            appLanguage.startsWith("ar", ignoreCase = true)

    val heroShape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(
                elevation = 6.dp,
                shape = heroShape,
                spotColor = Color.Black.copy(alpha = 0.15f),
                ambientColor = Color.Black.copy(alpha = 0.1f)
            )
            .clip(heroShape)
            .background(Color(0xFF0D1B1E), shape = heroShape)
            .clickable { viewModel.navigateTo(NoorDestination.SALAT) }
    ) {
        // Background Mosque Image centered
        Image(
            painter = painterResource(id = R.drawable.img_pinterest_hero),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = BiasAlignment(0f, -0.15f),
            alpha = 1.0f,
            modifier = Modifier.matchParentSize()
        )

        // Soft black overlay on top of the image
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.45f))
        )

        // Deep rich vertical black gradient overlay for crisp text readability
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x66000000),
                            Color(0x99000000),
                            Color(0xF0000000)
                        )
                    )
                )
        )

        // Content: Spiritual Sanctuary Banner + Next Salat + Minimal Prayer Times Strip
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Creative Spiritual Sanctuary Top Reminder
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NoorGoldAccent,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = if (isArabic) "الواحة الروحية • السكينة واليقين" else "Spiritual Sanctuary • Serenity & Peace",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.9f),
                            letterSpacing = 0.5.sp
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(NoorGoldAccent)
                )
            }

            // Next Salat Primary Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(NoorGoldAccent)
                        )
                        Text(
                            text = tr("home_header_upcoming_salat", viewModel),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                color = NoorGoldAccent
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = nextPrayerName.ifBlank { tr("prayer_isha", viewModel) },
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = Color.White
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = nextPrayerTime.ifBlank { "05:36 pm" },
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            letterSpacing = (-0.4).sp,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val displayCountdown = if (countdown.contains("left", ignoreCase = true) || countdown.contains("متبقية", ignoreCase = true)) {
                        countdown
                    } else {
                        String.format(tr("home_header_time_left", viewModel), countdown)
                    }
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = Color(0x33099382),
                        border = BorderStroke(1.dp, NoorGoldAccent.copy(alpha = 0.7f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(NoorGoldAccent)
                            )
                            Text(
                                text = displayCountdown,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NoorGoldAccent
                                )
                            )
                        }
                    }
                }
            }

            // Minimalist / Premium Prayer Times Strip below Next Salat
            val displayPrayers = if (prayers.isNotEmpty()) {
                prayers
            } else {
                listOf(
                    PrayerTime("Fajr", "الفجر", "05:12 AM", 5, 12),
                    PrayerTime("Sunrise", "الشروق", "06:34 AM", 6, 34),
                    PrayerTime("Dhuhr", "الظهر", "01:15 PM", 13, 15),
                    PrayerTime("Asr", "العصر", "04:45 PM", 16, 45),
                    PrayerTime("Maghrib", "المغرب", "07:30 PM", 19, 30),
                    PrayerTime("Isha", "العشاء", "08:50 PM", 20, 50)
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0x28FFFFFF),
                border = BorderStroke(1.dp, Color(0x38FFFFFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    displayPrayers.forEach { prayer ->
                        val isNext = prayer.isNext || prayer.name.equals(nextPrayerName, ignoreCase = true)
                        val nameStr = if (isArabic) prayer.arabicName else prayer.name
                        val cleanTime = prayer.timeString.replace(" AM", "").replace(" PM", "").replace(" ص", "").replace(" م", "").trim()

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(11.dp))
                                .background(if (isNext) Color(0x40D4A340) else Color.Transparent)
                                .then(
                                    if (isNext) Modifier.border(1.dp, NoorGoldAccent.copy(alpha = 0.85f), RoundedCornerShape(11.dp))
                                    else Modifier
                                )
                                .padding(vertical = 6.dp, horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = nameStr,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        fontWeight = if (isNext) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isNext) NoorGoldAccent else Color.White.copy(alpha = 0.9f)
                                    ),
                                    maxLines = 1,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = cleanTime,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 13.sp,
                                        fontWeight = if (isNext) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isNext) Color.White else Color.White.copy(alpha = 0.95f)
                                    ),
                                    maxLines = 1,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================
// 2. MERGED SALAT TIMES & TRACKER CARD (Overlapping Hero Tray Layer)
// ============================================================

@Composable
fun ChronologicalPrayerTracker(
    viewModel: MainViewModel,
    prayers: List<PrayerTime>,
    modifier: Modifier = Modifier
) {
    val completedPrayers by viewModel.completedPrayers.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isArabic = appLanguage.equals("Arabic", ignoreCase = true) ||
            appLanguage == "العربية" ||
            appLanguage.startsWith("ar", ignoreCase = true)

    val infiniteTransition = rememberInfiniteTransition(label = "salat_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Outer Green Container
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF074E44),
                        Color(0xFF0C8A79),
                        Color(0xFF074E44)
                    )
                )
            )
            .border(1.dp, NoorSoftGreenBorder, RoundedCornerShape(20.dp))
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header Row with white text matching green background
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        IslamicIconSalat(modifier = Modifier.size(20.dp), tint = Color.White)
                    }

                    Column {
                        Text(
                            text = if (isArabic) "مواقيت وتتبع الصلاة" else "Prayer Times & Tracker",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isArabic) "سجّل صلواتك اليومية" else "Tap to record prayers",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = NoorGoldSoft,
                    modifier = Modifier.clickable { viewModel.navigateToSalat(SalatTab.TIMES) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (isArabic) "تذكيرات" else "Reminders",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NoorGoldAccent
                            )
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = NoorGoldAccent,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Inner White Container
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color(0xFFF8FAFC)
                                )
                            )
                        )
                        .padding(horizontal = 10.dp, vertical = 12.dp)
                ) {
                    // Interactive Prayer Timeline: Connected line with circular checkmark nodes
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Connecting timeline rail spanning across the node centers, perfectly centered
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(34.dp)
                                .padding(top = 8.dp)
                        ) {
                            val itemWidth = size.width / 5f
                            val startX = itemWidth / 2f
                            val endX = size.width - (itemWidth / 2f)
                            val centerY = 17.dp.toPx()
                            drawLine(
                                color = NoorSoftGreenBorder,
                                start = Offset(startX, centerY),
                                end = Offset(endX, centerY),
                                strokeWidth = 2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }

                        // 5 Daily Prayer Tracker Nodes Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            val prayerItems = listOf(
                                Pair(tr("prayer_fajr", viewModel), "Fajr"),
                                Pair(tr("prayer_dhuhr", viewModel), "Dhuhr"),
                                Pair(tr("prayer_asr", viewModel), "Asr"),
                                Pair(tr("prayer_maghrib", viewModel), "Maghrib"),
                                Pair(tr("prayer_isha", viewModel), "Isha")
                            )

                            prayerItems.forEachIndexed { index, (localizedName, keyName) ->
                                val matchingPrayer = prayers.find { it.name.equals(keyName, ignoreCase = true) }
                                val isChecked = matchingPrayer != null && completedPrayers.contains(matchingPrayer.name)
                                val isCurrent = matchingPrayer?.isCurrent == true
                                val isActionable = matchingPrayer != null && (matchingPrayer.isPast || matchingPrayer.isCurrent)

                                val timeText = matchingPrayer?.timeString ?: when (index) {
                                    0 -> "05:43"
                                    1 -> "12:45"
                                    2 -> "16:39"
                                    3 -> "19:15"
                                    4 -> "21:07"
                                    else -> "--:--"
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable {
                                            matchingPrayer?.let { viewModel.togglePrayerCompleted(it) }
                                        }
                                        .padding(vertical = 8.dp, horizontal = 2.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .scale(if (isCurrent && !isChecked) pulseScale else 1f)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    isChecked -> NoorTealStart
                                                    else -> Color.White
                                                }
                                            )
                                            .border(
                                                width = if (isChecked) 2.dp else if (isCurrent) 2.5.dp else 1.2.dp,
                                                color = when {
                                                    isChecked -> Color.White
                                                    isCurrent -> NoorTealStart
                                                    else -> NoorSoftGreenBorder
                                                },
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        when {
                                            isChecked -> {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = tr("home_completed", viewModel),
                                                    tint = Color.White,
                                                    modifier = Modifier.size(17.dp)
                                                )
                                            }
                                            isCurrent -> {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Active Salat",
                                                    tint = NoorTealStart,
                                                    modifier = Modifier.size(17.dp)
                                                )
                                            }
                                            isActionable -> {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Not Yet Completed",
                                                    tint = NoorSageSlate.copy(alpha = 0.5f),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            else -> {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Upcoming",
                                                    tint = NoorSageSlate.copy(alpha = 0.25f),
                                                    modifier = Modifier.size(15.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = localizedName,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 12.5.sp,
                                            fontWeight = if (isCurrent || isChecked) FontWeight.Bold else FontWeight.SemiBold,
                                            color = when {
                                                isCurrent -> NoorTealStart
                                                isChecked -> NoorTealStart
                                                else -> NoorDarkPine
                                            }
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = timeText,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.5.sp,
                                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isCurrent) NoorDarkPine else NoorSageSlate
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpiritualEssentialCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.dp, NoorSoftGreenBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NoorSoftGreenBg),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = NoorSageSlate,
                    modifier = Modifier.size(14.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = NoorDarkPine
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.5.sp,
                        color = NoorSageSlate
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ============================================================
// 2. QUICK ACCESS ESSENTIALS (Borderless 2x2 Grid Matching All Tools Style + Customization Link)
// ============================================================

fun navigateToQuickAccessTool(viewModel: MainViewModel, tool: QuickAccessTool) {
    when (tool) {
        QuickAccessTool.QURAN -> viewModel.navigateTo(NoorDestination.QURAN_SURAH_LIST)
        QuickAccessTool.SALAT -> viewModel.navigateToSalat(SalatTab.TIMES)
        QuickAccessTool.QIBLA -> viewModel.navigateTo(NoorDestination.QIBLA)
        QuickAccessTool.TASBIH -> viewModel.navigateTo(NoorDestination.TASBIH)
        QuickAccessTool.DUAS -> viewModel.navigateTo(NoorDestination.DUAS_LIBRARY)
        QuickAccessTool.KHATMA -> viewModel.navigateTo(NoorDestination.QURAN_KHATMA)
        QuickAccessTool.STREAKS -> viewModel.navigateTo(NoorDestination.STREAKS)
        QuickAccessTool.HABITS -> viewModel.navigateTo(NoorDestination.HABIT_TRACKER)
        QuickAccessTool.AUDIO -> viewModel.navigateTo(NoorDestination.QURAN_AUDIO_STREAM)
    }
}

@Composable
fun QuickAccessMiniCard(
    tool: QuickAccessTool,
    isArabic: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    slotIndex: Int = 0
) {
    val slotTier = QuickAccessColorSystem.getSlotTier(slotIndex)
    val title = if (isArabic) tool.titleAr else tool.titleEn
    val subtitle = if (isArabic) tool.subtitleAr else tool.subtitleEn

    Surface(
        modifier = modifier
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0x18000000),
                ambientColor = slotTier.stripeColor.copy(alpha = 0.12f)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, NoorCardBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFFFAFCFA)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Soft 3D layered icon box matching customize color system
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(slotTier.iconBackground),
                    contentAlignment = Alignment.Center
                ) {
                    QuickAccessToolVisualIcon(
                        tool = tool,
                        tint = slotTier.iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NoorDarkPine,
                            fontSize = 13.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = NoorSageSlate,
                            fontSize = 11.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Left-edge accent stripe curving seamlessly with rounded corners (matching Customize Quick Access tool)
            Canvas(
                modifier = Modifier.matchParentSize()
            ) {
                val cornerRadius = 16.dp.toPx()
                val stripeWidth = 4.dp.toPx()
                val innerRadius = (cornerRadius - stripeWidth).coerceAtLeast(0f)

                val outerCardPath = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(0f, 0f, size.width, size.height),
                            topLeft = CornerRadius(cornerRadius, cornerRadius),
                            bottomLeft = CornerRadius(cornerRadius, cornerRadius)
                        )
                    )
                }

                val innerCardPath = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(stripeWidth, 0f, size.width, size.height),
                            topLeft = CornerRadius(innerRadius, innerRadius),
                            bottomLeft = CornerRadius(innerRadius, innerRadius)
                        )
                    )
                }

                val stripePath = Path().apply {
                    op(outerCardPath, innerCardPath, PathOperation.Difference)
                }

                drawPath(stripePath, color = slotTier.stripeColor)
            }
        }
    }
}

@Composable
fun SpiritualEssentialsGrid(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isArabic = appLanguage.equals("Arabic", ignoreCase = true) ||
            appLanguage == "العربية" ||
            appLanguage.startsWith("ar", ignoreCase = true)
    val quickAccessTools by viewModel.quickAccessTools.collectAsStateWithLifecycle()
    val displayTools = if (quickAccessTools.isNotEmpty()) quickAccessTools.take(4) else QuickAccessTool.defaultTools()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Clean Section Header: Leading Badge + Title & Subtitle + All Tools Action
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NoorSoftGreenBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DashboardCustomize,
                        contentDescription = null,
                        tint = DeepVibrantTeal,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Text(
                        text = if (isArabic) "الوصول السريع" else "Quick Access",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NoorDarkPine,
                            fontSize = 15.5.sp
                        )
                    )
                    Text(
                        text = if (isArabic) "أدواتك المفضلة والمختارة" else "Your essential spiritual tools",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = NoorSageSlate,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { viewModel.navigateTo(NoorDestination.ALL_TOOLS) },
                shape = RoundedCornerShape(10.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.home_all_tools),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepVibrantTeal
                        )
                    )
                    Icon(
                        imageVector = if (isArabic) Icons.AutoMirrored.Filled.ArrowBack else Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = DeepVibrantTeal,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }

        // 2x2 Grid (4 Cards Total)
        if (displayTools.size >= 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickAccessMiniCard(
                    tool = displayTools[0],
                    isArabic = isArabic,
                    slotIndex = 0,
                    onClick = { navigateToQuickAccessTool(viewModel, displayTools[0]) },
                    modifier = Modifier.weight(1f)
                )
                if (displayTools.size > 1) {
                    QuickAccessMiniCard(
                        tool = displayTools[1],
                        isArabic = isArabic,
                        slotIndex = 1,
                        onClick = { navigateToQuickAccessTool(viewModel, displayTools[1]) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        if (displayTools.size >= 3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickAccessMiniCard(
                    tool = displayTools[2],
                    isArabic = isArabic,
                    slotIndex = 2,
                    onClick = { navigateToQuickAccessTool(viewModel, displayTools[2]) },
                    modifier = Modifier.weight(1f)
                )
                if (displayTools.size > 3) {
                    QuickAccessMiniCard(
                        tool = displayTools[3],
                        isArabic = isArabic,
                        slotIndex = 3,
                        onClick = { navigateToQuickAccessTool(viewModel, displayTools[3]) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Customize Quick Access Link under the 2x2 grid (Grey text, no background color, no icon background)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { viewModel.openCustomizeQuickAccessSheet() }
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = Color(0xFF7A8B86),
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = if (isArabic) "تخصيص الوصول السريع" else "Customize Quick Access",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF7A8B86),
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = if (isArabic) "تعديل ٤ أدوات" else "Edit 4 tools",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF9EABA7),
                        fontSize = 11.sp
                    )
                )
                Icon(
                    imageVector = if (isArabic) Icons.AutoMirrored.Filled.ArrowBack else Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFF9EABA7),
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

// ============================================================
// 2.5 FAVORITES & BOOKMARKS SCROLLABLE SECTION
// ============================================================

@Composable
fun HomeFavoritesCard(
    title: String,
    count: Int,
    accentColor: Color,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isArabic: Boolean = false
) {
    val subtitleText = if (isArabic) {
        if (count == 1) "١ محفوظ" else "$count محفوظ"
    } else {
        "$count bookmarked"
    }

    Surface(
        modifier = modifier
            .width(140.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0x18000000),
                ambientColor = accentColor.copy(alpha = 0.14f)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, NoorCardBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFFFAFCFA)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Soft 3D layered icon container
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.16f),
                                    accentColor.copy(alpha = 0.08f)
                                )
                            )
                        )
                        .border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(11.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = NoorDarkPine,
                        fontSize = 13.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitleText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = NoorSageSlate,
                        fontSize = 11.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // View link stacked under the subtext
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(accentColor.copy(alpha = 0.08f))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isArabic) "عرض" else "View",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            fontSize = 11.sp
                        )
                    )
                    Icon(
                        imageVector = if (isArabic) Icons.AutoMirrored.Filled.ArrowBack else Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeFavoritesCarousel(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isArabic = appLanguage.equals("Arabic", ignoreCase = true) ||
            appLanguage == "العربية" ||
            appLanguage.startsWith("ar", ignoreCase = true)

    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val downloadedSurahs by viewModel.downloadedSurahs.collectAsStateWithLifecycle()

    // Dynamic category count calculations
    val duasCount = favorites.count {
        val t = it.type.uppercase()
        t == "DUA" || t == "SUPPLICATION" || t == "DUAS"
    }

    val quranCount = favorites.count {
        val t = it.type.uppercase()
        t == "SURAH" || t == "AYAH" || t == "QURAN" || t == "VERSE"
    }

    val azkarCount = favorites.count {
        val t = it.type.uppercase()
        t == "AZKAR" || t == "DHIKR" || t == "TASBIH"
    }

    val audioCount = favorites.count {
        val t = it.type.uppercase()
        t == "AUDIO" || t == "MP3" || t == "RECITER" || t == "TRACK"
    } + downloadedSurahs.size

    val hadithCount = favorites.count {
        val t = it.type.uppercase()
        t == "QUOTE" || t == "HADITH" || t == "WISDOM" || t == "REFLECTION"
    }

    val totalCount = favorites.size

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NoorSoftGreenBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = DeepVibrantTeal,
                        modifier = Modifier.size(17.dp)
                    )
                }

                Column {
                    Text(
                        text = if (isArabic) "المفضلات والمحفوظات" else "Favorites",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NoorDarkPine,
                            fontSize = 15.5.sp
                        )
                    )
                    Text(
                        text = if (totalCount > 0) {
                            if (isArabic) "$totalCount عنصر محفوظ في مكتبتك" else "$totalCount saved items in your library"
                        } else {
                            if (isArabic) "الوصول السريع للأدعية، السور، والصوتيات" else "Quick access to saved duas, surahs & audio"
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = NoorSageSlate,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { viewModel.navigateTo(NoorDestination.FAVORITES) },
                shape = RoundedCornerShape(10.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = if (isArabic) "عرض الكل" else "View All",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepVibrantTeal
                        )
                    )
                    Icon(
                        imageVector = if (isArabic) Icons.AutoMirrored.Filled.ArrowBack else Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = DeepVibrantTeal,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }

        // Horizontal Scrollable Row of Favorite Categories
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 2.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. Duas
            item(key = "fav_duas") {
                HomeFavoritesCard(
                    title = if (isArabic) "الأدعية المفضلة" else "Favorite Duas",
                    count = duasCount,
                    accentColor = Color(0xFFE06D53),
                    icon = {
                        IslamicIconDua(
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFFE06D53)
                        )
                    },
                    onClick = { viewModel.navigateTo(NoorDestination.FAVORITES) },
                    isArabic = isArabic
                )
            }

            // 2. Surahs
            item(key = "fav_quran") {
                HomeFavoritesCard(
                    title = if (isArabic) "السور المفضلة" else "Favorite Surahs",
                    count = quranCount,
                    accentColor = DeepVibrantTeal,
                    icon = {
                        IslamicIconMushaf(
                            modifier = Modifier.size(18.dp),
                            tint = DeepVibrantTeal
                        )
                    },
                    onClick = { viewModel.navigateTo(NoorDestination.FAVORITES) },
                    isArabic = isArabic
                )
            }

            // 3. Azkar
            item(key = "fav_azkar") {
                HomeFavoritesCard(
                    title = if (isArabic) "الأذكار المفضلة" else "Favorite Azkar",
                    count = azkarCount,
                    accentColor = Color(0xFF16A34A),
                    icon = {
                        IslamicIconTasbeeh(
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF16A34A)
                        )
                    },
                    onClick = { viewModel.navigateTo(NoorDestination.FAVORITES) },
                    isArabic = isArabic
                )
            }

            // 4. Audio & Recitations (MP3)
            item(key = "fav_audio") {
                HomeFavoritesCard(
                    title = if (isArabic) "التلاوات المفضلة" else "Favorite Audio",
                    count = audioCount,
                    accentColor = Color(0xFF7C3AED),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = null,
                            tint = Color(0xFF7C3AED),
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    onClick = { viewModel.navigateTo(NoorDestination.QURAN_AUDIO_STREAM) },
                    isArabic = isArabic
                )
            }

            // 5. Hadith & Wisdom Quotes
            item(key = "fav_hadith") {
                HomeFavoritesCard(
                    title = if (isArabic) "الأحاديث المفضلة" else "Favorite Hadith",
                    count = hadithCount,
                    accentColor = Color(0xFFD97706),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    onClick = { viewModel.navigateTo(NoorDestination.FAVORITES) },
                    isArabic = isArabic
                )
            }

            // 6. All Bookmarks Library
            item(key = "fav_all") {
                HomeFavoritesCard(
                    title = if (isArabic) "كل المحفوظات" else "All Bookmarks",
                    count = totalCount,
                    accentColor = NoorDarkPine,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = NoorDarkPine,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    onClick = { viewModel.navigateTo(NoorDestination.FAVORITES) },
                    isArabic = isArabic
                )
            }
        }
    }
}

// Backward compatibility alias
@Composable
fun GeometricFeaturedGrid(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    SpiritualEssentialsGrid(viewModel = viewModel, modifier = modifier)
}

// ============================================================
// 4. QURAN ACTIVE READING PROGRESS
// ============================================================

@Composable
fun QuranContinuationWidget(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val progressEntity by viewModel.readingProgress.collectAsStateWithLifecycle()
    val isArabic by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isLangArabic = isArabic.equals("Arabic", ignoreCase = true) || isArabic == "العربية"

    val hasBookmark = progressEntity != null
    val surahName = progressEntity?.surahName ?: stringResource(R.string.home_fatihah_name)
    val ayahNum = progressEntity?.ayahNumber ?: 1
    val totalAyahs = progressEntity?.totalAyahs ?: 7

    val onAction = {
        if (progressEntity != null) {
            viewModel.resumeReading(progressEntity!!)
        } else {
            viewModel.selectSurahForReading(QuranData.surahs.first(), 0)
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .clickable(onClick = onAction),
        shape = RoundedCornerShape(26.dp),
        color = Color.White,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, BorderTealGray)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                DeepVibrantTeal.copy(alpha = 0.06f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(SoftTealTint)
                                .border(1.dp, BorderTealLight, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = DeepVibrantTeal,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Text(
                                text = if (hasBookmark) stringResource(R.string.home_continue_reading) else stringResource(R.string.home_start_reading),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DarkPine,
                                    fontSize = 17.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (hasBookmark) {
                                    if (isLangArabic) "تابع القراءة من موضعك المحفوظ" else "Pick up from your saved bookmark"
                                } else {
                                    if (isLangArabic) "ابدأ وردك القرآني اليومي" else "Begin your daily recitation"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = SlateTealMuted,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SoftTealTint,
                        border = BorderStroke(1.dp, BorderTealLight)
                    ) {
                        Text(
                            text = if (hasBookmark) stringResource(R.string.action_continue) else stringResource(R.string.home_open_mushaf),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepVibrantTeal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Illuminated Mushaf Stage
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFF7FAF9),
                    border = BorderStroke(1.dp, Color(0xFFDFEBE5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = surahName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = DarkPine
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (hasBookmark) {
                                        "Juz ${progressEntity?.let { KhatmaEngine.getAyahCoordinate(KhatmaEngine.getAbsoluteAyahIndex(it.surahNumber, it.ayahNumber)).juzNumber } ?: 1} • ${stringResource(R.string.home_saved_bookmark_desc)}"
                                    } else {
                                        if (isLangArabic) "مكية • ٧ آيات" else "Meccan • 7 Ayahs"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SlateTealMuted,
                                        fontSize = 12.sp
                                    )
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = GoldBadgeBg,
                                border = BorderStroke(1.dp, MetallicGold.copy(alpha = 0.35f))
                            ) {
                                Text(
                                    text = if (hasBookmark) stringResource(R.string.home_ayah_counter, ayahNum, totalAyahs) else stringResource(R.string.home_surah_1),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MetallicGold,
                                        fontSize = 11.5.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val progress = if (hasBookmark) (ayahNum.toFloat() / totalAyahs.toFloat()).coerceIn(0.05f, 1f) else 0.05f
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFFE2EBE6))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(PrimaryTealGradient)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ============================================================
// 4B. QURAN KHATMA & READING PROGRESS WIDGET
// (Modularized into dedicated QuranKhatmaHomeWidget.kt)
// ============================================================

// ============================================================
// 5. DAILY REVELATION (AYAH OF THE DAY & AUTHENTIC SUPPLICATION)
// ============================================================

@Composable
fun DailyAyahAndDuaShowcase(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val dailyAyah = DuaData.dailyAyah
    val dailyDua = DuaData.dailyDua
    val showArabicSecondary by viewModel.showArabicSecondaryText.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val isArabicPrimary = appLanguage.equals("Arabic", ignoreCase = true) || appLanguage == "العربية" || appLanguage.startsWith("ar", ignoreCase = true)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Section Header Row: Leading Icon Badge (no border) + Title & Subtitle + Action Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NoorSoftGreenBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = DeepVibrantTeal,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Text(
                        text = stringResource(R.string.home_daily_revelation),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NoorDarkPine,
                            fontSize = 15.5.sp
                        )
                    )
                    Text(
                        text = stringResource(R.string.home_daily_revelation_sub),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = NoorSageSlate,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = NoorSurfaceSoft,
                border = BorderStroke(1.dp, NoorCardBorder),
                modifier = Modifier.clickable {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val textToCopy = if (isArabicPrimary) {
                        "${dailyAyah.arabicText}\n(${dailyAyah.referenceAr.ifBlank { dailyAyah.reference }})"
                    } else {
                        "${dailyAyah.translation} (${dailyAyah.reference})${if (showArabicSecondary) "\n" + dailyAyah.arabicText else ""}"
                    }
                    val clip = ClipData.newPlainText("Ayah of the Day", textToCopy)
                    clipboard.setPrimaryClip(clip)
                    viewModel.showToast(context.getString(R.string.home_ayah_copied))
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = NoorSageSlate,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = stringResource(R.string.action_copy),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NoorDarkPine,
                            fontSize = 11.5.sp
                        )
                    )
                }
            }
        }

        // Daily Ayah Editorial Manuscript - Inner border styling
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent,
            border = BorderStroke(1.2.dp, NoorGoldAccent.copy(alpha = 0.55f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFFDF5),
                                Color(0xFFFFF9EE),
                                Color(0xFFFFF4E0)
                            )
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFAF0D7),
                            border = BorderStroke(1.dp, Color(0xFFE8D2A0))
                        ) {
                            Text(
                                text = stringResource(R.string.home_ayah_of_the_day),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = NoorGoldAccent
                                )
                            )
                        }

                        Text(
                            text = if (isArabicPrimary) dailyAyah.referenceAr.ifBlank { dailyAyah.reference } else dailyAyah.reference,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NoorDarkPine
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (isArabicPrimary) {
                        // In Arabic mode: ONLY show pristine Arabic text, no English translation or transliteration
                        Text(
                            text = dailyAyah.arabicText,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = FontFamily.Serif,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Normal,
                                color = NoorDarkPine,
                                textAlign = TextAlign.Start,
                                lineHeight = 38.sp
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        // 1. PRIMARY LAYER (ENGLISH DOMINANT): Regular font weight, no quotes
                        Text(
                            text = dailyAyah.translation,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.5.sp,
                                color = NoorDarkPine,
                                lineHeight = 24.sp
                            )
                        )

                        // 2. SECONDARY LAYER: Phonetic Transliteration
                        if (dailyAyah.transliteration.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = dailyAyah.transliteration,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    fontSize = 13.5.sp,
                                    color = NoorSageSlate,
                                    lineHeight = 20.sp
                                )
                            )
                        }

                        // 3. TERTIARY LAYER: Traditional Arabic Script (Subject to global toggle, Regular weight)
                        if (showArabicSecondary && dailyAyah.arabicText.isNotBlank()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = NoorGoldBorder.copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = dailyAyah.arabicText,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 21.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = NoorDarkPine.copy(alpha = 0.9f),
                                    textAlign = TextAlign.End,
                                    lineHeight = 36.sp
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Daily Dua with dynamic language switching & regular font weight for Arabic
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(NoorDestination.DUAS_LIBRARY) },
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            border = BorderStroke(1.dp, NoorGoldBorder.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IslamicIconDua(modifier = Modifier.size(20.dp), tint = NoorGoldAccent)
                        Text(
                            text = if (isArabicPrimary) dailyDua.categoryAr.ifBlank { "دعاء اليوم" } else dailyDua.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = NoorGoldAccent
                            )
                        )
                    }

                    Text(
                        text = if (isArabicPrimary) dailyDua.referenceAr.ifBlank { dailyDua.reference } else dailyDua.reference,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = NoorSageSlate
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (isArabicPrimary) {
                    // In Arabic mode: ONLY show pristine Arabic text
                    Text(
                        text = dailyDua.arabicText,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = NoorDarkPine,
                            textAlign = TextAlign.Start,
                            lineHeight = 30.sp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    // 1. Primary Layer: English Translation
                    Text(
                        text = dailyDua.translation,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = NoorDarkPine,
                            lineHeight = 21.sp
                        )
                    )

                    // 2. Secondary Layer: Phonetic Transliteration
                    if (dailyDua.transliteration.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = dailyDua.transliteration,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                fontSize = 12.5.sp,
                                color = NoorSageSlate,
                                lineHeight = 18.sp
                            )
                        )
                    }

                    // 3. Tertiary Layer: Arabic Script (Regular weight)
                    if (showArabicSecondary && dailyDua.arabicText.isNotBlank()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = NoorGoldBorder.copy(alpha = 0.4f))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = dailyDua.arabicText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = FontFamily.Serif,
                                fontSize = 17.5.sp,
                                fontWeight = FontWeight.Normal,
                                color = NoorDarkPine.copy(alpha = 0.85f),
                                textAlign = TextAlign.End,
                                lineHeight = 28.sp
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// ============================================================
// 6. HEART & SOUL: MOOD & AI WISDOM REFLECTION
// ============================================================

@Composable
fun DailyMoodWisdomSection(
    viewModel: MainViewModel,
    selectedMood: String,
    isIslamic: Boolean,
    modifier: Modifier = Modifier
) {
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val moods = listOf(
        "Anxious" to stringResource(R.string.mood_anxious),
        "Grateful" to stringResource(R.string.mood_grateful),
        "Tired" to stringResource(R.string.mood_tired),
        "Hopeful" to stringResource(R.string.mood_hopeful),
        "Lost" to stringResource(R.string.mood_lost),
        "Peaceful" to stringResource(R.string.mood_peaceful)
    )

    NoorSectionContainer(
        icon = Icons.Default.Psychology,
        title = stringResource(R.string.home_spiritual_mood),
        subtitle = stringResource(R.string.home_how_is_heart_today),
        modifier = modifier
    ) {
        // Horizontal Mood Pills with subtle inner border
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 4.dp)
        ) {
            items(moods) { (moodKey, moodLabel) ->
                val isSelected = selectedMood.equals(moodKey, ignoreCase = true)
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) NoorTealStart else NoorSurfaceSoft,
                    border = BorderStroke(1.dp, if (isSelected) NoorTealStart else NoorCardBorder),
                    modifier = Modifier.clickable {
                        viewModel.selectMood(moodKey)
                    }
                ) {
                    Text(
                        text = moodLabel,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else NoorDarkPine
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Wisdom Response Box with subtle inner border
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = NoorSurfaceSoft,
            border = BorderStroke(1.dp, NoorCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NoorGoldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = stringResource(R.string.home_daily_reflection_wisdom),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = NoorGoldAccent
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val wisdom = viewModel.getCurrentMoodWisdom()
                val isArabicPrimary = appLanguage.equals("Arabic", ignoreCase = true) || appLanguage == "العربية" || appLanguage.startsWith("ar", ignoreCase = true)

                if (wisdom.arabicText.isNotBlank()) {
                    Text(
                        text = wisdom.arabicText,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = NoorDarkPine,
                            textAlign = TextAlign.End,
                            lineHeight = 30.sp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = if (isArabicPrimary) {
                        "${wisdom.explanationAr.ifBlank { wisdom.explanation }} — ${wisdom.sourceAr.ifBlank { wisdom.source }}"
                    } else {
                        "${wisdom.translation} — ${wisdom.source}"
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.5.sp,
                        color = NoorDarkPine,
                        lineHeight = 22.sp
                    )
                )
            }
        }
    }
}

// ============================================================
// 7. PREMIUM UPGRADE CARD (Under Spiritual Mood Section)
// ============================================================

@Composable
fun PremiumUpgradeCard(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                viewModel.showToast(context.getString(R.string.home_premium_toast))
            },
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFFDF5),
                            Color(0xFFFFF5DF),
                            Color(0xFFFFEFA8)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        // Clean Light & Gold Styling: Soft Golden/Mint container with Gold Icon
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFAF0D7))
                                .border(1.2.dp, Color(0xFFE2C983), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = "Premium Icon",
                                tint = NoorGoldAccent,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.home_unlock_pro),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = NoorDarkPine
                                    )
                                )
                                // Clean Light Gold PRO Badge
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFFAF0D7),
                                    border = BorderStroke(0.8.dp, Color(0xFFE2C983))
                                ) {
                                    Text(
                                        text = stringResource(R.string.home_pro_badge),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NoorGoldAccent
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = stringResource(R.string.home_pro_desc),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.5.sp,
                                    color = NoorSageSlate
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Feature Highlights Chips with subtle inner borders
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFAF0D7),
                        border = BorderStroke(1.dp, Color(0xFFE8D2A0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(text = "✨", fontSize = 12.sp)
                            Text(
                                text = stringResource(R.string.home_unlimited_ai),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NoorDarkPine
                                )
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFAF0D7),
                        border = BorderStroke(1.dp, Color(0xFFE8D2A0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(text = "🎧", fontSize = 12.sp)
                            Text(
                                text = stringResource(R.string.home_offline_qaris),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NoorDarkPine
                                )
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFAF0D7),
                        border = BorderStroke(1.dp, Color(0xFFE8D2A0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(text = "🕌", fontSize = 12.sp)
                            Text(
                                text = stringResource(R.string.home_ad_free),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NoorDarkPine
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Inverted CTA Button: Vibrant Golden Gradient Container with subtle border
                Surface(
                    onClick = {
                        viewModel.showToast(context.getString(R.string.home_premium_toast))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.Transparent,
                    border = BorderStroke(1.dp, Color(0xFFE5B958)),
                    shadowElevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFFFFD54F),
                                        Color(0xFFE5B54F),
                                        Color(0xFFD4A340)
                                    )
                                )
                            )
                            .padding(vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.home_unlock_upgrade_cta),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NoorDarkPine
                                )
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = NoorDarkPine,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}



// ============================================================
// 8. QURAN AUDIO RECITERS SHOWCASE
// ============================================================

@Composable
fun QuranRecitersShowcase(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val reciters = QuranData.reciters
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isArabic = appLanguage.equals("Arabic", ignoreCase = true) || appLanguage == "العربية" || appLanguage.startsWith("ar", ignoreCase = true)

    NoorSectionContainer(
        icon = Icons.Default.Headphones,
        title = stringResource(R.string.home_quran_recitations),
        subtitle = stringResource(R.string.home_reciters_sub),
        actionLabel = stringResource(R.string.home_audio_stream),
        onActionClick = { viewModel.navigateTo(NoorDestination.QURAN_RECITERS) },
        modifier = modifier
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 4.dp)
        ) {
            items(reciters) { reciter ->
                val reciterName = if (isArabic) reciter.nameAr.ifBlank { reciter.name } else reciter.name
                val reciterStyle = if (isArabic) reciter.styleAr.ifBlank { reciter.style } else reciter.style

                Surface(
                    modifier = Modifier
                        .width(168.dp)
                        .clickable {
                            viewModel.selectReciter(reciter)
                            viewModel.playSurahAudio(QuranData.surahs.first(), openPlayer = true)
                        },
                    shape = RoundedCornerShape(18.dp),
                    color = NoorSurfaceSoft,
                    border = BorderStroke(1.dp, NoorCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(NoorAccentGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = reciterName,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = NoorDarkPine,
                                textAlign = TextAlign.Center
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = reciterStyle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                color = NoorSageSlate
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}



data class ShareableCardData(
    val quote: String,
    val reference: String,
    val themeGradient: List<Color>
)

// ============================================================
// SHAREABLE IMAGES CAROUSEL (Bottom of Page)
// ============================================================

@Composable
fun ShareableImagesCarouselSection(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isArabic = appLanguage.equals("Arabic", ignoreCase = true) || appLanguage == "العربية" || appLanguage.startsWith("ar", ignoreCase = true)

    val shareableCards = if (isArabic) {
        listOf(
            ShareableCardData(
                quote = "«إن مع العسر يسراً»",
                reference = "سورة الشرح 94:6",
                themeGradient = listOf(Color(0xFF0C3829), Color(0xFF13503B))
            ),
            ShareableCardData(
                quote = "«وهو معكم أينما كنتم»",
                reference = "سورة الحديد 57:4",
                themeGradient = listOf(Color(0xFF2C1810), Color(0xFF5A3522))
            ),
            ShareableCardData(
                quote = "«سبحان الله وبحمده، سبحان الله العظيم»",
                reference = "صحيح البخاري",
                themeGradient = listOf(Color(0xFF1A365D), Color(0xFF2A4365))
            ),
            ShareableCardData(
                quote = "«بارك الله لك في بيتك وأهلك بالسلامة والبركة»",
                reference = "تذكير يومي عائلي",
                themeGradient = listOf(Color(0xFF4A3500), Color(0xFF745100))
            )
        )
    } else {
        listOf(
            ShareableCardData(
                quote = "“Verily, with hardship comes ease.”",
                reference = "Surah Ash-Sharh 94:6",
                themeGradient = listOf(Color(0xFF0C3829), Color(0xFF13503B))
            ),
            ShareableCardData(
                quote = "“And He is with you wherever you may be.”",
                reference = "Surah Al-Hadid 57:4",
                themeGradient = listOf(Color(0xFF2C1810), Color(0xFF5A3522))
            ),
            ShareableCardData(
                quote = "“SubhanAllah wa bihamdihi, SubhanAllah al-Azim.”",
                reference = "Sahih Al-Bukhari",
                themeGradient = listOf(Color(0xFF1A365D), Color(0xFF2A4365))
            ),
            ShareableCardData(
                quote = "“May Allah bless your home with peace & barakah.”",
                reference = "Family Daily Reminder",
                themeGradient = listOf(Color(0xFF4A3500), Color(0xFF745100))
            )
        )
    }

    NoorSectionContainer(
        icon = Icons.Default.Share,
        title = stringResource(R.string.home_shareable_cards_title),
        subtitle = stringResource(R.string.home_shareable_cards_sub),
        actionLabel = stringResource(R.string.home_more),
        onActionClick = { viewModel.showToast(context.getString(R.string.home_exploring_shareable_toast)) },
        modifier = modifier
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(shareableCards) { card ->
                Surface(
                    modifier = Modifier
                        .width(220.dp)
                        .height(150.dp)
                        .clickable {
                            viewModel.showToast(context.getString(R.string.home_shareable_card_copied))
                        },
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Transparent,
                    border = BorderStroke(1.dp, Color(0xFFE2C983).copy(alpha = 0.4f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.linearGradient(card.themeGradient))
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isArabic) "✨ النور" else "✨ Al-Noor",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFDF79)
                                    )
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = stringResource(R.string.action_share),
                                            tint = Color.White,
                                            modifier = Modifier.size(10.dp)
                                        )
                                        Text(
                                            text = stringResource(R.string.action_share),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        )
                                    }
                                }
                            }

                            Column {
                                Text(
                                    text = card.quote,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        lineHeight = 17.sp
                                    ),
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = card.reference,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 10.5.sp,
                                        color = Color(0xFFFFDF79)
                                    ),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
