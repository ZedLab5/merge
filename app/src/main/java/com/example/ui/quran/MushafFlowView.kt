package com.example.ui.quran

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.QuranArabicFont
import com.example.data.model.Surah
import com.example.data.model.Verse
import com.example.data.repository.NoorRepository
import com.example.ui.MainViewModel
import com.example.ui.NoorDestination
import com.example.ui.theme.MetallicGold

val AmiriQuranFontFamily = FontFamily(
    Font(R.font.amiri_quran, FontWeight.Normal)
)

/**
 * Helper to convert standard Int ayah number into Arabic-Indic digits string (U+0660–U+0669).
 */
fun toArabicIndic(number: Int): String {
    val sb = StringBuilder()
    for (ch in number.toString()) {
        if (ch in '0'..'9') {
            sb.append((ch - '0' + 0x0660).toChar())
        } else {
            sb.append(ch)
        }
    }
    return sb.toString()
}

/**
 * Ayah End Marker Badge:
 * Draws a uniform, perfectly round double-circle ornament with the verse number
 * centered inside it. If a note exists for this verse, a subtle gold indicator dot is drawn.
 */
@Composable
fun AyahEndMarkerBadge(
    verseNumber: Int,
    circleDiameterDp: Dp,
    themeColors: QuranReadingThemeColors,
    hasNote: Boolean = false,
    modifier: Modifier = Modifier
) {
    val digitCount = verseNumber.toString().length
    val numeralFontSize = when {
        digitCount <= 1 -> (circleDiameterDp.value * 0.44f).sp
        digitCount == 2 -> (circleDiameterDp.value * 0.38f).sp
        else -> (circleDiameterDp.value * 0.30f).sp
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(circleDiameterDp)
                .offset(y = 1.3.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val outerRadius = (size.minDimension / 2f) - 1.2.dp.toPx()

                // 1. Subtle warm background glow
                drawCircle(
                    color = if (hasNote) MetallicGold.copy(alpha = 0.25f) else themeColors.accent.copy(alpha = 0.09f),
                    radius = outerRadius,
                    center = center
                )

                // 2. Outer ornate circle ring
                drawCircle(
                    color = if (hasNote) MetallicGold else themeColors.accent.copy(alpha = 0.85f),
                    radius = outerRadius,
                    center = center,
                    style = Stroke(width = if (hasNote) 1.6.dp.toPx() else 1.2.dp.toPx())
                )

                // 3. Ornate 8-point geometric cardinal & diagonal accent points
                val markerRadius = outerRadius * 0.96f
                for (i in 0 until 8) {
                    val angleRad = (i * 45.0 * Math.PI / 180.0).toFloat()
                    val px = center.x + markerRadius * kotlin.math.cos(angleRad)
                    val py = center.y + markerRadius * kotlin.math.sin(angleRad)
                    val dotSize = if (i % 2 == 0) 1.2.dp.toPx() else 0.8.dp.toPx()
                    drawCircle(
                        color = if (hasNote) MetallicGold else themeColors.accent,
                        radius = dotSize,
                        center = Offset(px, py)
                    )
                }

                // 4. Inner delicate framing ring
                val innerRadius = (outerRadius - 2.4.dp.toPx()).coerceAtLeast(1f)
                drawCircle(
                    color = themeColors.accent.copy(alpha = 0.40f),
                    radius = innerRadius,
                    center = center,
                    style = Stroke(width = 0.7.dp.toPx())
                )

                // 5. Persistent note indicator badge dot
                if (hasNote) {
                    drawCircle(
                        color = Color(0xFFD97706),
                        radius = 2.4.dp.toPx(),
                        center = Offset(center.x, center.y + outerRadius - 1.dp.toPx())
                    )
                }
            }

            Text(
                text = toArabicIndic(verseNumber),
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    platformStyle = @Suppress("DEPRECATION") PlatformTextStyle(
                        includeFontPadding = false
                    ),
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.Both
                    ),
                    fontSize = numeralFontSize,
                    fontWeight = FontWeight.Bold,
                    color = if (hasNote) MetallicGold else themeColors.accent,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.wrapContentSize(Alignment.Center)
            )
        }
    }
}

