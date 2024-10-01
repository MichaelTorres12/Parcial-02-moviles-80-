// app/src/main/java/com/example/gestinpermisosapp/ui/components/SendNotificationButton.kt
package com.example.gestinpermisosapp.ui.components

import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun SendNotificationButton() {
    val context = LocalContext.current

    Button(onClick = {
        NotificationHelper.sendNotification(
            context = context,
            notificationId = 1,
            title = "Notificación Generada",
            content = "Esta notificación ha sido generada por la APP movil de Michael Torres para el parcial 02 de Apps Moviles Avanzadas."
        )
    }) {
        Text("Enviar Notificación")
    }
}
