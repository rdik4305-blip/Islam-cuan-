package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AppShortcut
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.ThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileUi(onBack: () -> Unit, onNavigateToPremium: () -> Unit, onNavigateToAvatarAi: () -> Unit, onNavigateToPublishCeramah: () -> Unit, onNavigateToBuyCoin: () -> Unit, onNavigateToBuyDiamond: () -> Unit, onNavigateToBuyFollower: () -> Unit) {
    var notifAudioAdzan by remember { mutableStateOf(false) }
    var expandedTheme by remember { mutableStateOf(false) }
    var showIconDialog by remember { mutableStateOf(false) }
    val totalSedekah by SedekahState.totalSedekah.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan Akun") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Keluar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                if (ThemeState.profilePhotoUri != null && ThemeState.isPremium) {
                    val shape = if (UserState.hasRamadhanFrame) RoundedCornerShape(16.dp) else CircleShape
                    val modifier = Modifier.size(100.dp).clip(shape).background(MaterialTheme.colorScheme.surfaceVariant)
                    
                    if (UserState.hasRamadhanFrame) {
                        Box(contentAlignment = Alignment.Center) {
                            AsyncImage(
                                model = ThemeState.profilePhotoUri,
                                contentDescription = "Profile Picture",
                                modifier = modifier.padding(8.dp) // inner padding for frame
                            )
                            // Ramadhan Frame Mock
                            Box(modifier = Modifier.size(100.dp).border(4.dp, MaterialTheme.colorScheme.primary, shape))
                        }
                    } else {
                        AsyncImage(
                            model = ThemeState.profilePhotoUri,
                            contentDescription = "Profile Picture",
                            modifier = modifier
                        )
                    }
                } else {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(100.dp), tint = MaterialTheme.colorScheme.primary)
                }
                
                if (ThemeState.isPremium) {
                    IconButton(
                        onClick = { 
                            // Galeri picker mock
                            ThemeState.profilePhotoUri = "https://i.pravatar.cc/300?u=Gallery"
                        },
                        modifier = Modifier.align(Alignment.BottomEnd).size(32.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            var isEditingBio by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Hamba Allah", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                if (ThemeState.isVerified) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.Verified, contentDescription = "Verified Badge", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                }
            }
            Text("hamba.allah@email.com", color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (isEditingBio) {
                OutlinedTextField(
                    value = UserState.bio,
                    onValueChange = { UserState.bio = it },
                    label = { Text("Bio") },
                    trailingIcon = {
                        IconButton(onClick = { isEditingBio = false }) {
                            Icon(Icons.Default.Verified, contentDescription = "Simpan")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(UserState.bio, color = MaterialTheme.colorScheme.onSurfaceVariant, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    IconButton(onClick = { isEditingBio = true }, modifier = Modifier.size(24.dp).padding(start = 8.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Bio", modifier = Modifier.size(16.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onNavigateToBuyFollower() }.padding(8.dp)) {
                    Text("${UserState.followers}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Followers", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                    Text("${UserState.rekamanCount}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Rekaman", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // Sedekah, Coin, Diamond Summary
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Sedekah", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer, fontSize = 10.sp)
                        Text("Rp $totalSedekah", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f).clickable { onNavigateToBuyCoin() },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Koin", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer, fontSize = 10.sp)
                        Text("${UserState.coins} Koin", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.tertiary)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f).clickable { onNavigateToBuyDiamond() },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(Icons.Default.Diamond, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Diamond", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 10.sp)
                        Text("${UserState.diamonds} Dmd", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (!ThemeState.isPremiumUltimate) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    onClick = onNavigateToPremium
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                if(ThemeState.isPremiumPro) "Tingkatkan ke Premium Ultimate" 
                                else if(ThemeState.isPremium) "Tingkatkan ke Premium Pro" 
                                else "Tingkatkan ke Premium", 
                                fontWeight = FontWeight.Bold
                            )
                            Text("Buka fitur Avatar AI & Publikasi Ceramah", fontSize = 12.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Anggota Premium Ultimate", fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Settings actions
            Box(modifier = Modifier.fillMaxWidth()) {
                ListItem(
                    headlineContent = { Text("Simulasi Habis Masa Aktif Premium") },
                    leadingContent = { Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                    modifier = Modifier.clickable { 
                        ThemeState.premiumExpiryTime = 0L
                        ThemeState.isPremium = false
                        ThemeState.isPremiumPro = false
                        ThemeState.isPremiumUltimate = false
                    }
                )
            }
            HorizontalDivider()
            Box(modifier = Modifier.fillMaxWidth()) {
                ListItem(
                    headlineContent = { Text("Tema Aplikasi") },
                    leadingContent = { Icon(Icons.Default.ColorLens, contentDescription = null) },
                    trailingContent = { if (!ThemeState.isPremium) Text("Premium", fontSize = 10.sp, color = MaterialTheme.colorScheme.error) },
                    modifier = Modifier.clickable { 
                        if (ThemeState.isPremium) expandedTheme = true else onNavigateToPremium() 
                    }
                )
                DropdownMenu(
                    expanded = expandedTheme,
                    onDismissRequest = { expandedTheme = false }
                ) {
                    DropdownMenuItem(text = { Text("Hijau (Default)") }, onClick = { ThemeState.selectedThemeIndex = 0; expandedTheme = false })
                    DropdownMenuItem(text = { Text("Biru") }, onClick = { ThemeState.selectedThemeIndex = 1; expandedTheme = false })
                    DropdownMenuItem(text = { Text("Ungu") }, onClick = { ThemeState.selectedThemeIndex = 2; expandedTheme = false })
                    DropdownMenuItem(text = { Text("Emas (Premium)") }, onClick = { ThemeState.selectedThemeIndex = 3; expandedTheme = false })
                    DropdownMenuItem(text = { Text("Gelap Elegan (Premium)") }, onClick = { ThemeState.selectedThemeIndex = 4; expandedTheme = false })
                }
            }
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Bikin Avatar Profil AI") },
                leadingContent = { Icon(Icons.Default.Face, contentDescription = null) },
                trailingContent = { if (!ThemeState.isPremiumUltimate) Text("Ultimate", fontSize = 10.sp, color = MaterialTheme.colorScheme.error) },
                modifier = Modifier.clickable { 
                    if (ThemeState.isPremiumUltimate) {
                        onNavigateToAvatarAi()
                    } else {
                        onNavigateToPremium() 
                    }
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Publikasikan Rekaman Ceramah") },
                leadingContent = { Icon(Icons.Default.Publish, contentDescription = null) },
                trailingContent = { if (!ThemeState.isPremiumUltimate) Text("Ultimate", fontSize = 10.sp, color = MaterialTheme.colorScheme.error) },
                modifier = Modifier.clickable { 
                    if (ThemeState.isPremiumUltimate) {
                        onNavigateToPublishCeramah()
                    } else {
                        onNavigateToPremium() 
                    }
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Event: Bingkai Ramadhan (Efek Khusus)") },
                leadingContent = { Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) },
                trailingContent = { 
                    Switch(checked = UserState.hasRamadhanFrame, onCheckedChange = { UserState.hasRamadhanFrame = it })
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Ubah Icon Aplikasi") },
                leadingContent = { Icon(Icons.Default.AppShortcut, contentDescription = null) },
                trailingContent = { if (!ThemeState.isPremium) Text("Premium", fontSize = 10.sp, color = MaterialTheme.colorScheme.error) },
                modifier = Modifier.clickable { 
                    if (ThemeState.isPremium) showIconDialog = true else onNavigateToPremium() 
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Audio Adzan") },
                leadingContent = { Icon(Icons.Default.NotificationsActive, contentDescription = null) },
                trailingContent = { 
                    Switch(
                        checked = notifAudioAdzan, 
                        onCheckedChange = { 
                            if (ThemeState.isPremium) notifAudioAdzan = it else onNavigateToPremium()
                        }
                    ) 
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Fitur Uji Coba (Beta)") },
                leadingContent = { Icon(Icons.Default.NewReleases, contentDescription = null) },
                trailingContent = { 
                    if (!ThemeState.isPremiumPro) Text("Premium Pro", fontSize = 10.sp, color = MaterialTheme.colorScheme.error)
                    else Switch(checked = ThemeState.betaEnabled, onCheckedChange = { ThemeState.betaEnabled = it })
                },
                modifier = Modifier.clickable { if(!ThemeState.isPremiumPro) onNavigateToPremium() }
            )
            HorizontalDivider()
        }
        
        if (showIconDialog) {
            AlertDialog(
                onDismissRequest = { showIconDialog = false },
                title = { Text("Pilih Icon Aplikasi") },
                text = { Text("Pilih icon favorit Anda untuk ditampilkan di layar utama perangkat.") },
                confirmButton = {
                    TextButton(onClick = { showIconDialog = false }) { Text("Tutup") }
                }
            )
        }
    }
}
