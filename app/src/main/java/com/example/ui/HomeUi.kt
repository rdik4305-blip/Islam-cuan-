package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CompassCalibration
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.api.Timings

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlinx.coroutines.delay

@Composable
fun HomeUi(
    viewModel: MainViewModel,
    onNavigateToQuran: () -> Unit,
    onNavigateToTasbih: () -> Unit,
    onNavigateToHajj: () -> Unit,
    onNavigateToAsmaulHusna: () -> Unit,
    onNavigateToDoa: () -> Unit,
    onNavigateToQibla: () -> Unit,
    onNavigateToSedekah: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToRadio: () -> Unit,
    onNavigateToQuranAudio: () -> Unit,
    onNavigateToChatUstadz: () -> Unit,
    onNavigateToJadwalSholat: () -> Unit,
    onNavigateToRecordingFeed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val city by viewModel.city.collectAsState()
    val country by viewModel.country.collectAsState()

    var currentTime by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        }
        while (true) {
            currentTime = sdf.format(Date())
            delay(1000)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // App Bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("LOKASI", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("$city, $country", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(20.dp))
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = MaterialTheme.colorScheme.onBackground)
                }
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant).clickable { onNavigateToProfile() }, 
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = MaterialTheme.colorScheme.onBackground)
                }
            }
        }

        when (val state = uiState) {
            is HomeUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is HomeUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is HomeUiState.Success -> {
                val timings = state.response.data.timings
                val dateInfo = state.response.data.date
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Hero: Next Prayer
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(32.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(24.dp)
                        ) {
                            Column {
                                Text(
                                    "Waktu Sekarang",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                    letterSpacing = 1.sp
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    currentTime,
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "Selanjutnya: Dzuhur ${timings.Dhuhr}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape).padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    item {
                        // Prayer Timeline
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PrayerTimeItem("Subuh", timings.Fajr, false)
                            PrayerTimeItem("Terbit", timings.Sunrise, false)
                            PrayerTimeItem("Dzuhur", timings.Dhuhr, true)
                            PrayerTimeItem("Ashar", timings.Asr, false)
                            PrayerTimeItem("Maghrib", timings.Maghrib, false)
                            PrayerTimeItem("Isya", timings.Isha, false)
                        }
                    }

                    item {
                        // Quick Actions Grid (2 rows)
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                QuickActionItem("Al-Quran", Icons.Outlined.Book) { onNavigateToQuran() }
                                QuickActionItem("Kiblat", Icons.Outlined.CompassCalibration) { onNavigateToQibla() }
                                QuickActionItem("Tasbih", Icons.Default.Mosque) { onNavigateToTasbih() }
                                QuickActionItem("Doa Harian", Icons.Outlined.Book) { onNavigateToDoa() }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                QuickActionItem("Haji/Umrah", Icons.Default.Mosque) { onNavigateToHajj() }
                                QuickActionItem("Asmaul Husna", Icons.Default.Mosque) { onNavigateToAsmaulHusna() }
                                QuickActionItem("Rekaman Sendiri", Icons.Default.Mic) { onNavigateToRecordingFeed() }
                                QuickActionItem("Sedekah", Icons.Default.Mosque) { onNavigateToSedekah() }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                QuickActionItem("Radio Islam", Icons.Default.Mosque) { onNavigateToRadio() }
                                QuickActionItem("Audio Quran", Icons.Outlined.Book) { onNavigateToQuranAudio() }
                                QuickActionItem("Chat Ustadz", Icons.Default.Person) { onNavigateToChatUstadz() }
                                QuickActionItem("Jadwal Sholat", Icons.Default.Mosque) { onNavigateToJadwalSholat() }
                            }
                        }
                    }

                    item {
                        // Verse of the Day
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("AYAT HARI INI", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 2.sp)
                                Text("Al-Baqarah: 286", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    "\"Allah tidak membebani seseorang melainkan sesuai dengan kesanggupannya.\"",
                                    fontSize = 20.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 28.sp
                                )
                                Text(
                                    "Jadikan hari ini penuh semangat dengan keyakinan bahwa setiap ujian ada jalan keluarnya.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                            Button(
                                onClick = { },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Bagikan Ayat", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerTimeItem(name: String, time: String, isActive: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = if (!isActive) Modifier.alpha(0.4f) else Modifier) {
        if (isActive) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
            Spacer(Modifier.height(4.dp))
        }
        Text(name.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground)
        Text(time, fontSize = 14.sp, fontWeight = if (isActive) FontWeight.Bold else FontWeight.SemiBold, color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun QuickActionItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }.padding(4.dp)) {
        Box(
            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Spacer(Modifier.height(8.dp))
        Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
