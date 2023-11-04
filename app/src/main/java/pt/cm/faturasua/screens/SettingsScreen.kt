package pt.cm.faturasua.screens

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.FaturasUATheme
import pt.cm.faturasua.utils.PreferencesManager
import pt.cm.faturasua.viewmodel.UserViewModel
import java.util.Locale


@Composable
fun SettingsScreen(
    context: Context,
    userViewModel: UserViewModel = viewModel()
){
    val preferencesManager = remember{ PreferencesManager(context)}
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

        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("xx-YY")
        Text(text = "$appLocale")

        val currentLang: String = Locale.getDefault().getDisplayName()
        Text(text = "$currentLang")

        LanguageDropdownMenu(
            options = listOf("English", "Portuguese"),
            selected = "English",
            label = "Language",
            onOptionChange = { selectedOption ->
                println("LanguageDropdownMenu - Selected option: $selectedOption")
                val locale : String = when (selectedOption) {
                    "Portuguese", "Português" -> "pt" // or pt_PT ?
                    else -> "en" // or en_US ?
                }
                println("LanguageDropdownMenu - Locale file: $locale")
                // TODO: Create and call language 'locale' according to the user choice
            }
        )
        Spacer(modifier = Modifier.size(20.dp))
        ToggleButton(
            label = "Dark mode",
            value = darkModeOn,
            onCheckedChange = {
                darkModeOn = !darkModeOn
                userViewModel.darkThemePreference.setValue(darkModeOn)
                preferencesManager.saveData(PreferencesManager.PREFERENCE_CODE_DARK_MODE, darkModeOn)
            }
        )
        Spacer(modifier = Modifier.size(20.dp))
        ToggleButton(
            label = "Notifications",
            value = notifOn,
            onCheckedChange = {
                notifOn = !notifOn
                userViewModel.notifsOn.setValue(notifOn)
                preferencesManager.saveData(PreferencesManager.PREFERENCE_CODE_NOTIFICATIONS, notifOn)

            }
        )
        Spacer(modifier = Modifier.size(20.dp))

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
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
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
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
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