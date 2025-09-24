package com.example.codeide.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File

object DocumentFileUtils {
    
    /**
     * Получает список файлов из выбранной папки (упрощенная версия)
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
     * Проверяет, является ли URI корневой папкой
     */
    fun isRootFolder(uri: Uri): Boolean {
        return uri.toString().contains("tree/")
    }
    
    /**
     * Получает имя корневой папки из URI
     */
    fun getRootFolderName(context: Context, uri: Uri): String {
        return try {
            // Определяем имя папки по URI
            when {
                uri.toString().contains("Downloads") -> "Downloads"
                uri.toString().contains("Documents") -> "Documents"
                uri.toString().contains("Pictures") -> "Pictures"
                uri.toString().contains("Music") -> "Music"
                uri.toString().contains("Movies") -> "Movies"
                else -> "Выбранная папка"
            }
        } catch (e: Exception) {
            "Выбранная папка"
        }
    }
    
    /**
     * Получает соответствующий File объект для URI
     */
    fun getFileFromUri(uri: Uri): File? {
        return try {
            when {
                uri.toString().contains("Downloads") -> {
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                }
                uri.toString().contains("Documents") -> {
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                }
                uri.toString().contains("Pictures") -> {
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                }
                uri.toString().contains("Music") -> {
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                }
                uri.toString().contains("Movies") -> {
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                }
                else -> null
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