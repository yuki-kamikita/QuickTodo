package com.akaiyukiusagi.quicktodo.ui_layer.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.akaiyukiusagi.quicktodo.R
import com.akaiyukiusagi.quicktodo.data_layer.BooleanPreference
import com.akaiyukiusagi.quicktodo.ui_layer.component.PreviewComponent
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.ISettingsViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.PreviewSettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: ISettingsViewModel,
    navigator: NavController = rememberNavController()
) {
    val showDoneTasks = viewModel.showDoneTasks.collectAsState(initial = BooleanPreference.SHOW_DONE_TASKS.initialValue).value
    val themeColor = viewModel.showDoneTasks.collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(id =R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceContainer)
            )
        },
        content = {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                Column {
                    SectionTitle(stringResource(R.string.color))
                    SettingsRow(stringResource(R.string.dark_mode), {})
                    SettingsRow(stringResource(R.string.theme_color), {})
                    SectionTitle(stringResource(R.string.display_settings))
                    SettingsRow(
                        text = stringResource(R.string.show_done_tasks),
                        onClick = { viewModel.changeShowDoneTask(!showDoneTasks) },
                        suffix = {
                            Switch(
                                checked = showDoneTasks,
                                onCheckedChange = { viewModel.changeShowDoneTask(it) }
                            )
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun SectionTitle(
    text: String
) {
    Column {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SettingsRow(
    text: String,
    onClick: (() -> Unit)? = null,
    suffix: @Composable () -> Unit = {}
) {
    val rowModifier = Modifier
        .fillMaxWidth()
        .then(if (onClick != null) Modifier.clickable { onClick.invoke() } else Modifier)
        .padding(12.dp)

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .weight(1f)
        )
        suffix()
    }
}

@PreviewLightDark
@PreviewDynamicColors
@PreviewFontScale
@Composable
fun PreviewSettingsScreen() {
    PreviewComponent {
        SettingsScreen(PreviewSettingsViewModel())
    }
}