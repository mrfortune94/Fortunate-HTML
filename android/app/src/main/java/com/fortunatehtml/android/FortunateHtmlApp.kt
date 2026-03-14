package com.fortunatehtml.android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.fortunatehtml.android.data.TrafficRepository

class FortunateHtmlApp : Application() {

    lateinit var trafficRepository: TrafficRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        trafficRepository = TrafficRepository()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Proxy Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Fortunate HTML VPN proxy service notification"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "fortunatehtml_vpn_service"
        lateinit var instance: FortunateHtmlApp
            private set
    }
}
