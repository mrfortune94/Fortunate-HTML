package com.proxyman.android.model

import org.junit.Assert.*
import org.junit.Test

class TrafficEntryTest {

    @Test
    fun `statusText returns status code when complete`() {
        val entry = TrafficEntry(
            method = "GET",
            url = "https://example.com",
            host = "example.com",
            path = "/",
            scheme = "https",
            statusCode = 200,
            state = TrafficEntry.State.COMPLETE
        )
        assertEquals("200", entry.statusText)
    }

    @Test
    fun `statusText returns Error when failed`() {
        val entry = TrafficEntry(
            method = "GET",
            url = "https://example.com",
            host = "example.com",
            path = "/",
            scheme = "https",
            state = TrafficEntry.State.FAILED
        )
        assertEquals("Error", entry.statusText)
    }

    @Test
    fun `statusText returns dots when pending`() {
        val entry = TrafficEntry(
            method = "GET",
            url = "https://example.com",
            host = "example.com",
            path = "/",
            scheme = "https",
            state = TrafficEntry.State.PENDING
        )
        assertEquals("...", entry.statusText)
    }

    @Test
    fun `default state is PENDING`() {
        val entry = TrafficEntry(
            method = "GET",
            url = "https://example.com",
            host = "example.com",
            path = "/",
            scheme = "https"
        )
        assertEquals(TrafficEntry.State.PENDING, entry.state)
    }

    @Test
    fun `isHttps flag is set correctly`() {
        val httpsEntry = TrafficEntry(
            method = "GET",
            url = "https://example.com",
            host = "example.com",
            path = "/",
            scheme = "https",
            isHttps = true
        )
        assertTrue(httpsEntry.isHttps)
        
        val httpEntry = TrafficEntry(
            method = "GET",
            url = "http://example.com",
            host = "example.com",
            path = "/",
            scheme = "http",
            isHttps = false
        )
        assertFalse(httpEntry.isHttps)
    }
}
