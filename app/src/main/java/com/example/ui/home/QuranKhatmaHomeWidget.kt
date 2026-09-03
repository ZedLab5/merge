package com.example.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Headphones
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.local.ReadingProgressEntity
import com.example.data.quran.KhatmaEngine
import com.example.data.quran.KhatmaFullDashboardState
import com.example.data.quran.KhatmaPaceStatus
import com.example.data.quran.QuranData
import com.example.ui.MainViewModel
import com.example.ui.NoorDestination
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

private val LocalNoorDarkPine = Color(0xFF10261F)
private val LocalNoorSageSlate = Color(0xFF5A756C)
private val LocalNoorSoftGreenBg = Color(0xFFF2F8F5)
private val LocalNoorCardBorder = Color(0xFFE2EBE6)

/**
 * Redesigned Quran & Khatma Home Section:
 * - Khatma is the main, prominent hero section with a clean, premium sanctuary aesthetic.
 * - Start Reading / Continue Reading has its own dedicated, balanced share.
 */
@Composable
fun QuranKhatmaHomeWidget(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val khatmaState by viewModel.khatmaDashboardState.collectAsStateWithLifecycle()
    val readingProgress by viewModel.readingProgress.collectAsStateWithLifecycle()
    val isArabic by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isLangArabic = isArabic.equals("Arabic", ignoreCase = true) || isArabic == "العربية"

    val state = khatmaState
    val isPlanActive = state != null && !state.plan.isCompleted

    val hasBookmark = readingProgress != null
    val surahName = readingProgress?.surahName ?: stringResource(R.string.home_fatihah_name)
    val ayahNum = readingProgress?.ayahNumber ?: 1
    val totalAyahs = readingProgress?.totalAyahs ?: 7

    val onResumeReading = {
        if (readingProgress != null) {
            viewModel.resumeReading(readingProgress!!)
        } else {
            viewModel.selectSurahForReading(QuranData.surahs.first(), 0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("quran_khatma_home_widget"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // =====================================================
        // SECTION HEADER
        // =====================================================
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
                        .background(LocalNoorSoftGreenBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = DeepVibrantTeal,
                        modifier = Modifier.size(17.dp)
                    )
                }

                Column {
                    Text(
                        text = if (isLangArabic) "القرآن الكريم والختمة" else "Holy Quran & Khatma",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = LocalNoorDarkPine,
                            fontSize = 15.5.sp
                        )
                    )
                    Text(
                        text = if (isPlanActive && state != null) {
                            if (isLangArabic) "مسيرة الختمة والمصحف الشريف" else "Active Khatma pace & personal recitation"
                        } else {
                            if (isLangArabic) "الختمة المنظمة والتلاوة اليومية" else "Guided Khatma & daily recitation"
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = LocalNoorSageSlate,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { viewModel.navigateTo(NoorDestination.QURAN_SURAH_LIST) },
                shape = RoundedCornerShape(10.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = if (isLangArabic) "المصحف" else "Mushaf",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepVibrantTeal
                        )
                    )
                    Icon(
                        imageVector = if (isLangArabic) Icons.AutoMirrored.Filled.ArrowBack else Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = DeepVibrantTeal,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }

        // =====================================================
        // 1. MAIN SECTION: KHATMA HERO (PREMIUM, PROMINENT & CLEAN)
        // =====================================================
        QuranKhatmaMainCard(
            viewModel = viewModel,
            state = state,
            isPlanActive = isPlanActive,
            isLangArabic = isLangArabic
        )

        // =====================================================
        // 2. DEDICATED SHARE: START / CONTINUE READING
        // =====================================================
        QuranReadingCompanionCard(
            viewModel = viewModel,
            readingProgress = readingProgress,
            hasBookmark = hasBookmark,
            surahName = surahName,
            ayahNum = ayahNum,
            totalAyahs = totalAyahs,
            isLangArabic = isLangArabic,
            onResumeReading = onResumeReading
        )
    }
}

/**
 * Main Section: Premium Khatma Hero Card
 */
@Composable
private fun QuranKhatmaMainCard(
    viewModel: MainViewModel,
    state: KhatmaFullDashboardState?,
    isPlanActive: Boolean,
    isLangArabic: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 0.dp,
        border = BorderStroke(1.2.dp, BorderTealGray)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Subtle Islamic ambient aura
            Box(
                modifier = Modifier
                    .size(210.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                (if (isPlanActive) DeepVibrantTeal else MetallicGold).copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                if (isPlanActive && state != null) {
                    // Active Khatma Status Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(NoorDestination.QURAN_KHATMA) },
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
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(13.dp))
                                    .background(SoftTealTint)
                                    .border(1.dp, BorderTealLight, RoundedCornerShape(13.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = "Quran Khatma",
                                    tint = DeepVibrantTeal,
                                    modifier = Modifier.size(21.dp)
                                )
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = if (isLangArabic) "ختمة القرآن الكريم" else "Noble Quran Khatma",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.5.sp,
                                            color = DarkPine
                                        )
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = SoftTealTint,
                                        border = BorderStroke(0.5.dp, DeepVibrantTeal.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = if (isLangArabic) "اليوم ${state.currentDayNumber}/${state.totalDays}" else "Day ${state.currentDayNumber}/${state.totalDays}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = DeepVibrantTeal,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.5.sp
                                            ),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (isLangArabic) {
                                        "متبقي ${state.daysRemaining} يوم • الورد: ${state.todayTargetAyahs} آية/يوم"
                                    } else {
                                        "${state.daysRemaining} days left • Target: ${state.todayTargetAyahs} Ayahs/day"
                                    },
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.5.sp,
                                        color = SlateTealMuted
                                    )
                                )
                            }
                        }

                        // Pace Badge
                        val paceText = when (state.paceStatus) {
                            KhatmaPaceStatus.AHEAD -> if (isLangArabic) "متقدم" else "Ahead"
                            KhatmaPaceStatus.ON_TRACK -> if (isLangArabic) "في الموعد" else "On Track"
                            KhatmaPaceStatus.BEHIND -> if (isLangArabic) "يحتاج متابعة" else "Catch Up"
                            KhatmaPaceStatus.COMPLETED -> if (isLangArabic) "مكتملة" else "Completed"
                        }
                        val isAheadOrOnTrack = state.paceStatus == KhatmaPaceStatus.ON_TRACK || state.paceStatus == KhatmaPaceStatus.AHEAD

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isAheadOrOnTrack) Color(0xFFF0F7F4) else Color(0xFFFBF8EE),
                            border = BorderStroke(1.dp, if (isAheadOrOnTrack) Color(0xFFCFE5DA) else Color(0xFFEADBBE))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isAheadOrOnTrack) DeepVibrantTeal else MetallicGold)
                                )
                                Text(
                                    text = paceText,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isAheadOrOnTrack) DeepVibrantTeal else MetallicGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Hero Recitation Stage Card with Authentic Arabic Calligraphy
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFFF7FAF9),
                        border = BorderStroke(1.dp, Color(0xFFDFEBE5)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isLangArabic) "محطة التلاوة القادمة" else "NEXT RECITATION MILESTONE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SlateTealMuted,
                                        letterSpacing = 0.5.sp
                                    )
                                )

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, Color(0xFFDFEBE5))
                                ) {
                                    Text(
                                        text = if (isLangArabic) "آية ${state.nextReadingPosition.ayahNumber} • جزء ${state.nextReadingPosition.juzNumber}" else "Ayah ${state.nextReadingPosition.ayahNumber} • Juz ${state.nextReadingPosition.juzNumber}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DarkPine,
                                            fontSize = 11.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = state.nextReadingPosition.surahNameArabic,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontFamily = AmiriQuranFontFamily,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 23.sp,
                                            color = DarkPine,
                                            lineHeight = 30.sp
                                        )
                                    )
                                    Text(
                                        text = "${state.nextReadingPosition.surahNumber}. ${state.nextReadingPosition.surahNameEnglish}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.5.sp,
                                            color = DeepVibrantTeal
                                        )
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, Color(0xFFDFEBE5))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "${state.progressPercentage}%",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Black,
                                                color = DeepVibrantTeal,
                                                fontSize = 16.5.sp
                                            )
                                        )
                                        Text(
                                            text = if (isLangArabic) "منجز" else "DONE",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = SlateTealMuted,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 8.5.sp,
                                                letterSpacing = 0.5.sp
                                            )
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = if (isLangArabic) {
                                        "ورد اليوم: قرأت ${state.todayReadAyahs} من ${state.todayTargetAyahs} آية"
                                    } else {
                                        "Today: ${state.todayReadAyahs} of ${state.todayTargetAyahs} Ayahs"
                                    },
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (state.isTodayTargetAchieved) DeepVibrantTeal else DarkPine,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                Text(
                                    text = if (state.todayRemainingAyahs > 0) {
                                        if (isLangArabic) "متبقي ${state.todayRemainingAyahs}" else "${state.todayRemainingAyahs} left"
                                    } else {
                                        if (isLangArabic) "اكتمل الورد ✓" else "Target met ✓"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (state.todayRemainingAyahs > 0) MetallicGold else DeepVibrantTeal,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFFE2EBE6))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(
                                            if (state.todayTargetAyahs > 0) {
                                                (state.todayReadAyahs.toFloat() / state.todayTargetAyahs.toFloat()).coerceIn(0.04f, 1f)
                                            } else state.progressFraction.coerceIn(0.04f, 1f)
                                        )
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(PrimaryTealGradient)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Tactile Primary CTA Button (Resume Khatma)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = DeepVibrantTeal,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        val surah = QuranData.surahs.find { it.number == state.nextReadingPosition.surahNumber }
                                            ?: QuranData.surahs.first()
                                        viewModel.selectSurahForReading(
                                            surah,
                                            (state.nextReadingPosition.ayahNumber - 1).coerceAtLeast(0)
                                        )
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 11.dp, horizontal = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(17.dp)
                                    )
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(
                                        text = if (isLangArabic) "متابعة تلاوة الختمة (آية ${state.nextReadingPosition.ayahNumber})" else "Resume Khatma at Ayah ${state.nextReadingPosition.ayahNumber}",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.5.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (state.isTodayTargetAchieved) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFBF8EE),
                            border = BorderStroke(1.dp, Color(0xFFEADBBE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MetallicGold,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = if (isLangArabic) "ما شاء الله! حققت ورد اليوم بنجاح (+${state.todayReadAyahs} آية)" else "Masha'Allah! Today's reading goal achieved (+${state.todayReadAyahs} Ayahs)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = DarkPine,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Secondary Action Ribbon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(9.dp),
                            color = SoftTealTint,
                            border = BorderStroke(1.dp, BorderTealLight),
                            modifier = Modifier
                                .clip(RoundedCornerShape(9.dp))
                                .clickable {
                                    val surah = QuranData.surahs.find { it.number == state.nextReadingPosition.surahNumber }
                                        ?: QuranData.surahs.first()
                                    viewModel.playSurahAudio(surah)
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Headphones,
                                    contentDescription = null,
                                    tint = DeepVibrantTeal,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = if (isLangArabic) "استماع للسورة" else "Listen Audio",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = DeepVibrantTeal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.navigateTo(NoorDestination.QURAN_KHATMA) }
                                .padding(horizontal = 6.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isLangArabic) "عرض جدول الختمة والتفاصيل" else "Khatma Plan & Schedule",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DeepVibrantTeal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                )
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = DeepVibrantTeal,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                } else {
                    // NO ACTIVE KHATMA PLAN: Premium Invitation to Embark on a Khatma
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(13.dp))
                                    .background(GoldBadgeBg)
                                    .border(1.dp, MetallicGold.copy(alpha = 0.35f), RoundedCornerShape(13.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MetallicGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = if (isLangArabic) "رحلة ختم القرآن الكريم" else "Noble Quran Khatma",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DarkPine,
                                        fontSize = 16.5.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (isLangArabic) "نظّم تلاوتك واختم كتاب الله بورد يومي ميسر" else "Organize daily reading and complete the Quran",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = SlateTealMuted,
                                        fontSize = 11.5.sp
                                    )
                                )
                            }
                        }

                        Text(
                            text = if (isLangArabic) "تخصيص" else "Custom",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepVibrantTeal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { viewModel.navigateTo(NoorDestination.QURAN_KHATMA) }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 Interactive Preset Goal Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KhatmaQuickPresetCard(
                            title = if (isLangArabic) "٣٠ يوماً" else "30 Days",
                            subtitle = if (isLangArabic) "جزء يومياً" else "1 Juz/day",
                            isHighlighted = true,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.createOrResetKhatma(
                                    days = 30,
                                    sessionsCount = 1,
                                    title = if (isLangArabic) "ختمة الشهر (٣٠ يوم)" else "30-Day Ramadan Pace"
                                )
                            }
                        )

                        KhatmaQuickPresetCard(
                            title = if (isLangArabic) "٦٠ يوماً" else "60 Days",
                            subtitle = if (isLangArabic) "نصف جزء" else "10 pgs/day",
                            isHighlighted = false,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.createOrResetKhatma(
                                    days = 60,
                                    sessionsCount = 1,
                                    title = if (isLangArabic) "ختمة الستين يوماً" else "60-Day Gentle Pace"
                                )
                            }
                        )

                        KhatmaQuickPresetCard(
                            title = if (isLangArabic) "٩٠ يوماً" else "90 Days",
                            subtitle = if (isLangArabic) "ثلث جزء" else "1/3 Juz/day",
                            isHighlighted = false,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.createOrResetKhatma(
                                    days = 90,
                                    sessionsCount = 1,
                                    title = if (isLangArabic) "ختمة التسعين يوماً" else "90-Day Steady Pace"
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 1-Tap Start Khatma Button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DeepVibrantTeal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.createOrResetKhatma(
                                    days = 30,
                                    sessionsCount = 1,
                                    title = if (isLangArabic) "ختمة الشهر (٣٠ يوم)" else "30-Day Ramadan Pace"
                                )
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 11.dp, horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isLangArabic) "بدء مسيرة الختمة (خطة ٣٠ يوماً)" else "Begin 30-Day Khatma Plan",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dedicated Share: Start Reading / Continue Reading Companion Card
 */
@Composable
private fun QuranReadingCompanionCard(
    viewModel: MainViewModel,
    readingProgress: ReadingProgressEntity?,
    hasBookmark: Boolean,
    surahName: String,
    ayahNum: Int,
    totalAyahs: Int,
    isLangArabic: Boolean,
    onResumeReading: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, LocalNoorCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SoftTealTint)
                            .border(1.dp, BorderTealLight, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = DeepVibrantTeal,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    Column {
                        Text(
                            text = if (hasBookmark) {
                                if (isLangArabic) "متابعة التلاوة الحرة" else "Continue Reading"
                            } else {
                                if (isLangArabic) "بدء تلاوة المصحف" else "Start Quran Reading"
                            },
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkPine,
                                fontSize = 14.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (hasBookmark) {
                                if (isLangArabic) "موضعك المحفوظ في المصحف" else "Saved Mushaf bookmark"
                            } else {
                                if (isLangArabic) "تلاوة حرة من بداية المصحف" else "Free reading from the beginning"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = SlateTealMuted,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // Coordinate Badge (Juz number)
                val juzNumber = readingProgress?.let {
                    KhatmaEngine.getAyahCoordinate(KhatmaEngine.getAbsoluteAyahIndex(it.surahNumber, it.ayahNumber)).juzNumber
                } ?: 1

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LocalNoorSoftGreenBg,
                    border = BorderStroke(0.5.dp, Color(0xFFCFE5DA))
                ) {
                    Text(
                        text = if (hasBookmark) {
                            if (isLangArabic) "جزء $juzNumber" else "Juz $juzNumber"
                        } else {
                            if (isLangArabic) "سورة الفاتحة" else "Al-Fatihah"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepVibrantTeal,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Surah Spotlight Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF7FAF9),
                border = BorderStroke(1.dp, Color(0xFFDFEBE5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = surahName,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = AmiriQuranFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 21.sp,
                                color = DarkPine,
                                lineHeight = 28.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFDFEBE5))
                        ) {
                            Text(
                                text = if (hasBookmark) {
                                    if (isLangArabic) "آية $ayahNum من $totalAyahs" else "Ayah $ayahNum of $totalAyahs"
                                } else {
                                    if (isLangArabic) "٧ آيات • مكية" else "7 Ayahs • Meccan"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepVibrantTeal,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Surah Progress Bar
                    val surahProgress = if (hasBookmark && totalAyahs > 0) {
                        (ayahNum.toFloat() / totalAyahs.toFloat()).coerceIn(0.04f, 1f)
                    } else 0.05f

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(2.5.dp))
                            .background(Color(0xFFE2EBE6))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(surahProgress)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(2.5.dp))
                                .background(PrimaryTealGradient)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row: Resume Button (Primary) + Surah List Button (Secondary)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Primary Continue / Start Button
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SoftTealTint,
                    border = BorderStroke(1.dp, DeepVibrantTeal.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onResumeReading)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = DeepVibrantTeal,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (hasBookmark) {
                                if (isLangArabic) "متابعة من آية $ayahNum" else "Resume at Ayah $ayahNum"
                            } else {
                                if (isLangArabic) "فتح المصحف" else "Open Mushaf"
                            },
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = DeepVibrantTeal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = DeepVibrantTeal,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Secondary Surah Index Button
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF4F8F6),
                    border = BorderStroke(1.dp, Color(0xFFDFEBE5)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { viewModel.navigateTo(NoorDestination.QURAN_SURAH_LIST) }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        IslamicIconMushaf(
                            modifier = Modifier.size(15.dp),
                            tint = DarkPine
                        )
                        Text(
                            text = if (isLangArabic) "السور" else "Surahs",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = DarkPine,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Interactive Khatma preset duration pill (e.g. 30, 60, 90 days)
 */
@Composable
private fun KhatmaQuickPresetCard(
    title: String,
    subtitle: String,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isHighlighted) DeepVibrantTeal else Color.White,
        border = BorderStroke(
            1.dp,
            if (isHighlighted) DeepVibrantTeal else Color(0xFFDFEBE5)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isHighlighted) Color.White else DarkPine,
                    fontSize = 12.sp
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (isHighlighted) Color.White.copy(alpha = 0.85f) else SlateTealMuted,
                    fontSize = 10.sp
                )
            )
        }
    }
}
