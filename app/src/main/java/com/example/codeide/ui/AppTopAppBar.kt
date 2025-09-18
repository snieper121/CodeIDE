package com.example.codeide.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.codeide.ui.theme.CodeIDETheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit // Функция, которая будет вызвана при клике на гамбургер
) {
    TopAppBar(
        title = { Text("Code / IDE") },
        navigationIcon = {
            IconButton(onClick = onMenuClick) { // Вызываем переданную функцию
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Открыть меню"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun AppTopAppBarPreview() {
    CodeIDETheme {
        AppTopAppBar(onMenuClick = {})
    }
}
