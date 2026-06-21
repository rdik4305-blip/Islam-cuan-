package com.example.api

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class AladhanResponse(val data: AladhanData)
data class AladhanData(val timings: Timings, val date: DateInfo)
data class Timings(
    val Fajr: String,
    val Sunrise: String,
    val Dhuhr: String,
    val Asr: String,
    val Sunset: String,
    val Maghrib: String,
    val Isha: String
)
data class DateInfo(val readable: String, val hijri: HijriDate)
data class HijriDate(val date: String, val month: HijriMonth, val year: String, val day: String)
data class HijriMonth(val en: String, val ar: String)

interface AladhanApi {
    @GET("v1/timingsByCity")
    suspend fun getTimings(
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("method") method: Int = 2
    ): AladhanResponse

    companion object {
        fun create(): AladhanApi {
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            return Retrofit.Builder()
                .baseUrl("https://api.aladhan.com/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(AladhanApi::class.java)
        }
    }
}
