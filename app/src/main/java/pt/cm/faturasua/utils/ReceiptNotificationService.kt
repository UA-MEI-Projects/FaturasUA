package pt.cm.faturasua.utils

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import pt.cm.faturasua.R
import javax.inject.Inject
import kotlin.random.Random

class ReceiptNotificationService(
    private val context: Context
) {
    fun notificationBase(title: String, text: String): Notification{
        val notification = NotificationCompat.Builder(context, NOTIFICATION_MSG_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Dissapears after clicking the notification
            .setAutoCancel(true)
            .setTimeoutAfter(NOTIFICATION_TIME_OUT)
            .build()
        return notification
    }

    fun sendReceiptAddedNotification(){
        val notification = notificationBase(
                            title = "Recibo adicionado!",
                            text = "O seu recibo foi adicionado com sucesso."
                            )

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(Random.nextInt(), notification)
    }

    fun sendReceiptErrorNotification(){
        val notification = notificationBase(
                            title = "Falha ao adicionar recibo." ,
                            text = "Não foi possível adicionar o seu recibo. Por favor, veja se tem conexão à internet."
                                )

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(Random.nextInt(), notification)
    }

    fun sendReceiptErrorFormatNotification(){
        val notification = notificationBase(
            title = "Falha ao adicionar recibo." ,
            text = "Não foi possível adicionar o seu recibo."
        )


        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(Random.nextInt(), notification)
    }
    companion object{
        val NOTIFICATION_MSG_ID: String = "receipt_added_notification"
        val NOTIFICATION_TIME_OUT:Long =  5000
    }
}