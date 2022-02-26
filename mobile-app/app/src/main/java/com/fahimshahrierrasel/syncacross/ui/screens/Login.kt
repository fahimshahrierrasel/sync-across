package com.fahimshahrierrasel.syncacross.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.fahimshahrierrasel.syncacross.R
import com.fahimshahrierrasel.syncacross.ui.NavigationScreens
import com.fahimshahrierrasel.syncacross.ui.theme.AccentColor
import com.fahimshahrierrasel.syncacross.ui.theme.SecondaryColor
import kotlinx.coroutines.launch

@Composable
fun Login(navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val showProgress = remember { mutableStateOf(false) }

    fun onLoginClicked() {
        showProgress.value = !showProgress.value;
        scope.launch {
//            scaffoldState.snackbarHostState.showSnackbar("Login Processing...")
            navController.navigate(NavigationScreens.Home.route)
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            if (showProgress.value) {
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(6.dp))
                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text(stringResource(id = R.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Button(onClick = { onLoginClicked() }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(id = R.string.log_in))
                }
            }
        }
    }
}