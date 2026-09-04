package com.example.ui.khatma

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.KhatmaHistoryEntity
import com.example.data.local.KhatmaPlanEntity
import com.example.data.quran.KhatmaDayItem
import com.example.data.quran.KhatmaEngine
import com.example.data.quran.KhatmaFullDashboardState
import com.example.data.quran.KhatmaPaceStatus
import com.example.data.quran.KhatmaSessionInfo
import com.example.ui.MainViewModel
import com.example.ui.components.NoorGlassIconButton
import com.example.ui.components.NoorTopBar
import com.example.ui.theme.ReadingThemes
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

data class KhatmaThemeColors(
    val bg: Color,
    val card: Color,
    val elevated: Color,
    val border: Color,
    val borderLight: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val accent: Color,
    val accentSoft: Color,
    val gold: Color,
    val goldBg: Color,
    val isDark: Boolean
)

val KhatmaDarkColors = KhatmaThemeColors(
    bg = Color(0xFF0F1418),
    card = Color(0xFF182026),
    elevated = Color(0xFF222C34),
    border = Color(0xFF26333C),
    borderLight = Color(0xFF33434F),
    textPrimary = Color(0xFFF1F5F9),
    textSecondary = Color(0xFF94A3B8),
    textMuted = Color(0xFF64748B),
    accent = Color(0xFF10B981), // Crisp Emerald Accent
    accentSoft = Color(0xFF10B981).copy(alpha = 0.15f),
    gold = Color(0xFFE5C378),
    goldBg = Color(0xFFE5C378).copy(alpha = 0.15f),
    isDark = true
)

val KhatmaLightColors = KhatmaThemeColors(
    bg = Color(0xFFF4F7F6), // Clean mint-neutral light canvas matching Noor
    card = Color(0xFFFFFFFF), // Crisp pure white cards
    elevated = Color(0xFFEBF1EE), // Soft elevated light surface
    border = Color(0xFFDFE6E3), // Clean card border
    borderLight = Color(0xFFE8EFEA),
    textPrimary = Color(0xFF14201D), // Dark slate-pine primary text
    textSecondary = Color(0xFF4A5D57), // Balanced slate-pine secondary text
    textMuted = Color(0xFF82948F), // Muted label text
    accent = Color(0xFF0D9488), // Rich Islamic Teal/Emerald
    accentSoft = Color(0xFF0D9488).copy(alpha = 0.12f),
    gold = Color(0xFFB58014), // Elegant antique gold
    goldBg = Color(0xFFB58014).copy(alpha = 0.12f),
    isDark = false
)

val LocalKhatmaColors = staticCompositionLocalOf {
    KhatmaLightColors
}

