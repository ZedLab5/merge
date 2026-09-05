package com.example.ui.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Build
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Widgets
import com.example.widget.PrayerWidgetReceiver
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.MainViewModel

import com.example.ui.theme.ReadingThemes
import com.example.ui.theme.ReadingThemeColors

// Palette tokens matching the clean Islamic light canvas
private val NoorTealDark = Color(0xFF099382)
private val NoorTealVibrant = Color(0xFF13A795)
private val NoorDarkPine = Color(0xFF10261F)
private val NoorSageSlate = Color(0xFF5A756C)
private val NoorGoldAccent = Color(0xFFD4A340)
private val NoorGoldSoft = Color(0xFFFAF3E6)
private val NoorGoldBorder = Color(0xFFE8D4A8)
private val NoorCardBorder = Color(0xFFE2EBE6)
private val NoorSurfaceSoft = Color(0xFFF6FAF8)
private val NoorSoftGreenBg = Color(0xFFF2F8F5)
private val NoorSoftGreenBorder = Color(0xFFCCE4DC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsModal(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val themeColors = remember(isDarkMode) { if (isDarkMode) ReadingThemes.ObsidianNight else ReadingThemes.MadaniCrisp }

    val showArabicSecondary by viewModel.showArabicSecondaryText.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val morningAzkarNotif by viewModel.morningEveningAzkarNotification.collectAsStateWithLifecycle()
    val dailyAyahNotif by viewModel.dailyAyahNotification.collectAsStateWithLifecycle()
    val qazaNotif by viewModel.qazaReminderNotification.collectAsStateWithLifecycle()
    val vibrateAdhan by viewModel.vibrationOnAdhan.collectAsStateWithLifecycle()
    val adhanVolume by viewModel.adhanSoundVolume.collectAsStateWithLifecycle()

    // Independent expansion state for each individual drop-down accordion
    var isThemeExpanded by remember { mutableStateOf(false) }
    var isLanguageExpanded by remember { mutableStateOf(false) }
    var isNotifExpanded by remember { mutableStateOf(false) }
    var isCalcExpanded by remember { mutableStateOf(false) }
    var isWidgetExpanded by remember { mutableStateOf(false) }
    var isContactExpanded by remember { mutableStateOf(false) }
    var isPrivacyExpanded by remember { mutableStateOf(false) }
    var isAboutExpanded by remember { mutableStateOf(false) }

    var showLanguageSelector by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 8.dp)
        ) {
            // Elegant Drag Handle Pill with generous breathing room
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp, bottom = 12.dp)
                    .size(width = 42.dp, height = 4.5.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.outline)
            )

            // Header Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.linearGradient(listOf(NoorTealDark, NoorTealVibrant))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Text(
                                text = stringResource(R.string.settings_title),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 20.sp
                                )
                            )
                            Text(
                                text = stringResource(R.string.settings_subtitle),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = themeColors.translationText,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isDarkMode) themeColors.border else NoorSurfaceSoft)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = themeColors.arabicText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Scrollable Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. THEME ACCORDION (Simplified Light / Dark Switch Only)
                item(key = "section_theme") {
                    SettingsAccordionCard(
                        icon = if (isDarkMode) Icons.Default.Palette else Icons.Default.LightMode,
                        title = stringResource(R.string.settings_section_theme),
                        subtitle = if (isDarkMode) "Dark Mode (Obsidian Night) active" else "Light Mode active",
                        isExpanded = isThemeExpanded,
                        onToggleExpand = { isThemeExpanded = !isThemeExpanded },
                        themeColors = themeColors
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Dark Mode",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = themeColors.arabicText,
                                        fontSize = 14.5.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = if (isDarkMode) "Obsidian Night active app-wide" else "Default clean light theme active",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = themeColors.translationText,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                )
                            }

                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { checked ->
                                    viewModel.setSharedReadingTheme(if (checked) "Obsidian Night" else "Madani Crisp")
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = themeColors.accent,
                                    uncheckedThumbColor = themeColors.translationText,
                                    uncheckedTrackColor = themeColors.border
                                )
                            )
                        }
                    }
                }

                // 2. LANGUAGE & LOCALIZATION ACCORDION (Primary + Secondary Arabic Toggle)
                item(key = "section_language") {
                    SettingsAccordionCard(
                        icon = Icons.Default.Language,
                        title = stringResource(R.string.settings_section_language),
                        subtitle = stringResource(R.string.settings_language_sub),
                        isExpanded = isLanguageExpanded,
                        onToggleExpand = { isLanguageExpanded = !isLanguageExpanded },
                        themeColors = themeColors
                    ) {
                        // Global App Language Selector
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (themeColors.isDark) themeColors.border else NoorSurfaceSoft)
                                .clickable { showLanguageSelector = !showLanguageSelector }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = stringResource(R.string.settings_primary_language),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = themeColors.translationText,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    )
                                )
                                Text(
                                    text = "$appLanguage",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = themeColors.arabicText,
                                        fontSize = 14.sp
                                    )
                                )
                            }

                            Icon(
                                imageVector = if (showLanguageSelector) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = themeColors.accent
                            )
                        }

                        AnimatedVisibility(
                            visible = showLanguageSelector,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val languages = listOf(
                                    "English" to "English (Default)",
                                    "Arabic" to "العربية (Arabic)",
                                    "French" to "Français (French)",
                                    "Urdu" to "اردو (Urdu)",
                                    "Indonesian" to "Bahasa Indonesia",
                                    "Turkish" to "Türkçe (Turkish)"
                                )

                                languages.forEach { (code, label) ->
                                    val isSelected = appLanguage == code
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.setAppLanguage(code)
                                                showLanguageSelector = false
                                            },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) (if (themeColors.isDark) themeColors.border else NoorSoftGreenBg) else (if (themeColors.isDark) themeColors.surface else Color.White),
                                        border = BorderStroke(1.dp, if (isSelected) themeColors.accent else themeColors.border)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = label,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) themeColors.accent else themeColors.arabicText,
                                                    fontSize = 13.sp
                                                )
                                            )
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = themeColors.accent,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = themeColors.border.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.height(14.dp))

                        // GLOBAL ARABIC SECONDARY LAYER TOGGLE
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.settings_show_arabic_secondary),
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = themeColors.arabicText,
                                        fontSize = 14.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = stringResource(R.string.settings_show_arabic_desc),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = themeColors.translationText,
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp
                                    )
                                )
                            }

                            Switch(
                                checked = showArabicSecondary,
                                onCheckedChange = { viewModel.toggleArabicSecondaryText(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = themeColors.accent,
                                    uncheckedThumbColor = themeColors.translationText,
                                    uncheckedTrackColor = themeColors.border
                                )
                            )
                        }
                    }
                }

                // 3. NOTIFICATIONS & REMINDERS ACCORDION
                item(key = "section_notifications") {
                    SettingsAccordionCard(
                        icon = Icons.Default.Notifications,
                        title = stringResource(R.string.settings_section_notifications),
                        subtitle = stringResource(R.string.settings_notif_sub),
                        isExpanded = isNotifExpanded,
                        onToggleExpand = { isNotifExpanded = !isNotifExpanded },
                        themeColors = themeColors
                    ) {
                        NotificationToggleRow(
                            title = stringResource(R.string.settings_notif_azkar),
                            description = stringResource(R.string.settings_notif_azkar_desc),
                            checked = morningAzkarNotif,
                            onCheckedChange = { viewModel.toggleMorningEveningAzkarNotification() },
                            themeColors = themeColors
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        NotificationToggleRow(
                            title = stringResource(R.string.settings_notif_daily_ayah),
                            description = stringResource(R.string.settings_notif_ayah_desc),
                            checked = dailyAyahNotif,
                            onCheckedChange = { viewModel.toggleDailyAyahNotification() },
                            themeColors = themeColors
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        NotificationToggleRow(
                            title = stringResource(R.string.settings_notif_qaza),
                            description = stringResource(R.string.settings_notif_qaza_desc),
                            checked = qazaNotif,
                            onCheckedChange = { viewModel.toggleQazaReminderNotification() },
                            themeColors = themeColors
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        NotificationToggleRow(
                            title = stringResource(R.string.settings_notif_vibrate_adhan),
                            description = stringResource(R.string.settings_notif_vibrate_desc),
                            checked = vibrateAdhan,
                            onCheckedChange = { viewModel.toggleVibrationOnAdhan() },
                            themeColors = themeColors
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Adhan Volume Slider
                        Column {
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
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = null,
                                        tint = themeColors.accent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.settings_adhan_volume),
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = themeColors.arabicText
                                        )
                                    )
                                }
                                Text(
                                    text = "$adhanVolume%",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = themeColors.accent
                                    )
                                )
                            }
                            Slider(
                                value = adhanVolume.toFloat(),
                                onValueChange = { viewModel.setAdhanSoundVolume(it.toInt()) },
                                valueRange = 0f..100f,
                                colors = SliderDefaults.colors(
                                    thumbColor = themeColors.accent,
                                    activeTrackColor = themeColors.accent,
                                    inactiveTrackColor = themeColors.border
                                )
                            )
                        }
                    }
                }

                // 4. PRAYER CALCULATION ACCORDION
                item(key = "section_calculation") {
                    SettingsAccordionCard(
                        icon = Icons.Default.Calculate,
                        title = stringResource(R.string.settings_section_calc),
                        subtitle = stringResource(R.string.settings_calc_sub),
                        trailingBadge = "MWL",
                        isExpanded = isCalcExpanded,
                        onToggleExpand = { isCalcExpanded = !isCalcExpanded },
                        themeColors = themeColors
                    ) {
                        Text(
                            text = stringResource(R.string.settings_calc_details),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = themeColors.translationText,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }

                // 5. HOME SCREEN WIDGET ACCORDION
                item(key = "section_widget") {
                    SettingsAccordionCard(
                        icon = Icons.Default.Widgets,
                        title = stringResource(R.string.settings_section_widget),
                        subtitle = stringResource(R.string.settings_widget_sub),
                        isExpanded = isWidgetExpanded,
                        onToggleExpand = { isWidgetExpanded = !isWidgetExpanded },
                        themeColors = themeColors
                    ) {
                        Text(
                            text = stringResource(R.string.settings_widget_desc),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = themeColors.translationText,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        val appWidgetManager = remember { AppWidgetManager.getInstance(context) }
                        val isPinSupported = remember {
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                                    appWidgetManager != null &&
                                    appWidgetManager.isRequestPinAppWidgetSupported
                        }

                        if (isPinSupported) {
                            Button(
                                onClick = {
                                    val provider = ComponentName(context, PrayerWidgetReceiver::class.java)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        try {
                                            val callbackIntent = Intent(context, PrayerWidgetReceiver::class.java).apply {
                                                action = "com.example.ACTION_WIDGET_PINNED"
                                            }
                                            val successPendingIntent = PendingIntent.getBroadcast(
                                                context,
                                                0,
                                                callbackIntent,
                                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                            )
                                            val isRequested = appWidgetManager.requestPinAppWidget(provider, null, successPendingIntent)
                                            if (isRequested) {
                                                viewModel.showToast(
                                                    if (viewModel.isArabicLanguage()) "تم إرسال طلب إضافة الودجت للشاشة الرئيسية"
                                                    else "Add widget request sent to home screen!"
                                                )
                                            } else {
                                                viewModel.showToast(
                                                    if (viewModel.isArabicLanguage()) "اضغط مطولاً على الشاشة الرئيسية > الودجت > نور لإضافته"
                                                    else "To add: Long press Home Screen -> Widgets -> Noor"
                                                )
                                            }
                                        } catch (e: Exception) {
                                            viewModel.showToast(
                                                if (viewModel.isArabicLanguage()) "اضغط مطولاً على الشاشة الرئيسية > الودجت > نور"
                                                else "Long press Home Screen -> Widgets -> Noor"
                                            )
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = themeColors.accent),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Widgets,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.settings_widget_pin_button),
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        // Manual Step-by-step guidance card
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = if (themeColors.isDark) themeColors.surface else NoorSurfaceSoft,
                            border = BorderStroke(1.dp, themeColors.border)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = stringResource(R.string.settings_widget_manual_title),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = themeColors.arabicText,
                                        fontSize = 12.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = stringResource(R.string.settings_widget_step_1),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = themeColors.translationText,
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = stringResource(R.string.settings_widget_step_2),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = themeColors.translationText,
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = stringResource(R.string.settings_widget_step_3),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = themeColors.translationText,
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // 6. INDEPENDENT ACCORDION: CONTACT US & SUPPORT
                item(key = "section_contact_us") {
                    SettingsAccordionCard(
                        icon = Icons.Default.Email,
                        title = stringResource(R.string.settings_section_contact),
                        subtitle = stringResource(R.string.settings_contact_sub),
                        isExpanded = isContactExpanded,
                        onToggleExpand = { isContactExpanded = !isContactExpanded },
                        themeColors = themeColors
                    ) {
                        Text(
                            text = stringResource(R.string.settings_contact_desc),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = themeColors.translationText,
                                fontSize = 12.sp,
                                lineHeight = 16.5.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:support@alnoorapp.com")
                                        putExtra(Intent.EXTRA_SUBJECT, "Al-Noor App Feedback & Support")
                                    }
                                    try {
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("Support Email", "support@alnoorapp.com"))
                                        viewModel.showToast("Copied support@alnoorapp.com to clipboard")
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = themeColors.accent),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(stringResource(R.string.settings_email_support), color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Support Email", "support@alnoorapp.com"))
                                    viewModel.showToast("Email address copied: support@alnoorapp.com")
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, themeColors.accent.copy(alpha = 0.5f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = themeColors.accent),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    tint = themeColors.accent,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(stringResource(R.string.settings_copy_email), color = themeColors.accent, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // 6. INDEPENDENT ACCORDION: PRIVACY POLICY & SECURITY
                item(key = "section_privacy_policy") {
                    SettingsAccordionCard(
                        icon = Icons.Default.Security,
                        title = stringResource(R.string.settings_section_privacy),
                        subtitle = stringResource(R.string.settings_privacy_sub),
                        isExpanded = isPrivacyExpanded,
                        onToggleExpand = { isPrivacyExpanded = !isPrivacyExpanded },
                        themeColors = themeColors
                    ) {
                        Text(
                            text = stringResource(R.string.settings_privacy_desc),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = themeColors.translationText,
                                fontSize = 12.sp,
                                lineHeight = 16.5.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (themeColors.isDark) themeColors.border else NoorSoftGreenBg)
                                .border(1.dp, if (themeColors.isDark) themeColors.accent.copy(alpha = 0.3f) else NoorSoftGreenBorder, RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PrivacyItem(title = stringResource(R.string.settings_privacy_item1_title), detail = stringResource(R.string.settings_privacy_item1_desc), themeColors = themeColors)
                            PrivacyItem(title = stringResource(R.string.settings_privacy_item2_title), detail = stringResource(R.string.settings_privacy_item2_desc), themeColors = themeColors)
                            PrivacyItem(title = stringResource(R.string.settings_privacy_item3_title), detail = stringResource(R.string.settings_privacy_item3_desc), themeColors = themeColors)
                        }
                    }
                }

                // 7. INDEPENDENT ACCORDION: ABOUT US & MISSION
                item(key = "section_about_us") {
                    SettingsAccordionCard(
                        icon = Icons.Default.Info,
                        title = stringResource(R.string.settings_section_about),
                        subtitle = stringResource(R.string.settings_about_sub),
                        isExpanded = isAboutExpanded,
                        onToggleExpand = { isAboutExpanded = !isAboutExpanded },
                        themeColors = themeColors
                    ) {
                        Text(
                            text = stringResource(R.string.settings_about_desc),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = themeColors.translationText,
                                fontSize = 12.sp,
                                lineHeight = 16.5.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (themeColors.isDark) themeColors.border else NoorSurfaceSoft)
                                .border(1.dp, themeColors.border, RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.settings_about_sources_title),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.accent,
                                    fontSize = 11.5.sp
                                )
                            )
                            Text(
                                text = stringResource(R.string.settings_about_sources_list),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = themeColors.arabicText,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }

                // 8. SHARE APP UTILITIES (Clean Standalone Card)
                item(key = "section_share") {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = themeColors.surface,
                        border = BorderStroke(1.dp, themeColors.border)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(11.dp))
                                        .background(if (themeColors.isDark) themeColors.border else NoorSoftGreenBg)
                                        .border(1.dp, if (themeColors.isDark) themeColors.accent.copy(alpha = 0.3f) else NoorSoftGreenBorder, RoundedCornerShape(11.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = null,
                                        tint = themeColors.accent,
                                        modifier = Modifier.size(19.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Share Al-Noor",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = themeColors.arabicText,
                                            fontSize = 15.5.sp
                                        )
                                    )
                                    Text(
                                        text = "Spread beneficial knowledge with loved ones",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = themeColors.translationText,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "\"Whoever guides someone to goodness will have a reward like one who did it.\" (Sahih Muslim)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = themeColors.arabicText,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    fontSize = 12.sp,
                                    lineHeight = 16.5.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.Transparent,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(com.example.ui.components.NoorTopBarGradient)
                                        .clickable {
                                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(
                                                    Intent.EXTRA_TEXT,
                                                    "Assalamu Alaikum! Check out Al-Noor – Your spiritual companion with verified Quran, prayer times, authentic Du'as, Khatma plans, and Ask Noor AI.\n\nhttps://alnoorapp.com"
                                                )
                                            }
                                            context.startActivity(Intent.createChooser(shareIntent, "Share Al-Noor via"))
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Share App", color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (themeColors.isDark) themeColors.border else Color(0xFFF4FAF7),
                                    border = BorderStroke(1.dp, themeColors.border),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            clipboard.setPrimaryClip(ClipData.newPlainText("Al-Noor Link", "https://alnoorapp.com"))
                                            viewModel.showToast("App share link copied to clipboard!")
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = null,
                                            tint = themeColors.accent,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Copy Link",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = themeColors.accent,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.5.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 9. FOLLOW US ON SOCIAL MEDIA (Clean Standalone Card)
                item(key = "section_social") {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = themeColors.surface,
                        border = BorderStroke(1.dp, themeColors.border)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(11.dp))
                                        .background(if (themeColors.isDark) themeColors.border else NoorSoftGreenBg)
                                        .border(1.dp, if (themeColors.isDark) themeColors.accent.copy(alpha = 0.3f) else NoorSoftGreenBorder, RoundedCornerShape(11.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Public,
                                        contentDescription = null,
                                        tint = themeColors.accent,
                                        modifier = Modifier.size(19.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Follow Us",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = themeColors.arabicText,
                                            fontSize = 15.5.sp
                                        )
                                    )
                                    Text(
                                        text = "Official social channels & updates",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = themeColors.translationText,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SocialChannelChip(
                                    label = "𝕏 Twitter",
                                    handle = "@AlNoorIslamic",
                                    themeColors = themeColors,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    onClick = {
                                        viewModel.showToast("Opening @AlNoorIslamic on 𝕏")
                                    }
                                )
                                SocialChannelChip(
                                    label = "YouTube",
                                    handle = "@AlNoorApp",
                                    themeColors = themeColors,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    onClick = {
                                        viewModel.showToast("Opening @AlNoorApp on YouTube")
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SocialChannelChip(
                                    label = "Telegram",
                                    handle = "t.me/AlNoorApp",
                                    themeColors = themeColors,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    onClick = {
                                        viewModel.showToast("Opening Al-Noor Telegram channel")
                                    }
                                )
                                SocialChannelChip(
                                    label = "Instagram",
                                    handle = "@AlNoor.App",
                                    themeColors = themeColors,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    onClick = {
                                        viewModel.showToast("Opening @AlNoor.App on Instagram")
                                    }
                                )
                            }
                        }
                    }
                }

                item(key = "bottom_space") {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun SettingsAccordionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    trailingBadge: String? = null,
    themeColors: ReadingThemeColors = ReadingThemes.MadaniCrisp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = themeColors.surface,
        border = BorderStroke(1.dp, if (isExpanded) themeColors.accent.copy(alpha = 0.5f) else themeColors.border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggleExpand),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(if (themeColors.isDark) themeColors.border else (if (isExpanded) NoorSoftGreenBg else NoorSurfaceSoft))
                            .border(1.dp, if (isExpanded) themeColors.accent.copy(alpha = 0.3f) else themeColors.border, RoundedCornerShape(11.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = themeColors.accent,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.arabicText,
                                    fontSize = 15.sp
                                )
                            )
                            if (trailingBadge != null) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (themeColors.isDark) themeColors.border else NoorSoftGreenBg,
                                    border = BorderStroke(0.8.dp, if (themeColors.isDark) themeColors.accent.copy(alpha = 0.3f) else NoorSoftGreenBorder)
                                ) {
                                    Text(
                                        text = trailingBadge,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = themeColors.accent
                                        )
                                    )
                                }
                            }
                        }
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = themeColors.translationText,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = if (themeColors.isDark) themeColors.border else (if (isExpanded) NoorSoftGreenBg else NoorSurfaceSoft),
                    border = BorderStroke(1.dp, if (isExpanded) themeColors.accent.copy(alpha = 0.4f) else themeColors.border),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = if (isExpanded) themeColors.accent else themeColors.translationText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                ) {
                    HorizontalDivider(color = themeColors.border.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(12.dp))
                    content()
                }
            }
        }
    }
}

@Composable
private fun NotificationToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    themeColors: ReadingThemeColors = ReadingThemes.MadaniCrisp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    color = themeColors.arabicText
                )
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = themeColors.translationText,
                    fontSize = 11.5.sp,
                    lineHeight = 15.sp
                )
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = themeColors.accent,
                uncheckedThumbColor = themeColors.translationText,
                uncheckedTrackColor = themeColors.border
            )
        )
    }
}