/**
 * Continuous Reading Mode:
 * Renders all ayahs of the surah as one uninterrupted, justified paragraph of Arabic text
 * in the user's selected Arabic calligraphy style with manual inline Ayah End Marker badges.
 * Supports active audio ayah highlighting, persistent note markers, and long-press contextual actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MushafFlowView(
    surah: Surah,
    fontSizeSp: Int,
    arabicFont: QuranArabicFont = QuranArabicFont.AMIRI,
    themeColors: QuranReadingThemeColors,
    isPlaying: Boolean = false,
    currentPlayingVerse: Int = 0,
    isAudioDisabled: Boolean = false,
    viewModel: MainViewModel,
    notesMap: Map<Int, String> = emptyMap(),
    onOpenNoteForVerse: (Verse) -> Unit = {},
    onToggleBookmarkForVerse: (Verse) -> Unit = {},
    onPlayVerse: (Verse) -> Unit = {},
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    isFullscreenMode: Boolean = false,
    onContentTap: () -> Unit = {}
) {
    val verses = surah.verses
    val density = LocalDensity.current

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var textTopInParent by remember { mutableFloatStateOf(0f) }
    var contextMenuVerse by remember { mutableStateOf<Verse?>(null) }

    // Consistent circle dimensions scaled smoothly with font size
    val circleDiameterDp = (fontSizeSp * 0.88f).dp
    val markerSizeSp = (fontSizeSp * 0.88f).sp

    // Build single continuous AnnotatedString with per-ayah character ranges, note underlines & marker placeholders
    val (annotatedText, verseRanges) = remember(
        verses,
        fontSizeSp,
        themeColors,
        isPlaying,
        currentPlayingVerse,
        surah.number,
        notesMap
    ) {
        val ranges = mutableMapOf<Int, Pair<Int, Int>>()

        val builder = buildAnnotatedString {
            verses.forEachIndexed { _, verse ->
                val isCurrentlyPlaying = isPlaying && currentPlayingVerse == verse.verseNumber
                val hasNote = notesMap[verse.verseNumber]?.isNotBlank() == true
                val startIndex = length
                val cleanArabic = NoorRepository.sanitizeArabicVerseText(
                    surah.number,
                    verse.verseNumber,
                    verse.arabicText.trim()
                )
                append(cleanArabic)
                val endIndex = length
                ranges[verse.verseNumber] = Pair(startIndex, endIndex)

                if (isCurrentlyPlaying) {
                    addStyle(
                        style = SpanStyle(
                            background = themeColors.accent.copy(alpha = 0.22f),
                            color = themeColors.arabicText,
                            fontWeight = FontWeight.SemiBold
                        ),
                        start = startIndex,
                        end = endIndex
                    )
                }

                if (hasNote) {
                    addStyle(
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            color = themeColors.accent
                        ),
                        start = startIndex,
                        end = endIndex
                    )
                }

                append(" ")
                appendInlineContent(id = "marker_${verse.verseNumber}", alternateText = " [${verse.verseNumber}] ")
                append(" ")
            }
        }

        Pair(builder, ranges)
    }

    // Inline content mapping for manual Compose-rendered ayah markers
    val inlineContentMap = remember(verses, fontSizeSp, themeColors, circleDiameterDp, markerSizeSp, notesMap) {
        verses.associate { verse ->
            val markerId = "marker_${verse.verseNumber}"
            val hasNote = notesMap[verse.verseNumber]?.isNotBlank() == true
            markerId to InlineTextContent(
                Placeholder(
                    width = markerSizeSp,
                    height = markerSizeSp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                )
            ) {
                AyahEndMarkerBadge(
                    verseNumber = verse.verseNumber,
                    circleDiameterDp = circleDiameterDp,
                    themeColors = themeColors,
                    hasNote = hasNote
                )
            }
        }
    }

    // Auto-scroll when active playing ayah changes in Reading Mode
    LaunchedEffect(currentPlayingVerse, isPlaying, textLayoutResult, surah.number) {
        if (isPlaying) {
            if (currentPlayingVerse == 0) {
                scrollState.animateScrollTo(0)
            } else if (currentPlayingVerse > 0) {
                val range = verseRanges[currentPlayingVerse]
                val layout = textLayoutResult
                if (range != null && layout != null) {
                    val line = layout.getLineForOffset(range.first)
                    val lineTop = layout.getLineTop(line)
                    val targetPx = textTopInParent + lineTop - with(density) { 100.dp.toPx() }
                    scrollState.animateScrollTo(targetPx.coerceAtLeast(0f).toInt())
                }
            }
        }
    }

    val topPadding = if (isFullscreenMode) {
        WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp
    } else {
        16.dp
    }
    val bottomPadding = if (isFullscreenMode) 32.dp else 90.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(themeColors.background)
            .verticalScroll(scrollState)
            .padding(start = 16.dp, end = 16.dp, top = topPadding, bottom = bottomPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Surah Header Banner
        SurahHeaderBanner(
            surah = surah,
            themeColors = themeColors,
            onPreviousSurah = { viewModel.openPreviousSurah() },
            onNextSurah = { viewModel.openNextSurah() }
        )

        if (isAudioDisabled) {
            Mp3PlaybackActiveNotice()
        }

        // 2. Bismillah Header
        if (surah.number != 9) {
            BismillahBannerCard(
                themeColors = themeColors,
                arabicFont = arabicFont,
                isActive = isPlaying && currentPlayingVerse == 0
            )
        }

        // 3. Verses Rendering with Long-Press Detection
        if (verses.isEmpty()) {
            QuranEmptyVersesCard(
                surah = surah,
                themeColors = themeColors,
                viewModel = viewModel,
                onRetry = { viewModel.reloadCurrentSurah() }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 8.dp)
                    .onGloballyPositioned { coordinates ->
                        textTopInParent = coordinates.positionInParent().y
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onContentTap() },
                            onLongPress = { tapOffset ->
                                val layout = textLayoutResult
                                if (layout != null) {
                                    val charIndex = layout.getOffsetForPosition(tapOffset)
                                    val verseNum = verseRanges.entries.firstOrNull { (_, range) ->
                                        charIndex >= range.first && charIndex <= range.second + 4
                                    }?.key
                                    if (verseNum != null) {
                                        val targetVerse = surah.verses.firstOrNull { it.verseNumber == verseNum }
                                        if (targetVerse != null) {
                                            viewModel.triggerHaptic()
                                            contextMenuVerse = targetVerse
                                        }
                                    }
                                }
                            }
                        )
                    }
            ) {
                Text(
                    text = annotatedText,
                    inlineContent = inlineContentMap,
                    onTextLayout = { textLayoutResult = it },
                    style = TextStyle(
                        fontFamily = arabicFont.fontFamily,
                        fontSize = fontSizeSp.sp,
                        lineHeight = (fontSizeSp * 2.1).sp,
                        fontWeight = FontWeight.Normal,
                        color = themeColors.arabicText,
                        textAlign = TextAlign.Center,
                        textDirection = TextDirection.Rtl
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 4. Surah Navigation Footer
        SurahNavigationFooter(
            currentSurah = surah,
            themeColors = themeColors,
            onPrevious = { viewModel.openPreviousSurah() },
            onNext = { viewModel.openNextSurah() },
            onOpenList = { viewModel.navigateTo(NoorDestination.QURAN_SURAH_LIST) }
        )
    }

    // Contextual Long-Press Popup Sheet
    if (contextMenuVerse != null) {
        val targetVerse = contextMenuVerse!!
        val hasExistingNote = notesMap[targetVerse.verseNumber]?.isNotBlank() == true

        ModalBottomSheet(
            onDismissRequest = { contextMenuVerse = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = themeColors.surface,
            contentColor = themeColors.arabicText
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Surah ${surah.nameEnglish} • Ayah ${targetVerse.verseNumber}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = themeColors.arabicText
                            )
                        )
                        Text(
                            text = "Contextual Verse Options",
                            style = MaterialTheme.typography.bodySmall.copy(color = themeColors.translationText)
                        )
                    }

                    IconButton(onClick = { contextMenuVerse = null }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = themeColors.translationText
                        )
                    }
                }

                HorizontalDivider(color = themeColors.border.copy(alpha = 0.5f))

                // Action 1: Save Bookmark
                Surface(
                    onClick = {
                        onToggleBookmarkForVerse(targetVerse)
                        contextMenuVerse = null
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = themeColors.background,
                    border = BorderStroke(1.dp, themeColors.border)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = MetallicGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Bookmark Ayah",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.arabicText
                                )
                            )
                            Text(
                                text = "Save exact reading bookmark position here",
                                style = MaterialTheme.typography.bodySmall.copy(color = themeColors.translationText)
                            )
                        }
                    }
                }

                // Action 2: Play Recitation
                Surface(
                    onClick = {
                        onPlayVerse(targetVerse)
                        contextMenuVerse = null
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = themeColors.background,
                    border = BorderStroke(1.dp, themeColors.border)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = null,
                            tint = themeColors.accent,
                            modifier = Modifier.size(20.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Play Recitation",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.arabicText
                                )
                            )
                            Text(
                                text = "Listen to recitation for this verse",
                                style = MaterialTheme.typography.bodySmall.copy(color = themeColors.translationText)
                            )
                        }
                    }
                }

                // Action 3: Add / Edit Note
                Surface(
                    onClick = {
                        val verseToNote = targetVerse
                        contextMenuVerse = null
                        onOpenNoteForVerse(verseToNote)
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = if (hasExistingNote) themeColors.accent.copy(alpha = 0.12f) else themeColors.background,
                    border = BorderStroke(1.dp, if (hasExistingNote) themeColors.accent else themeColors.border)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = null,
                            tint = themeColors.accent,
                            modifier = Modifier.size(22.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (hasExistingNote) "Edit Note" else "Add Note",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.arabicText
                                )
                            )
                            Text(
                                text = if (hasExistingNote) "View or edit saved note for this verse" else "Attach a study reflection or personal note",
                                style = MaterialTheme.typography.bodySmall.copy(color = themeColors.translationText)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