// Dynamic properties that automatically resolve according to active Light/Dark theme:
private val KhatmaDarkBg: Color @Composable get() = LocalKhatmaColors.current.bg
private val KhatmaDarkCard: Color @Composable get() = LocalKhatmaColors.current.card
private val KhatmaDarkElevated: Color @Composable get() = LocalKhatmaColors.current.elevated
private val KhatmaDarkBorder: Color @Composable get() = LocalKhatmaColors.current.border
private val KhatmaDarkBorderLight: Color @Composable get() = LocalKhatmaColors.current.borderLight
private val KhatmaDarkTextPrimary: Color @Composable get() = LocalKhatmaColors.current.textPrimary
private val KhatmaDarkTextSecondary: Color @Composable get() = LocalKhatmaColors.current.textSecondary
private val KhatmaDarkTextMuted: Color @Composable get() = LocalKhatmaColors.current.textMuted
private val KhatmaDarkAccent: Color @Composable get() = LocalKhatmaColors.current.accent
private val KhatmaDarkAccentSoft: Color @Composable get() = LocalKhatmaColors.current.accentSoft
private val KhatmaDarkGold: Color @Composable get() = LocalKhatmaColors.current.gold
private val KhatmaDarkGoldBg: Color @Composable get() = LocalKhatmaColors.current.goldBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranKhatmaScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val dashboardState by viewModel.khatmaDashboardState.collectAsState()
    val isSetupOpen by viewModel.isKhatmaSetupSheetOpen.collectAsState()
    val isSettingsOpen by viewModel.isKhatmaSettingsSheetOpen.collectAsState()
    val isHistoryOpen by viewModel.isKhatmaHistorySheetOpen.collectAsState()
    val isCompletionOpen by viewModel.isKhatmaCompletionCelebrationOpen.collectAsState()
    val isPaceAdjustOpen by viewModel.isKhatmaPaceAdjustSheetOpen.collectAsState()
    val historyList by viewModel.khatmaHistory.collectAsState()
    val isSystemDark by viewModel.isDarkMode.collectAsState()

    val khatmaColors = if (isSystemDark) KhatmaDarkColors else KhatmaLightColors
    val readingThemeColors = if (isSystemDark) ReadingThemes.ObsidianNight else ReadingThemes.MadaniCrisp

    var showQuickAddDialog by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalKhatmaColors provides khatmaColors) {
        Scaffold(
            topBar = {
                NoorTopBar(
                    title = "Quran Khatma",
                    eyebrow = "ختمة القرآن",
                    subtitle = "Completion Planner & Progress",
                    onBackClick = onNavigateBack,
                    backContentDescription = "Back",
                    isDark = isSystemDark,
                    themeColors = readingThemeColors,
                    actions = {
                        NoorGlassIconButton(
                            onClick = { viewModel.isKhatmaHistorySheetOpen.value = true },
                            icon = Icons.Default.History,
                            contentDescription = "Khatma History"
                        )
                        if (dashboardState != null) {
                            NoorGlassIconButton(
                                onClick = { viewModel.isKhatmaSettingsSheetOpen.value = true },
                                icon = Icons.Default.Settings,
                                contentDescription = "Khatma Settings"
                            )
                        }
                    }
                )
            },
            containerColor = KhatmaDarkBg
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val state = dashboardState
                if (state == null || state.plan.isCompleted) {
                    // Empty or completed state -> Onboarding Setup
                    KhatmaSetupView(
                        viewModel = viewModel,
                        isExistingKhatmaCompleted = state?.plan?.isCompleted == true,
                        onOpenHistory = { viewModel.isKhatmaHistorySheetOpen.value = true }
                    )
                } else {
                    // Active Khatma Dashboard
                    KhatmaDashboardContent(
                        state = state,
                        viewModel = viewModel,
                        onOpenSettings = { viewModel.isKhatmaSettingsSheetOpen.value = true },
                        onOpenPaceAdjust = { viewModel.isKhatmaPaceAdjustSheetOpen.value = true },
                        onQuickAdd = { showQuickAddDialog = true }
                    )
                }
            }
        }

        // Modal Sheets & Dialogs
        if (isSetupOpen) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.isKhatmaSetupSheetOpen.value = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = KhatmaDarkCard
            ) {
                KhatmaSetupSheetContent(
                    viewModel = viewModel,
                    onDismiss = { viewModel.isKhatmaSetupSheetOpen.value = false }
                )
            }
        }

        if (isSettingsOpen) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.isKhatmaSettingsSheetOpen.value = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = KhatmaDarkCard
            ) {
                KhatmaSettingsSheetContent(
                    viewModel = viewModel,
                    state = dashboardState,
                    onDismiss = { viewModel.isKhatmaSettingsSheetOpen.value = false },
                    onOpenPaceAdjust = {
                        viewModel.isKhatmaSettingsSheetOpen.value = false
                        viewModel.isKhatmaPaceAdjustSheetOpen.value = true
                    }
                )
            }
        }

        if (isPaceAdjustOpen) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.isKhatmaPaceAdjustSheetOpen.value = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = KhatmaDarkCard
            ) {
                KhatmaPaceAdjustmentSheetContent(
                    viewModel = viewModel,
                    state = dashboardState,
                    onDismiss = { viewModel.isKhatmaPaceAdjustSheetOpen.value = false }
                )
            }
        }

        if (isHistoryOpen) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.isKhatmaHistorySheetOpen.value = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = KhatmaDarkCard
            ) {
                KhatmaHistorySheetContent(
                    historyList = historyList,
                    onDismiss = { viewModel.isKhatmaHistorySheetOpen.value = false }
                )
            }
        }

        if (isCompletionOpen) {
            KhatmaCompletionCelebrationDialog(
                viewModel = viewModel,
                onDismiss = { viewModel.isKhatmaCompletionCelebrationOpen.value = false }
            )
        }

        if (showQuickAddDialog) {
            QuickLogAyahsDialog(
                onDismiss = { showQuickAddDialog = false },
                onAdd = { count ->
                    viewModel.advanceKhatmaByAyahs(count)
                    showQuickAddDialog = false
                }
            )
        }
    }
}

/**
 * Clean Khatma Setup / Onboarding View
 */
