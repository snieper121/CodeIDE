package com.example.codeide.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.codeide.ui.theme.CodeIDETheme

@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    onCloseDrawer: () -> Unit // Функция, которая будет вызвана для закрытия меню
) {
    ModalDrawerSheet(modifier = modifier) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            Text(
                text = "Файловый менеджер",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
            // Здесь в будущем можно будет добавить другие пункты меню
            // Например, кнопки, которые будут вызывать onCloseDrawer()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppDrawerPreview() {
    CodeIDETheme {
        AppDrawer(onCloseDrawer = {})
    }
}
