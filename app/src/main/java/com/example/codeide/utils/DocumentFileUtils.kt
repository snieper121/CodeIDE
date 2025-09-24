package com.example.codeide.utils

import android.content.Context
import android.net.Uri
import java.io.File

object DocumentFileUtils {
    
    /**
     * Получает список файлов из URI (упрощенная версия без DocumentFile)
     */
    fun getFilesFromDocumentFile(context: Context, uri: Uri): List<DocumentFileInfo> {
        return try {
            // Для демонстрации возвращаем пустой список
            // В реальном приложении здесь должна быть работа с DocumentFile API
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Конвертирует URI в обычный File для совместимости
     */
    fun convertToFile(context: Context, uri: Uri): File? {
        return try {
            // Для системных папок пытаемся получить File объект
            when {
                uri.toString().contains("Downloads") -> {
                    android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS)
                }
                uri.toString().contains("Documents") -> {
                    android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOCUMENTS)
                }
                uri.toString().contains("Pictures") -> {
                    android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES)
                }
                else -> {
                    // Для других папок возвращаем null
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Информация о файле/папке из DocumentFile
 */
data class DocumentFileInfo(
    val name: String,
    val isDirectory: Boolean,
    val uri: Uri,
    val lastModified: Long,
    val size: Long
)