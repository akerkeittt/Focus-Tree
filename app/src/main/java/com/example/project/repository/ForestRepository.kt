package com.example.project.repository

import com.example.project.data.Tree
import com.example.project.data.TreeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForestRepository(private val treeDao: TreeDao) {

    suspend fun addTree(minutes: Int) {
        withContext(Dispatchers.IO) {
            treeDao.insertTree(Tree(minutes = minutes))
        }
    }

    suspend fun getTrees(timeLimit: Long): List<Tree> {
        return withContext(Dispatchers.IO) {
            treeDao.getTrees(timeLimit)
        }
    }
}
