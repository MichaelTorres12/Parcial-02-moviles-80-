// app/src/main/java/com/example/gestinpermisosapp/ui/components/PermissionRequestDialog.kt
package com.example.gestinpermisosapp.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.gestinpermisosapp.R

/**
 * Composable que muestra un diálogo para justificar y solicitar un permiso.
 *
 * @param title Título del diálogo.
 * @param description Descripción de por qué se solicita el permiso.
 * @param onDismiss Función a ejecutar al cerrar el diálogo.
 * @param onRequestPermission Función a ejecutar al confirmar la solicitud de permiso.
 */
@Composable
fun PermissionRequestDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    onRequestPermission: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = { Text(text = description) },
        confirmButton = {
            Button(onClick = {
                onRequestPermission()
                onDismiss()
            }) {
                Text(stringResource(R.string.accept))
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

