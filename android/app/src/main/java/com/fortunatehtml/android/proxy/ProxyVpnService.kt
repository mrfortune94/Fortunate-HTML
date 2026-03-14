package com.fortunatehtml.android.proxy

import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.fortunatehtml.android.FortunateHtmlApp
import com.fortunatehtml.android.R
import com.fortunatehtml.android.data.PreferencesManager
import com.fortunatehtml.android.ui.MainActivity
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

class ProxyVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null
    private var proxyServer: ProxyServer? = null
    @Volatile
    private var running = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startVpn()
            ACTION_STOP -> stopVpn()
        }
        return START_STICKY
    }

    private fun startVpn() {
        if (running) return

        val prefs = PreferencesManager(this)
        val port = prefs.proxyPort

        // Start the local proxy server
        val certManager = CertificateManager(this)
        certManager.initialize()

        val app = application as FortunateHtmlApp
        proxyServer = ProxyServer(port, certManager, app.trafficRepository, prefs.mitmEnabled)
        proxyServer?.start()

        // Set up VPN interface
        val builder = Builder()
            .setSession("Fortunate HTML")
            .addAddress("10.0.0.2", 32)
            .addRoute("0.0.0.0", 0)
            .addDnsServer("8.8.8.8")
            .setMtu(1500)

        vpnInterface = builder.establish()

        running = true
        startForeground(NOTIFICATION_ID, createNotification())

        // Start VPN packet processing
        Thread { processPackets() }.start()
    }

    private fun stopVpn() {
        running = false
        proxyServer?.stop()
        vpnInterface?.close()
        vpnInterface = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun processPackets() {
        val vpnFd = vpnInterface?.fileDescriptor ?: return
        val input = FileInputStream(vpnFd)
        val output = FileOutputStream(vpnFd)
        val buffer = ByteBuffer.allocate(32767)

        while (running) {
            try {
                buffer.clear()
                val length = input.read(buffer.array())
                if (length > 0) {
                    buffer.limit(length)
                    // Forward packet through VPN tunnel
                    handlePacket(buffer, output)
                }
                Thread.sleep(1)
            } catch (_: Exception) {
                if (!running) break
            }
        }

        input.close()
        output.close()
    }

    private fun handlePacket(buffer: ByteBuffer, output: FileOutputStream) {
        // Basic IP packet forwarding - packets will be routed through the proxy
        // The VPN captures all traffic and the proxy server handles HTTP/HTTPS
        try {
            output.write(buffer.array(), 0, buffer.limit())
        } catch (_: Exception) {
        }
    }

    private fun createNotification(): android.app.Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, FortunateHtmlApp.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Fortunate HTML Active")
            .setContentText("Intercepting network traffic")
            .setSmallIcon(R.drawable.ic_vpn_key)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        stopVpn()
        super.onDestroy()
    }

    companion object {
        const val ACTION_START = "com.fortunatehtml.android.START_VPN"
        const val ACTION_STOP = "com.fortunatehtml.android.STOP_VPN"
        private const val NOTIFICATION_ID = 1
    }
}
