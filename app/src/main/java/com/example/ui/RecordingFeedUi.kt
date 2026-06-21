package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordingFeedUi(onBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by remember { mutableStateOf(0) } // 0: Semua Rekaman, 1: Halaman Rekomendasi (Orbit)
    
    // Wallet / Withdrawal Dialog States
    var showWalletDialog by remember { mutableStateOf(false) }
    var danaNumber by remember { mutableStateOf("0889509884480") }
    var inputWithdrawAmount by remember { mutableStateOf("10000") }
    var withdrawError by remember { mutableStateOf<String?>(null) }
    var withdrawSuccessMessage by remember { mutableStateOf<String?>(null) }

    // Dialog & Social Share States
    var showGiftDialog by remember { mutableStateOf<RecordingItem?>(null) }
    var giftAmount by remember { mutableStateOf("10.000") }
    var isGiftConfirmed by remember { mutableStateOf(false) }
    
    var showCommentsDialog by remember { mutableStateOf<RecordingItem?>(null) }
    var newCommentText by remember { mutableStateOf("") }
    
    var showShareDialog by remember { mutableStateOf<RecordingItem?>(null) }
    
    var currentlyPlayingItem by remember { mutableStateOf<RecordingItem?>(null) }
    var playProgress by remember { mutableStateOf(0f) }
    var targetReachedOrbitDialog by remember { mutableStateOf<String?>(null) }
    
    val allItems = UserState.recordingList

    // Playing animation simulator
    LaunchedEffect(currentlyPlayingItem) {
        if (currentlyPlayingItem != null) {
            playProgress = 0f
            while (playProgress < 1f) {
                delay(30)
                playProgress += 0.05f
            }
            // Done playing: increase views!
            val updated = currentlyPlayingItem!!
            val initialViews = updated.views
            val newViews = initialViews + 1500
            
            val idx = UserState.recordingList.indexOfFirst { it.id == updated.id }
            if (idx != -1) {
                val currentItem = UserState.recordingList[idx]
                val itemCopy = currentItem.copy(views = newViews)
                UserState.recordingList[idx] = itemCopy
                
                // If it was less than 10k, but now crossed 10k:
                if (initialViews < 10000 && newViews >= 10000) {
                    targetReachedOrbitDialog = itemCopy.title
                }
            }
            currentlyPlayingItem = null
        }
    }

    // 1. Social Sharing Dialog Modal
    if (showShareDialog != null) {
        val shareItem = showShareDialog!!
        AlertDialog(
            onDismissRequest = { showShareDialog = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 8.dp))
                    Text("Bagikan Rekaman Syiar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Bagikan rekaman '${shareItem.title}' karya ${shareItem.author} ke media sosial untuk meluaskan dakwah dan meraup berkah syiar.", fontSize = 13.sp)
                    
                    Text("Pilih Media Sosial:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SocialShareIcon("WhatsApp", Color(0xFF25D366)) {
                            coroutineScope.launch {
                                showShareDialog = null
                                snackbarHostState.showSnackbar("Berhasil dibagikan ke WhatsApp!")
                            }
                        }
                        SocialShareIcon("Facebook", Color(0xFF1877F2)) {
                            coroutineScope.launch {
                                showShareDialog = null
                                snackbarHostState.showSnackbar("Berhasil dibagikan ke Facebook!")
                            }
                        }
                        SocialShareIcon("Instagram", Color(0xFFE4405F)) {
                            coroutineScope.launch {
                                showShareDialog = null
                                snackbarHostState.showSnackbar("Berhasil dibagikan ke Instagram Story!")
                            }
                        }
                        SocialShareIcon("Twitter", Color(0xFF1DA1F2)) {
                            coroutineScope.launch {
                                showShareDialog = null
                                snackbarHostState.showSnackbar("Berhasil dibagikan ke Twitter / X!")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                showShareDialog = null
                                snackbarHostState.showSnackbar("Tautan rekaman telah disalin ke Clipboard!")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Salin Tautan Rekaman", fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showShareDialog = null }) { Text("Kembali") }
            }
        )
    }

    // 2. Wallet & Cashout DANA Modal
    if (showWalletDialog) {
        AlertDialog(
            onDismissRequest = { 
                showWalletDialog = false 
                withdrawError = null
                withdrawSuccessMessage = null
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 8.dp))
                    Text("Dompet Syiar & Pencairan", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Penghasilan Gift Anda", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Rp ${UserState.giftBalance}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text("Dana dapat langsung dicairkan ke Akun DANA Anda", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer, textAlign = TextAlign.Center)
                        }
                    }

                    if (withdrawSuccessMessage != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(withdrawSuccessMessage!!, color = Color(0xFF2E7D32), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    if (withdrawError != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Error, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(withdrawError!!, color = MaterialTheme.colorScheme.onErrorContainer, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Text("Pencairan Dana ke DANA:", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    OutlinedTextField(
                        value = danaNumber,
                        onValueChange = { danaNumber = it },
                        label = { Text("Nomor Akun DANA") },
                        placeholder = { Text("Contoh: 08xxxxxxxxxx") },
                        leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = inputWithdrawAmount,
                        onValueChange = { inputWithdrawAmount = it },
                        label = { Text("Nominal Pencairan (Rp)") },
                        placeholder = { Text("Minimal 10000") },
                        leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Text("* Minimal penarikan saldo adalah Rp 10.000", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = {
                            val amt = inputWithdrawAmount.toIntOrNull() ?: 0
                            withdrawError = null
                            withdrawSuccessMessage = null
                            
                            if (danaNumber.trim().length < 9) {
                                withdrawError = "Format nomor HP DANA tidak valid!"
                            } else if (amt < 10000) {
                                withdrawError = "Minimal nominal penarikan adalah Rp 10.000!"
                            } else if (amt > UserState.giftBalance) {
                                withdrawError = "Saldo Anda tidak mencukupi untuk melakukan penarikan ini!"
                            } else {
                                // Process Withdrawal
                                UserState.giftBalance -= amt
                                UserState.giftTransactions.add(
                                    0, // Insert at top
                                    GiftTransaction(
                                        id = UserState.giftTransactions.size + 1,
                                        description = "Pencairan Saldo ke DANA ($danaNumber)",
                                        date = "Hari Ini",
                                        amount = amt,
                                        isOutflow = true
                                    )
                                )
                                withdrawSuccessMessage = "Berhasil! Penarikan Rp $amt telah dikirim ke nomor DANA $danaNumber. Silakan cek aplikasi DANA Anda!"
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Output, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mulai Cairkan ke DANA")
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Text("Riwayat Transaksi:", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    LazyColumn(
                        modifier = Modifier.height(110.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(UserState.giftTransactions) { trx ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(trx.description, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text(trx.date, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text(
                                    text = if (trx.isOutflow) "-Rp ${trx.amount}" else "+Rp ${trx.amount}",
                                    color = if (trx.isOutflow) Color(0xFFD32F2F) else Color(0xFF388E3C),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showWalletDialog = false 
                        withdrawError = null
                        withdrawSuccessMessage = null
                    }
                ) {
                    Text("Tutup Dompet")
                }
            }
        )
    }

    if (targetReachedOrbitDialog != null) {
        AlertDialog(
            onDismissRequest = { targetReachedOrbitDialog = null },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Stars, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Tembus 10.000 Views!", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            },
            text = {
                Text(
                    "Luar biasa! Rekaman '${targetReachedOrbitDialog}' telah menembus batas 10.000 views!\n\nRekaman ini telah resmi masuk ke Halaman Rekomendasi utama. Tim Produser dari Studio Musik Indonesia akan mengorbitkan rekaman suara emas ini ke Label Music Nasional mereka sendiri! Selamat!",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { targetReachedOrbitDialog = null },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Masya Allah, Alhamdulillah!")
                }
            }
        )
    }

    // 3. Gift Support Dialog Modal
    if (showGiftDialog != null) {
        val gItem = showGiftDialog!!
        AlertDialog(
            onDismissRequest = { showGiftDialog = null },
            title = { Text("Kirim Gift Support") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Dukung karya syiar dari ${gItem.author} agar semakin termotivasi mensyiarkan kebaikan.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    
                    Text("Pilih Nominal Dukungan:", fontWeight = FontWeight.Bold)
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("10.000", "25.000", "50.000").forEach { amount ->
                            FilterChip(
                                selected = giftAmount == amount,
                                onClick = { giftAmount = amount },
                                label = { Text("Rp $amount") }
                            )
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Metode Transfer:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer, fontSize = 12.sp)
                            Text("DANA: 0889509884480", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onErrorContainer)
                            Text("Silakan transfer sesuai nominal pilihan Anda ke nomor DANA di atas.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isGiftConfirmed, onCheckedChange = { isGiftConfirmed = it })
                        Text("Saya sudah melakukan transfer Rp $giftAmount ke DANA", fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val numAmount = giftAmount.replace(".", "").toIntOrNull() ?: 10000
                        
                        // If user gifts their OWN item, the virtual funds immediately reflect in UserState's giftBalance!
                        if (gItem.isUser) {
                            UserState.giftBalance += numAmount
                            UserState.giftTransactions.add(
                                0,
                                GiftTransaction(
                                    id = UserState.giftTransactions.size + 1,
                                    description = "Dukungan Fans Masuk (Rp $giftAmount)",
                                    date = "Hari Ini",
                                    amount = numAmount,
                                    isOutflow = false
                                )
                            )
                        }

                        coroutineScope.launch {
                            showGiftDialog = null
                            isGiftConfirmed = false
                            snackbarHostState.showSnackbar("Dukungan support Rp $giftAmount berhasil dikirim!")
                        }
                    },
                    enabled = isGiftConfirmed
                ) {
                    Text("Kirim Dukungan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGiftDialog = null }) { Text("Batal") }
            }
        )
    }

    // 4. Comments Modal Dialog
    if (showCommentsDialog != null) {
        val dialogItem = showCommentsDialog!!
        val listIndex = allItems.indexOfFirst { it.id == dialogItem.id }
        AlertDialog(
            onDismissRequest = { showCommentsDialog = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Comment, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 8.dp))
                    Text("Komentar Syiar (${dialogItem.comments.size})", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(modifier = Modifier.height(300.dp)) {
                    LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(dialogItem.comments) { comment ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (comment.length > 2) comment.take(1).uppercase() else "H", 
                                            fontWeight = FontWeight.Bold, 
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 12.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text("Pengguna Lain", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                                        Text(comment, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = newCommentText,
                            onValueChange = { newCommentText = it },
                            placeholder = { Text("Tulis komentar syiar...") },
                            modifier = Modifier.weight(1f),
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (newCommentText.isNotBlank()) {
                                    if (listIndex != -1) {
                                        UserState.recordingList[listIndex].comments.add(newCommentText)
                                        showCommentsDialog = UserState.recordingList[listIndex]
                                    }
                                    newCommentText = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Kirim", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCommentsDialog = null }) { Text("Tutup") }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Halaman Rekaman Sendiri") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    // Open DANA wallet cashout modal!
                    IconButton(onClick = { showWalletDialog = true }) {
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Dompet Pencairan DANA", tint = MaterialTheme.colorScheme.primary)
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
            // Tab Header
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Semua Rekaman") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Rekomendasi Orbit")
                        }
                    }
                )
            }

            AnimatedVisibility(visible = currentlyPlayingItem != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Audiotrack, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Sedang Mendengarkan: ${currentlyPlayingItem?.title}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            CircularProgressIndicator(progress = { playProgress }, strokeWidth = 3.dp, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Simulasi memutar audio untuk mendongkrak views (+1500 views per pemutaran)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }

            val filteredItems = allItems.filter { item ->
                if (selectedTab == 0) {
                    true
                } else {
                    item.views >= 10000
                }
            }

            if (filteredItems.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            if (selectedTab == 1) "Belum ada rekaman yang menembus 10.000 views.\nMainkan rekaman lokal milik Anda atau pengguna lain agar dipromosikan ke Studio Musik Indonesia!"
                            else "Belum ada rekaman diunggah. Masuk ke halaman profil, klik 'Publikasikan Ceramah' untuk mengunggah karya pertama Anda!",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredItems) { item ->
                        val itemIndex = allItems.indexOfFirst { it.id == item.id }
                        val isOrbit = item.views >= 10000

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isOrbit) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) 
                                                 else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isOrbit) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Icon(Icons.Default.Stars, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("REKOMENDASI - Orbit Studio Musik Indonesia", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text("Syiar Kreator", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                        }
                                    }

                                    // SIMULATOR: Click here to instantly send fan virtual gift check!
                                    if (item.isUser) {
                                        Button(
                                            onClick = {
                                                val simulatedGift = listOf(10000, 25000, 50000).random()
                                                UserState.giftBalance += simulatedGift
                                                UserState.giftTransactions.add(
                                                    0,
                                                    GiftTransaction(
                                                        id = UserState.giftTransactions.size + 1,
                                                        description = "Fan virtual gift (Simulasi)",
                                                        date = "Hari Ini",
                                                        amount = simulatedGift,
                                                        isOutflow = false
                                                    )
                                                )
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("Dapat virtual gift Rp $simulatedGift masuk ke Dompet Pencairan!")
                                                }
                                            },
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9), contentColor = Color(0xFF2E7D32)),
                                            modifier = Modifier.height(26.dp)
                                        ) {
                                            Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text("Get Fan Gift", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Text("Oleh: ${item.author}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    
                                    // Play/Listen Button
                                    IconButton(
                                        onClick = {
                                            currentlyPlayingItem = item
                                        },
                                        enabled = currentlyPlayingItem == null
                                    ) {
                                        Icon(Icons.Default.PlayCircle, contentDescription = "Putar", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(item.description, fontSize = 13.sp)

                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // View count pill
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("${item.views} Views", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                    
                                    if (!isOrbit) {
                                        Text(" (Butuh ${10000 - item.views} views lagi ke Orbit)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(modifier = Modifier.alpha(0.5f))
                                Spacer(modifier = Modifier.height(8.dp))

                                // Social Actions (Like, Komentar, Share, Gift)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Like Action
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clickable {
                                                if (itemIndex != -1) {
                                                    val current = UserState.recordingList[itemIndex]
                                                    val newLiked = !current.isLikedByUser
                                                    val newLikesCount = if (newLiked) current.likes + 1 else current.likes - 1
                                                    UserState.recordingList[itemIndex] = current.copy(isLikedByUser = newLiked, likes = newLikesCount)
                                                }
                                            }
                                            .padding(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (item.isLikedByUser) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Like",
                                            tint = if (item.isLikedByUser) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("${item.likes}", fontSize = 12.sp)
                                    }

                                    // Comment Action
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clickable { showCommentsDialog = item }
                                            .padding(4.dp)
                                    ) {
                                        Icon(Icons.Default.Comment, contentDescription = "Komentar", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("${item.comments.size}", fontSize = 12.sp)
                                    }

                                    // Share Action
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clickable {
                                                showShareDialog = item
                                            }
                                            .padding(4.dp)
                                    ) {
                                        Icon(Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Bagikan", fontSize = 12.sp)
                                    }

                                    // Gift Support Action!
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clickable { showGiftDialog = item }
                                            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(Icons.Default.CardGiftcard, contentDescription = "Gift", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Gift Support", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SocialShareIcon(name: String, brandColor: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(brandColor),
            contentAlignment = Alignment.Center
        ) {
            Text(name.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(name, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}
