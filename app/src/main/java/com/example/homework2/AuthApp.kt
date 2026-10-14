package com.example.homework2

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AuthApp(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler(
        enabled = viewModel.currentScreen == AuthScreen.RESULT
    ) {
        viewModel.backToForm()
    }

    when (viewModel.currentScreen) {
        AuthScreen.FORM -> {
            AuthFormScreen(
                email = viewModel.email,
                password = viewModel.password,
                emailError = viewModel.emailError,
                passwordError = viewModel.passwordError,
                onEmailChange = viewModel::changeEmail,
                onPasswordChange = viewModel::changePassword,
                onSubmit = viewModel::submit,
                modifier = modifier
            )
        }

        AuthScreen.RESULT -> {
            val data = viewModel.submittedData

            if (data != null) {
                AuthResultScreen(
                    data = data,
                    generatedPassword = viewModel.generatedPassword,
                    onBack = viewModel::backToForm,
                    modifier = modifier
                )
            }
        }
    }
}