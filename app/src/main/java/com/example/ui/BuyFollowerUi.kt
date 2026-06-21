package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.People
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
fun BuyFollowerUi(onBack: () -> Unit) {
    var paymentMethod by remember { mutableStateOf("DANA") }
    var selectedPackage by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var isPaid by remember { mutableStateOf(false) }

    val followerPackages = listOf(
        Pair(100, 10000),
        Pair(500, 45000),
        Pair(1000, 85000),
        Pair(5000, 400000)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beli Follower") },
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
                    Icon(Icons.Default.People, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Follower Anda", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("${UserState.followers}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(followerPackages) { packagePair ->
                    val (followers, price) = packagePair
                    val isSelected = selectedPackage == packagePair
                    Card(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                        onClick = {
                            selectedPackage = packagePair
                            isPaid = false
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.People, contentDescription = null, tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("$followers Followers", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface)
                            }
                            Text("Rp $price", fontWeight = FontWeight.Bold, color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                if (selectedPackage != null) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Pilih Pembayaran", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically, 
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(selected = paymentMethod == "DANA", onClick = { paymentMethod = "DANA" })
                            Text("Transfer ke DANA (0889509884480)")
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Checkbox(checked = isPaid, onCheckedChange = { isPaid = it })
                            Text("Saya sudah melakukan transfer Rp ${selectedPackage?.second ?: 0}", fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (isPaid) {
                                    UserState.followers += selectedPackage!!.first
                                    selectedPackage = null
                                    isPaid = false
                                }
                            },
                            enabled = isPaid,
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Beli Follower", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
