package com.example.homework2

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random

data class AuthData(
    val email: String,
    val password: String
)

enum class AuthScreen {
    FORM,
    RESULT
}

class AuthViewModel : ViewModel() {

    val generatedPassword: String = generatePassword()

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var currentScreen by mutableStateOf(AuthScreen.FORM)
        private set

    var submittedData by mutableStateOf<AuthData?>(null)
        private set

    fun changeEmail(value: String) {
        email = value
        emailError = null
    }

    fun changePassword(value: String) {
        password = value
        passwordError = null
    }

    fun submit() {
        val checkedEmail = email.trim()

        emailError = when {
            checkedEmail.isEmpty() ->
                "Введите электронную почту"

            !Patterns.EMAIL_ADDRESS.matcher(checkedEmail).matches() ->
                "Некорректный адрес электронной почты"

            else -> null
        }

        passwordError = when {
            password.isEmpty() ->
                "Введите пароль"

            password.length < 8 ->
                "Пароль должен содержать минимум 8 символов"

            !password.any { it in 'a'..'z' || it in 'A'..'Z' } ->
                "Добавьте хотя бы одну латинскую букву"

            !password.any { it in '0'..'9' } ->
                "Добавьте хотя бы одну цифру"

            else -> null
        }

        if (emailError == null && passwordError == null) {
            submittedData = AuthData(
                email = checkedEmail,
                password = password
            )

            currentScreen = AuthScreen.RESULT
        }
    }

    fun backToForm() {
        currentScreen = AuthScreen.FORM
    }
}

private fun generatePassword(): String {
    val letters = ('a'..'z') + ('A'..'Z')
    val digits = ('0'..'9').toList()
    val allCharacters = letters + digits

    val characters = mutableListOf<Char>()

    characters.add(letters.random())
    characters.add(digits.random())

    repeat(8) {
        characters.add(allCharacters.random())
    }

    return characters
        .shuffled(Random.Default)
        .joinToString("")
}