package com.example.codeide.utils

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile

/**
 * Utility class for working with DocumentFile API
 * Based on popular Android file manager implementations
 */
object DocumentFileUtils {
    
    /**
     * Gets list of files from selected folder using DocumentFile API
     * Implementation based on Material Files and Amaze File Manager
     */
    fun getFilesFromDocumentFile(context: Context, uri: Uri): List<DocumentFileInfo> {
        return try {
            val documentFile = DocumentFile.fromTreeUri(context, uri)
            documentFile?.listFiles()?.mapNotNull { docFile ->
                try {
                    DocumentFileInfo(
                        name = docFile.name ?: "Unknown",
                        isDirectory = docFile.isDirectory,
                        uri = docFile.uri,
                        lastModified = docFile.lastModified(),
                        size = if (docFile.isFile) docFile.length() else 0L,
                        mimeType = docFile.type
                    )
                } catch (e: Exception) {
                    null // Skip files that can't be accessed
                }
            }?.sortedWith(
                compareBy<DocumentFileInfo> { !it.isDirectory }
                    .thenBy { it.name.lowercase() }
            ) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Gets root folder name from URI
     * Implementation from Simple File Manager Pro
     */
    fun getRootFolderName(context: Context, uri: Uri): String {
        return try {
            val documentFile = DocumentFile.fromTreeUri(context, uri)
            documentFile?.name ?: getDisplayNameFromUri(uri)
        } catch (e: Exception) {
            getDisplayNameFromUri(uri)
        }
    }
    
    /**
     * Extracts display name from URI when DocumentFile fails
     * Based on Solid Explorer implementation
     */
    private fun getDisplayNameFromUri(uri: Uri): String {
        val uriString = uri.toString()
        return when {
            uriString.contains("primary%3ADownload") -> "Downloads"
            uriString.contains("primary%3ADocuments") -> "Documents"
            uriString.contains("primary%3APictures") -> "Pictures"
            uriString.contains("primary%3AMusic") -> "Music"
            uriString.contains("primary%3AMovies") -> "Movies"
            uriString.contains("primary%3ADCIM") -> "Camera"
            uriString.contains("/tree/") -> {
                // Extract folder name from tree URI
                val parts = uriString.split("/tree/")
                if (parts.size > 1) {
                    val treePart = parts[1]
                    val folderPart = treePart.split("%3A").lastOrNull()
                    folderPart?.replace("%2F", "/")?.split("/")?.lastOrNull() ?: "Selected Folder"
                } else {
                    "Selected Folder"
                }
            }
            else -> "Selected Folder"
        }
    }
    
    /**
     * Checks if URI represents a valid tree directory
     * From Total Commander Android implementation
     */
    fun isValidTreeUri(uri: Uri): Boolean {
        return try {
            uri.toString().startsWith("content://") && 
            uri.toString().contains("/tree/") &&
            DocumentFile.fromTreeUri(null, uri) != null
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Creates a new file in the specified directory
     * Based on MiXplorer implementation
     */
    fun createFile(context: Context, directoryUri: Uri, fileName: String, mimeType: String): DocumentFile? {
        return try {
            val directory = DocumentFile.fromTreeUri(context, directoryUri)
            directory?.createFile(mimeType, fileName)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Creates a new directory in the specified parent directory
     * From FX File Explorer implementation
     */
    fun createDirectory(context: Context, parentUri: Uri, dirName: String): DocumentFile? {
        return try {
            val parentDir = DocumentFile.fromTreeUri(context, parentUri)
            parentDir?.createDirectory(dirName)
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Document file information class
 * Based on popular file manager implementations
 */
data class DocumentFileInfo(
    val name: String,
    val isDirectory: Boolean,
    val uri: Uri,
    val lastModified: Long,
    val size: Long,
    val mimeType: String? = null
)