package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.api.SurahDetails

import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*

@Composable
fun QuranUi(viewModel: QuranViewModel, onNavigateToBuyDiamond: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedLockedSurah by remember { mutableStateOf<Int?>(null) }

    if (selectedLockedSurah != null) {
        AlertDialog(
            onDismissRequest = { selectedLockedSurah = null },
            title = { Text("Surah Terkunci") },
            text = { 
                if (UserState.diamonds >= 10) {
                    Text("Buka surah ini dengan 10 Diamond?")
                } else {
                    Text("Diamond Anda tidak cukup (10 Diamond diperlukan). Silakan beli Diamond terlebih dahulu.")
                }
            },
            confirmButton = {
                if (UserState.diamonds >= 10) {
                    Button(onClick = { 
                        UserState.diamonds -= 10
                        UserState.unlockedSurahs.add(selectedLockedSurah!!)
                        viewModel.loadSurahDetail(selectedLockedSurah!!)
                        selectedLockedSurah = null
                    }) {
                        Text("Buka (10 Diamond)")
                    }
                } else {
                    Button(onClick = { 
                        selectedLockedSurah = null
                        onNavigateToBuyDiamond()
                    }) {
                        Text("Beli Diamond")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedLockedSurah = null }) { Text("Batal") }
            }
        )
    }

    when (val state = uiState) {
        is QuranUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is QuranUiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.message, color = MaterialTheme.colorScheme.error)
            }
        }
        is QuranUiState.SurahList -> {
            LazyColumn(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.surahs) { surah ->
                    val isLocked = surah.number > 10 && !UserState.unlockedSurahs.contains(surah.number)
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { 
                            if (isLocked) {
                                selectedLockedSurah = surah.number
                            } else {
                                UserState.coins += 5 // Earn free coins by reading
                                viewModel.loadSurahDetail(surah.number)
                            }
                        },
                        colors = CardDefaults.cardColors(containerColor = if (isLocked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isLocked) {
                                    Icon(Icons.Default.Lock, contentDescription = "Locked", tint = MaterialTheme.colorScheme.error, modifier = Modifier.padding(end = 12.dp))
                                }
                                Column {
                                    Text("${surah.number}. ${surah.englishName}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text(surah.englishNameTranslation, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(surah.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text("${surah.numberOfAyahs} Ayahs", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
        is QuranUiState.SurahDetail -> {
            SurahDetailUi(state.details) { viewModel.loadSurahs() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahDetailUi(details: SurahDetails, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        TopAppBar(
            title = { Text(details.englishName) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(details.ayahs) { ayah ->
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        text = ayah.text,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Ayah ${ayah.numberInSurah}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(Modifier.padding(top = 16.dp))
                }
            }
        }
    }
}
