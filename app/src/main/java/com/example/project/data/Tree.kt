package com.example.project.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trees")
data class Tree(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val minutes: Int,
    val timestamp: Long = System.currentTimeMillis()
)
