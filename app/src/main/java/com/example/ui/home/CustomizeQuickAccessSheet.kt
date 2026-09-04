package com.example.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.QuickAccessTool
import com.example.ui.MainViewModel
import com.example.ui.theme.DarkPine
import com.example.ui.theme.DeepVibrantTeal
import com.example.ui.theme.SlateTealMuted

fun getQuickAccessToolIcon(tool: QuickAccessTool): ImageVector {
    return when (tool) {
        QuickAccessTool.QURAN -> Icons.AutoMirrored.Filled.MenuBook
        QuickAccessTool.SALAT -> Icons.Default.AccessTime
        QuickAccessTool.QIBLA -> Icons.Default.CompassCalibration
        QuickAccessTool.TASBIH -> Icons.Default.AccessTime
        QuickAccessTool.DUAS -> Icons.Default.Favorite
        QuickAccessTool.KHATMA -> Icons.Default.Bookmark
        QuickAccessTool.STREAKS -> Icons.Default.LocalFireDepartment
        QuickAccessTool.HABITS -> Icons.Default.Bookmark
        QuickAccessTool.AUDIO -> Icons.Default.Headphones
    }
}

fun getQuickAccessToolAccent(tool: QuickAccessTool): Color {
    return when (tool) {
        QuickAccessTool.QURAN -> Color(0xFF0F9D8A) // 1. Holy Qur’an → soft emerald/teal (#0F9D8A)
        QuickAccessTool.SALAT -> Color(0xFF5BA9D6) // 2. Salat & Tracker → soft sky blue (#5BA9D6)
        QuickAccessTool.QIBLA -> Color(0xFFD4A84F) // 3. Qibla Finder → muted amber/gold (#D4A84F)
        QuickAccessTool.TASBIH -> Color(0xFF5FBF9A) // 4. Smart Tasbih → soft mint/green (#5FBF9A)
        QuickAccessTool.DUAS -> Color(0xFFD98282) // 5. Du’as & Azkar → muted rose/coral (#D98282)
        QuickAccessTool.KHATMA -> Color(0xFF8174B8) // 6. Khatma Planner → soft violet/indigo (#8174B8)
        QuickAccessTool.STREAKS -> Color(0xFFD99568) // 7. Spiritual Streaks → muted orange/peach (#D99568)
        QuickAccessTool.HABITS -> Color(0xFF7FA58A) // 8. Sunnah Habits → soft sage green (#7FA58A)
        QuickAccessTool.AUDIO -> Color(0xFF9B82C9) // 9. MP3 Quran Player → soft lavender/purple (#9B82C9)
    }
}

