package com.example.codeide.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codeide.ui.AppDrawer
import com.example.codeide.ui.AppTopAppBar
import com.example.codeide.ui.theme.CodeIDETheme
import com.example.codeide.viewmodels.FileManagerViewModel
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    fileManagerViewModel: FileManagerViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val fileList by fileManagerViewModel.files.collectAsState()
    val currentFolder by fileManagerViewModel.currentFolder.collectAsState()
    val rootUri by fileManagerViewModel.rootUri.collectAsState()
    val rootFolderName by fileManagerViewModel.rootFolderName.collectAsState()

    var hasPermission by remember { mutableStateOf(false) }

    val manageStorageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                hasPermission = Environment.isExternalStorageManager()
            }
        }
    )

    val readStorageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission = isGranted
        }
    )

    // Launcher для системного диалога выбора папки
    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let { selectedUri ->
            // Предоставляем постоянный доступ к выбранной папке
            context.contentResolver.takePersistableUriPermission(
                selectedUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            
            // Устанавливаем выбранную папку как корневую для приложения
            fileManagerViewModel.setRootFolder(context, selectedUri)
        }
    }

    fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val isManager = Environment.isExternalStorageManager()
            hasPermission = isManager
            if (!isManager) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:${context.packageName}")
                manageStorageLauncher.launch(intent)
            }
        } else {
            readStorageLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    // Функция для открытия системного диалога выбора папки
    fun openFolderPicker() {
        folderPickerLauncher.launch(null)
    }

    // Убираем автоматическую загрузку файлов при старте
    // LaunchedEffect(hasPermission) {
    //     if (hasPermission) {
    //         fileManagerViewModel.loadFiles(Environment.getExternalStorageDirectory())
    //     }
    // }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                files = fileList,
                onFileClick = { file ->
                    if (file.isDirectory) {
                        fileManagerViewModel.loadFiles(file)
                    }
                    scope.launch { drawerState.close() }
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                },
                onSelectFolder = { openFolderPicker() },
                currentFolder = currentFolder,
                rootFolderName = rootFolderName,
                onNavigateToRoot = {
                    rootUri?.let { uri ->
                        fileManagerViewModel.navigateToFolder(context, uri)
                    }
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopAppBar(
                    onMenuClick = {
                        checkAndRequestPermissions()
                        scope.launch { drawerState.open() }
                    },
                    onMoreClick = {
                        // Дополнительные опции (можно добавить позже)
                    }
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                if (!hasPermission) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Button(onClick = { checkAndRequestPermissions() }) {
                            Text(" Предоставить доступ к файлам ")
                        }
                    }
                } else if (fileList.isEmpty()) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Нажмите на меню (☰) и выберите папку для просмотра файлов",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                // Здесь будет основной контент для отображения файлов
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CodeIDETheme {
        HomeScreen()
    }
}
