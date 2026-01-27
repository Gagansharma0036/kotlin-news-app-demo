package com.example.firstapp

// -------- Android & Compose imports --------
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// -------- Main Activity (Android entry point) --------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent replaces XML layouts
        // Everything inside this block is Jetpack Compose UI
        setContent {
            AppRoot()
        }
    }
}

// -------- App-level navigation state --------

// Enum defines all possible screens in the app
// Reviewer note: This is state-driven navigation (no fragments, no intents)
enum class Screen {
    LOGIN, HOME
}

@Composable
fun AppRoot() {
    // Holds which screen should currently be visible
    // mutableStateOf ensures UI updates automatically when value changes
    var currentScreen by remember { mutableStateOf(Screen.LOGIN) }

    // when() decides which composable to render
    when (currentScreen) {
        Screen.LOGIN -> LoginScreen(
            // On successful login, move to HOME screen
            onLoginSuccess = { currentScreen = Screen.HOME }
        )
        Screen.HOME -> HomeScreen(
            // Logout simply resets screen state back to LOGIN
            onLogout = { currentScreen = Screen.LOGIN }
        )
    }
}

// -------- Login Screen --------

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    // State for username input field
    var username by remember { mutableStateOf("") }

    // State for password input field
    var password by remember { mutableStateOf("") }

    // Holds validation error message (null means no error)
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Username input field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it }, // updates state on each keystroke
            label = { Text("Username") },
            // If errorMessage is not null, field shows error state
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password input field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it }, // updates password state
            label = { Text("Password") },

            // Same error state as username field
            isError = errorMessage != null
        )

        // Validation error message shown only when errorMessage is not null
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // -------- Validation logic --------
                // If either field is empty, show error message
                if (username.isBlank() || password.isBlank()) {
                    errorMessage = "Username and password cannot be empty"
                } else {
                    // Clear error and proceed to Home screen
                    errorMessage = null
                    onLoginSuccess()
                }
            }
        ) {
            Text("Login")
        }
    }
}

// -------- Home Screen --------

// Data model for each list item
// Immutable data class (recommended for Compose)
data class Item(
    val id: Int,
    val title: String,
    val description: String,
    val isFavourite: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onLogout: () -> Unit) {
    // List of items stored as Compose state
    // Any change to this list triggers recomposition
    var items by remember {
        mutableStateOf(
            listOf(
                Item(1, "First APP", "Gagan creates his first app using kotlin. However, it is not get great. Can be improved. Keep going"),
                Item(2, "Did you built is by yourself?", "Got the help from internet, youtube and chatgpt. Understood the code and added few logic from my side. However still a long way to go."),
                Item(3, "How much confidence did you gain?", "I am pretty much condfident that i can understand the code written, however to write code myself, still much more practice is needed.")
            )
        )
    }

    // Scaffold provides basic Material layout structure
    Scaffold(
        topBar = {
            // Top app bar with title and logout action
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { paddingValues ->

        // LazyColumn efficiently renders scrollable list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(items) { item ->
                ItemRow(
                    item = item,
                    onFavouriteToggle = {
                        // -------- Favourite toggle logic --------
                        // We create a NEW list with updated item
                        // copy() ensures immutability
                        items = items.map {
                            if (it.id == item.id)
                                it.copy(isFavourite = !it.isFavourite)
                            else it
                        }
                    }
                )
            }
        }
    }
}

// -------- Individual Item Row --------

@Composable
fun ItemRow(item: Item, onFavouriteToggle: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Column holds title and description
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.description)
            }

            // Heart icon button for favourite toggle
            IconButton(onClick = onFavouriteToggle) {
                Icon(
                    imageVector = if (item.isFavourite)
                        Icons.Filled.Favorite // filled heart
                    else
                        Icons.Outlined.FavoriteBorder, // outline heart
                    contentDescription = "Favourite"
                )
            }
        }
    }
}