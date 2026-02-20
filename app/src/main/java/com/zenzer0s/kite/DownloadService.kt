package com.zenzer0s.kite

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.zenzer0s.kite.util.NotificationUtil
import com.zenzer0s.kite.util.NotificationUtil.SERVICE_NOTIFICATION_ID

private const val TAG = "DownloadService"

/** This `Service` does nothing */
class DownloadService : Service() {

    override fun onBind(intent: Intent): IBinder {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            }
        val notification = NotificationUtil.makeServiceNotification(pendingIntent)
        startForeground(SERVICE_NOTIFICATION_ID, notification)
        return DownloadServiceBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: ")
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        return super.onUnbind(intent)
    }

    class DownloadServiceBinder : Binder()
}
