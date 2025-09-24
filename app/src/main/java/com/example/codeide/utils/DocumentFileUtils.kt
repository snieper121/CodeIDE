package com.example.codeide.utils

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.File

object DocumentFileUtils {
    
    /**
     * Получает список файлов из выбранной папки через DocumentFile API
     */
    fun getFilesFromDocumentFile(context: Context, uri: Uri): List<DocumentFileInfo> {
        return try {
            val documentFile = DocumentFile.fromTreeUri(context, uri)
            documentFile?.listFiles()?.map { docFile ->
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
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Проверяет, является ли URI корневой папкой
     */
    fun isRootFolder(uri: Uri): Boolean {
        // Проверяем, что это корневая папка (не подпапка)
        return uri.toString().contains("tree/") && !uri.toString().contains("%2F")
    }
    
    /**
     * Получает имя корневой папки из URI
     */
    fun getRootFolderName(context: Context, uri: Uri): String {
        return try {
            val documentFile = DocumentFile.fromTreeUri(context, uri)
            documentFile?.name ?: "Выбранная папка"
        } catch (e: Exception) {
            "Выбранная папка"
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