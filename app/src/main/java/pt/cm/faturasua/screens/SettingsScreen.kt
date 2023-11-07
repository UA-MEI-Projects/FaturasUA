package pt.cm.faturasua.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.FaturasUATheme
import pt.cm.faturasua.R
import pt.cm.faturasua.utils.PreferencesManager
import pt.cm.faturasua.viewmodel.UserViewModel
import java.util.Locale


@Composable
fun SettingsScreen(
    context: Context,
    userViewModel: UserViewModel = viewModel()
){
    val preferencesManager = remember{ PreferencesManager(context)}
    var languageOn by remember {
        mutableStateOf(preferencesManager.getData(PreferencesManager.PREFERENCE_CODE_LANGUAGE, "English"))
    }
    var darkModeOn by remember {
        mutableStateOf(preferencesManager.getData(PreferencesManager.PREFERENCE_CODE_DARK_MODE, false))
    }
    var notifOn by remember{
        mutableStateOf(preferencesManager.getData(PreferencesManager.PREFERENCE_CODE_NOTIFICATIONS, true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        val deviceLanguage: String = Locale.getDefault().displayLanguage.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        val optionsList : List<String> = listOf(
            stringResource(
                R.string.settings_device_language_option,
                deviceLanguage
            ), "English", "Português")

        LanguageDropdownMenu(
            options = optionsList,
            selected = languageOn,
            label = stringResource(R.string.settings_language_label),
            onOptionChange = { selectedOption ->
                println("LanguageDropdownMenu - Selected option: $selectedOption")
                val locale : String = when (selectedOption) {
                    optionsList[0] -> "auto"
                    optionsList[2] -> "pt" // or pt_PT ?
                    else -> "en" // or en_US ?
                }
                // TODO: Force call language 'locale' according to the user choice instead of using device auto
                println("LanguageDropdownMenu - Locale file: $locale")
                preferencesManager.saveData(PreferencesManager.PREFERENCE_CODE_LANGUAGE, selectedOption)
            }
        )
        Spacer(modifier = Modifier.size(20.dp))
        ToggleButton(
            label = stringResource(R.string.settings_dark_mode),
            value = darkModeOn,
            onCheckedChange = {
                darkModeOn = !darkModeOn
                userViewModel.updateTheme(darkModeOn)
                preferencesManager.saveData(PreferencesManager.PREFERENCE_CODE_DARK_MODE, darkModeOn)
            }
        )
        Spacer(modifier = Modifier.size(20.dp))
        ToggleButton(
            label = stringResource(R.string.settings_notifications),
            value = notifOn,
            onCheckedChange = {
                notifOn = !notifOn
                userViewModel.updateNotifs(notifOn)
                preferencesManager.saveData(PreferencesManager.PREFERENCE_CODE_NOTIFICATIONS, notifOn)

            }
        )
        Spacer(modifier = Modifier.size(50.dp))
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(R.string.settings_about_title), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.size(10.dp))
            Text(text = stringResource(R.string.settings_about_description), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.size(30.dp))
            Image(painter = painterResource(id = R.drawable.ua_logo),
                contentDescription = "Universidade de Aveiro",
                modifier = Modifier.size(250.dp)
            )
        }
    }
}

@Composable
fun ToggleButton(
    label : String,
    value : Boolean,
    onCheckedChange : () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.size(10.dp))
        Switch(
            checked = value,
            onCheckedChange = {
                onCheckedChange()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropdownMenu(
    options : List<String>,
    selected : String,
    label: String,
    onOptionChange : (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(selected) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedLabelColor = Color.DarkGray,
                    focusedLabelColor = Color.DarkGray,
                ),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                            onOptionChange(selectionOption)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingsPreview(){
    FaturasUATheme {
        SettingsScreen(LocalContext.current)
    }
}