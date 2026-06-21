package com.example.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ThemeState {
    var selectedThemeIndex by mutableStateOf(0)
    var isPremium by mutableStateOf(false) // 30k tier
    var isPremiumPro by mutableStateOf(false) // 50k tier
    var isPremiumUltimate by mutableStateOf(false) // 150k tier
    var premiumExpiryTime by mutableStateOf(0L) // Timestamp for expiry
    var isVerified by mutableStateOf(false)
    var profilePhotoUri by mutableStateOf<String?>(null)
    var betaEnabled by mutableStateOf(false)
    
    fun checkSubscription() {
        if (premiumExpiryTime > 0 && System.currentTimeMillis() > premiumExpiryTime) {
            isPremium = false
            isPremiumPro = false
            isPremiumUltimate = false
            premiumExpiryTime = 0L
            isVerified = false
        }
    }
}
