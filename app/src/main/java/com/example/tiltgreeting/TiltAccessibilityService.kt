package com.example.tiltgreeting

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TiltAccessibilityService : AccessibilityService() {
    private var lastSignature = ""
    private var lastWrite = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val root = rootInActiveWindow ?: return
        val now = System.currentTimeMillis()
        if (now - lastWrite < 1500) return

        val packageName = root.packageName?.toString() ?: ""
        if (!packageName.contains("tilt", ignoreCase = true)) return

        val lines = mutableListOf<String>()
        lines += "Captured: " + SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK).format(Date())
        lines += "Package: $packageName"
        lines += "Event: ${event?.eventType}"
        lines += "---- ACCESSIBILITY TREE ----"
        dumpNode(root, lines, 0)

        val signature = lines.joinToString("\n")
        if (signature != lastSignature) {
            File(filesDir, "tilt_accessibility_dump.txt").writeText(signature)
            lastSignature = signature
            lastWrite = now
        }
    }

    private fun dumpNode(node: AccessibilityNodeInfo, out: MutableList<String>, depth: Int) {
        if (depth > 30) return
        val indent = "  ".repeat(depth)
        val cls = node.className?.toString() ?: ""
        val text = node.text?.toString()?.replace("\n", " ") ?: ""
        val desc = node.contentDescription?.toString()?.replace("\n", " ") ?: ""
        val id = node.viewIdResourceName ?: ""
        if (text.isNotBlank() || desc.isNotBlank() || id.isNotBlank() || node.isClickable || node.isEditable) {
            out += "$indent class=$cls | text=$text | desc=$desc | id=$id | clickable=${node.isClickable} | editable=${node.isEditable} | focusable=${node.isFocusable} | visible=${node.isVisibleToUser}"
        }
        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { child ->
                dumpNode(child, out, depth + 1)
                child.recycle()
            }
        }
    }

    override fun onInterrupt() = Unit
}