@Composable
fun KhatmaSetupView(
    viewModel: MainViewModel,
    isExistingKhatmaCompleted: Boolean = false,
    onOpenHistory: () -> Unit = {}
) {
    var selectedDays by remember { mutableIntStateOf(30) }
    var isCustomSelected by remember { mutableStateOf(false) }
    var customDays by remember { mutableFloatStateOf(30f) }
    var selectedSessions by remember { mutableIntStateOf(1) }
    var reminderEnabled by remember { mutableStateOf(true) }
    var reminderTime by remember { mutableStateOf("20:30") }
    var planTitle by remember { mutableStateOf("Quran Khatma") }

    val effectiveDays = if (isCustomSelected) customDays.roundToInt() else selectedDays
    val dailyTargetAyahs = (KhatmaEngine.TOTAL_QURAN_AYAHS.toDouble() / effectiveDays.toDouble()).roundToInt()
    val estCompletionDate = remember(effectiveDays) {
        LocalDate.now().plusDays(effectiveDays.toLong()).format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault()))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Spiritual Welcome Banner
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = KhatmaDarkCard),
                border = BorderStroke(1.dp, KhatmaDarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = KhatmaDarkAccent,
                            fontFamily = FontFamily.Serif
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isExistingKhatmaCompleted) "Start a Fresh Khatma" else "Begin Your Quran Khatma",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = KhatmaDarkTextPrimary,
                            letterSpacing = (-0.5).sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Set a peaceful, structured reading plan. Track your progress verse by verse with daily barakah.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = KhatmaDarkTextSecondary
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item {
            // Duration Presets
            Text(
                text = "Choose Completion Goal",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = KhatmaDarkTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            val presets = listOf(7, 15, 30, 45, 60, 90)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(presets) { days ->
                    val isSelected = !isCustomSelected && selectedDays == days
                    val chipBg by animateColorAsState(
                        if (isSelected) KhatmaDarkAccent else KhatmaDarkCard,
                        label = "chipBg"
                    )
                    val contentColor = if (isSelected) Color.White else KhatmaDarkTextPrimary

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = chipBg,
                        border = if (isSelected) null else BorderStroke(1.dp, KhatmaDarkBorder),
                        modifier = Modifier
                            .clickable {
                                isCustomSelected = false
                                selectedDays = days
                            }
                            .testTag("khatma_preset_${days}_days")
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$days Days",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = contentColor
                                )
                            )
                            Text(
                                text = when (days) {
                                    7 -> "Intensive"
                                    15 -> "1/2 Month"
                                    30 -> "1 Juz / Day"
                                    45 -> "Steady Pace"
                                    60 -> "1 Hizb / Day"
                                    else -> "Gentle Journey"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) Color.White.copy(alpha = 0.85f) else KhatmaDarkTextMuted
                                )
                            )
                        }
                    }
                }

                item {
                    val isSelected = isCustomSelected
                    val chipBg by animateColorAsState(
                        if (isSelected) KhatmaDarkAccent else KhatmaDarkCard,
                        label = "chipBg"
                    )
                    val contentColor = if (isSelected) Color.White else KhatmaDarkTextPrimary

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = chipBg,
                        border = if (isSelected) null else BorderStroke(1.dp, KhatmaDarkBorder),
                        modifier = Modifier
                            .clickable { isCustomSelected = true }
                            .testTag("khatma_preset_custom_days")
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Custom",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = contentColor
                                )
                            )
                            Text(
                                text = "${customDays.roundToInt()} Days",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) Color.White.copy(alpha = 0.85f) else KhatmaDarkTextMuted
                                )
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isCustomSelected,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Custom Duration",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = KhatmaDarkTextPrimary
                            )
                        )
                        Text(
                            text = "${customDays.roundToInt()} Days",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = KhatmaDarkAccent
                            )
                        )
                    }
                    Slider(
                        value = customDays,
                        onValueChange = { customDays = it },
                        valueRange = 5f..180f,
                        steps = 34,
                        colors = SliderDefaults.colors(
                            thumbColor = KhatmaDarkAccent,
                            activeTrackColor = KhatmaDarkAccent,
                            inactiveTrackColor = KhatmaDarkBorderLight
                        ),
                        modifier = Modifier.testTag("khatma_custom_slider")
                    )
                }
            }
        }

        item {
            // Daily Split Sessions Selector
            Text(
                text = "Daily Reading Sessions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = KhatmaDarkTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Split your daily target into bite-sized reflection sessions",
                style = MaterialTheme.typography.bodySmall.copy(color = KhatmaDarkTextSecondary)
            )
            Spacer(modifier = Modifier.height(10.dp))

            val sessionOptions = listOf(
                Pair(1, "1 Session (Daily)"),
                Pair(2, "2 Sessions (Morning / Evening)"),
                Pair(3, "3 Sessions (Morning / Afternoon / Night)"),
                Pair(5, "5 Sessions (After Each Prayer)")
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                sessionOptions.forEach { (count, label) ->
                    val isSelected = selectedSessions == count
                    OutlinedCard(
                        onClick = { selectedSessions = count },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = if (isSelected) KhatmaDarkAccentSoft else KhatmaDarkCard
                        ),
                        border = BorderStroke(
                            if (isSelected) 1.5.dp else 1.dp,
                            if (isSelected) KhatmaDarkAccent else KhatmaDarkBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) KhatmaDarkAccent else KhatmaDarkTextPrimary
                                )
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = KhatmaDarkAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            // Daily Reminder Switch & Time
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = KhatmaDarkCard),
                border = BorderStroke(1.dp, KhatmaDarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = if (reminderEnabled) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                            contentDescription = "Reminder",
                            tint = if (reminderEnabled) KhatmaDarkAccent else KhatmaDarkTextMuted
                        )
                        Column {
                            Text(
                                text = "Daily Reminder",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = KhatmaDarkTextPrimary
                                )
                            )
                            Text(
                                text = if (reminderEnabled) "Notify at $reminderTime" else "Disabled",
                                style = MaterialTheme.typography.bodySmall.copy(color = KhatmaDarkTextSecondary)
                            )
                        }
                    }
                    Switch(
                        checked = reminderEnabled,
                        onCheckedChange = { reminderEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = KhatmaDarkAccent,
                            uncheckedThumbColor = KhatmaDarkTextMuted,
                            uncheckedTrackColor = KhatmaDarkBorder
                        )
                    )
                }
            }
        }

        item {
            // Plan Summary & Calculation Preview
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = KhatmaDarkCard),
                border = BorderStroke(1.dp, KhatmaDarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Plan Summary",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = KhatmaDarkAccent
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Daily Target:", style = MaterialTheme.typography.bodyMedium.copy(color = KhatmaDarkTextSecondary))
                        Text(
                            text = "~$dailyTargetAyahs Ayahs / day",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Estimated Completion:", style = MaterialTheme.typography.bodyMedium.copy(color = KhatmaDarkTextSecondary))
                        Text(
                            text = estCompletionDate,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Holy Quran:", style = MaterialTheme.typography.bodyMedium.copy(color = KhatmaDarkTextSecondary))
                        Text(
                            text = "6,236 Ayahs (114 Surahs)",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
                        )
                    }
                }
            }
        }

        item {
            // Start Khatma CTA Button
            Button(
                onClick = {
                    viewModel.createOrResetKhatma(
                        days = effectiveDays,
                        startDate = LocalDate.now(),
                        sessionsCount = selectedSessions,
                        reminderEnabled = reminderEnabled,
                        reminderTime = reminderTime,
                        title = planTitle
                    )
                },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KhatmaDarkAccent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("create_khatma_button")
            ) {
                Icon(imageVector = Icons.Default.AutoStories, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Begin Khatma (بِسْمِ اللَّهِ)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }

        if (isExistingKhatmaCompleted) {
            item {
                OutlinedButton(
                    onClick = onOpenHistory,
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, KhatmaDarkBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.History, contentDescription = null, tint = KhatmaDarkAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Completed Khatma History", color = KhatmaDarkAccent, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

/**
 * Main Active Dashboard Content
 */
@Composable
fun KhatmaDashboardContent(
    state: KhatmaFullDashboardState,
    viewModel: MainViewModel,
    onOpenSettings: () -> Unit,
    onOpenPaceAdjust: () -> Unit,
    onQuickAdd: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Dashboard, 1: Timeline Plan

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = KhatmaDarkBg,
            contentColor = KhatmaDarkAccent,
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = KhatmaDarkAccent
                    )
                }
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Dashboard", fontWeight = FontWeight.Bold, color = if (selectedTab == 0) KhatmaDarkAccent else KhatmaDarkTextSecondary) },
                icon = { Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(18.dp), tint = if (selectedTab == 0) KhatmaDarkAccent else KhatmaDarkTextSecondary) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Reading Plan (${state.totalDays} Days)", fontWeight = FontWeight.Bold, color = if (selectedTab == 1) KhatmaDarkAccent else KhatmaDarkTextSecondary) },
                icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp), tint = if (selectedTab == 1) KhatmaDarkAccent else KhatmaDarkTextSecondary) }
            )
        }

        when (selectedTab) {
            0 -> KhatmaDashboardOverview(
                state = state,
                viewModel = viewModel,
                onOpenPaceAdjust = onOpenPaceAdjust,
                onQuickAdd = onQuickAdd
            )
            1 -> KhatmaTimelineView(
                timeline = state.dayPlanTimeline,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun KhatmaDashboardOverview(
    state: KhatmaFullDashboardState,
    viewModel: MainViewModel,
    onOpenPaceAdjust: () -> Unit,
    onQuickAdd: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Hero Progress Card with Circular / Linear Progress & "Continue Reading"
            KhatmaHeroProgressCard(
                state = state,
                onContinueReading = { viewModel.continueKhatmaReading() },
                onQuickAdd = onQuickAdd
            )
        }

        item {
            // Today's Target & Session Breakdown
            KhatmaTodaySessionsCard(
                state = state,
                onCompleteSession = { sessionIndex, targetAyahs ->
                    viewModel.completeKhatmaSession(sessionIndex, targetAyahs)
                },
                onReadSessionPortion = { surahNum, ayahNum ->
                    viewModel.openKhatmaReadingAtAyah(surahNum, ayahNum)
                }
            )
        }

        item {
            // Pace Status & Smart Adjuster Banner
            KhatmaPaceBannerCard(
                state = state,
                onOpenPaceAdjust = onOpenPaceAdjust
            )
        }

        item {
            // Khatma Journey Stats Matrix
            KhatmaStatsMatrix(state = state)
        }

        item {
            // Quick Action Row (Mark 100% complete, adjust pace)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenPaceAdjust,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, KhatmaDarkBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(18.dp), tint = KhatmaDarkAccent)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Adjust Pace", fontSize = 13.sp, color = KhatmaDarkAccent, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = { viewModel.markKhatmaCompleted() },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = KhatmaDarkAccent
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.CheckCircleOutline, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Finish Khatma", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Hero Progress Card with Large Overall Progress Bar & Percentage
 */
@Composable
fun KhatmaHeroProgressCard(
    state: KhatmaFullDashboardState,
    onContinueReading: () -> Unit,
    onQuickAdd: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = state.progressFraction,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "heroProgress"
    )

    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = KhatmaDarkCard
        ),
        border = BorderStroke(1.dp, KhatmaDarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Title & Pace Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = state.plan.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
                    )
                    Text(
                        text = "Day ${state.currentDayNumber} of ${state.totalDays} • ${state.daysRemaining} Days remaining",
                        style = MaterialTheme.typography.bodySmall.copy(color = KhatmaDarkTextSecondary)
                    )
                }

                PaceBadge(status = state.paceStatus, diff = state.paceDiffAyahs)
            }

            // Central Progress Gauge / Numbers
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "${state.readAyahsCount.formatNumber()} / ${state.totalAyahs.formatNumber()}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = KhatmaDarkTextPrimary,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    Text(
                        text = "Ayahs completed (${state.progressPercentage}%)",
                        style = MaterialTheme.typography.bodyMedium.copy(color = KhatmaDarkTextSecondary)
                    )
                }

                // Circular Progress Indicator Ring
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(72.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFF222E38),
                        strokeWidth = 6.dp
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = KhatmaDarkAccent,
                        strokeWidth = 6.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = "${state.progressPercentage}%",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = KhatmaDarkAccent
                        )
                    )
                }
            }

            // Linear Progress Bar
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = KhatmaDarkAccent,
                    trackColor = Color(0xFF222E38),
                    strokeCap = StrokeCap.Round
                )
            }

            // Current Reading Position Badge
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = KhatmaDarkElevated,
                border = BorderStroke(1.dp, KhatmaDarkBorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = null,
                            tint = KhatmaDarkAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = "Current Position",
                                style = MaterialTheme.typography.labelSmall.copy(color = KhatmaDarkTextMuted)
                            )
                            Text(
                                text = "${state.currentPosition.surahNameEnglish} (Ayah ${state.currentPosition.ayahNumber})",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = KhatmaDarkGoldBg,
                        border = BorderStroke(1.dp, KhatmaDarkGold.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "Juz ${state.currentPosition.juzNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = KhatmaDarkGold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Prominent "Continue Reading" Button
            Button(
                onClick = onContinueReading,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KhatmaDarkAccent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("continue_reading_button")
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Continue Reading (${state.nextReadingPosition.displayShort})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

/**
 * Today's Target & Split Sessions Card
 */
@Composable
fun KhatmaTodaySessionsCard(
    state: KhatmaFullDashboardState,
    onCompleteSession: (Int, Int) -> Unit,
    onReadSessionPortion: (Int, Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = KhatmaDarkCard),
        border = BorderStroke(1.dp, KhatmaDarkBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Today's Target",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
                    )
                    Text(
                        text = "${state.todayReadAyahs} / ${state.todayTargetAyahs} Ayahs",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (state.isTodayTargetAchieved) KhatmaDarkAccent else KhatmaDarkTextPrimary
                        )
                    )
                }

                if (state.isTodayTargetAchieved) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = KhatmaDarkAccent
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Today's Goal Done!",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                } else {
                    Text(
                        text = "${state.todayRemainingAyahs} Ayahs remaining",
                        style = MaterialTheme.typography.bodySmall.copy(color = KhatmaDarkTextSecondary)
                    )
                }
            }

            // Today's Linear Progress
            val todayProgress = (state.todayReadAyahs.toFloat() / state.todayTargetAyahs.toFloat()).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { todayProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = KhatmaDarkAccent,
                trackColor = Color(0xFF222E38),
                strokeCap = StrokeCap.Round
            )

            // Split Sessions List
            Text(
                text = "Daily Reflection Sessions (${state.dailySessions.size})",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = KhatmaDarkTextSecondary
                )
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.dailySessions.forEach { session ->
                    SessionRowItem(
                        session = session,
                        onComplete = { onCompleteSession(session.index, session.targetAyahsCount) },
                        onRead = { onReadSessionPortion(session.startAyahCoord.surahNumber, session.startAyahCoord.ayahNumber) }
                    )
                }
            }
        }
    }
}

