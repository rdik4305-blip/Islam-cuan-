package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class HajjStep(val title: String, val description: String)

val hajjSteps = listOf(
    HajjStep("Ihram", "Berniat haji dan mengenakan pakaian ihram dari miqat."),
    HajjStep("Wukuf di Arafah", "Hadir dan berdiam diri di Arafah pada tanggal 9 Dzulhijjah."),
    HajjStep("Mabit di Muzdalifah", "Bermalam di Muzdalifah untuk mencari kerikil yang akan digunakan untuk melempar jumrah."),
    HajjStep("Melempar Jumrah Aqabah", "Melempar 7 batu kerikil ke Jumrah Aqabah pada tanggal 10 Dzulhijjah."),
    HajjStep("Tahallul Awal", "Mencukur sebagian atau seluruh rambut setelah melempar Jumrah Aqabah."),
    HajjStep("Tawaf Ifadah", "Mengelilingi Ka'bah sebanyak 7 kali."),
    HajjStep("Sa'i", "Berlari-lari kecil antara bukit Shafa dan Marwah sebanyak 7 kali."),
    HajjStep("Mabit di Mina", "Bermalam di Mina pada hari-hari Tasyrik (11, 12, 13 Dzulhijjah)."),
    HajjStep("Melempar Tiga Jumrah", "Melempar Jumrah Ula, Wustha, dan Aqabah pada hari-hari Tasyrik."),
    HajjStep("Tawaf Wada'", "Tawaf perpisahan sebelum meninggalkan kota Makkah.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HajjUi(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panduan Haji") },
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
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(hajjSteps.withIndex().toList()) { (index, step) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer, shape = androidx.compose.foundation.shape.CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = step.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = step.description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
