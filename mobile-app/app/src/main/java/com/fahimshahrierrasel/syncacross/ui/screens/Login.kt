package com.fahimshahrierrasel.syncacross.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fahimshahrierrasel.syncacross.R
import com.fahimshahrierrasel.syncacross.ui.NavigationScreens
import com.fahimshahrierrasel.syncacross.ui.theme.AccentColor
import com.fahimshahrierrasel.syncacross.viewmodels.LoginViewModel
import com.fahimshahrierrasel.syncacross.viewmodels.LoginShotEvent
import com.fahimshahrierrasel.syncacross.viewmodels.LoginUIAction
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun Login(viewModel: LoginViewModel, navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val viewState = viewModel.viewState.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect("key") {
        viewModel.shotEvents.onEach {
            when (it) {
                LoginShotEvent.NavigateToHome -> {
                    navController.navigate(NavigationScreens.Home.route) {
                        popUpTo(NavigationScreens.Login.route) { inclusive = true }
                    }
                }
                LoginShotEvent.LoginError -> {
                    scaffoldState.snackbarHostState.showSnackbar(viewState.value.message)
                }
            }
        }.collect()
    }

    fun onLoginClicked() {
        focusManager.clearFocus()
        scope.launch {
            viewModel.onAction(LoginUIAction.LoginUser(email = email.value, password = password.value))
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            if (viewState.value.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = AccentColor
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }
            Column(
                Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(64.dp),
                    painter = painterResource(id = R.drawable.ic_loop),
                    contentDescription = stringResource(id = R.string.logo)
                )
                Text(
                    stringResource(id = R.string.app_name),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Column(
                Modifier
                    .weight(2f)
                    .width(intrinsicSize = IntrinsicSize.Min),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text(stringResource(id = R.string.email)) },
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.moveFocus(
                            FocusDirection.Next
                        )
                    }),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(6.dp))
                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text(stringResource(id = R.string.password)) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
                )
                Button(
                    onClick = { onLoginClicked() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !viewState.value.isLoading
                ) {
                    Text(stringResource(id = R.string.log_in))
                }
            }
        }
    }
}