package com.example.exoticworld.ui.components

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import java.io.File

@Composable
fun ProfileImage(){
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var camaraImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri:Uri? ->
        uri?.let { imageUri = it }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success){
            imageUri = camaraImageUri
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true
        val storageGranted =
            permissions[Manifest.permission.READ_MEDIA_IMAGES] == true || permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true

        if (cameraGranted && storageGranted) {
            showDialog = true
        }
    }

    Box(
        modifier = Modifier
            .size(120.dp)
            .padding(16.dp)
            .clickable{
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
       if (imageUri != null){
           AsyncImage(
               model = imageUri,
               contentDescription = "Foto de perfil",
               modifier = Modifier.fillMaxSize(),
               contentScale = ContentScale.Crop
           )
       } else{
           Icon(
               imageVector = Icons.Default.Person,
               contentDescription = "Sin foto de perfil",
               modifier = Modifier.size(80.dp),
               tint = MaterialTheme.colorScheme.primary
           )
       }
    }

    if (showDialog){
        AlertDialog(
            onDismissRequest = {showDialog = false},
            title = { Text("cambiar foto de perfil") },
            text = {Text("Selecciona o toma una foto")},
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false

                    val photoFile = File.createTempFile(
                        "profile_", ".jpg",
                        context.cacheDir
                    )
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        photoFile
                    )
                    camaraImageUri = uri
                    camaraLauncher.launch(uri)
                }) {
                    Text("Tomese una foto")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog= false
                    galleryLauncher.launch("image/*")
                }) {
                    Text("elige una foto de tu galería")
                }
            }
        )
    }
}