package pt.cm.faturasua.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import pt.cm.faturasua.R
import javax.inject.Inject
import kotlin.random.Random

class ReceiptNotificationService(
    private val context: Context
) {


    fun sendReceiptAddedNotification(){
        val notification = NotificationCompat.Builder(context, NOTIFICATION_MSG_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Recibo adicionado!")
            .setContentText("O seu recibo foi adicionado com sucesso.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Dissapears after clicking the notification
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(Random.nextInt(), notification)
    }
    companion object{
        val NOTIFICATION_MSG_ID: String = "receipt_added_notification"
    }
}