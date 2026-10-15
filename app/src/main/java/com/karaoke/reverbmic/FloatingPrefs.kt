package com.karaoke.reverbmic

import android.content.Context

// Nyimpen posisi (x,y), ukuran (width,height) terakhir panel floating, dan
// apakah lagi di-minimize (bentuk bulatan) atau kebuka (panel penuh) - biar
// pas service restart / HP di-reboot, posisi & ukurannya balik ke terakhir
// kali user atur, gak reset ke default terus.
data class FloatingGeometry(
    val x: Int,
    val y: Int,
    val widthPx: Int,
    val heightPx: Int,
    val minimized: Boolean,
)

object FloatingPrefs {
    private const val PREFS_NAME = "floating_window_prefs"

    fun load(context: Context, defaultWidthPx: Int, defaultHeightPx: Int): FloatingGeometry {
        val p = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        // Layout panel dirombak lagi (cuma Mic ON/OFF + Volume Mic, lebih
        // ringkas). Ukuran lama yang kesimpen diabaikan sekali - posisi tetap
        // dipakai. Ukuran baru ditandai layout_v = 4.
        val sizeIsCurrent = p.getInt("layout_v", 0) >= 4
        return FloatingGeometry(
            x = p.getInt("x", 0),
            y = p.getInt("y", 200),
            widthPx = if (sizeIsCurrent) p.getInt("width", defaultWidthPx) else defaultWidthPx,
            heightPx = if (sizeIsCurrent) p.getInt("height", defaultHeightPx) else defaultHeightPx,
            minimized = p.getBoolean("minimized", false),
        )
    }

    fun save(context: Context, geo: FloatingGeometry) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt("x", geo.x)
            .putInt("y", geo.y)
            .putInt("layout_v", 4)
            .putInt("width", geo.widthPx)
            .putInt("height", geo.heightPx)
            .putBoolean("minimized", geo.minimized)
            .apply()
    }
}