@Composable
fun SessionRowItem(
    session: KhatmaSessionInfo,
    onComplete: () -> Unit,
    onRead: () -> Unit
) {
    val isDone = session.isCompleted
    val containerBg by animateColorAsState(
        if (isDone) KhatmaDarkAccentSoft else KhatmaDarkElevated,
        label = "sessionBg"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = containerBg,
        border = BorderStroke(
            1.dp,
            if (isDone) KhatmaDarkAccent.copy(alpha = 0.5f) else KhatmaDarkBorderLight
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = onComplete,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("session_complete_btn_${session.index}")
                ) {
                    Icon(
                        imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.CheckCircleOutline,
                        contentDescription = if (isDone) "Completed" else "Mark Complete",
                        tint = if (isDone) KhatmaDarkAccent else KhatmaDarkTextMuted
                    )
                }

                Column {
                    Text(
                        text = session.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isDone) KhatmaDarkAccent else KhatmaDarkTextPrimary
                        )
                    )
                    Text(
                        text = "${session.startAyahCoord.displayShort} → ${session.endAyahCoord.displayShort} (${session.targetAyahsCount} Ayahs)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = KhatmaDarkTextSecondary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            OutlinedButton(
                onClick = onRead,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isDone) KhatmaDarkAccent.copy(alpha = 0.4f) else KhatmaDarkBorderLight),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Text(
                    text = if (isDone) "Review" else "Read",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = KhatmaDarkAccent
                )
            }
        }
    }
}

