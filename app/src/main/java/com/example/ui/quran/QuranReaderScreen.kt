package com.example.ui.quran

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.QuranArabicFont
import com.example.data.model.Surah
import com.example.data.model.Verse
import com.example.data.quran.KhatmaEngine
import com.example.ui.MainViewModel
import com.example.data.localization.tr
import com.example.ui.NoorDestination
import com.example.ui.components.NoorGlassIconButton
import com.example.ui.components.NoorTopBar
import com.example.ui.theme.BorderTealGray
import com.example.ui.theme.ReadingThemeColors
import com.example.ui.theme.CanvasMint
import com.example.ui.theme.DarkPine
import com.example.ui.theme.DeepVibrantTeal
import com.example.ui.theme.GoldBadgeBg
import com.example.ui.theme.MetallicGold
import com.example.ui.theme.SlateTealMuted
import com.example.ui.theme.SoftTealTint
import com.example.ui.theme.ReadingThemes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AutoScrollSpeed(
    val label: String,
    val dpPerSecond: Float
) {
    SLOW("Slow", 26f),
    MEDIUM("Medium", 52f),
    FAST("Fast", 96f)
}

typealias QuranReadingThemeColors = ReadingThemeColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReaderScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentSurah by viewModel.selectedSurahForReading.collectAsStateWithLifecycle()
    val fontSizeSp by viewModel.arabicFontSizeSp.collectAsStateWithLifecycle()
    val showTransliteration by viewModel.showTransliteration.collectAsStateWithLifecycle()
    val showTranslation by viewModel.showTranslation.collectAsStateWithLifecycle()
    val sharedThemeName by viewModel.sharedReadingTheme.collectAsStateWithLifecycle()
    val isSepiaMode by viewModel.isQuranSepiaMode.collectAsStateWithLifecycle()
    val isAudioPlaying by viewModel.isAudioPlaying.collectAsStateWithLifecycle()
    val currentPlayingVerse by viewModel.currentPlayingVerse.collectAsStateWithLifecycle()
    val currentPlayingSurah by viewModel.currentPlayingSurah.collectAsStateWithLifecycle()
    val isAyahAudioMode by viewModel.isAyahAudioMode.collectAsStateWithLifecycle()
    val isMp3PlayerRunning = isAudioPlaying && !isAyahAudioMode
    val isCurrentSurahPlaying = isAudioPlaying && currentPlayingSurah.number == currentSurah.number
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val readingProgress by viewModel.readingProgress.collectAsStateWithLifecycle()
    val targetAyahToScrollTo by viewModel.targetAyahToScrollTo.collectAsStateWithLifecycle()
    val khatmaState by viewModel.khatmaDashboardState.collectAsStateWithLifecycle()
    val isMushafFlowMode by viewModel.isMushafFlowMode.collectAsStateWithLifecycle()
    val selectedArabicFont by viewModel.selectedArabicFont.collectAsStateWithLifecycle()
    val isFullscreenMode by viewModel.isQuranReaderFullscreen.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val density = LocalDensity.current
    var showSettingsSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val listState = rememberLazyListState()
    val mushafFlowScrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    // Auto-Scroll States (Works in both normal cards and Mushaf Flow mode)
    var isAutoScrolling by remember { mutableStateOf(false) }
    var isAutoScrollPaused by remember { mutableStateOf(false) }
    var autoScrollSpeed by remember { mutableStateOf(AutoScrollSpeed.MEDIUM) }

    // Tap-to-toggle fullscreen handler
    val onContentTap = {
        viewModel.toggleQuranReaderFullscreen()
    }

    // Automatically clean up on leaving the Quran reader screen
    DisposableEffect(Unit) {
        onDispose {
            isAutoScrolling = false
            isAutoScrollPaused = false
            viewModel.setQuranReaderFullscreen(false)
            if (viewModel.isAyahAudioMode.value) {
                viewModel.stopAudio()
            }
        }
    }

    // Reset auto-scroll on surah change and record daily reading activity
    LaunchedEffect(currentSurah.number) {
        viewModel.recordQuranActivity()
        isAutoScrolling = false
        isAutoScrollPaused = false
    }

    // Pause auto-scroll cleanly when switching reading modes (simplest and safest per requirement)
    LaunchedEffect(isMushafFlowMode) {
        if (isAutoScrolling) {
            isAutoScrollPaused = true
        }
    }

    // User drag detection: pause auto-scroll immediately whenever user touches/drags content
    val isListDragged by listState.interactionSource.collectIsDraggedAsState()
    val isMushafDragged by mushafFlowScrollState.interactionSource.collectIsDraggedAsState()
    val isUserDragging = if (isMushafFlowMode) isMushafDragged else isListDragged

    LaunchedEffect(isUserDragging) {
        if (isUserDragging && isAutoScrolling && !isAutoScrollPaused) {
            isAutoScrollPaused = true
        }
    }

    // Continuous downward auto-scroll engine (branches across LazyListState & ScrollState)
    val pxPerSec = with(density) { autoScrollSpeed.dpPerSecond.dp.toPx() }
    LaunchedEffect(isAutoScrolling, isAutoScrollPaused, autoScrollSpeed, isMushafFlowMode) {
        if (!isAutoScrolling || isAutoScrollPaused) return@LaunchedEffect

        val activeScrollable: ScrollableState = if (isMushafFlowMode) mushafFlowScrollState else listState

        try {
            activeScrollable.scroll(MutatePriority.Default) {
                var lastTimeNanos = 0L
                while (isActive && isAutoScrolling && !isAutoScrollPaused) {
                    withFrameNanos { frameTimeNanos ->
                        if (lastTimeNanos > 0L) {
                            val dt = (frameTimeNanos - lastTimeNanos) / 1_000_000_000f
                            val delta = pxPerSec * dt
                            val consumed = scrollBy(delta)
                            if (delta > 0.05f && consumed <= 0.01f && !activeScrollable.canScrollForward) {
                                isAutoScrolling = false
                                isAutoScrollPaused = false
                            }
                        }
                        lastTimeNanos = frameTimeNanos
                    }
                }
            }
        } catch (e: CancellationException) {
            // User gesture (MutatePriority.UserInput) naturally preempts scroll
            if (isAutoScrolling) {
                isAutoScrollPaused = true
            }
        }
    }

    // Exit Button Visibility & Countdown Logic:
    // Uses active mode's scroll state as source of truth (stays visible during scrolling, 2.5s fade after stop)
    val isScrolling = if (isMushafFlowMode) {
        mushafFlowScrollState.isScrollInProgress
    } else {
        listState.isScrollInProgress
    }

    var isExitButtonFadedOut by remember { mutableStateOf(false) }

    LaunchedEffect(isScrolling, isFullscreenMode) {
        if (!isFullscreenMode) {
            isExitButtonFadedOut = false
            return@LaunchedEffect
        }
        if (isScrolling) {
            isExitButtonFadedOut = false
        } else {
            delay(2500)
            isExitButtonFadedOut = true
        }
    }

    val exitButtonAlpha by animateFloatAsState(
        targetValue = when {
            !isFullscreenMode -> 0f
            isScrolling -> 0.95f
            isExitButtonFadedOut -> 0f
            else -> 0.48f
        },
        animationSpec = tween(durationMillis = 220),
        label = "exitButtonAlpha"
    )

    // Auto-scroll to target bookmarked Ayah (only if targetAyahToScrollTo > 1, otherwise always open from the top at item 0)
    LaunchedEffect(targetAyahToScrollTo, currentSurah.number) {
        if (targetAyahToScrollTo > 1 && currentSurah.number != 1) {
            var offset = 1 // SurahHeaderBanner
            if (isMp3PlayerRunning) offset++
            if (currentSurah.number != 9) offset++
            val targetIndex = (targetAyahToScrollTo - 1 + offset).coerceIn(0, currentSurah.verses.size + offset)
            listState.animateScrollToItem(targetIndex)
        } else {
            listState.scrollToItem(0)
        }
    }

    // Smart Auto-Scrolling Audio Player: dynamically follow active Ayah only during Ayah-by-Ayah recitation mode
    LaunchedEffect(currentPlayingVerse, isAudioPlaying, isAyahAudioMode, currentPlayingSurah.number, currentSurah.number) {
        if (isAudioPlaying && isAyahAudioMode && currentPlayingSurah.number == currentSurah.number) {
            var offset = 1 // SurahHeaderBanner
            if (isMp3PlayerRunning) offset++
            if (currentSurah.number != 9) offset++

            if (currentPlayingVerse == 0 && currentSurah.number != 9) {
                val bismillahIndex = if (isMp3PlayerRunning) 2 else 1
                listState.animateScrollToItem(bismillahIndex)
            } else if (currentPlayingVerse >= 1) {
                val targetIndex = (currentPlayingVerse - 1 + offset).coerceIn(0, currentSurah.verses.size + offset)
                listState.animateScrollToItem(targetIndex)
            }
        }
    }

    val colorScheme = MaterialTheme.colorScheme
    val isSystemDark by viewModel.isDarkMode.collectAsStateWithLifecycle()

    // Determine current theme colors for Quran Reader: Sepia if toggled, Obsidian if dark mode, otherwise app-wide MaterialTheme.colorScheme
    val themeColors = remember(isSepiaMode, colorScheme, isSystemDark) {
        if (isSystemDark) {
            ReadingThemes.ObsidianNight
        } else if (isSepiaMode) {
            ReadingThemes.SepiaParchment
        } else {
            ReadingThemes.fromColorScheme(colorScheme, isSystemDark)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                AnimatedVisibility(
                    visible = !isFullscreenMode,
                    enter = fadeIn(tween(220)) + slideInVertically(tween(220)) { -it },
                    exit = fadeOut(tween(220)) + slideOutVertically(tween(220)) { -it }
                ) {
                    NoorTopBar(
                        title = "${currentSurah.number}. ${currentSurah.nameEnglish}",
                        eyebrow = "${currentSurah.revelationType.uppercase()} • ${currentSurah.nameArabic}",
                        subtitle = "${currentSurah.totalVerses} Ayahs • ${currentSurah.englishMeaning}",
                        onBackClick = { viewModel.navigateBack() },
                        backContentDescription = "Back",
                        isDark = themeColors.isDark,
                        themeColors = themeColors,
                        actions = {
                            // Quick Toggle: Mushaf Flow Mode
                            NoorGlassIconButton(
                                onClick = { viewModel.toggleMushafFlowMode() },
                                icon = Icons.Default.ViewAgenda,
                                contentDescription = "Distraction-Free Mushaf Flow",
                                isActive = isMushafFlowMode
                            )

                            // Minimal Audio Play/Pause Button for Reading & Listening
                            NoorGlassIconButton(
                                onClick = {
                                    if (isMp3PlayerRunning) {
                                        viewModel.showToast("MP3 player is active. Pause it using the floating bar to start recitation here.")
                                    } else if (isCurrentSurahPlaying && isAyahAudioMode) {
                                        viewModel.toggleAudioPlayback(currentSurah)
                                    } else {
                                        val startVerse = if (currentPlayingVerse > 0) currentPlayingVerse else 1
                                        viewModel.playAyah(currentSurah, startVerse, openPlayer = false)
                                    }
                                },
                                icon = if (isCurrentSurahPlaying && isAyahAudioMode) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isCurrentSurahPlaying && isAyahAudioMode) "Pause Recitation" else "Play Recitation",
                                isActive = isCurrentSurahPlaying && isAyahAudioMode
                            )

                            // Reading Display Settings
                            NoorGlassIconButton(
                                onClick = { showSettingsSheet = true },
                                icon = Icons.Default.Settings,
                                contentDescription = "Reading Settings"
                            )
                        }
                    )
                }
            },
            containerColor = themeColors.background,
            modifier = modifier
        ) { paddingValues ->
            if (isMushafFlowMode) {
                // Distraction-Free Continuous Reading Mode (Pure Uthmani text)
                MushafFlowView(
                    surah = currentSurah,
                    fontSizeSp = fontSizeSp,
                    arabicFont = selectedArabicFont,
                    themeColors = themeColors,
                    isPlaying = isCurrentSurahPlaying && isAyahAudioMode,
                    currentPlayingVerse = currentPlayingVerse,
                    isAudioDisabled = isMp3PlayerRunning,
                    viewModel = viewModel,
                    modifier = Modifier.padding(paddingValues),
                    scrollState = mushafFlowScrollState,
                    isFullscreenMode = isFullscreenMode,
                    onContentTap = onContentTap
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { onContentTap() }
                            )
                        }
                        .padding(paddingValues),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = if (isFullscreenMode) WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp else 16.dp,
                        bottom = if (isFullscreenMode) 32.dp else 90.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Surah Header Banner with Previous / Next Navigation
                    item(key = "surah_header") {
                        SurahHeaderBanner(
                            surah = currentSurah,
                            themeColors = themeColors,
                            onPreviousSurah = { viewModel.openPreviousSurah() },
                            onNextSurah = { viewModel.openNextSurah() }
                        )
                    }

                    // Static audio notice directly beneath hero section when MP3 player is active
                    if (isMp3PlayerRunning) {
                        item(key = "mp3_audio_notice") {
                            Mp3PlaybackActiveNotice()
                        }
                    }

                    // Bismillah Header (for all except Surah 9 At-Tawbah)
                    if (currentSurah.number != 9) {
                        item(key = "bismillah_card") {
                            BismillahBannerCard(
                                themeColors = themeColors,
                                arabicFont = selectedArabicFont,
                                isActive = isCurrentSurahPlaying && isAyahAudioMode && currentPlayingVerse == 0
                            )
                        }
                    }

                    // Empty state fallback if verses are still loading or empty
                    if (currentSurah.verses.isEmpty()) {
                        item(key = "empty_verses_card") {
                            QuranEmptyVersesCard(
                                surah = currentSurah,
                                themeColors = themeColors,
                                viewModel = viewModel,
                                onRetry = { viewModel.reloadCurrentSurah() }
                            )
                        }
                    } else {
                        // Verses List
                        items(
                            items = currentSurah.verses,
                            key = { "${currentSurah.number}_${it.verseNumber}" }
                        ) { verse ->
                            val isVerseActive = isCurrentSurahPlaying && currentPlayingVerse == verse.verseNumber
                            val isFavorite = favorites.any { it.title.contains("Surah ${currentSurah.nameEnglish} Ayah ${verse.verseNumber}") }
                            val isExactBookmark = readingProgress?.surahNumber == currentSurah.number &&
                                    readingProgress?.ayahNumber == verse.verseNumber

                            val currentPlan = khatmaState?.plan
                            val isKhatmaActive = currentPlan != null && !currentPlan.isCompleted
                            val currentReadCount = currentPlan?.readAyahsCount ?: 0
                            val verseAbsIndex = remember(currentSurah.number, verse.verseNumber) {
                                KhatmaEngine.getAbsoluteAyahIndex(currentSurah.number, verse.verseNumber)
                            }
                            val isKhatmaRead = isKhatmaActive && verseAbsIndex <= currentReadCount
                            val isKhatmaCurrentPointer = isKhatmaActive && verseAbsIndex == currentReadCount

                            VerseCardItem(
                                verse = verse,
                                surah = currentSurah,
                                fontSizeSp = fontSizeSp,
                                arabicFont = selectedArabicFont,
                                showTransliteration = showTransliteration,
                                showTranslation = showTranslation,
                                isActive = isVerseActive && isAyahAudioMode,
                                isBookmarked = isFavorite,
                                isReadingBookmark = isExactBookmark,
                                isKhatmaActive = isKhatmaActive,
                                isKhatmaRead = isKhatmaRead,
                                isKhatmaCurrentPointer = isKhatmaCurrentPointer,
                                isAudioDisabled = isMp3PlayerRunning,
                                themeColors = themeColors,
                                onPlayVerse = {
                                    if (isMp3PlayerRunning) {
                                        viewModel.showToast("MP3 player is currently active. Pause it to recite individual verses.")
                                    } else if (isCurrentSurahPlaying && isAyahAudioMode && currentPlayingVerse == verse.verseNumber) {
                                        viewModel.toggleAudioPlayback(currentSurah)
                                    } else {
                                        viewModel.playAyah(currentSurah, verse.verseNumber)
                                    }
                                },
                                onToggleBookmark = {
                                    viewModel.saveExactReadingBookmark(currentSurah, verse.verseNumber)
                                },
                                onMarkKhatma = {
                                    viewModel.markKhatmaProgressToVerse(currentSurah, verse.verseNumber)
                                },
                                onCopyVerse = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText(
                                        "Ayah ${currentSurah.nameEnglish} ${verse.verseNumber}",
                                        "${verse.arabicText}\n\n${verse.transliteration}\n\n${verse.translation}\n[Qur'an ${currentSurah.number}:${verse.verseNumber}]"
                                    )
                                    clipboard.setPrimaryClip(clip)
                                    viewModel.showToast("Ayah copied to clipboard!")
                                }
                            )
                        }
                    }

                    // Next / Previous Surah Navigation Footer
                    item(key = "surah_nav_footer") {
                        SurahNavigationFooter(
                            currentSurah = currentSurah,
                            themeColors = themeColors,
                            onPrevious = { viewModel.openPreviousSurah() },
                            onNext = { viewModel.openNextSurah() },
                            onOpenList = { viewModel.navigateTo(NoorDestination.QURAN_SURAH_LIST) }
                        )
                    }
                }
            }
        }

        // Reading Settings Modal Bottom Sheet
        if (showSettingsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSettingsSheet = false },
                sheetState = sheetState,
                containerColor = themeColors.surface
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                            .navigationBarsPadding(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Reading Preferences",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.arabicText,
                                    fontSize = 19.sp
                                )
                            )

                            IconButton(onClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    showSettingsSheet = false
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = themeColors.arabicText
                                )
                            }
                        }

                        // 1. Reading Display Mode (Part 1 - Two Option Picker)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Reading Display Mode",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.arabicText
                                )
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Option A: Mixed Reading
                                val isMixedSelected = !isMushafFlowMode
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.setReadingDisplayMode(false) },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isMixedSelected) themeColors.accent.copy(alpha = 0.08f) else themeColors.background,
                                    border = BorderStroke(
                                        if (isMixedSelected) 1.5.dp else 1.dp,
                                        if (isMixedSelected) themeColors.accent else themeColors.border.copy(alpha = 0.6f)
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (themeColors.isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9))
                                                .padding(horizontal = 8.dp, vertical = 6.dp),
                                            verticalArrangement = Arrangement.spacedBy(2.dp)
                                        ) {
                                            Text(
                                                text = "الْحَمْدُ لِلَّهِ",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = selectedArabicFont.fontFamily,
                                                    color = themeColors.arabicText
                                                ),
                                                textAlign = TextAlign.End,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Text(
                                                text = "All praise is to Allah",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 8.5.sp,
                                                    color = themeColors.translationText
                                                ),
                                                maxLines = 1
                                            )
                                        }
                                        Text(
                                            text = "Mixed Reading",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = if (isMixedSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isMixedSelected) themeColors.accent else themeColors.arabicText,
                                                fontSize = 13.sp
                                            )
                                        )
                                    }
                                }

                                // Option B: Arabic Only (Continuous Mushaf Flow)
                                val isArabicOnlySelected = isMushafFlowMode
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.setReadingDisplayMode(true) },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isArabicOnlySelected) themeColors.accent.copy(alpha = 0.08f) else themeColors.background,
                                    border = BorderStroke(
                                        if (isArabicOnlySelected) 1.5.dp else 1.dp,
                                        if (isArabicOnlySelected) themeColors.accent else themeColors.border.copy(alpha = 0.6f)
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (themeColors.isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9))
                                                .padding(horizontal = 8.dp, vertical = 11.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ۝",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = selectedArabicFont.fontFamily,
                                                    color = themeColors.arabicText
                                                ),
                                                textAlign = TextAlign.Center,
                                                maxLines = 1
                                            )
                                        }
                                        Text(
                                            text = "Arabic Only",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = if (isArabicOnlySelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isArabicOnlySelected) themeColors.accent else themeColors.arabicText,
                                                fontSize = 13.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        // 2. Arabic Calligraphy Style (Part 2 - 3 Horizontal Rows)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Arabic Calligraphy Style",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.arabicText
                                )
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                QuranArabicFont.values().forEach { fontOption ->
                                    val isSelected = selectedArabicFont == fontOption
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.setSelectedArabicFont(fontOption) },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) themeColors.accent.copy(alpha = 0.08f) else themeColors.background,
                                        border = BorderStroke(
                                            if (isSelected) 1.5.dp else 1.dp,
                                            if (isSelected) themeColors.accent else themeColors.border.copy(alpha = 0.5f)
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = fontOption.displayName,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isSelected) themeColors.accent else themeColors.arabicText,
                                                    fontSize = 13.5.sp
                                                )
                                            )
                                            Text(
                                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ",
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontFamily = fontOption.fontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp,
                                                    color = if (isSelected) themeColors.accent else themeColors.arabicText
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 3. Arabic Font Size Controls (Small, Medium, Large)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                                    Icon(
                                        imageVector = Icons.Default.FormatSize,
                                        contentDescription = null,
                                        tint = themeColors.accent,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Arabic Font Size",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = themeColors.arabicText
                                        )
                                    )
                                }
                                Text(
                                    text = "${fontSizeSp}sp",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = themeColors.accent
                                    )
                                )
                            }

                            // Predefined Selector Tabs (Small, Medium, Large)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(themeColors.background)
                                    .border(1.dp, themeColors.border, RoundedCornerShape(12.dp))
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val fontOptions = listOf("Small" to 22, "Medium" to 28, "Large" to 34)
                                fontOptions.forEach { (label, spSize) ->
                                    val isSelected = when (label) {
                                        "Small" -> fontSizeSp <= 24
                                        "Medium" -> fontSizeSp in 25..30
                                        else -> fontSizeSp > 30
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) themeColors.accent else Color.Transparent,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { viewModel.arabicFontSizeSp.value = spSize }
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isSelected) Color.White else themeColors.arabicText,
                                                    fontSize = 13.sp
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 4. Reading Canvas Sepia Parchment Toggle (Exclusive to Quran Reader)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { viewModel.toggleQuranSepiaMode() },
                            shape = RoundedCornerShape(14.dp),
                            color = if (themeColors.isDark) themeColors.surface else Color(0xFFFAF6EE),
                            border = BorderStroke(
                                1.2.dp,
                                if (isSepiaMode) Color(0xFFB57E1A) else themeColors.border.copy(alpha = 0.6f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFF9F4E8))
                                                .border(1.dp, Color(0xFFB57E1A), CircleShape)
                                        )
                                        Text(
                                            text = "Sepia Parchment Theme",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = themeColors.arabicText,
                                                fontSize = 14.5.sp
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "Warm vintage parchment canvas exclusively for Quran reading",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = themeColors.translationText,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp
                                        )
                                    )
                                }

                                Switch(
                                    checked = isSepiaMode,
                                    onCheckedChange = { viewModel.toggleQuranSepiaMode(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(0xFFB57E1A),
                                        uncheckedThumbColor = themeColors.translationText,
                                        uncheckedTrackColor = themeColors.border
                                    )
                                )
                            }
                        }

                        // 5. Translation & Transliteration Toggles (Only relevant in Mixed Reading mode)
                        if (!isMushafFlowMode) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "English Translation",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = themeColors.arabicText
                                            )
                                        )
                                        Text(
                                            text = "Clear Sahih International translation",
                                            style = MaterialTheme.typography.bodySmall.copy(color = themeColors.translationText)
                                        )
                                    }
                                    Switch(
                                        checked = showTranslation,
                                        onCheckedChange = { viewModel.showTranslation.value = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = themeColors.accent
                                        )
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Phonetic Transliteration",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = themeColors.arabicText
                                            )
                                        )
                                        Text(
                                            text = "Helps non-Arabic readers pronounce correctly",
                                            style = MaterialTheme.typography.bodySmall.copy(color = themeColors.translationText)
                                        )
                                    }
                                    Switch(
                                        checked = showTransliteration,
                                        onCheckedChange = { viewModel.showTransliteration.value = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = themeColors.accent
                                        )
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = themeColors.border.copy(alpha = 0.5f))

                        // 6. Fullscreen Reading Mode Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Fullscreen Reading Mode",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = themeColors.arabicText
                                    )
                                )
                                Text(
                                    text = "Hides top and bottom bars for an uninterrupted reading canvas",
                                    style = MaterialTheme.typography.bodySmall.copy(color = themeColors.translationText)
                                )
                            }
                            Switch(
                                checked = isFullscreenMode,
                                onCheckedChange = { checked ->
                                    viewModel.setQuranReaderFullscreen(checked)
                                    if (checked) {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            showSettingsSheet = false
                                        }
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = themeColors.accent
                                )
                            )
                        }

                        HorizontalDivider(color = themeColors.border.copy(alpha = 0.5f))

                            // Auto-Scroll Section with Preset Speed Controls
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Auto-Scroll",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = themeColors.arabicText
                                            )
                                        )
                                        Text(
                                            text = "Hands-free continuous downward reading at a steady pace",
                                            style = MaterialTheme.typography.bodySmall.copy(color = themeColors.translationText)
                                        )
                                    }
                                    Switch(
                                        checked = isAutoScrolling,
                                        onCheckedChange = { enabled ->
                                            isAutoScrolling = enabled
                                            isAutoScrollPaused = false
                                            if (enabled) {
                                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                                    showSettingsSheet = false
                                                }
                                            }
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = themeColors.accent
                                        )
                                    )
                                }

                                // Auto-Scroll Speed Preset Selector
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Scroll Speed",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Medium,
                                                color = themeColors.arabicText
                                            )
                                        )
                                        Text(
                                            text = autoScrollSpeed.label,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = themeColors.accent
                                            )
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(themeColors.background)
                                            .border(1.dp, themeColors.border, RoundedCornerShape(12.dp))
                                            .padding(4.dp),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        AutoScrollSpeed.values().forEach { speed ->
                                            val isSelected = autoScrollSpeed == speed
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = if (isSelected) themeColors.accent else Color.Transparent,
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable { autoScrollSpeed = speed }
                                            ) {
                                                Box(
                                                    modifier = Modifier.padding(vertical = 8.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = speed.label,
                                                        style = MaterialTheme.typography.labelMedium.copy(
                                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                            color = if (isSelected) Color.White else themeColors.arabicText,
                                                            fontSize = 13.sp
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        // Persistent Minimal Exit Button (when in Fullscreen Mode)
        if (isFullscreenMode && exitButtonAlpha > 0.01f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(top = 12.dp, end = 16.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(
                            enabled = exitButtonAlpha > 0.1f,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { viewModel.setQuranReaderFullscreen(false) },
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (themeColors.name == "Obsidian Night") Color(0xFF1E2830).copy(alpha = 0.90f) else Color.White.copy(alpha = 0.90f),
                        border = BorderStroke(1.dp, themeColors.accent.copy(alpha = 0.35f)),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .size(36.dp)
                            .alpha(exitButtonAlpha)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FullscreenExit,
                                contentDescription = "Exit Fullscreen Mode",
                                tint = themeColors.accent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // "Resume Auto-Scroll" Floating Affordance
        AnimatedVisibility(
            visible = isAutoScrolling && isAutoScrollPaused,
            enter = fadeIn(tween(220)) + slideInVertically(tween(220)) { it / 2 },
            exit = fadeOut(tween(220)) + slideOutVertically(tween(220)) { it / 2 },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (isFullscreenMode) 24.dp else 94.dp)
        ) {
            Surface(
                onClick = { isAutoScrollPaused = false },
                shape = RoundedCornerShape(24.dp),
                color = DeepVibrantTeal,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier.padding(start = 14.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Resume Auto-Scroll",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.22f))
                            .clickable {
                                isAutoScrolling = false
                                isAutoScrollPaused = false
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Stop Auto-Scroll",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SurahHeaderBanner(
    surah: Surah,
    themeColors: QuranReadingThemeColors,
    onPreviousSurah: () -> Unit,
    onNextSurah: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF0D151C),
        border = BorderStroke(1.2.dp, MetallicGold.copy(alpha = 0.35f))
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_pinterest_hero),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.40f,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x660D151C),
                                Color(0x990D151C),
                                Color(0xE60D151C)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Navigation Bar: Left: Surah position | Right: Prev/Next buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Surah Position Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        border = BorderStroke(0.8.dp, MetallicGold.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "Surah ${surah.number} / 114",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 11.5.sp
                            ),
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                        )
                    }

                    // Navigation Controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (surah.number > 1) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.10f),
                                border = BorderStroke(0.8.dp, MetallicGold.copy(alpha = 0.4f)),
                                modifier = Modifier.clickable(onClick = onPreviousSurah)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Previous Surah",
                                        tint = MetallicGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Prev",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }

                        if (surah.number < 114) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.10f),
                                border = BorderStroke(0.8.dp, MetallicGold.copy(alpha = 0.4f)),
                                modifier = Modifier.clickable(onClick = onNextSurah)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Next",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 11.sp
                                        )
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Next Surah",
                                        tint = MetallicGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Left-to-Right Hero Details Row (Left: English Names & Metadata | Right: Arabic Calligraphy)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Revelation Type & Verses Row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MetallicGold.copy(alpha = 0.20f),
                                border = BorderStroke(0.8.dp, MetallicGold.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = surah.revelationType.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MetallicGold,
                                        fontSize = 10.sp,
                                        letterSpacing = 0.5.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Text(
                                text = "${surah.totalVerses} Ayahs",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            )
                        }

                        // English Name
                        Text(
                            text = surah.nameEnglish,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 22.sp
                            )
                        )

                        // Meaning
                        Text(
                            text = "\"${surah.englishMeaning}\"",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MetallicGold,
                                fontSize = 13.sp
                            )
                        )
                    }

                    // Arabic Calligraphy on Right
                    Text(
                        text = "سُورَةُ ${surah.nameArabic}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = QuranArabicFont.AMIRI.fontFamily,
                            color = Color.White,
                            fontSize = 25.sp
                        ),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
