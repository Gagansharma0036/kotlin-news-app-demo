package com.example.firstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

// Data class for news
data class News(val title: String, val description: String)

// Main activity
class MainActivity : ComponentActivity() {
    private val newsList = listOf(
        News("Title 1", "Description 1"),
        News("Title 2", "Description 2"),
        News("Title 3", "Description 3")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsList(newsList)
        }
    }
}

// Composable function to display news
@Composable
fun NewsList(news: List<News>) {
    LazyColumn {
        items(news) { item ->
            Column {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

// Preview in Android Studio
@Preview(showBackground = true)
@Composable
fun PreviewNewsList() {
    NewsList(
        listOf(
            News("Sample 1", "Description 1"),
            News("Sample 2", "Description 2")
        )
    )
}
