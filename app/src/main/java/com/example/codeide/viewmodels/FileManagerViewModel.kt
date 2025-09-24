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
    
    // Текущий корневой URI (выбранная папка)
    private val _rootUri = MutableStateFlow<Uri?>(null)
    val rootUri: StateFlow<Uri?> = _rootUri.asStateFlow()
    
    // Список файлов из DocumentFile
    private val _documentFiles = MutableStateFlow<List<DocumentFileInfo>>(emptyList())
    val documentFiles: StateFlow<List<DocumentFileInfo>> = _documentFiles.asStateFlow()
    
    // Имя корневой папки
    private val _rootFolderName = MutableStateFlow<String>("")
    val rootFolderName: StateFlow<String> = _rootFolderName.asStateFlow()

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
    
    fun setRootFolder(context: Context, uri: Uri) {
        viewModelScope.launch {
            // Сохраняем корневой URI
            _rootUri.value = uri
            
            // Получаем имя корневой папки
            _rootFolderName.value = DocumentFileUtils.getRootFolderName(context, uri)
            
            // Получаем соответствующий File объект и загружаем файлы
            val rootFile = DocumentFileUtils.getFileFromUri(uri)
            if (rootFile != null && rootFile.exists()) {
                loadFiles(rootFile)
            }
            
            // Очищаем DocumentFile список
            _documentFiles.value = emptyList()
        }
    }
    
    fun navigateToFolder(context: Context, uri: Uri) {
        viewModelScope.launch {
            // Получаем соответствующий File объект и загружаем файлы
            val folderFile = DocumentFileUtils.getFileFromUri(uri)
            if (folderFile != null && folderFile.exists()) {
                loadFiles(folderFile)
            }
        }
    }
    
    // Функция для очистки списка файлов
    fun clearFiles() {
        _files.value = emptyList()
        _documentFiles.value = emptyList()
        _currentFolder.value = null
        _rootUri.value = null
        _rootFolderName.value = ""
    }
}
