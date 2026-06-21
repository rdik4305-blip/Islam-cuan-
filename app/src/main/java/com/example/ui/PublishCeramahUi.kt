package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishCeramahUi(onBack: () -> Unit, onNavigateToBuyCoin: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    var submitSuccess by remember { mutableStateOf(false) }

    val REQUIRED_COIN = 50

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }

    var showLocalFolderPicker by remember { mutableStateOf(false) }

    val mockFiles = listOf(
        "rekaman_murottal_pribadi_syahdu.mp3" to "5.4 MB",
        "sholawat_merdu_jiwa_hamba.mp3" to "4.8 MB",
        "ceramah_singkat_iman_ikhlas.mp3" to "3.6 MB",
        "lantunan_doa_pasrah_diri.mp3" to "6.1 MB"
    )

    if (showLocalFolderPicker) {
        AlertDialog(
            onDismissRequest = { showLocalFolderPicker = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 8.dp))
                    Text("Pilih dari Folder Rekaman", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Silakan pilih file rekaman suara Anda dari folder lokal aplikasi, atau gunakan File Manager Sistem.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    
                    Text("File Terdeteksi di HP:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        mockFiles.forEach { (name, size) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedFileUri = Uri.parse("content://mock_audio_recordings/$name")
                                        showLocalFolderPicker = false
                                    },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Audiotrack, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(name, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    }
                                    Text(size, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedButton(
                        onClick = {
                            launcher.launch("audio/mpeg")
                            showLocalFolderPicker = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Buka File Explorer Sistem")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showLocalFolderPicker = false }
                ) {
                    Text("Kembali ke Aplikasi")
                }
            }
        )
    }

    if (submitSuccess) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Berhasil Dikirim!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Rekaman Anda sedang direview oleh tim moderasi. Proses ini singkat, mohon tunggu sekitar jam 12:00 untuk melihat status penerimaan. Jika diterima, karya Anda akan masuk sistem rekomendasi.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onBack) {
                Text("Kembali ke Profil")
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publikasikan Ceramah") },
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
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { showLocalFolderPicker = true }, // Launch in-app file browser
                contentAlignment = Alignment.Center
            ) {
                if (selectedFileUri == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Audiotrack, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
                        Text("Pilih File MP3 dari HP", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text("File Terpilih:\n${selectedFileUri?.path?.substringAfterLast("/")}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Ceramah") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi Singkat / Metadata") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Biaya Publikasi", fontWeight = FontWeight.Bold)
                        Text("Membutuhkan $REQUIRED_COIN Koin")
                        Text("Koin Anda saat ini: ${UserState.coins}", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                    }
                    if (UserState.coins < REQUIRED_COIN) {
                        Button(onClick = onNavigateToBuyCoin) {
                            Text("Beli Koin")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (UserState.coins >= REQUIRED_COIN) {
                        UserState.coins -= REQUIRED_COIN
                        isSubmitting = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = selectedFileUri != null && title.isNotBlank() && description.isNotBlank() && UserState.coins >= REQUIRED_COIN && !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Kirim untuk Direview")
                }
            }
        }
    }

    LaunchedEffect(isSubmitting) {
        if (isSubmitting) {
            delay(1500)
            submitSuccess = true
            UserState.rekamanCount += 1
            UserState.recordingList.add(
                RecordingItem(
                    id = UserState.recordingList.size + 1,
                    title = title,
                    description = description,
                    author = "Hamba Allah", // User's name
                    views = 0,
                    likes = 0,
                    comments = mutableStateListOf("Masya Allah, sangat menginspirasi ceramahnya!", "Bagus sekali rekaman audionya, jernih!")
                )
            )
        }
    }
}
