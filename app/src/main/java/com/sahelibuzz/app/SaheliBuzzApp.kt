package com.sahelibuzz.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.sahelibuzz.app.navigation.AppNavigation

@Composable
fun SaheliBuzzApp() {
    val auth = remember { FirebaseAuth.getInstance() }

    var currentUser by remember {
        mutableStateOf(auth.currentUser)
    }

    var checkingSession by remember {
        mutableStateOf(true)
    }

    DisposableEffect(auth) {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            currentUser = firebaseAuth.currentUser
            checkingSession = false
        }

        auth.addAuthStateListener(listener)

        onDispose {
            auth.removeAuthStateListener(listener)
        }
    }

    when {
        checkingSession -> {
            SessionLoadingScreen()
        }

        currentUser != null -> {
            val navController = rememberNavController()

            AppNavigation(
                navController = navController
            )
        }

        else -> {
            SaheliBuzzAuth()
        }
    }
}

@Composable
private fun SessionLoadingScreen() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()

            Text(
                text = "SaheliBuzz"
            )
        }
    }
}
