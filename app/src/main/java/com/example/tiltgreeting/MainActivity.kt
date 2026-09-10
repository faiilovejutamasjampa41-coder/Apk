package com.example.tiltgreeting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import java.io.File

class MainActivity : Activity() {
    private lateinit var report: TextView

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        root.addView(TextView(this).apply {
            text = "Tilt Greeting Assistant"
            textSize = 24f
        })
        root.addView(TextView(this).apply {
            text = "\nStep 1: enable the accessibility service.\n" +
                    "Step 2: open the exact Tilt live-room screen.\n" +
                    "Step 3: leave it visible for 5–10 seconds.\n" +
                    "Step 4: return here and inspect the captured UI tree.\n\n" +
                    "This build starts in inspection mode. It does not post messages. " +
                    "The report is used to identify Tilt's actual Android controls before any optional greeting action is enabled."
        })

        root.addView(Button(this).apply {
            text = "Enable Accessibility"
            setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        })
        root.addView(Button(this).apply {
            text = "Refresh UI Report"
            setOnClickListener { refreshReport() }
        })
        root.addView(Button(this).apply {
            text = "Clear UI Report"
            setOnClickListener {
                File(filesDir, "tilt_accessibility_dump.txt").delete()
                refreshReport()
            }
        })

        report = TextView(this).apply {
            textSize = 12f
            setTextIsSelectable(true)
        }
        root.addView(ScrollView(this).apply { addView(report) }, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)
        refreshReport()
    }

    override fun onResume() {
        super.onResume()
        refreshReport()
    }

    private fun refreshReport() {
        val f = File(filesDir, "tilt_accessibility_dump.txt")
        report.text = if (f.exists()) f.readText() else
            "No report yet. Enable the service, open the Tilt room, wait a few seconds, then return here."
    }
}
