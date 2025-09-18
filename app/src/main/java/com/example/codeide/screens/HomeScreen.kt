package com.example.codeide.screens
/*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.codeide.ui.theme.CodeIDETheme
import kotlinx.coroutines.launch

// Импортируем наши новые UI-компоненты
import com.example.codeide.ui.AppDrawer
import com.example.codeide.ui.AppTopAppBar
*/
import android.Manifest
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.codeide.ui.AppDrawer
import com.example.codeide.ui.AppTopAppBar
import com.example.codeide.ui.theme.CodeIDETheme

import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    // 1. Получаем экземпляр нашей ViewModel
    fileManagerViewModel: FileManagerViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // 2. Собираем состояние (список файлов) из ViewModel
    val fileList by fileManagerViewModel.files.collectAsState()

    // 3. Создаем лаунчер для запроса разрешений
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Если разрешение получено, загружаем файлы
                fileManagerViewModel.loadFiles(Environment.getExternalStorageDirectory())
            } else {
                // Здесь можно обработать случай, когда пользователь отказал в разрешении
                // Например, показать сообщение
            }
        }
    )

    // 4. Этот эффект будет запущен один раз при старте экрана
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                files = fileList, // 5. Передаем список файлов в боковое меню
                onFileClick = { file ->
                    // Обрабатываем клик по файлу/папке
                    if (file.isDirectory) {
                        fileManagerViewModel.loadFiles(file)
                    }
                    // Закрываем меню после клика
                    scope.launch { drawerState.close() }
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopAppBar(
                    onMenuClick = {
                        scope.launch { drawerState.open() }
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
                // Основной контент
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

