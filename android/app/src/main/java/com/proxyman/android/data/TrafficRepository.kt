package com.proxyman.android.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.proxyman.android.model.TrafficEntry
import java.util.concurrent.CopyOnWriteArrayList

class TrafficRepository {

    private val entries = CopyOnWriteArrayList<TrafficEntry>()
    private val _trafficEntries = MutableLiveData<List<TrafficEntry>>(emptyList())
    val trafficEntries: LiveData<List<TrafficEntry>> = _trafficEntries

    fun addEntry(entry: TrafficEntry) {
        entries.add(entry)
        _trafficEntries.postValue(ArrayList(entries))
    }

    fun updateEntry(id: String, updater: (TrafficEntry) -> TrafficEntry) {
        val index = entries.indexOfFirst { it.id == id }
        if (index >= 0) {
            entries[index] = updater(entries[index])
            _trafficEntries.postValue(ArrayList(entries))
        }
    }

    fun clear() {
        entries.clear()
        _trafficEntries.postValue(emptyList())
    }

    fun getEntry(id: String): TrafficEntry? {
        return entries.find { it.id == id }
    }

    fun getEntries(): List<TrafficEntry> {
        return ArrayList(entries)
    }
}
