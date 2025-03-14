package com.example.project.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.project.data.Tree
import com.example.project.data.TreeDatabase
import com.example.project.repository.ForestRepository
import kotlinx.coroutines.launch

class ForestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ForestRepository

    init {
        val treeDao = TreeDatabase.getDatabase(application).treeDao()
        repository = ForestRepository(treeDao)
    }

    private val _trees = MutableLiveData<List<Tree>>()
    val trees: LiveData<List<Tree>> get() = _trees

    fun plantTree(minutes: Int) {
        viewModelScope.launch {
            repository.addTree(minutes)
            _trees.value = repository.getTrees(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
        }
    }
}
