package com.example.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.HomeWidgetType
import com.example.ui.MainViewModel
import com.example.ui.NoorDestination
import com.example.ui.components.KhatmaMilestoneCelebrationDialog
import com.example.ui.theme.CanvasMint
import com.example.ui.theme.ReadingThemes

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val readingThemeName by viewModel.sharedReadingTheme.collectAsStateWithLifecycle()
    val themeColors = remember(readingThemeName) { ReadingThemes.getThemeByName(readingThemeName) }
    val isDark = themeColors.isDark

    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val location by viewModel.locationName.collectAsStateWithLifecycle()
    val nextPrayerName by viewModel.nextPrayerName.collectAsStateWithLifecycle()
    val nextPrayerTime by viewModel.nextPrayerTimeStr.collectAsStateWithLifecycle()
    val prayers by viewModel.prayerTimes.collectAsStateWithLifecycle()
    val selectedMood by viewModel.selectedMood.collectAsStateWithLifecycle()
    val isIslamic by viewModel.isIslamicWisdomMode.collectAsStateWithLifecycle()
    val khatmaState by viewModel.khatmaDashboardState.collectAsStateWithLifecycle()
    val toastMsg by viewModel.toastMessage.collectAsStateWithLifecycle()

    val widgetsOrder by viewModel.homeWidgetsOrder.collectAsStateWithLifecycle()
    val widgetsVisibility by viewModel.homeWidgetsVisibility.collectAsStateWithLifecycle()
    val isCustomizeSheetOpen by viewModel.isCustomizeHomeSheetOpen.collectAsStateWithLifecycle()
    val isCustomizeQuickAccessOpen by viewModel.isCustomizeQuickAccessSheetOpen.collectAsStateWithLifecycle()
    val quickAccessTools by viewModel.quickAccessTools.collectAsStateWithLifecycle()
    val milestoneData by viewModel.khatmaMilestoneModal.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMsg) {
        toastMsg?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    val screenBg = if (isDark) themeColors.background else Color.White

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(screenBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // 1. Profile Row (Directly on page background)
            item(key = "profile_row") {
                UserProfileRow(
                    viewModel = viewModel,
                    userName = userName,
                    location = location
                )
            }

            // 2. Hero Prayer Card (Self-contained rounded rectangle card with margin)
            item(key = "hero_prayer_card") {
                HeroPrayerCard(
                    viewModel = viewModel,
                    nextPrayerName = nextPrayerName,
                    nextPrayerTime = nextPrayerTime,
                    prayers = prayers
                )
            }

            // Dynamically Ordered & Toggled Widgets Feed
            widgetsOrder.forEach { widgetType ->
                val isVisible = widgetsVisibility[widgetType] ?: widgetType.defaultVisible
                if (isVisible) {
                    val boxModifier = Modifier.padding(horizontal = 16.dp)

                    when (widgetType) {
                        HomeWidgetType.SALAT_TIMELINE -> {
                            item(key = "prayer_tracker_section") {
                                Box(
                                    modifier = boxModifier
                                ) {
                                    ChronologicalPrayerTracker(
                                        viewModel = viewModel,
                                        prayers = prayers
                                    )
                                }
                            }
                        }

                        HomeWidgetType.SPIRITUAL_ESSENTIALS -> {
                            item(key = "spiritual_essentials_section") {
                                Box(
                                    modifier = boxModifier
                                ) {
                                    SpiritualEssentialsGrid(viewModel = viewModel)
                                }
                            }
                        }

                        HomeWidgetType.FAVORITES_CAROUSEL -> {
                            item(key = "favorites_carousel_section") {
                                Box(
                                    modifier = boxModifier
                                ) {
                                    HomeFavoritesCarousel(viewModel = viewModel)
                                }
                            }
                        }

                        HomeWidgetType.UNIFIED_STREAKS -> {
                            item(key = "unified_streaks_section") {
                                Box(
                                    modifier = boxModifier
                                ) {
                                    UnifiedStreakHomeCard(viewModel = viewModel)
                                }
                            }
                        }

                        HomeWidgetType.DAILY_REVELATION -> {
                            item(key = "ayah_dua_section") {
                                Box(
                                    modifier = boxModifier
                                ) {
                                    DailyAyahAndDuaShowcase(viewModel = viewModel)
                                }
                            }
                        }

                        HomeWidgetType.KHATMA_TRACKER -> {
                            item(key = "quran_khatma_section") {
                                Box(
                                    modifier = boxModifier
                                ) {
                                    QuranKhatmaHomeWidget(viewModel = viewModel)
                                }
                            }
                        }

                        HomeWidgetType.MOOD_REFLECTION -> {
                            item(key = "mood_engine_section") {
                                Box(
                                    modifier = boxModifier
                                ) {
                                    DailyMoodWisdomSection(
                                        viewModel = viewModel,
                                        selectedMood = selectedMood,
                                        isIslamic = isIslamic
                                    )
                                }
                            }
                        }


                        HomeWidgetType.AUDIO_RECITERS -> {
                            item(key = "reciters_showcase_section") {
                                Box(
                                    modifier = boxModifier
                                ) {
                                    QuranRecitersShowcase(viewModel = viewModel)
                                }
                            }
                        }

                        HomeWidgetType.PREMIUM_BANNER -> {
                            item(key = "premium_upgrade_section") {
                                Box(
                                    modifier = boxModifier
                                ) {
                                    PremiumUpgradeCard(viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }

            item(key = "bottom_spacing") {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Customization Bottom Sheet for Home Feed
        if (isCustomizeSheetOpen) {
            CustomizeHomeFeedSheet(
                viewModel = viewModel,
                widgetsOrder = widgetsOrder,
                widgetsVisibility = widgetsVisibility,
                onDismiss = { viewModel.closeCustomizeHomeSheet() }
            )
        }

        // Customization Bottom Sheet for Quick Access Tools
        if (isCustomizeQuickAccessOpen) {
            CustomizeQuickAccessSheet(
                viewModel = viewModel,
                selectedTools = quickAccessTools,
                onDismiss = { viewModel.closeCustomizeQuickAccessSheet() }
            )
        }

        // Khatma Milestone Celebration Dialog
        milestoneData?.let { milestone ->
            KhatmaMilestoneCelebrationDialog(
                milestone = milestone,
                onDismiss = { viewModel.dismissKhatmaMilestoneModal() },
                onReturnHome = { viewModel.navigateTo(NoorDestination.HOME) },
                onContinueReading = { viewModel.navigateTo(NoorDestination.QURAN_READER) }
            )
        }
    }
}