/**
 * Pace Status & Smart Pace Adjuster Banner
 */
@Composable
fun KhatmaPaceBannerCard(
    state: KhatmaFullDashboardState,
    onOpenPaceAdjust: () -> Unit
) {
    val (bgColor, borderColor, icon, title, description) = when (state.paceStatus) {
        KhatmaPaceStatus.AHEAD -> Quintuple(
            KhatmaDarkCard,
            KhatmaDarkBorder,
            Icons.Default.TrendingUp,
            "Ahead of Schedule (+${state.paceDiffAyahs} Ayahs)",
            "Masha'Allah! You are reading ahead of your planned timeline. Keep this blessed momentum."
        )
        KhatmaPaceStatus.BEHIND -> Quintuple(
            KhatmaDarkCard,
            Color(0xFFE57373).copy(alpha = 0.5f),
            Icons.Default.Speed,
            "Behind Schedule (${state.paceDiffAyahs} Ayahs)",
            "Life happens. Choose a gentle catch-up pace or extend your timeline with peace and barakah."
        )
        KhatmaPaceStatus.ON_TRACK -> Quintuple(
            KhatmaDarkCard,
            KhatmaDarkBorder,
            Icons.Default.CheckCircle,
            "Right on Track",
            "You are adhering faithfully to your daily Khatma goals. May Allah accept every letter."
        )
        KhatmaPaceStatus.COMPLETED -> Quintuple(
            KhatmaDarkCard,
            KhatmaDarkBorder,
            Icons.Default.Star,
            "Khatma Completed! Alhamdulillah",
            "You have recited all 6,236 Ayahs of the Holy Quran."
        )
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (state.paceStatus == KhatmaPaceStatus.BEHIND) Color(0xFFEF5350) else KhatmaDarkAccent
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = KhatmaDarkTextPrimary
                    )
                )
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = KhatmaDarkTextSecondary
                )
            )

            if (state.paceStatus == KhatmaPaceStatus.BEHIND) {
                Button(
                    onClick = onOpenPaceAdjust,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KhatmaDarkAccent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Compassionate Pace Adjuster", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * 2x2 Stats Matrix (Estimated Date, Days Left, Total Surahs, Daily Target)
 */
@Composable
fun KhatmaStatsMatrix(state: KhatmaFullDashboardState) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatGridItem(
                title = "Est. Completion",
                value = state.estimatedCompletionDate,
                subtitle = "In ${state.daysRemaining} days",
                icon = Icons.Default.DateRange,
                modifier = Modifier.weight(1f)
            )
            StatGridItem(
                title = "Daily Average",
                value = "${state.todayTargetAyahs} Ayahs",
                subtitle = "${state.plan.dailySessionsCount} sessions/day",
                icon = Icons.Default.Speed,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatGridItem(
                title = "Current Surah",
                value = state.currentPosition.surahNameEnglish,
                subtitle = state.currentPosition.surahNameArabic,
                icon = Icons.Default.AutoStories,
                modifier = Modifier.weight(1f)
            )
            StatGridItem(
                title = "Current Juz",
                value = "Juz ${state.currentPosition.juzNumber}",
                subtitle = "of 30 Juz's",
                icon = Icons.Default.CheckCircle,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatGridItem(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = KhatmaDarkCard),
        border = BorderStroke(1.dp, KhatmaDarkBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(color = KhatmaDarkTextSecondary)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = KhatmaDarkAccent,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(color = KhatmaDarkTextSecondary),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Full Day Timeline / Calendar Tab
 */
@Composable
fun KhatmaTimelineView(
    timeline: List<KhatmaDayItem>,
    viewModel: MainViewModel
) {
    var filterMode by remember { mutableIntStateOf(0) } // 0: All, 1: Upcoming, 2: Completed

    val filteredList = remember(filterMode, timeline) {
        when (filterMode) {
            1 -> timeline.filter { it.isUpcoming || it.isToday }
            2 -> timeline.filter { it.isCompleted }
            else -> timeline
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            // Filter Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(
                    selected = filterMode == 0,
                    onClick = { filterMode = 0 },
                    label = { Text("All Days (${timeline.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = KhatmaDarkAccent,
                        selectedLabelColor = Color.White,
                        containerColor = KhatmaDarkCard,
                        labelColor = KhatmaDarkTextSecondary
                    ),
                    border = BorderStroke(1.dp, if (filterMode == 0) KhatmaDarkAccent else KhatmaDarkBorder)
                )
                FilterChip(
                    selected = filterMode == 1,
                    onClick = { filterMode = 1 },
                    label = { Text("Upcoming") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = KhatmaDarkAccent,
                        selectedLabelColor = Color.White,
                        containerColor = KhatmaDarkCard,
                        labelColor = KhatmaDarkTextSecondary
                    ),
                    border = BorderStroke(1.dp, if (filterMode == 1) KhatmaDarkAccent else KhatmaDarkBorder)
                )
                FilterChip(
                    selected = filterMode == 2,
                    onClick = { filterMode = 2 },
                    label = { Text("Completed") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = KhatmaDarkAccent,
                        selectedLabelColor = Color.White,
                        containerColor = KhatmaDarkCard,
                        labelColor = KhatmaDarkTextSecondary
                    ),
                    border = BorderStroke(1.dp, if (filterMode == 2) KhatmaDarkAccent else KhatmaDarkBorder)
                )
            }
        }

        items(filteredList) { item ->
            TimelineDayCard(
                dayItem = item,
                onReadDayPortion = {
                    viewModel.openKhatmaReadingAtAyah(item.startCoord.surahNumber, item.startCoord.ayahNumber)
                }
            )
        }
    }
}

@Composable
fun TimelineDayCard(
    dayItem: KhatmaDayItem,
    onReadDayPortion: () -> Unit
) {
    val isToday = dayItem.isToday
    val isCompleted = dayItem.isCompleted

    val containerBg = when {
        isToday -> KhatmaDarkAccentSoft
        else -> KhatmaDarkCard
    }

    val borderColor = when {
        isToday -> KhatmaDarkAccent
        else -> KhatmaDarkBorder
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerBg),
        border = BorderStroke(if (isToday) 1.5.dp else 1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Status Indicator Icon
                Surface(
                    shape = CircleShape,
                    color = when {
                        isCompleted -> KhatmaDarkAccent
                        isToday -> KhatmaDarkAccent.copy(alpha = 0.2f)
                        else -> KhatmaDarkElevated
                    },
                    border = BorderStroke(1.dp, if (isToday) KhatmaDarkAccent else KhatmaDarkBorderLight),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                text = "${dayItem.dayNumber}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isToday) KhatmaDarkAccent else KhatmaDarkTextSecondary
                                )
                            )
                        }
                    }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Day ${dayItem.dayNumber}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
                        )
                        Text(
                            text = "• ${dayItem.dateFormatted}",
                            style = MaterialTheme.typography.bodySmall.copy(color = KhatmaDarkTextSecondary)
                        )
                        if (isToday) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = KhatmaDarkAccent
                            ) {
                                Text(
                                    text = "TODAY",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = "${dayItem.startCoord.displayShort} → ${dayItem.endCoord.displayShort}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = KhatmaDarkTextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Text(
                        text = "${dayItem.targetAyahsCount} Ayahs • Juz ${dayItem.startCoord.juzNumber}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = KhatmaDarkTextSecondary
                        )
                    )
                }
            }

            OutlinedButton(
                onClick = onReadDayPortion,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isCompleted) KhatmaDarkAccent.copy(alpha = 0.4f) else KhatmaDarkBorderLight),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = if (isCompleted) "Review" else "Read",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = KhatmaDarkAccent
                )
            }
        }
    }
}

