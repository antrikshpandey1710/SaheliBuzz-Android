package com.sahelibuzz.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SaheliBuzzAuth()
        }
    }
}

@Composable
fun SaheliBuzzAuth() {

    val auth = remember { FirebaseAuth.getInstance() }

    var isSignUp by remember { mutableStateOf(false) }

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "SaheliBuzz",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isSignUp)
                    "Create your account"
                else
                    "Welcome back",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(28.dp))

            if (isSignUp) {

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        message = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Username") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    message = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    message = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {

                CircularProgressIndicator()

            } else {

                Button(
                    onClick = {

                        val cleanEmail = email.trim()
                        val cleanUsername = username
                            .trim()
                            .lowercase()
                            .replace("\\s+".toRegex(), "")

                        if (cleanEmail.isEmpty()) {
                            message = "Please enter your email."
                            return@Button
                        }

                        if (password.length < 6) {
                            message = "Password must be at least 6 characters."
                            return@Button
                        }

                        if (isSignUp && cleanUsername.length < 3) {
                            message =
                                "Username must be at least 3 characters."
                            return@Button
                        }

                        isLoading = true
                        message = ""

                        if (isSignUp) {

                            auth.createUserWithEmailAndPassword(
                                cleanEmail,
                                password
                            ).addOnCompleteListener { task ->

                                isLoading = false

                                if (task.isSuccessful) {
                                    message =
                                        "Account created successfully."
                                } else {
                                    message =
                                        task.exception?.message
                                            ?: "Unable to create account."
                                }
                            }

                        } else {

                            auth.signInWithEmailAndPassword(
                                cleanEmail,
                                password
                            ).addOnCompleteListener { task ->

                                isLoading = false

                                if (task.isSuccessful) {
                                    message =
                                        "Login successful."
                                } else {
                                    message =
                                        task.exception?.message
                                            ?: "Unable to login."
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        if (isSignUp)
                            "Create Account"
                        else
                            "Log In"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = {
                    isSignUp = !isSignUp
                    message = ""
                }
            ) {

                Text(
                    if (isSignUp)
                        "Already have an account? Sign In"
                    else
                        "Need an account? Sign Up"
                )
            }

            if (!isSignUp) {

                OutlinedButton(
                    onClick = {

                        val cleanEmail = email.trim()

                        if (cleanEmail.isEmpty()) {
                            message =
                                "Enter your email first."
                            return@OutlinedButton
                        }

                        isLoading = true
                        message = ""

                        auth.sendPasswordResetEmail(cleanEmail)
                            .addOnCompleteListener { task ->

                                isLoading = false

                                message =
                                    if (task.isSuccessful) {
                                        "Password reset email sent."
                                    } else {
                                        task.exception?.message
                                            ?: "Unable to send reset email."
                                    }
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Forgot Password")
                }
            }

            if (message.isNotEmpty()) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
