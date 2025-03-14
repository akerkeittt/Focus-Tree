package com.example.project.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tree::class], version = 1, exportSchema = false)
abstract class TreeDatabase : RoomDatabase() {
    abstract fun treeDao(): TreeDao

    companion object {
        @Volatile private var INSTANCE: TreeDatabase? = null

        fun getDatabase(context: Context): TreeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TreeDatabase::class.java,
                    "tree_database"
                )
                    .fallbackToDestructiveMigration() // Добавил обработку миграций (по желанию)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

