package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ThemeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatUstadzUi(onBack: () -> Unit, onNavigateToPremium: () -> Unit) {
    if (!ThemeState.isPremiumPro) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Fitur Premium Pro", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Berlangganan Premium Pro (Rp 50.000) untuk fitur tanya jawab dan Chat live dengan Ustadz di seluruh dunia.", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
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

    var selectedUstadz by remember { mutableStateOf<String?>(null) }
    var messageText by remember { mutableStateOf("") }
    
    // We mock messages separately for each Ustadz for simplicity, but here just general list
    val messages = remember { mutableStateListOf<Pair<Boolean, String>>() } // true: user, false: ustadz
    
    val ustadzList = listOf(
        "Ustadz Abdul Somad",
        "Ustadz Adi Hidayat",
        "Ustadz Hanan Attaki",
        "Ustadz Khalid Basalamah",
        "Ustadz Felix Siauw"
    )

    if (selectedUstadz == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Pilih Ustadz") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding).background(MaterialTheme.colorScheme.background)) {
                items(ustadzList) { ustadz ->
                    ListItem(
                        headlineContent = { Text(ustadz, fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("Online - Siap menjawab pertanyaan") },
                        leadingContent = { 
                            Box(modifier = Modifier.size(40.dp).clip(androidx.compose.foundation.shape.CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                        },
                        modifier = Modifier.clickable {
                            selectedUstadz = ustadz
                            messages.clear()
                            messages.add(Pair(false, "Assalamu'alaikum. Saya $ustadz, ada yang bisa saya bantu?"))
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
        return
    }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedUstadz ?: "Chat") },
                navigationIcon = {
                    IconButton(onClick = { selectedUstadz = null }) {
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
            if (ThemeState.isPremiumUltimate) {
                Row(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer).padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.FlashOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Jalur Prioritas (Fast Response) Aktif", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.errorContainer).clickable { onNavigateToPremium() }.padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tingkatkan ke Ultimate untuk Fast Response chat", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    val isUser = msg.first
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp, 
                                        topEnd = 16.dp, 
                                        bottomStart = if (isUser) 16.dp else 0.dp, 
                                        bottomEnd = if (isUser) 0.dp else 16.dp
                                    )
                                )
                                .background(if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                .padding(12.dp)
                        ) {
                            Text(msg.second, color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ketik pesan Anda...") },
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            val userMsg = messageText
                            messages.add(Pair(true, userMsg))
                            messageText = ""
                            
                            // Mock realistic answer
                            coroutineScope.launch {
                                delay(if (ThemeState.isPremiumUltimate) 1000 else 3000)
                                val response = if (userMsg.contains("hukum", ignoreCase = true)) {
                                    "Wa'alaikumsalam warahmatullahi wabarakatuh. Mengenai hukum tersebut, ulama berbeda pendapat, namun pendapat yang paling kuat adalah..."
                                } else if (userMsg.contains("doa", ignoreCase = true)) {
                                    "Untuk doa terkait hal tersebut, bisa diamalkan doa berikut: Rabbanaa aatinaa fid-dunyaa hasanah wa fil aakhirati hasanah wa qinaa 'adzaaban-naar."
                                } else {
                                    "Wa'alaikumsalam. Pertanyaan yang sangat bagus. InsyaAllah ini adalah ujian dari Allah untuk meningkatkan derajat kita. Bersabarlah."
                                }
                                messages.add(Pair(false, response))
                            }
                        }
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary, shape = androidx.compose.foundation.shape.CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
