package com.example.mathapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Database class for the Room database, managing the ScoreBoard entity.
@Database(entities = [ScoreBoard::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    // Abstract function to get the ScoreDAO for accessing the score_board table.
    abstract fun scoreDao(): ScoreDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mathapp_database" // Name of the database.
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE score_board ADD COLUMN newColumn TEXT")
            }
        }
    }
}