/**
 * Pace Badge Indicator
 */
@Composable
fun PaceBadge(status: KhatmaPaceStatus, diff: Int) {
    val (bg, text, border, label) = when (status) {
        KhatmaPaceStatus.AHEAD -> Quadruple(
            KhatmaDarkGoldBg,
            KhatmaDarkGold,
            KhatmaDarkGold.copy(alpha = 0.4f),
            "+$diff Ahead"
        )
        KhatmaPaceStatus.BEHIND -> Quadruple(
            Color(0xFF3B1E22),
            Color(0xFFEF5350),
            Color(0xFFEF5350).copy(alpha = 0.5f),
            "$diff Behind"
        )
        KhatmaPaceStatus.ON_TRACK -> Quadruple(
            KhatmaDarkAccentSoft,
            KhatmaDarkAccent,
            KhatmaDarkAccent.copy(alpha = 0.4f),
            "On Track"
        )
        KhatmaPaceStatus.COMPLETED -> Quadruple(
            KhatmaDarkAccent,
            Color.White,
            KhatmaDarkAccent,
            "Completed"
        )
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = bg,
        border = BorderStroke(1.dp, border)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = text
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

/**
 * Bottom Sheet for Settings & Changing Duration / Reset
 */
@Composable
fun KhatmaSettingsSheetContent(
    viewModel: MainViewModel,
    state: KhatmaFullDashboardState?,
    onDismiss: () -> Unit,
    onOpenPaceAdjust: () -> Unit
) {
    if (state == null) return

    var selectedDays by remember { mutableIntStateOf(state.totalDays) }
    var reminderEnabled by remember { mutableStateOf(state.plan.reminderEnabled) }
    var reminderTime by remember { mutableStateOf(state.plan.reminderTime) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Khatma Plan Settings",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
        )

        // Duration Adjustment
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Change Total Duration",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = KhatmaDarkTextPrimary)
            )
            val presets = listOf(7, 15, 30, 45, 60, 90)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(presets) { days ->
                    FilterChip(
                        selected = selectedDays == days,
                        onClick = {
                            selectedDays = days
                            viewModel.changeKhatmaTotalDays(days)
                        },
                        label = { Text("$days Days") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = KhatmaDarkAccent,
                            selectedLabelColor = Color.White,
                            containerColor = KhatmaDarkElevated,
                            labelColor = KhatmaDarkTextSecondary
                        ),
                        border = BorderStroke(1.dp, if (selectedDays == days) KhatmaDarkAccent else KhatmaDarkBorder)
                    )
                }
            }
        }

        // Reminder Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Daily Reminder",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = KhatmaDarkTextPrimary)
                )
                Text(
                    text = if (reminderEnabled) "Reminder at $reminderTime" else "Disabled",
                    style = MaterialTheme.typography.bodySmall.copy(color = KhatmaDarkTextSecondary)
                )
            }
            Switch(
                checked = reminderEnabled,
                onCheckedChange = {
                    reminderEnabled = it
                    viewModel.updateKhatmaReminder(it, reminderTime)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = KhatmaDarkAccent,
                    uncheckedThumbColor = KhatmaDarkTextMuted,
                    uncheckedTrackColor = KhatmaDarkBorder
                )
            )
        }

        // Compassionate Pace Adjustment
        Button(
            onClick = onOpenPaceAdjust,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KhatmaDarkAccent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Speed, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Open Pace Adjuster",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Reset / Delete Plan
        OutlinedButton(
            onClick = { showDeleteConfirm = true },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF5350)),
            border = BorderStroke(1.dp, Color(0xFFEF5350).copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color(0xFFEF5350))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reset Khatma Plan", color = Color(0xFFEF5350), fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Reset Khatma?", fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary) },
            text = { Text("Are you sure you want to reset your current Khatma plan? You can start a new one anytime.", color = KhatmaDarkTextSecondary) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteActiveKhatma()
                        showDeleteConfirm = false
                        onDismiss()
                    }
                ) {
                    Text("Reset Plan", color = Color(0xFFEF5350), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel", color = KhatmaDarkTextSecondary)
                }
            },
            containerColor = KhatmaDarkCard
        )
    }
}

