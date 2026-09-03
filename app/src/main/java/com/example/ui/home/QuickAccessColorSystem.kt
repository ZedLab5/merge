package com.example.ui.home

import androidx.compose.ui.graphics.Color

/**
 * 3-Tier Color System for Quick Access Slots:
 *
 * Slot 0 (1st): Turquoise
 * Slot 1 (2nd): Soft Rosewood/Maroon
 * Slot 2 (3rd): Sage / Emerald Green
 * Slot 3 (4th): Deep Indigo / Slate Blue
 *
 * 3 Distinct Visible Layers:
 * - Tier 1 (Icon Tint): Exact slot hex (the strongest/most saturated tier per slot)
 * - Tier 2 (Icon Background): Softer derived version of the slot color
 * - Tier 3 (Stripe Accent): Same color as icon tint, applied only as the thin right-edge accent (~4dp wide)
 *
 * Cards have reverted to white with soft 3D feel and soft neutral border (NoorCardBorder).
 */
data class QuickAccessSlotTier(
    val slotIndex: Int,
    val iconTint: Color,
    val iconBackground: Color,
    val stripeColor: Color = iconTint,
    val cardBackground: Color = Color.White,
    val cardBorder: Color = Color(0xFFE2EBE6)
)

object QuickAccessColorSystem {
    // 4 slot base card colors
    val SLOT_BASE_COLORS = listOf(
        Color(0xFFD3F0ED), // Slot 0 (1st): Turquoise
        Color(0xFFF0DCE0), // Slot 1 (2nd): Soft Rosewood/Maroon
        Color(0xFFD9EEDD), // Slot 2 (3rd): Sage/Emerald Green
        Color(0xFFDDE3F5)  // Slot 3 (4th): Deep Indigo/Blue
    )

    // Neutral grey palette for unselected tools in Customize picker
    val UNSELECTED_ICON_TINT = Color(0xFF94A3B8)
    val UNSELECTED_ICON_BG = Color(0xFFF1F5F9)
    val UNSELECTED_CARD_BG = Color.White
    val UNSELECTED_CARD_BORDER = Color(0xFFE2EBE6)
    val UNSELECTED_STRIPE_COLOR = Color.Transparent

    // 4 explicit slot tiers engineered for high elegance and visual harmony:
    // Layer 1 (iconTint): Softened slot hue with decreased opacity
    // Layer 2 (iconBackground): Gentle pastel container carrying the slot's hue softly
    // Layer 3 (stripeColor): Left-edge accent stripe curving seamlessly with rounded corners
    // Card background: Clean white with soft 3D feel and neutral border
    private val PRESET_SLOT_TIERS = listOf(
        // Slot 0 (1st): Turquoise (#D3F0ED)
        QuickAccessSlotTier(
            slotIndex = 0,
            iconTint = Color(0xFF26837A).copy(alpha = 0.58f),
            iconBackground = Color(0xFFB5E4DF).copy(alpha = 0.38f),
            stripeColor = Color(0xFF26837A).copy(alpha = 0.65f),
            cardBackground = Color.White,
            cardBorder = Color(0xFFE2EBE6)
        ),
        // Slot 1 (2nd): Soft Rosewood/Maroon (#F0DCE0)
        QuickAccessSlotTier(
            slotIndex = 1,
            iconTint = Color(0xFF8E4756).copy(alpha = 0.58f),
            iconBackground = Color(0xFFE2C2CA).copy(alpha = 0.38f),
            stripeColor = Color(0xFF8E4756).copy(alpha = 0.65f),
            cardBackground = Color.White,
            cardBorder = Color(0xFFE2EBE6)
        ),
        // Slot 2 (3rd): Sage / Emerald Green (#D9EEDD)
        QuickAccessSlotTier(
            slotIndex = 2,
            iconTint = Color(0xFF338354).copy(alpha = 0.58f),
            iconBackground = Color(0xFFBCE0C3).copy(alpha = 0.38f),
            stripeColor = Color(0xFF338354).copy(alpha = 0.65f),
            cardBackground = Color.White,
            cardBorder = Color(0xFFE2EBE6)
        ),
        // Slot 3 (4th): Deep Indigo / Slate Blue (#DDE3F5)
        QuickAccessSlotTier(
            slotIndex = 3,
            iconTint = Color(0xFF4863A6).copy(alpha = 0.58f),
            iconBackground = Color(0xFFC3D0F2).copy(alpha = 0.38f),
            stripeColor = Color(0xFF4863A6).copy(alpha = 0.65f),
            cardBackground = Color.White,
            cardBorder = Color(0xFFE2EBE6)
        )
    )

    /**
     * Derives or retrieves the 3-tier color system for a slot index (0..3).
     * Guaranteed high visibility and clear separation between:
     * Icon Tint -> Icon Container -> Right-Edge Accent Stripe.
     */
    fun getSlotTier(slotIndex: Int): QuickAccessSlotTier {
        val safeIndex = (slotIndex.coerceAtLeast(0)) % PRESET_SLOT_TIERS.size
        return PRESET_SLOT_TIERS[safeIndex]
    }
}
