package com.fortunatehtml.android.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fortunatehtml.android.FortunateHtmlApp
import com.fortunatehtml.android.R

class TrafficDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic_detail)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val entryId = intent.getStringExtra(EXTRA_ENTRY_ID) ?: run {
            finish()
            return
        }

        val app = application as FortunateHtmlApp
        val entry = app.trafficRepository.getEntry(entryId) ?: run {
            finish()
            return
        }

        supportActionBar?.title = "${entry.method} ${entry.host}"

        findViewById<TextView>(R.id.urlValue).text = entry.url
        findViewById<TextView>(R.id.methodValue).text = entry.method
        findViewById<TextView>(R.id.statusValue).text = entry.statusText
        findViewById<TextView>(R.id.schemeValue).text = entry.scheme.uppercase()
        findViewById<TextView>(R.id.durationValue).text = if (entry.duration != null) "${entry.duration}ms" else "N/A"
        findViewById<TextView>(R.id.sizeValue).text = if (entry.responseSize != null) "${entry.responseSize} bytes" else "N/A"

        // Request Headers
        val reqHeaders = entry.requestHeaders.entries.joinToString("\n") { "${it.key}: ${it.value}" }
        findViewById<TextView>(R.id.requestHeadersValue).text = reqHeaders.ifEmpty { "None" }

        // Request Body
        findViewById<TextView>(R.id.requestBodyValue).text = entry.requestBody ?: "None"

        // Response Headers
        val resHeaders = entry.responseHeaders.entries.joinToString("\n") { "${it.key}: ${it.value}" }
        findViewById<TextView>(R.id.responseHeadersValue).text = resHeaders.ifEmpty { "None" }

        // Response Body
        val responseBody = entry.responseBody
        findViewById<TextView>(R.id.responseBodyValue).text = when {
            responseBody == null -> "None"
            responseBody.length > 10000 -> responseBody.substring(0, 10000) + "... (truncated)"
            else -> responseBody
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ENTRY_ID = "entry_id"
    }
}
