// app/src/main/java/com/example/gestinpermisosapp/ui/screens/MainScreen.kt
package com.example.gestinpermisosapp.ui.screens

import android.Manifest
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gestinpermisosapp.ui.components.PermissionRequestDialog
import com.example.gestinpermisosapp.ui.components.SendNotificationButton
import com.example.gestinpermisosapp.ui.components.NotificationHelper
import androidx.navigation.NavController
import com.google.accompanist.permissions.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current

    var showLocationDialog by remember { mutableStateOf(false) }
    var showMediaDialog by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }

    // Definir permisos de medios según la versión de Android
    val mediaPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    // Definir permiso de notificaciones según la versión de Android
    val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.POST_NOTIFICATIONS
    } else {
        null // No es necesario solicitar permiso en versiones anteriores
    }

    // Launcher para solicitar permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.any { it }
        if (isGranted) {
            Toast.makeText(context, "Permiso de Ubicación concedido", Toast.LENGTH_SHORT).show()
            navController.navigate("map")
        } else {
            Toast.makeText(context, "Permiso de Ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Launcher para solicitar permisos de acceso a medios
    val mediaPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        if (isGranted) {
            Toast.makeText(context, "Permiso de Medios concedido", Toast.LENGTH_SHORT).show()
            navController.navigate("upload")
        } else {
            Toast.makeText(context, "Permiso de Medios denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Launcher para solicitar permiso de notificaciones
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permiso de Notificaciones concedido", Toast.LENGTH_SHORT).show()
            NotificationHelper.createNotificationChannel(context)
        } else {
            Toast.makeText(context, "Permiso de Notificaciones denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Permisos") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Botón para ver el mapa
            Button(
                onClick = { showLocationDialog = true },
                enabled = true
            ) {
                Text("Ver Mapa")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para subir imágenes/videos
            Button(
                onClick = { showMediaDialog = true },
                enabled = true
            ) {
                Text("Subir Imágenes/Videos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para enviar notificación
            Button(
                onClick = {
                    if (notificationPermission != null) {
                        showNotificationDialog = true
                    } else {
                        // En versiones anteriores, no se requiere permiso
                        NotificationHelper.createNotificationChannel(context)
                        navController.navigate("notification")
                    }
                },
                enabled = true
            ) {
                Text("Enviar Notificación")
            }
        }

        // Diálogo para permiso de ubicación
        if (showLocationDialog) {
            PermissionRequestDialog(
                title = "Permiso de Ubicación",
                description = "Esta aplicación necesita acceder a tu ubicación para mostrar mapas y servicios relacionados.",
                onDismiss = { showLocationDialog = false },
                onRequestPermission = {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            )
        }

        // Diálogo para permiso de acceso a medios
        if (showMediaDialog) {
            PermissionRequestDialog(
                title = "Permiso de Acceso a Medios",
                description = "Esta aplicación necesita acceder a tus imágenes y videos para que puedas subir contenido multimedia.",
                onDismiss = { showMediaDialog = false },
                onRequestPermission = {
                    mediaPermissionLauncher.launch(mediaPermissions)
                }
            )
        }

        // Diálogo para permiso de notificaciones
        if (showNotificationDialog && notificationPermission != null) {
            PermissionRequestDialog(
                title = "Permiso de Notificaciones",
                description = "Esta aplicación quiere enviarte notificaciones importantes.",
                onDismiss = { showNotificationDialog = false },
                onRequestPermission = {
                    notificationPermissionLauncher.launch(notificationPermission)
                }
            )
        }
    }

    // Verificar si el permiso de notificaciones ha sido concedido o no es necesario
    val notificationPermissionGranted = if (notificationPermission == null) {
        true // No es necesario solicitar permiso en versiones anteriores
    } else {
        ContextCompat.checkSelfPermission(context, notificationPermission) == PackageManager.PERMISSION_GRANTED
    }

    if (notificationPermissionGranted) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            SendNotificationButton()
        }
    }
}
