package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranAudioUi(onBack: () -> Unit, onNavigateToPremium: () -> Unit) {
    if (!ThemeState.isPremiumPro) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Fitur Premium Pro", fontSize = 24.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Berlangganan Premium Pro (Rp 50.000) untuk mendengarkan audio Murottal Al-Quran pilihan.", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onNavigateToPremium) {
                Text("Tingkatkan ke Premium Pro")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = onBack) {
                Text("Kembali")
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Murottal Al-Quran") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val quranList = listOf("Surah Al-Fatihah", "Surah Yasin", "Surah Ar-Rahman", "Surah Al-Waqi'ah", "Surah Al-Mulk", "Juz 30 (Murottal Lengkap)")
            items(quranList.size) { index ->
                var isPlaying by remember { mutableStateOf(false) }
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(quranList[index], fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 16.sp)
                            Text("Qari: Mishary Rashid Alafasy", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        var isDownloaded by remember { mutableStateOf(false) }
                        IconButton(
                            onClick = { 
                                if (ThemeState.isPremiumUltimate) {
                                    isDownloaded = true 
                                } else {
                                    onNavigateToPremium()
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Download, 
                                contentDescription = "Download", 
                                tint = if (isDownloaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)
                        ) {
                            Icon(
                                if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow, 
                                contentDescription = "Play/Stop",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
