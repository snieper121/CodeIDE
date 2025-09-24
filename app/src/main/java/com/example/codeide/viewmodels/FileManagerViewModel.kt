package com.example.codeide.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codeide.utils.DocumentFileInfo
import com.example.codeide.utils.DocumentFileUtils
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
    
    // Текущая выбранная папка
    private val _currentFolder = MutableStateFlow<File?>(null)
    val currentFolder: StateFlow<File?> = _currentFolder.asStateFlow()
    
    // Текущий URI для DocumentFile
    private val _currentUri = MutableStateFlow<Uri?>(null)
    val currentUri: StateFlow<Uri?> = _currentUri.asStateFlow()
    
    // Список DocumentFile объектов
    private val _documentFiles = MutableStateFlow<List<DocumentFileInfo>>(emptyList())
    val documentFiles: StateFlow<List<DocumentFileInfo>> = _documentFiles.asStateFlow()

    fun loadFiles(directory: File) {
        viewModelScope.launch {
            if (directory.exists() && directory.isDirectory) {
                // Сохраняем текущую папку
                _currentFolder.value = directory
                // Получаем список файлов, обрабатываем случай, если он null
                val fileList = directory.listFiles()?.toList() ?: emptyList()
                // Сортируем: сначала папки, потом файлы, все по алфавиту
                _files.value = fileList.sortedWith(
                    compareBy({ !it.isDirectory }, { it.name.lowercase() })
                )
            }
        }
    }
    
    fun loadFilesFromUri(context: Context, uri: Uri) {
        viewModelScope.launch {
            _currentUri.value = uri
            val documentFilesList = DocumentFileUtils.getFilesFromDocumentFile(context, uri)
            _documentFiles.value = documentFilesList
            
            // Также пытаемся загрузить обычные файлы для совместимости
            val file = DocumentFileUtils.convertToFile(context, uri)
            if (file != null) {
                loadFiles(file)
            }
        }
    }
    
    // Функция для очистки списка файлов
    fun clearFiles() {
        _files.value = emptyList()
        _documentFiles.value = emptyList()
        _currentFolder.value = null
        _currentUri.value = null
    }
}
