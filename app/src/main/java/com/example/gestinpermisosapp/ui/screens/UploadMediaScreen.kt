// app/src/main/java/com/example/gestinpermisosapp/ui/screens/UploadMediaScreen.kt
package com.example.gestinpermisosapp.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.gestinpermisosapp.ui.components.PermissionRequestDialog
import com.google.accompanist.permissions.*
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UploadMediaScreen() {
    val context = LocalContext.current

    // Definir permisos de medios según la versión de Android
    val mediaPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
    } else {
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    val mediaPermissionState = rememberMultiplePermissionsState(
        permissions = mediaPermissions
    )

    var showMediaDialog by remember { mutableStateOf(false) }
    var mediaUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val mediaPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        mediaUris = uris.take(4)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (mediaPermissionState.allPermissionsGranted) {
                    mediaPickerLauncher.launch("*/*")
                } else {
                    showMediaDialog = true
                }
            },
            enabled = true
        ) {
            Text("Subir Imágenes/Videos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mediaUris.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(mediaUris) { uri ->
                    MediaPreview(uri = uri)
                }
            }
        }
    }

    if (showMediaDialog) {
        PermissionRequestDialog(
            title = "Permiso de Acceso a Medios",
            description = "Esta aplicación necesita acceder a tus imágenes y videos para que puedas subir contenido multimedia.",
            onDismiss = { showMediaDialog = false },
            onRequestPermission = {
                mediaPermissionState.launchMultiplePermissionRequest()
            }
        )
    }

    // Manejar el caso en que el permiso ha sido concedido
    LaunchedEffect(mediaPermissionState.allPermissionsGranted) {
        if (mediaPermissionState.allPermissionsGranted && showMediaDialog) {
            showMediaDialog = false
            mediaPickerLauncher.launch("*/*")
        }
    }

    // Manejar el caso en que el permiso ha sido denegado permanentemente
    if (!mediaPermissionState.allPermissionsGranted && !mediaPermissionState.shouldShowRationale) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Permiso Necesario") },
            text = { Text("Necesitas habilitar el permiso de medios desde la configuración.") },
            confirmButton = {
                TextButton(onClick = {
                    // Abrir la configuración de la aplicación
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", context.packageName, null)
                    context.startActivity(intent)
                }) {
                    Text("Abrir Configuración")
                }
            }
        )
    }
}

@Composable
fun MediaPreview(uri: Uri) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = uri),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
