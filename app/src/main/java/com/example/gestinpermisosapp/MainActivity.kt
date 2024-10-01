// app/src/main/java/com/example/gestinpermisosapp/MainActivity.kt
package com.example.gestinpermisosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.gestinpermisosapp.ui.screens.*
import com.example.gestinpermisosapp.ui.theme.GestionPermisosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestionPermisosAppTheme {
                // Configuración de la navegación
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") { MainScreen(navController = navController) }
                    composable("map") { MapScreen() }
                    composable("upload") { UploadMediaScreen() }
                }
            }
        }
    }
}
