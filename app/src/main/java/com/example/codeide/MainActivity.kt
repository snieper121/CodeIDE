package com.example.codeide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.tooling.preview.Preview
import com.example.codeide.screens.HomeScreen // Импортируем наш HomeScreen
import com.example.codeide.ui.theme.CodeIDETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodeIDETheme {
                HomeScreen() // Вызываем наш главный экран
            }
        }
    }
}

// Preview для MainActivity больше не нужен, так как HomeScreen имеет свой Preview
// @Preview(showBackground = true)
// @Composable
// fun DefaultPreview() {
//     CodeIDETheme {
//         HomeScreen()
//     }
// }
