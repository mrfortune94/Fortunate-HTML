package com.proxyman.android.data

import com.proxyman.android.model.TrafficEntry
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TrafficRepositoryTest {

    private lateinit var repository: TrafficRepository

    @Before
    fun setup() {
        repository = TrafficRepository()
    }

    @Test
    fun `addEntry adds entry to repository`() {
        val entry = TrafficEntry(
            method = "GET",
            url = "https://example.com/api",
            host = "example.com",
            path = "/api",
            scheme = "https"
        )
        repository.addEntry(entry)
        assertEquals(1, repository.getEntries().size)
        assertEquals(entry.url, repository.getEntries()[0].url)
    }

    @Test
    fun `updateEntry updates existing entry`() {
        val entry = TrafficEntry(
            method = "GET",
            url = "https://example.com/api",
            host = "example.com",
            path = "/api",
            scheme = "https"
        )
        repository.addEntry(entry)
        repository.updateEntry(entry.id) { it.copy(statusCode = 200, state = TrafficEntry.State.COMPLETE) }
        
        val updated = repository.getEntry(entry.id)
        assertNotNull(updated)
        assertEquals(200, updated?.statusCode)
        assertEquals(TrafficEntry.State.COMPLETE, updated?.state)
    }

    @Test
    fun `clear removes all entries`() {
        val entry = TrafficEntry(method = "GET", url = "https://example.com", host = "example.com", path = "/", scheme = "https")
        repository.addEntry(entry)
        assertEquals(1, repository.getEntries().size)
        repository.clear()
        assertEquals(0, repository.getEntries().size)
    }

    @Test
    fun `getEntry returns null for non-existent id`() {
        assertNull(repository.getEntry("non-existent"))
    }
}
