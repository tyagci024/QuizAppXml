import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.*

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            // Intent'ten yanlış soruların listesini çıkar
            val wrongQuestions = intent.getStringArrayListExtra("wrongQuestions")

            // Liste boş değilse
            if (!wrongQuestions.isNullOrEmpty()) {
                // Her bir yanlış soru için bir bildirim oluştur
                for ((index, wrongQuestion) in wrongQuestions.withIndex()) {
                    val notificationId = index + 1 // Her bildirim için benzersiz ID

                    // Bildirimi oluştur
                    val notification = buildNotification(context, wrongQuestion)

                    // Kullanıcıya bildir
                    showNotification(context, notificationId, notification)
                }
            }
        }
    }

    private fun buildNotification(context: Context, contentText: String): Notification {
        val channelId = "default_channel"
        val channelName = "Varsayılan Kanal"

        // Bildirim kanalını oluştur (Android Oreo ve sonrası için gereklidir)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Bildirimi oluştur
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("Yanlış Soru")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .build()
    }

    private fun showNotification(context: Context, notificationId: Int, notification: Notification) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Kullanıcıya bildir
        notificationManager.notify(notificationId, notification)
    }
}
