package com.example.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf

data class RecordingItem(
    val id: Int,
    val title: String,
    val description: String,
    val author: String,
    var views: Int,
    var likes: Int,
    val comments: MutableList<String> = mutableStateListOf(),
    val isUser: Boolean = false,
    var isLikedByUser: Boolean = false
)

data class GiftTransaction(
    val id: Int,
    val description: String,
    val date: String,
    val amount: Int,
    val isOutflow: Boolean // true for cash withdrawals, false for virtual gifts received
)

object UserState {
    var coins by mutableStateOf(0)
    var diamonds by mutableStateOf(0)
    var hasRamadhanFrame by mutableStateOf(false)
    val unlockedSurahs = mutableStateListOf<Int>()
    var followers by mutableStateOf(0)
    var rekamanCount by mutableStateOf(0)
    var bio by mutableStateOf("Hamba Allah yang fakir ilmu")
    
    // Earned gift balance from fans supporting recordings
    var giftBalance by mutableStateOf(35000) 
    val giftTransactions = mutableStateListOf<GiftTransaction>().apply {
        add(GiftTransaction(1, "Gift Support dari Fans (Murottal Ar-Rahman)", "20-06-2026", 15000, false))
        add(GiftTransaction(2, "Gift Support dari Fans (Sholawat Jibril)", "21-06-2026", 20000, false))
    }
    
    val recordingList = mutableStateListOf<RecordingItem>().apply {
        add(
            RecordingItem(
                id = 1,
                title = "Sholawat Jibril Pembuka Rezeki",
                description = "Lantunan Sholawat Jibril yang sangat merdu penentram hati dan pembuka pintu rezeki dari segala penjuru.",
                author = "Hamba Allah (Anda)",
                views = 12500,
                likes = 890,
                comments = mutableStateListOf("Masya Allah merdu sekali suaranya", "Bikin hati jadi tenang dan damai", "Semoga diorbitkan terus oleh Studio Musik!"),
                isUser = true
            )
        )
        add(
            RecordingItem(
                id = 2,
                title = "Murottal Ar-Rahman Syahdu",
                description = "Murottal Al-Quran Surah Ar-Rahman dengan irama Nahawand yang sangat indah dan menyentuh jiwa.",
                author = "Hamba Allah (Anda)",
                views = 9800,
                likes = 450,
                comments = mutableStateListOf("Adem banget dengerinnya malem-malem", "Sedikit lagi tembus 10.000 views, yuk dengerin terus!"),
                isUser = true
            )
        )
        add(
            RecordingItem(
                id = 3,
                title = "Ceramah Singkat Pentingnya Shalat",
                description = "Kultum singkat mengenai urgensi menjaga shalat lima waktu tepat waktu di masjid bagi kaum laki-laki.",
                author = "Ustadz Hanafi",
                views = 4200,
                likes = 120,
                comments = mutableStateListOf("Sangat bermanfaat ustadz, terima kasih pengingatnya."),
                isUser = false
            )
        )
    }
}
