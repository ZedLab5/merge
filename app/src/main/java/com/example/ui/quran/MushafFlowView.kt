package com.example.ui.quran

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.QuranArabicFont
import com.example.data.model.Surah
import com.example.data.repository.NoorRepository
import com.example.ui.MainViewModel
import com.example.ui.NoorDestination

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
 * centered both horizontally and vertically inside it.
 * Sits within a dedicated inline slot with generous horizontal padding so it never
 * collides or overlaps with adjacent Arabic words.
 */
@Composable
fun AyahEndMarkerBadge(
    verseNumber: Int,
    circleDiameterDp: Dp,
    themeColors: QuranReadingThemeColors,
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
        // Enforce strict square geometry with a slight vertical downward offset for optical alignment
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
                    color = themeColors.accent.copy(alpha = 0.09f),
                    radius = outerRadius,
                    center = center
                )

                // 2. Outer ornate circle ring
                drawCircle(
                    color = themeColors.accent.copy(alpha = 0.85f),
                    radius = outerRadius,
                    center = center,
                    style = Stroke(width = 1.2.dp.toPx())
                )

                // 3. Ornate 8-point geometric cardinal & diagonal accent points
                val markerRadius = outerRadius * 0.96f
                for (i in 0 until 8) {
                    val angleRad = (i * 45.0 * Math.PI / 180.0).toFloat()
                    val px = center.x + markerRadius * kotlin.math.cos(angleRad)
                    val py = center.y + markerRadius * kotlin.math.sin(angleRad)
                    val dotSize = if (i % 2 == 0) 1.2.dp.toPx() else 0.8.dp.toPx()
                    drawCircle(
                        color = themeColors.accent,
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
                    color = themeColors.accent,
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
 * Supports active audio ayah highlighting and synchronized smooth auto-scrolling.
 */
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
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    isFullscreenMode: Boolean = false,
    onContentTap: () -> Unit = {}
) {
    val verses = surah.verses
    val density = LocalDensity.current

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var textTopInParent by remember { mutableFloatStateOf(0f) }

    // Consistent circle dimensions scaled smoothly with font size - tightly matched to avoid extra gaps
    val circleDiameterDp = (fontSizeSp * 0.88f).dp
    val markerSizeSp = (fontSizeSp * 0.88f).sp

    // Build a single continuous AnnotatedString with per-ayah character ranges & manual inline ayah marker placeholders
    val (annotatedText, verseRanges) = remember(
        verses,
        fontSizeSp,
        themeColors,
        isPlaying,
        currentPlayingVerse,
        surah.number
    ) {
        val ranges = mutableMapOf<Int, Pair<Int, Int>>() // verseNumber -> (startIndex, endIndex) of Arabic text only

        val builder = buildAnnotatedString {
            verses.forEachIndexed { index, verse ->
                val isCurrentlyPlaying = isPlaying && currentPlayingVerse == verse.verseNumber
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

                // Append single space + inline Ayah End Marker Composable placeholder + single space
                append(" ")
                appendInlineContent(id = "marker_${verse.verseNumber}", alternateText = " [${verse.verseNumber}] ")
                append(" ")
            }
        }

        Pair(builder, ranges)
    }

    // Inline content mapping for manual Compose-rendered ayah markers
    val inlineContentMap = remember(verses, fontSizeSp, themeColors, circleDiameterDp, markerSizeSp) {
        verses.associate { verse ->
            val markerId = "marker_${verse.verseNumber}"
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
                    themeColors = themeColors
                )
            }
        }
    }

    // Auto-scroll when active playing ayah changes in Reading Mode
    LaunchedEffect(currentPlayingVerse, isPlaying, textLayoutResult, surah.number) {
        if (isPlaying) {
            if (currentPlayingVerse == 0) {
                // Basmala is reciting -> scroll to top
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

    // Match top and bottom padding precisely with normal card reading mode
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onContentTap() }
                )
            }
            .padding(start = 16.dp, end = 16.dp, top = topPadding, bottom = bottomPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Surah Header Banner (keeps standard banner with prev/next navigation)
        SurahHeaderBanner(
            surah = surah,
            themeColors = themeColors,
            onPreviousSurah = { viewModel.openPreviousSurah() },
            onNextSurah = { viewModel.openNextSurah() }
        )

        // Notice if MP3 audio player is active
        if (isAudioDisabled) {
            Mp3PlaybackActiveNotice()
        }

        // 2. Bismillah Header (for all except Surah 9 At-Tawbah)
        if (surah.number != 9) {
            BismillahBannerCard(
                themeColors = themeColors,
                arabicFont = arabicFont,
                isActive = isPlaying && currentPlayingVerse == 0
            )
        }

        // 3. Verses Rendering (Continuous justified paragraph on plain background)
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
            ) {
                Text(
                    text = annotatedText,
                    inlineContent = inlineContentMap,
                    onTextLayout = { textLayoutResult = it },
                    style = TextStyle(
                        fontFamily = arabicFont.fontFamily,
                        fontSize = fontSizeSp.sp,
                        lineHeight = (fontSizeSp * 2.1).sp, // Natural, balanced line-height for Arabic diacritics
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
}
