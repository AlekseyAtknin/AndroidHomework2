package com.example.homework2

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.PersistableBundle
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun AuthFormScreen(
    email: String,
    password: String,
    emailError: String?,
    passwordError: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier) {
        TextField(
            value = email,
            onValueChange = onEmailChange,
            label = {
                Text("Электронная почта")
            },
            singleLine = true,
            isError = emailError != null,
            supportingText = {
                emailError?.let { message ->
                    Text(message)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = {
                Text("Пароль")
            },
            singleLine = true,
            isError = passwordError != null,
            supportingText = {
                passwordError?.let { message ->
                    Text(message)
                }
            },
            visualTransformation =
                PasswordVisualTransformation(mask = '*'),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onSubmit()
                }
            )
        )

        Button(onClick = onSubmit, modifier = Modifier.width(170.dp)) {
            Text(
                text = "Продолжить",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun AuthResultScreen(
    data: AuthData,
    generatedPassword: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    val passwordsMatch = data.password == generatedPassword

    val imageUrl = if (passwordsMatch) {
        "https://placehold.co/600x400/png?text=SUCCESS"
    } else {
        "https://placehold.co/600x400/png?text=FAIL"
    }

    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = if (passwordsMatch) {
                "Пароли совпадают"
            } else {
                "Пароли не совпадают"
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "email: " + data.email)

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = generatedPassword,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = {
                Text("Сгенерированный пароль")
            },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation(mask = '*')
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                passwordVisible = !passwordVisible
            },
            modifier = Modifier.width(170.dp)
        ) {
            Text(
                text = if (passwordVisible) "Скрыть" else "Показать",
                 fontSize = 18.sp
            )
        }

        Button(
            modifier = Modifier.width(170.dp),
            onClick = {
                val clipboard = context.getSystemService(
                    Context.CLIPBOARD_SERVICE
                ) as ClipboardManager

                val clip = ClipData.newPlainText(
                    "Сгенерированный пароль",
                    generatedPassword
                )

                clip.description.extras = PersistableBundle().apply {
                    putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                }

                clipboard.setPrimaryClip(clip)
            }
        ) {
            Text("Копировать пароль")
        }

        Button(
            onClick = onBack,
            modifier = Modifier.width(170.dp)
        ) {
            Text(
                text = "Вернуться",
                fontSize = 18.sp
            )
        }
    }
}