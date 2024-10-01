// app/src/main/java/com/example/gestinpermisosapp/ui/components/NotificationHelper.kt
package com.example.gestinpermisosapp.ui.components

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.gestinpermisosapp.R

object NotificationHelper {
    private const val CHANNEL_ID = "gestin_permisos_app_channel"
    private const val CHANNEL_NAME = "Gestión Permisos App Notifications"
    private const val CHANNEL_DESCRIPTION = "Canal de notificaciones para la aplicación Gestión Permisos App"

    /**
     * Crea el canal de notificación si es necesario.
     *
     * @param context Contexto de la aplicación.
     */
    fun createNotificationChannel(context: Context) {
        // Crear el canal de notificación solo en Android 8.0 o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }

            // Registrar el canal en el sistema
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Envía una notificación.
     *
     * @param context Contexto de la aplicación.
     * @param notificationId ID de la notificación.
     * @param title Título de la notificación.
     * @param content Contenido de la notificación.
     */
    fun sendNotification(context: Context, notificationId: Int, title: String, content: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Asegúrate de tener un ícono en drawable
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}
