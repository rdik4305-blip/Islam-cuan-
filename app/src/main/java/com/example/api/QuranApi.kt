package com.example.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class QuranSurahResponse(val data: List<Surah>)
data class Surah(val number: Int, val name: String, val englishName: String, val englishNameTranslation: String, val numberOfAyahs: Int)

data class QuranAyahResponse(val data: SurahDetails)
data class SurahDetails(val number: Int, val name: String, val englishName: String, val ayahs: List<Ayah>)
data class Ayah(val number: Int, val text: String, val numberInSurah: Int)

interface QuranApi {
    @GET("v1/surah")
    suspend fun getSurahs(): QuranSurahResponse

    @GET("v1/surah/{number}")
    suspend fun getSurahDetails(@Path("number") number: Int): QuranAyahResponse

    companion object {
        fun create(): QuranApi {
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            return Retrofit.Builder()
                .baseUrl("https://api.alquran.cloud/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(QuranApi::class.java)
        }
    }
}
