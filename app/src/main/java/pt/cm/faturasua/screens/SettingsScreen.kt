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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.FaturasUATheme
import pt.cm.faturasua.utils.PreferencesManager
import pt.cm.faturasua.viewmodel.UserViewModel


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

        ToggleButton(
            label = "Dark Mode",
            value = darkModeOn,
            onCheckedChange = {
                darkModeOn = !darkModeOn
                userViewModel.updateTheme(darkModeOn)
                preferencesManager.saveData(PreferencesManager.PREFERENCE_CODE_DARK_MODE, darkModeOn)
            }
        )
        Spacer(modifier = Modifier.size(20.dp))
        ToggleButton(
            label = "Notifications",
            value = notifOn,
            onCheckedChange = {
                notifOn = !notifOn
                userViewModel.updateNotifs(notifOn)
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
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, shape)
            .clip(shape)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
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

@Preview
@Composable
fun SettingsPreview(){
    FaturasUATheme {
        SettingsScreen(LocalContext.current)
    }
}