/**
 * Compassionate Pace Adjustment Sheet
 */
@Composable
fun KhatmaPaceAdjustmentSheetContent(
    viewModel: MainViewModel,
    state: KhatmaFullDashboardState?,
    onDismiss: () -> Unit
) {
    if (state == null) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Smart Pace Adjuster",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
        )
        Text(
            text = "Reciting the Quran is a spiritual relationship built on devotion, not stress. Choose how you would like to comfortably adapt your reading goals:",
            style = MaterialTheme.typography.bodyMedium.copy(color = KhatmaDarkTextSecondary)
        )

        // Option 1: Spread evenly
        OutlinedCard(
            onClick = {
                viewModel.adjustKhatmaPace("SPREAD")
                onDismiss()
            },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = KhatmaDarkElevated),
            border = BorderStroke(1.dp, KhatmaDarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. Spread Evenly Across Remaining Days",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Recalculates the remaining ${KhatmaEngine.TOTAL_QURAN_AYAHS - state.readAyahsCount} Ayahs equally over the remaining ${state.daysRemaining} days.",
                    style = MaterialTheme.typography.bodySmall.copy(color = KhatmaDarkTextSecondary)
                )
            }
        }

        // Option 2: Catch up gradually
        OutlinedCard(
            onClick = {
                viewModel.adjustKhatmaPace("GRADUAL")
                onDismiss()
            },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = KhatmaDarkElevated),
            border = BorderStroke(1.dp, KhatmaDarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2. Catch Up Gradually (+15 Ayahs / Day)",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Adds a small, manageable booster to your daily sessions until you are back on track.",
                    style = MaterialTheme.typography.bodySmall.copy(color = KhatmaDarkTextSecondary)
                )
            }
        }

        // Option 3: Extend completion deadline
        OutlinedCard(
            onClick = {
                viewModel.adjustKhatmaPace("EXTEND")
                onDismiss()
            },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = KhatmaDarkElevated),
            border = BorderStroke(1.dp, KhatmaDarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "3. Extend Completion Deadline",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Maintains a calm, comfortable daily pace and smoothly pushes the target completion date outward.",
                    style = MaterialTheme.typography.bodySmall.copy(color = KhatmaDarkTextSecondary)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Khatma History Sheet
 */
@Composable
fun KhatmaHistorySheetContent(
    historyList: List<KhatmaHistoryEntity>,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Completed Khatmas History",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
        )

        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        tint = KhatmaDarkTextMuted.copy(alpha = 0.5f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "No completed Khatmas yet",
                        style = MaterialTheme.typography.bodyMedium.copy(color = KhatmaDarkTextPrimary, fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "Your completed Quran milestones will be preserved here.",
                        style = MaterialTheme.typography.bodySmall.copy(color = KhatmaDarkTextSecondary)
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(historyList) { item ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = KhatmaDarkElevated),
                        border = BorderStroke(1.dp, KhatmaDarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary)
                                )
                                Text(
                                    text = "Completed in ${item.daysTaken} days • ${item.completionDateFormatted}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = KhatmaDarkTextSecondary)
                                )
                                Text(
                                    text = "6,236 Ayahs • Full Quran",
                                    style = MaterialTheme.typography.labelSmall.copy(color = KhatmaDarkAccent, fontWeight = FontWeight.Bold)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = KhatmaDarkAccent,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Khatma Setup Sheet Content (for editing / creating)
 */
@Composable
fun KhatmaSetupSheetContent(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    KhatmaSetupView(
        viewModel = viewModel,
        isExistingKhatmaCompleted = false,
        onOpenHistory = {
            onDismiss()
            viewModel.isKhatmaHistorySheetOpen.value = true
        }
    )
}

/**
 * Serene Celebration Dialog with Dua Khatm Al-Quran
 */
@Composable
fun KhatmaCompletionCelebrationDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    viewModel.isKhatmaSetupSheetOpen.value = true
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KhatmaDarkAccent)
            ) {
                Text("Start a New Khatma", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = KhatmaDarkTextSecondary)
            }
        },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "الحمد لله رب العالمين 🤍",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = KhatmaDarkAccent
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Khatma Completed!",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary),
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "May Allah accept your recitation, make the Quran a guiding light for your heart, and elevate your rank in Jannah.",
                        style = MaterialTheme.typography.bodySmall.copy(color = KhatmaDarkTextSecondary),
                        textAlign = TextAlign.Center
                    )
                }

                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = KhatmaDarkElevated,
                        border = BorderStroke(1.dp, KhatmaDarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "دعاء ختم القرآن الكريم",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = KhatmaDarkAccent
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = KhatmaEngine.DUA_KHATM_ARABIC,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 22.sp,
                                    textAlign = TextAlign.Right,
                                    color = KhatmaDarkTextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = KhatmaEngine.DUA_KHATM_TRANSLATION,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = KhatmaDarkTextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        },
        containerColor = KhatmaDarkCard
    )
}

/**
 * Quick Log Ayahs Dialog
 */
@Composable
fun QuickLogAyahsDialog(
    onDismiss: () -> Unit,
    onAdd: (Int) -> Unit
) {
    val options = listOf(5, 10, 20, 50)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Ayahs Read", fontWeight = FontWeight.Bold, color = KhatmaDarkTextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Select the number of Ayahs read to advance your Khatma progress:", color = KhatmaDarkTextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    options.forEach { count ->
                        OutlinedButton(
                            onClick = { onAdd(count) },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, KhatmaDarkBorderLight),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+$count", color = KhatmaDarkAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = KhatmaDarkTextSecondary)
            }
        },
        containerColor = KhatmaDarkCard
    )
}

private data class Quintuple<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

private fun Int.formatNumber(): String {
    return String.format(Locale.getDefault(), "%,d", this)
}
