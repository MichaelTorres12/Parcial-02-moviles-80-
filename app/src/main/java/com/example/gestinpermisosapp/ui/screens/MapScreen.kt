// app/src/main/java/com/example/gestinpermisosapp/ui/screens/MapScreen.kt
package com.example.gestinpermisosapp.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.*
import com.google.maps.android.compose.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen() {
    // Obtener el contexto y el scope para las corutinas
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Estado para la ubicación actual
    var currentLocation by remember { mutableStateOf<Location?>(null) }

    // Recordar el permiso de ubicación fina y gruesa
    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // Solicitar permiso si no está concedido
    LaunchedEffect(Unit) {
        if (!locationPermissionState.allPermissionsGranted) {
            locationPermissionState.launchMultiplePermissionRequest()
        }
    }

    // Obtener la ubicación actual si los permisos están concedidos
    if (locationPermissionState.allPermissionsGranted) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        LaunchedEffect(Unit) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    currentLocation = location
                }
        }
    }

    // Mostrar mensaje si los permisos no son concedidos
    if (!locationPermissionState.allPermissionsGranted) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("Permisos de ubicación no concedidos.")
        }
    } else {
        // Mostrar el mapa
        currentLocation?.let { location ->
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude),
                        15f
                    )
                },
                properties = MapProperties(isMyLocationEnabled = true),
                uiSettings = MapUiSettings(myLocationButtonEnabled = true)
            ) {
                Marker(
                    state = MarkerState(position = com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude)),
                    title = "Tu ubicación"
                )
            }
        } ?: run {
            // Mostrar mensaje de carga si la ubicación aún no se ha obtenido
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
