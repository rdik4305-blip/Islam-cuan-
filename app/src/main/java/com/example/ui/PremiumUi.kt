package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumUi(onBack: () -> Unit, onUpgrade: () -> Unit) {
    var paymentMethod by remember { mutableStateOf("DANA") }
    var selectedTier by remember { mutableStateOf(1) } // 1 for 30k, 2 for 50k
    var isPaid by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Muslim Pro Premium") },
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Pilih Paket Langganan", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))

            // Tier 1: Premium (30.000)
            TierCard(
                title = "Premium (Rp 30.000 / Bulan)",
                benefits = "• Akses semua tema warna aplikasi\n• Pengingat adzan dengan audio penuh\n• Bisa pasang & ganti foto profil\n• Ubah icon aplikasi sesuka hati",
                isSelected = selectedTier == 1,
                onClick = { selectedTier = 1 }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tier 2: Premium Pro (50.000)
            TierCard(
                title = "Premium Pro (Rp 50.000 / Bulan)",
                benefits = "Semua fitur Premium, ditambah:\n• Streaming Radio Islam sepuasnya\n• Audio bacaan Al-Quran live\n• Chat dengan Ustadz di seluruh dunia\n• Lencana Verifikasi Bulanan\n• Akses fitur Uji Coba (Beta) sebelum rilis",
                isSelected = selectedTier == 2,
                onClick = { selectedTier = 2 }
            )

            // Tier 3: Premium Ultimate (150.000)
            TierCard(
                title = "Premium Ultimate (Rp 150.000 / Bulan)",
                benefits = "Semua fitur Pro, ditambah:\n• Bikin Avatar AI untuk profil\n• Download audio ceramah\n• Prioritas chat (Fast Response) dgn Ustadz\n• Publikasi rekaman audio ceramah Anda sendiri",
                isSelected = selectedTier == 3,
                onClick = { selectedTier = 3 }
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Metode Pembayaran", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { paymentMethod = "DANA" }) {
                RadioButton(selected = paymentMethod == "DANA", onClick = { paymentMethod = "DANA" })
                Text("Transfer ke DANA (0889509884480)")
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { paymentMethod = "Rekening Bank" }) {
                RadioButton(selected = paymentMethod == "Rekening Bank", onClick = { paymentMethod = "Rekening Bank" })
                Text("Transfer ke Rekening (0889509884480)")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = isPaid, onCheckedChange = { isPaid = it })
                Text("Saya sudah melakukan transfer pembayaran", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (selectedTier == 1) {
                        ThemeState.isPremium = true
                        ThemeState.isPremiumPro = false
                        ThemeState.isPremiumUltimate = false
                    } else if (selectedTier == 2) {
                        ThemeState.isPremium = true
                        ThemeState.isPremiumPro = true
                        ThemeState.isPremiumUltimate = false
                        ThemeState.isVerified = true // Berikan badge verifikasi otomatis
                    } else if (selectedTier == 3) {
                        ThemeState.isPremium = true
                        ThemeState.isPremiumPro = true
                        ThemeState.isPremiumUltimate = true
                        ThemeState.isVerified = true // Berikan badge verifikasi otomatis
                    }
                    ThemeState.premiumExpiryTime = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000) // 30 days
                    onUpgrade()
                },
                enabled = isPaid,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(if (isPaid) "Aktifkan Paket Sekarang" else "Selesaikan Pembayaran", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TierCard(title: String, benefits: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface)
                if (isSelected) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(benefits, fontSize = 14.sp, color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
