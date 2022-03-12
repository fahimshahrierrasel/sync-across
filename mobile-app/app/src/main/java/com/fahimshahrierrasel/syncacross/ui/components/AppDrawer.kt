package com.fahimshahrierrasel.syncacross.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fahimshahrierrasel.syncacross.BuildConfig
import com.fahimshahrierrasel.syncacross.R
import com.fahimshahrierrasel.syncacross.config.FirebaseConfig
import com.fahimshahrierrasel.syncacross.data.models.Tag
import com.fahimshahrierrasel.syncacross.viewmodels.HomeUIAction
import com.fahimshahrierrasel.syncacross.viewmodels.HomeViewModel
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun AppDrawer(viewModel: HomeViewModel) {
    val isFormDialogOpened = remember { mutableStateOf(false) }
    val viewState = viewModel.viewState.collectAsState()

    fun onLogoutClick() {
        FirebaseConfig.auth.signOut();
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    modifier = Modifier
                        .height(64.dp)
                        .padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.ic_loop),
                    contentDescription = stringResource(id = R.string.logo)
                )
                Text(
                    stringResource(id = R.string.app_name),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("v${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})", color = Color.Gray)

            }
            Divider(Modifier.padding(vertical = 4.dp), thickness = 2.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.tags),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                )
                IconButton(onClick = { isFormDialogOpened.value = true }) {
                    Icon(Icons.Rounded.Add, contentDescription = stringResource(R.string.add))
                }
            }

            FlowRow(
                mainAxisSpacing = 4.dp, crossAxisSpacing = 4.dp, modifier = Modifier
                    .padding(vertical = 4.dp)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {
                viewState.value.tags.forEach { Tag(label = it.title) }
            }
        }
        Button(onClick = { onLogoutClick() }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Rounded.Logout, contentDescription = stringResource(R.string.logout))
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.logout), textAlign = TextAlign.Center)
        }

        if (isFormDialogOpened.value) {
            TagFormDialog(
                viewModel = viewModel,
                onDismiss = { isFormDialogOpened.value = false },
                onSave = {
                    viewModel.onAction(HomeUIAction.NewTag(it))
                    isFormDialogOpened.value = false
                })
        }
    }
}


