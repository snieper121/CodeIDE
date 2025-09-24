package com.example.codeide.utils

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.File

object DocumentFileUtils {
    
    /**
     * Получает список файлов из DocumentFile
     */
    fun getFilesFromDocumentFile(context: Context, uri: Uri): List<DocumentFileInfo> {
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        return documentFile?.listFiles()?.map { docFile ->
            DocumentFileInfo(
                name = docFile.name ?: "Unknown",
                isDirectory = docFile.isDirectory,
                uri = docFile.uri,
                lastModified = docFile.lastModified(),
                size = if (docFile.isFile) docFile.length() else 0
            )
        }?.sortedWith(
            compareBy({ !it.isDirectory }, { it.name.lowercase() })
        ) ?: emptyList()
    }
    
    /**
     * Конвертирует DocumentFile в обычный File для совместимости
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