@Composable
fun QuickAccessToolVisualIcon(
    tool: QuickAccessTool,
    tint: Color,
    modifier: Modifier = Modifier
) {
    when (tool) {
        QuickAccessTool.QURAN -> IslamicIconMushaf(modifier = modifier, tint = tint)
        QuickAccessTool.SALAT -> IslamicIconSalat(modifier = modifier, tint = tint)
        QuickAccessTool.QIBLA -> IslamicIconQibla(modifier = modifier, tint = tint)
        QuickAccessTool.TASBIH -> IslamicIconAzkar(modifier = modifier, tint = tint)
        QuickAccessTool.DUAS -> IslamicIconDua(modifier = modifier, tint = tint)
        QuickAccessTool.KHATMA -> Icon(
            imageVector = Icons.Default.Bookmark,
            contentDescription = null,
            tint = tint,
            modifier = modifier
        )
        QuickAccessTool.STREAKS -> Icon(
            imageVector = Icons.Default.LocalFireDepartment,
            contentDescription = null,
            tint = tint,
            modifier = modifier
        )
        QuickAccessTool.HABITS -> IslamicIconTask(modifier = modifier, tint = tint)
        QuickAccessTool.AUDIO -> Icon(
            imageVector = Icons.Default.Headphones,
            contentDescription = null,
            tint = tint,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeQuickAccessSheet(
    viewModel: MainViewModel,
    selectedTools: List<QuickAccessTool>,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isArabic = appLanguage.equals("Arabic", ignoreCase = true) ||
            appLanguage == "العربية" ||
            appLanguage.startsWith("ar", ignoreCase = true)

    val allTools = QuickAccessTool.entries
    val isDoneEnabled = selectedTools.size == 4
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .size(width = 42.dp, height = 4.5.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isDark) MaterialTheme.colorScheme.outlineVariant else Color(0xFFD4E0DA))
            )
        },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (isDark) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFF2F8F5),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Widgets,
                                contentDescription = null,
                                tint = if (isDark) MaterialTheme.colorScheme.primary else DeepVibrantTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = if (isArabic) "تخصيص الوصول السريع" else "Customize Quick Access",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = if (isArabic) "اختر ٤ أدوات بترتيب النقر للشبكة الرئيسية" else "Select exactly 4 tools in your desired order",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Selection Status Banner & Counter Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = if (isDoneEnabled) {
                        if (isDark) Color(0xFF1B3D22) else Color(0xFFE8F5E9)
                    } else {
                        if (isDark) Color(0xFF3E2723) else Color(0xFFFFF3E0)
                    },
                    border = BorderStroke(
                        1.dp,
                        if (isDoneEnabled) {
                            if (isDark) Color(0xFF2E7D32) else Color(0xFF81C784)
                        } else {
                            if (isDark) Color(0xFFE65100) else Color(0xFFFFB74D)
                        }
                    )
                ) {
                    Text(
                        text = if (isArabic) {
                            if (isDoneEnabled) "تم اختيار ٤ من ٤ ✓" else "المحدد: ${selectedTools.size} من ٤ (يلزم ٤)"
                        } else {
                            if (isDoneEnabled) "4 of 4 Selected ✓" else "Selected: ${selectedTools.size} of 4 (4 Required)"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isDoneEnabled) {
                                if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32)
                            } else {
                                if (isDark) Color(0xFFFFB74D) else Color(0xFFE65100)
                            },
                            fontSize = 11.5.sp
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                Text(
                    text = if (isArabic) "الترتيب يتبع أسبقية الاختيار" else "Numbered by selection order",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(10.dp))

            // Tools List
            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                itemsIndexed(allTools, key = { _, tool -> tool.id }) { _, tool ->
                    val isSelected = selectedTools.contains(tool)
                    val selectedIndex = selectedTools.indexOf(tool)
                    val slotTier = if (isSelected) QuickAccessColorSystem.getSlotTier(selectedIndex, isDark = isDark) else null

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, if (isDark) MaterialTheme.colorScheme.outline.copy(alpha = 0.4f) else QuickAccessColorSystem.UNSELECTED_CARD_BORDER),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.toggleQuickAccessTool(tool)
                            }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                            // Left: Selection Order Number Badge (if selected) or unselected placeholder
                            if (isSelected) {
                                Surface(
                                    shape = CircleShape,
                                    color = DeepVibrantTeal,
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${selectedIndex + 1}",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        )
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(if (isDark) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFF1F5F9))
                                        .border(1.dp, if (isDark) MaterialTheme.colorScheme.outline else Color(0xFFCBD5E1), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(if (isDark) MaterialTheme.colorScheme.outline else Color(0xFF94A3B8))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Icon Box: Selected tools use the 3-tier slot color system; unselected tools use plain neutral grey
                            val iconBoxBg = if (isSelected && slotTier != null) slotTier.iconBackground else QuickAccessColorSystem.UNSELECTED_ICON_BG
                            val iconTint = if (isSelected && slotTier != null) slotTier.iconTint else QuickAccessColorSystem.UNSELECTED_ICON_TINT

                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(11.dp))
                                    .background(iconBoxBg),
                                contentAlignment = Alignment.Center
                            ) {
                                QuickAccessToolVisualIcon(
                                    tool = tool,
                                    tint = iconTint,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Title & Subtitle
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (isArabic) tool.titleAr else tool.titleEn,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) DarkPine else SlateTealMuted,
                                        fontSize = 14.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (isArabic) tool.subtitleAr else tool.subtitleEn,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isSelected) SlateTealMuted else Color(0xFF94A3B8),
                                        fontSize = 11.5.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Selection Checkmark Badge
                            if (isSelected) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DeepVibrantTeal,
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.Transparent,
                                    border = BorderStroke(1.2.dp, Color(0xFFCBD5E1)),
                                    modifier = Modifier.size(26.dp)
                                ) {}
                            }
                        }

                        if (isSelected && slotTier != null) {
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
            }
        }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.resetQuickAccessTools() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, if (isDark) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else DeepVibrantTeal.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = if (isDark) MaterialTheme.colorScheme.primary else DeepVibrantTeal)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isArabic) "إعادة تعيين" else "Reset")
                }

                Button(
                    onClick = onDismiss,
                    enabled = isDoneEnabled,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) MaterialTheme.colorScheme.primary else DeepVibrantTeal,
                        contentColor = if (isDark) MaterialTheme.colorScheme.onPrimary else Color.White,
                        disabledContainerColor = if (isDark) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFE2EBE6),
                        disabledContentColor = if (isDark) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f) else Color(0xFF94A3B8)
                    )
                ) {
                    Text(
                        text = if (isDoneEnabled) {
                            if (isArabic) "تم وحفظ" else "Done"
                        } else {
                            if (isArabic) "اختر ٤ أدوات (${selectedTools.size}/4)" else "Select 4 (${selectedTools.size}/4)"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

