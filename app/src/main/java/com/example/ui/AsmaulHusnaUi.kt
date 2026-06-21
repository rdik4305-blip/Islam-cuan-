package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AsmaulHusna(val id: Int, val arabic: String, val latin: String, val meaning: String)

val asmaulHusnaList = listOf(
    AsmaulHusna(1, "الرَّحْمَنُ", "Ar-Rahman", "Maha Pengasih"),
    AsmaulHusna(2, "الرَّحِيمُ", "Ar-Rahim", "Maha Penyayang"),
    AsmaulHusna(3, "الْمَلِكُ", "Al-Malik", "Maha Merajai"),
    AsmaulHusna(4, "الْقُدُّوسُ", "Al-Quddus", "Maha Suci"),
    AsmaulHusna(5, "السَّلاَمُ", "As-Salam", "Maha Memberi Kesejahteraan"),
    AsmaulHusna(6, "الْمُؤْمِنُ", "Al-Mu'min", "Maha Memberi Keamanan"),
    AsmaulHusna(7, "الْمُهَيْمِنُ", "Al-Muhaimin", "Maha Memelihara"),
    AsmaulHusna(8, "الْعَزِيزُ", "Al-Aziz", "Maha Perkasa"),
    AsmaulHusna(9, "الْجَبَّارُ", "Al-Jabbar", "Maha Berkuasa"),
    AsmaulHusna(10, "الْمُتَكَبِّرُ", "Al-Mutakabbir", "Maha Megah")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsmaulHusnaUi(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asmaul Husna") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(asmaulHusnaList) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = item.arabic, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = item.latin, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                        Text(text = item.meaning, fontSize = 12.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
