package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalSholatUi(viewModel: MainViewModel, onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val city by viewModel.city.collectAsState()
    val country by viewModel.country.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jadwal Sholat") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
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
                .padding(16.dp)
        ) {
            Text("Lokasi: $city, $country", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            when (val state = uiState) {
                is HomeUiState.Loading -> CircularProgressIndicator()
                is HomeUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
                is HomeUiState.Success -> {
                    val timings = state.response.data.timings
                    val dateInfo = state.response.data.date
                    
                    Text("Tanggal: ${dateInfo.readable}", style = MaterialTheme.typography.bodyLarge)
                    Text("Hijriah: ${dateInfo.hijri.day} ${dateInfo.hijri.month.en} ${dateInfo.hijri.year}", style = MaterialTheme.typography.bodyMedium)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        item { PrayerRow("Subuh", timings.Fajr) }
                        item { PrayerRow("Matahari Terbit", timings.Sunrise) }
                        item { PrayerRow("Dzuhur", timings.Dhuhr) }
                        item { PrayerRow("Ashar", timings.Asr) }
                        item { PrayerRow("Terbenam Matahari", timings.Sunset) }
                        item { PrayerRow("Maghrib", timings.Maghrib) }
                        item { PrayerRow("Isya", timings.Isha) }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerRow(name: String, time: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, fontWeight = FontWeight.Bold)
            Text(time, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}
