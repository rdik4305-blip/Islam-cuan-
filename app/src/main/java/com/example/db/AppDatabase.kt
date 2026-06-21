package com.example.db

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "tasbih")
data class TasbihSession(
    @PrimaryKey val id: Int = 1,
    val count: Int = 0,
    val target: Int = 33
)

@Dao
interface TasbihDao {
    @Query("SELECT * FROM tasbih WHERE id = 1")
    fun getSession(): Flow<TasbihSession?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSession(session: TasbihSession)
}

@Database(entities = [TasbihSession::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tasbihDao(): TasbihDao
    
    companion object {
        @Volatile private var instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "muslim_db")
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }
    }
}
