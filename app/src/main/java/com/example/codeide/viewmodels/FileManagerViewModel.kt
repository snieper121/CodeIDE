package com.example.codeide.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class FileManagerViewModel : ViewModel() {

    // Приватный MutableStateFlow для хранения и обновления списка файлов
    private val _files = MutableStateFlow<List<File>>(emptyList())
    // Публичный StateFlow, на который будет подписываться UI (только для чтения)
    val files: StateFlow<List<File>> = _files.asStateFlow()
    
    private val _currentFolder = MutableStateFlow<File?>(null)
    val currentFolder: StateFlow<File?> = _currentFolder.asStateFlow()
    
    // Обновить loadFiles:
    fun loadFiles(directory: File) {
        viewModelScope.launch {
            _currentFolder.value = directory
            // остальная логика...
            if (directory.exists() && directory.isDirectory) {
                // Получаем список файлов, обрабатываем случай, если он null
                val fileList = directory.listFiles()?.toList() ?: emptyList()
                // Сортируем: сначала папки, потом файлы, все по алфавиту
                _files.value = fileList.sortedWith(
                    compareBy({ !it.isDirectory }, { it.name.lowercase() })
                )
            }
        }
    }
}

