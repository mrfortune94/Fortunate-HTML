package com.fortunatehtml.android.model

import java.util.UUID

data class TrafficEntry(
    val id: String = UUID.randomUUID().toString(),
    val method: String,
    val url: String,
    val host: String,
    val path: String,
    val scheme: String,
    val requestHeaders: Map<String, String> = emptyMap(),
    val requestBody: String? = null,
    val statusCode: Int? = null,
    val responseHeaders: Map<String, String> = emptyMap(),
    val responseBody: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val duration: Long? = null,
    val responseSize: Long? = null,
    val isHttps: Boolean = false,
    val state: State = State.PENDING
) {
    enum class State {
        PENDING,
        COMPLETE,
        FAILED
    }

    val statusText: String
        get() = when {
            state == State.FAILED -> "Error"
            statusCode != null -> statusCode.toString()
            else -> "..."
        }
}
