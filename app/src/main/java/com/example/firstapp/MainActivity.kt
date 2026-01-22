package com.example.firstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppRoot()
        }
    }
}

// -------- App State & Navigation --------

enum class Screen {
    LOGIN, HOME
}

@Composable
fun AppRoot() {
    var currentScreen by remember { mutableStateOf(Screen.LOGIN) }

    when (currentScreen) {
        Screen.LOGIN -> LoginScreen(onLoginSuccess = { currentScreen = Screen.HOME })
        Screen.HOME -> HomeScreen()
    }
}

// -------- Login Screen --------

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    onLoginSuccess()
                }
            }
        ) {
            Text("Login")
        }
    }
}

// -------- Home Screen --------

data class Item(
    val id: Int,
    val title: String,
    val isFavourite: Boolean = false
)

@Composable
fun HomeScreen() {
    var items by remember {
        mutableStateOf(
            listOf(
                Item(1, "Item One"),
                Item(2, "Item Two"),
                Item(3, "Item Three")
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {
            items(items) { item ->
                ItemRow(
                    item = item,
                    onFavouriteToggle = {
                        items = items.map {
                            if (it.id == item.id) it.copy(isFavourite = !it.isFavourite)
                            else it
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ItemRow(item: Item, onFavouriteToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.title,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = if (item.isFavourite) "★" else "☆",
            modifier = Modifier
                .clickable { onFavouriteToggle() }
                .padding(8.dp)
        )
    }
}
