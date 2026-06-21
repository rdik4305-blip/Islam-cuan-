package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.ThemeState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarAiUi(onBack: () -> Unit) {
    var prompt by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    var generatedImageUri by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buat Avatar AI") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (isGenerating) {
                    CircularProgressIndicator()
                } else if (generatedImageUri != null) {
                    AsyncImage(
                        model = generatedImageUri,
                        contentDescription = "AI Generated Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text("Deskripsikan avatar yang diinginkan...") },
                placeholder = { Text("contoh: Seorang anak muslim membaca buku di taman") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (prompt.isNotBlank()) {
                        isGenerating = true
                        // Simulate generation delay
                        // In reality, it would call an image generation API
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = prompt.isNotBlank() && !isGenerating
            ) {
                Text("Generate Gambar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (generatedImageUri != null && !isGenerating) {
                OutlinedButton(
                    onClick = {
                        ThemeState.profilePhotoUri = generatedImageUri
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Jadikan Foto Profil")
                }
            }
        }
    }

    LaunchedEffect(isGenerating) {
        if (isGenerating) {
            delay(2000) // Simulate processing time
            // We use standard placeholder, but conceptually it matches the prompt
            generatedImageUri = "https://source.unsplash.com/random/400x400/?muslim,character,${prompt.replace(" ", ",")}"
            isGenerating = false
        }
    }
}