@Composable
private fun PaletteSwatch(
    color: Color,
    name: String,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color,
        border = BorderStroke(1.dp, NoorCardBorder),
        modifier = modifier.height(32.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else NoorDarkPine
                )
            )
        }
    }
}

@Composable
private fun PrivacyItem(
    title: String,
    detail: String,
    themeColors: ReadingThemeColors = ReadingThemes.MadaniCrisp
) {
    Column {
        Text(
            text = "• $title",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = themeColors.accent,
                fontSize = 12.sp
            )
        )
        Text(
            text = detail,
            style = MaterialTheme.typography.bodySmall.copy(
                color = themeColors.arabicText.copy(alpha = 0.85f),
                fontSize = 11.5.sp,
                lineHeight = 15.sp
            )
        )
    }
}

@Composable
private fun SocialChannelChip(
    label: String,
    handle: String,
    modifier: Modifier = Modifier,
    themeColors: ReadingThemeColors = ReadingThemes.MadaniCrisp,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (themeColors.isDark) themeColors.border else NoorSurfaceSoft,
        border = BorderStroke(1.dp, themeColors.border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = themeColors.arabicText,
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = handle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = themeColors.translationText,
                        fontSize = 10.5.sp
                    )
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = themeColors.accent,
                modifier = Modifier.size(13.dp)
            )
        }
    }
}
