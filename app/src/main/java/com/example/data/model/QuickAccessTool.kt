package com.example.data.model

enum class QuickAccessTool(
    val id: String,
    val titleEn: String,
    val titleAr: String,
    val subtitleEn: String,
    val subtitleAr: String,
    val defaultSelected: Boolean = false
) {
    QURAN(
        id = "quran",
        titleEn = "Holy Qur'an",
        titleAr = "القرآن الكريم",
        subtitleEn = "Surahs & Reading",
        subtitleAr = "السور والتلاوة",
        defaultSelected = true
    ),
    SALAT(
        id = "salat",
        titleEn = "Salat & Tracker",
        titleAr = "الصلاة والمواقيت",
        subtitleEn = "Times & Records",
        subtitleAr = "المواقيت والتدوين",
        defaultSelected = false
    ),
    QIBLA(
        id = "qibla",
        titleEn = "Qibla Finder",
        titleAr = "اتجاه القبلة",
        subtitleEn = "Live Compass",
        subtitleAr = "بوصلة الكعبة",
        defaultSelected = true
    ),
    TASBIH(
        id = "tasbih",
        titleEn = "Smart Tasbih",
        titleAr = "المسبحة الذكية",
        subtitleEn = "Daily Dhikr",
        subtitleAr = "الأذكار والعداد",
        defaultSelected = true
    ),
    DUAS(
        id = "duas",
        titleEn = "Du'as & Azkar",
        titleAr = "الأذكار والأدعية",
        subtitleEn = "Fortress of Muslim",
        subtitleAr = "حصن المسلم الشامل",
        defaultSelected = false
    ),
    KHATMA(
        id = "khatma",
        titleEn = "Khatma Planner",
        titleAr = "ختمة القرآن",
        subtitleEn = "Custom Goals",
        subtitleAr = "خطة ومتابعة",
        defaultSelected = true
    ),
    STREAKS(
        id = "streaks",
        titleEn = "Spiritual Streaks",
        titleAr = "سلسلة الالتزام",
        subtitleEn = "Daily Milestones",
        subtitleAr = "تتبع الأيام والأوسمة",
        defaultSelected = false
    ),
    HABITS(
        id = "habits",
        titleEn = "Sunnah Habits",
        titleAr = "عادات السنن",
        subtitleEn = "Daily Routines",
        subtitleAr = "الروتين اليومي",
        defaultSelected = false
    ),
    AUDIO(
        id = "audio",
        titleEn = "MP3 Quran Player",
        titleAr = "مشغل التلاوات",
        subtitleEn = "Background Stream",
        subtitleAr = "تلاوات قرآنية عذبة",
        defaultSelected = false
    );

    companion object {
        fun defaultTools(): List<QuickAccessTool> = listOf(
            QURAN,
            QIBLA,
            TASBIH,
            KHATMA
        )
    }
}
