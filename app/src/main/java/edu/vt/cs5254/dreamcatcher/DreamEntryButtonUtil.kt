package edu.vt.cs5254.dreamcatcher

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.Button
import androidx.core.graphics.ColorUtils

/**
 * Set the specified color as the background for the button.
 *
 * Defaults to using white text, but uses black text when
 * white text would provide insufficient contrast.
 *
 * @param backgroundColor a CSS-compatible "hex triplet" string
 * of the form "#RRGGBB" or any of the following supported names:
 * red, blue, green, black, white, gray, cyan, magenta, yellow, lightgray,
 * darkgray, grey, lightgrey, darkgrey, aqua, fuchsia, lime, maroon, navy,
 * olive, purple, silver, and teal.
 */
fun Button.setBackgroundWithContrastingText(backgroundColor: String) {
    val bgColor = Color.parseColor(backgroundColor)
    backgroundTintList = ColorStateList.valueOf(bgColor)
    val contrast = ColorUtils.calculateContrast(Color.WHITE, bgColor)
    setTextColor(if (contrast > 4.5) Color.WHITE else Color.BLACK)
}

fun Button.configureForEntry(entry: DreamEntry) {
    visibility = View.VISIBLE
    when (entry.kind) {
        DreamEntryKind.CONCEIVED -> {
            setBackgroundWithContrastingText("teal")
            text = entry.kind.toString()
        }
        DreamEntryKind.DEFERRED -> {
            setBackgroundWithContrastingText("grey")
            text = entry.kind.toString()
        }
        DreamEntryKind.FULFILLED -> {
            setBackgroundWithContrastingText("green")
            text = entry.kind.toString()
        }
        DreamEntryKind.REFLECTION -> {
            setBackgroundWithContrastingText("#87ceeb")
            text = entry.text
            isAllCaps = false
        }
    }
}