fun Mp3PlaybackActiveNotice(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFFFF2F2),
        border = BorderStroke(1.dp, Color(0xFFFCA5A5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFEE2E2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Ayah Audio Playback Unavailable",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF991B1B),
                        fontSize = 13.5.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "The MP3 player is currently active. Pause it to enable Ayah recitation.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFB91C1C),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                )
            }
        }
    }
}

@Composable
fun QuranEmptyVersesCard(
    surah: Surah,
    themeColors: QuranReadingThemeColors,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(20.dp),
        color = themeColors.surface,
        border = BorderStroke(1.dp, themeColors.border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(themeColors.accent.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = themeColors.accent,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = String.format(tr("surah_format", viewModel), surah.nameEnglish, surah.nameArabic),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = themeColors.arabicText
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = tr("verses_loading_message", viewModel),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = themeColors.translationText
                ),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = themeColors.accent),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = tr("load_verses_button", viewModel),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun BismillahBannerCard(
    themeColors: QuranReadingThemeColors,
    arabicFont: QuranArabicFont = QuranArabicFont.AMIRI,
    isActive: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isActive) themeColors.accent.copy(alpha = 0.12f) else themeColors.surface,
        border = BorderStroke(
            width = if (isActive) 1.5.dp else 1.dp,
            color = if (isActive) themeColors.accent else themeColors.border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp,
                    fontFamily = arabicFont.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = themeColors.arabicText
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "In the Name of Allah, the Most Gracious, the Most Merciful",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    color = themeColors.translationText.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun VerseCardItem(
    verse: Verse,
    surah: Surah,
    fontSizeSp: Int,
    arabicFont: QuranArabicFont = QuranArabicFont.AMIRI,
    showTransliteration: Boolean,
    showTranslation: Boolean,
    isActive: Boolean,
    isBookmarked: Boolean,
    isReadingBookmark: Boolean,
    isKhatmaActive: Boolean = false,
    isKhatmaRead: Boolean = false,
    isKhatmaCurrentPointer: Boolean = false,
    isAudioDisabled: Boolean = false,
    themeColors: QuranReadingThemeColors,
    onPlayVerse: () -> Unit,
    onToggleBookmark: () -> Unit,
    onMarkKhatma: () -> Unit = {},
    onCopyVerse: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isActive) themeColors.accent.copy(alpha = 0.08f) else themeColors.surface,
        border = BorderStroke(
            if (isActive) 1.5.dp else if (isKhatmaCurrentPointer || isReadingBookmark) 1.2.dp else 0.8.dp,
            if (isActive) themeColors.accent else if (isKhatmaCurrentPointer) Color(0xFF10B981) else if (isReadingBookmark) MetallicGold else themeColors.border.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Verse Header Bar (Left: Clean Verse Number Pill & Bookmark/Khatma labels | Right: Ghost Line Action Icons)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Side: Verse Number + (Bookmark OR Khatma Bookmark swapped based on active Khatma)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isActive) themeColors.accent else themeColors.accent.copy(alpha = 0.10f),
                        border = BorderStroke(0.8.dp, themeColors.accent.copy(alpha = 0.25f))
                    ) {
                        Text(
                            text = "${surah.number}:${verse.verseNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isActive) Color.White else themeColors.accent,
                                fontSize = 11.5.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    if (!isKhatmaActive) {
                        // Normal Reading Mode: Only Normal Bookmark appears
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isReadingBookmark || isBookmarked) GoldBadgeBg else Color.Transparent,
                            border = BorderStroke(
                                0.8.dp,
                                if (isReadingBookmark || isBookmarked) MetallicGold.copy(alpha = 0.6f) else themeColors.border.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier.clickable { onToggleBookmark() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = if (isReadingBookmark || isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (isReadingBookmark || isBookmarked) MetallicGold else themeColors.translationText.copy(alpha = 0.7f),
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = if (isReadingBookmark || isBookmarked) "Bookmarked" else "Bookmark",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isReadingBookmark || isBookmarked) MetallicGold else themeColors.translationText.copy(alpha = 0.8f)
                                    )
                                )
                            }
                        }
                    } else {
                        // Khatma Mode Active: Normal bookmark is swapped with Khatma bookmark
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when {
                                isKhatmaCurrentPointer -> Color(0xFFFEF3C7)
                                isKhatmaRead -> SoftTealTint
                                else -> Color.Transparent
                            },
                            border = BorderStroke(
                                0.8.dp,
                                when {
                                    isKhatmaCurrentPointer -> Color(0xFFF59E0B)
                                    isKhatmaRead -> DeepVibrantTeal.copy(alpha = 0.4f)
                                    else -> themeColors.border.copy(alpha = 0.7f)
                                }
                            ),
                            modifier = Modifier.clickable { onMarkKhatma() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = if (isKhatmaRead || isKhatmaCurrentPointer) Icons.Default.CheckCircle else Icons.Default.AutoStories,
                                    contentDescription = "Khatma Progress",
                                    tint = when {
                                        isKhatmaCurrentPointer -> Color(0xFFD97706)
                                        isKhatmaRead -> DeepVibrantTeal
                                        else -> themeColors.translationText.copy(alpha = 0.7f)
                                    },
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = when {
                                        isKhatmaCurrentPointer -> "Current"
                                        isKhatmaRead -> "Khatma Done"
                                        else -> "Mark Khatma"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isKhatmaCurrentPointer -> Color(0xFFD97706)
                                            isKhatmaRead -> DeepVibrantTeal
                                            else -> themeColors.translationText.copy(alpha = 0.8f)
                                        }
                                    )
                                )
                            }
                        }
                    }
                }

                // Right Side: Action Icons (Audio, Copy) - Flag/Bookmark icon removed to avoid repetition
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Audio Recite Icon
                    IconButton(
                        onClick = onPlayVerse,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = if (isActive) Icons.Default.Pause else Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Recite Ayah",
                            tint = if (isAudioDisabled) {
                                themeColors.translationText.copy(alpha = 0.28f)
                            } else if (isActive) {
                                MetallicGold
                            } else {
                                themeColors.translationText.copy(alpha = 0.65f)
                            },
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    // Copy Icon
                    IconButton(
                        onClick = onCopyVerse,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Ayah Text",
                            tint = themeColors.translationText.copy(alpha = 0.65f),
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }

            // Pure Arabic Text Display (Centered/Right-aligned, beautifully rendered)
            Text(
                text = verse.arabicText,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = fontSizeSp.sp,
                    lineHeight = (fontSizeSp * 1.75).sp,
                    fontFamily = arabicFont.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = themeColors.arabicText
                ),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            // Transliteration (if enabled)
            if (showTransliteration && verse.transliteration.isNotBlank()) {
                Text(
                    text = verse.transliteration,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = themeColors.transliterationText,
                        fontSize = 13.5.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            // English Translation (if enabled)
            if (showTranslation && verse.translation.isNotBlank()) {
                Text(
                    text = verse.translation,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = themeColors.translationText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                )
            }

            // Short Tafsir / Spiritual Context (if available)
            if (verse.tafsirShort.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = themeColors.background,
                    border = BorderStroke(0.8.dp, themeColors.border),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Tafsir Note",
                            tint = themeColors.accent,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = verse.tafsirShort,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = themeColors.translationText.copy(alpha = 0.9f),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SurahNavigationFooter(
    currentSurah: Surah,
    themeColors: QuranReadingThemeColors,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onOpenList: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = themeColors.surface,
        border = BorderStroke(1.dp, themeColors.border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onPrevious,
                    enabled = currentSurah.number > 1,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = themeColors.accent
                    ),
                    border = BorderStroke(1.dp, themeColors.border)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous Surah",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Prev Surah",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = onOpenList,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = themeColors.accent,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = "All Surahs",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "114 Surahs",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                OutlinedButton(
                    onClick = onNext,
                    enabled = currentSurah.number < 114,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = themeColors.accent
                    ),
                    border = BorderStroke(1.dp, themeColors.border)
                ) {
                    Text(
                        text = "Next Surah",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Surah",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
