package jp.ict.muffin.otasukejuru.other

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import jp.ict.muffin.otasukejuru.`object`.GlobalValue.notificationContent
import jp.ict.muffin.otasukejuru.`object`.GlobalValue.notificationId

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = intent.getIntExtra(
                notificationId,
                0
        )
        val content = intent.getStringExtra(notificationContent)
        notificationManager.notify(
                id,
                buildNotification(
                        context,
                        content
                )
        )
    }

    private fun buildNotification(
        context: Context,
        content: String
    ): Notification {
        val builder = NotificationCompat.Builder(
                context,
                content
        )
        builder.apply {
            setContentTitle("Notification!!")
            setContentText(content)
            setSmallIcon(android.R.drawable.sym_def_app_icon)
        }
        return builder.build()
    }
}
