package com.example.project.data

import androidx.room.*

@Dao
interface TreeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTree(tree: Tree)

    @Query("SELECT * FROM trees WHERE timestamp >= :timeLimit ORDER BY timestamp DESC")
    fun getTrees(timeLimit: Long): List<Tree>
}
