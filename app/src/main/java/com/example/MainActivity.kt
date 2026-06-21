package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.db.AppDatabase
import com.example.ui.AsmaulHusnaUi
import com.example.ui.HajjUi
import com.example.ui.HomeUi
import com.example.ui.LoginUi
import com.example.ui.DoaUi
import com.example.ui.QiblaUi
import com.example.ui.PremiumUi
import com.example.ui.ProfileUi
import com.example.ui.SedekahUi
import com.example.ui.BuyCoinUi
import com.example.ui.BuyDiamondUi
import com.example.ui.BuyFollowerUi
import com.example.ui.JadwalSholatUi
import com.example.ui.RecordingFeedUi
import com.example.ui.AvatarAiUi
import com.example.ui.PublishCeramahUi
import com.example.ui.RadioUi
import com.example.ui.QuranAudioUi
import com.example.ui.ChatUstadzUi
import com.example.ui.MainViewModel
import com.example.ui.QuranUi
import com.example.ui.QuranViewModel
import com.example.ui.TasbihUi
import com.example.ui.TasbihViewModel
import com.example.ui.TasbihViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    val database = AppDatabase.getDatabase(this)
    setContent {
      MyApplicationTheme {
        val navController = rememberNavController()
        val mainViewModel: MainViewModel = viewModel()
        val quranViewModel: QuranViewModel = viewModel()
        val tasbihViewModel: TasbihViewModel = viewModel(factory = TasbihViewModelFactory(database.tasbihDao()))
        
        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                if (currentRoute !in listOf("login", "hajj", "asmaulhusna", "doa", "qibla", "premium", "profile", "sedekah")) {
                    NavigationBar(
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") },
                            selected = currentRoute == "home",
                            onClick = {
                                navController.navigate("home") {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Book, contentDescription = "Quran") },
                            label = { Text("Quran") },
                            selected = currentRoute == "quran",
                            onClick = {
                                navController.navigate("quran") {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Mosque, contentDescription = "Tasbih") },
                            label = { Text("Tasbih") },
                            selected = currentRoute == "tasbih",
                            onClick = {
                                navController.navigate("tasbih") {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("login") { 
                    LoginUi(onLoginSuccess = { 
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        } 
                    }) 
                }
                composable("home") { 
                    HomeUi(
                        viewModel = mainViewModel,
                        onNavigateToQuran = { navController.navigate("quran") },
                        onNavigateToTasbih = { navController.navigate("tasbih") },
                        onNavigateToHajj = { navController.navigate("hajj") },
                        onNavigateToAsmaulHusna = { navController.navigate("asmaulhusna") },
                        onNavigateToDoa = { navController.navigate("doa") },
                        onNavigateToQibla = { navController.navigate("qibla") },
                        onNavigateToSedekah = { navController.navigate("sedekah") },
                        onNavigateToProfile = { navController.navigate("profile") },
                        onNavigateToRadio = { navController.navigate("radio") },
                        onNavigateToQuranAudio = { navController.navigate("quranaudio") },
                        onNavigateToChatUstadz = { navController.navigate("chatustadz") },
                        onNavigateToJadwalSholat = { navController.navigate("jadwal_sholat") },
                        onNavigateToRecordingFeed = { navController.navigate("recording_feed") }
                    ) 
                }
                composable("quran") { 
                    QuranUi(
                        viewModel = quranViewModel,
                        onNavigateToBuyDiamond = { navController.navigate("buy_diamond") }
                    ) 
                }
                composable("tasbih") { TasbihUi(tasbihViewModel) }
                composable("jadwal_sholat") { JadwalSholatUi(viewModel = mainViewModel, onBack = { navController.popBackStack() }) }
                composable("recording_feed") { RecordingFeedUi(onBack = { navController.popBackStack() }) }
                composable("hajj") { HajjUi(onBack = { navController.popBackStack() }) }
                composable("asmaulhusna") { AsmaulHusnaUi(onBack = { navController.popBackStack() }) }
                composable("doa") { DoaUi(onBack = { navController.popBackStack() }) }
                composable("qibla") { QiblaUi(onBack = { navController.popBackStack() }) }
                composable("premium") { 
                    PremiumUi(
                        onBack = { navController.popBackStack() },
                        onUpgrade = { navController.popBackStack() }
                    ) 
                }
                composable("profile") { 
                    ProfileUi(
                        onBack = { navController.popBackStack() },
                        onNavigateToPremium = { navController.navigate("premium") },
                        onNavigateToAvatarAi = { navController.navigate("avatar_ai") },
                        onNavigateToPublishCeramah = { navController.navigate("publish_ceramah") },
                        onNavigateToBuyCoin = { navController.navigate("buy_coin") },
                        onNavigateToBuyDiamond = { navController.navigate("buy_diamond") },
                        onNavigateToBuyFollower = { navController.navigate("buy_follower") }
                    ) 
                }
                composable("sedekah") { SedekahUi(onBack = { navController.popBackStack() }) }
                composable("buy_coin") { BuyCoinUi(onBack = { navController.popBackStack() }) }
                composable("buy_diamond") { BuyDiamondUi(onBack = { navController.popBackStack() }) }
                composable("buy_follower") { BuyFollowerUi(onBack = { navController.popBackStack() }) }
                composable("avatar_ai") { AvatarAiUi(onBack = { navController.popBackStack() }) }
                composable("publish_ceramah") { 
                    PublishCeramahUi(
                        onBack = { navController.popBackStack() },
                        onNavigateToBuyCoin = { navController.navigate("buy_coin") }
                    ) 
                }
                composable("radio") { 
                    RadioUi(
                        onBack = { navController.popBackStack() },
                        onNavigateToPremium = { navController.navigate("premium") }
                    ) 
                }
                composable("quranaudio") { 
                    QuranAudioUi(
                        onBack = { navController.popBackStack() },
                        onNavigateToPremium = { navController.navigate("premium") }
                    ) 
                }
                composable("chatustadz") { 
                    ChatUstadzUi(
                        onBack = { navController.popBackStack() },
                        onNavigateToPremium = { navController.navigate("premium") }
                    ) 
                }
            }
        }
      }
    }
  }
}
