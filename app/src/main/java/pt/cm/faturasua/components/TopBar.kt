package pt.cm.faturasua.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pt.cm.faturasua.R
import pt.cm.faturasua.classes.DropdownMenuClass
import pt.cm.faturasua.utils.FirebaseUtil
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    firebaseUtil: FirebaseUtil,
    modifier: Modifier = Modifier,
    onSignOutCallback: () -> Unit,
    screens: List<DropdownMenuClass>
) {
    val scope = rememberCoroutineScope()

    var menuExpanded by remember{
        mutableStateOf(false)
    }


    TopAppBar(title = {
        navController.currentBackStackEntry?.destination?.route?.let {
            var title = it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            if (it == "dashboard")
                 title = "FaturasUA"
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold
            )
        }
    },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            if (navController.previousBackStackEntry != null && navController.currentBackStackEntry?.destination?.route != "dashboard"){
                IconButton(onClick = { navController.navigate("dashboard") }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
                null
            }
        },
        actions = {
            IconButton(onClick = { menuExpanded = !menuExpanded}) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Options"
                )
            }

            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                screens.forEach { screen ->
                    DropdownMenuItem(
                        text = { Text(text= screen.title) },
                        onClick = {

                            navController.navigate(screen.route)
                            menuExpanded = !menuExpanded
                                  },
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
                DropdownMenuItem(
                    text = { Text(text= stringResource(R.string.navbar_menu_log_out)) },
                    onClick = {
                        onSignOutCallback()
                        menuExpanded = !menuExpanded
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        },
        modifier = modifier
    )
}