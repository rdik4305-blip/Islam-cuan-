package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyCoinUi(onBack: () -> Unit) {
    var paymentMethod by remember { mutableStateOf("DANA") }
    var selectedCoins by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var isPaid by remember { mutableStateOf(false) }

    val coinPackages = listOf(
        Pair(50, 2000),
        Pair(250, 10000),
        Pair(1250, 50000),
        Pair(4000, 150000)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beli Koin") },
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
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Total Koin Anda", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("${UserState.coins}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(coinPackages) { packagePair ->
                    val (coins, price) = packagePair
                    val isSelected = selectedCoins == packagePair
                    Card(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                        onClick = {
                            selectedCoins = packagePair
                            isPaid = false
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("$coins Koin", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface)
                            }
                            Text("Rp $price", fontWeight = FontWeight.Bold, color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                if (selectedCoins != null) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Pilih Pembayaran", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { paymentMethod = "DANA" }) {
                            RadioButton(selected = paymentMethod == "DANA", onClick = { paymentMethod = "DANA" })
                            Text("Transfer ke DANA (0889509884480)")
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Checkbox(checked = isPaid, onCheckedChange = { isPaid = it })
                            Text("Saya sudah melakukan transfer Rp ${selectedCoins?.second ?: 0}", fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (isPaid) {
                                    UserState.coins += selectedCoins!!.first
                                    selectedCoins = null
                                    isPaid = false
                                }
                            },
                            enabled = isPaid,
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Beli Koin Sekarang", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Info: Koin gratis bisa didapatkan dengan rutin membaca Al-Quran di dalam aplikasi.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
