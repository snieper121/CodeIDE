// CodeIDE/app/src/main/java/com/example/codeide/ui/AppDrawer.kt

package com.example.codeide.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.codeide.ui.theme.CodeIDETheme
import com.example.codeide.utils.DocumentFileInfo
import java.io.File

@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    files: List<File>, // Принимаем список файлов
    onFileClick: (File) -> Unit, // Функция для обработки клика
    onCloseDrawer: () -> Unit,
    onSelectFolder: () -> Unit = {}, // Функция для выбора папки
    currentFolder: File? = null, // Текущая папка
    documentFiles: List<DocumentFileInfo> = emptyList(), // DocumentFile список
    rootFolderName: String = "", // Имя корневой папки
    onDocumentFileClick: (DocumentFileInfo) -> Unit = {}, // Обработка клика на DocumentFile
    onNavigateToRoot: () -> Unit = {} // Переход к корневой папке
) {
    ModalDrawerSheet(modifier = modifier) {
        // Заголовок
        Text(
            text = "Файловый менеджер",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        
        // Показываем корневую папку
        if (rootFolderName.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToRoot() }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Корневая папка",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "🏠 $rootFolderName",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // Кнопка выбора папки
        Button(
            onClick = onSelectFolder,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("📁 Выбрать корневую папку")
        }
        
        Divider() // Разделитель

        // Список файлов с прокруткой
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // Отображаем DocumentFile если есть, иначе обычные файлы
            if (documentFiles.isNotEmpty()) {
                items(documentFiles) { docFile ->
                    DocumentFileListItem(docFile = docFile, onClick = { onDocumentFileClick(docFile) })
                }
            } else {
                items(files) { file ->
                    FileListItem(file = file, onClick = { onFileClick(file) })
                }
            }
        }
    }
}

@Composable
fun FileListItem(
    file: File,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.Default.Description,
            contentDescription = if (file.isDirectory) "Папка" else "Файл",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = file.name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun DocumentFileListItem(
    docFile: DocumentFileInfo,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (docFile.isDirectory) Icons.Default.Folder else Icons.Default.Description,
            contentDescription = if (docFile.isDirectory) "Папка" else "Файл",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = docFile.name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AppDrawerPreview() {
    val sampleFiles = listOf(
        File("/example/folder1"),
        File("/example/file1.txt"),
        File("/example/folder2")
    )
    CodeIDETheme {
        AppDrawer(files = sampleFiles, onFileClick = {}, onCloseDrawer = {}, onSelectFolder = {})
    }
}
