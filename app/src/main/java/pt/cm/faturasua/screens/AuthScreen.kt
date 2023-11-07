package pt.cm.faturasua.screens

import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindowProvider
import pt.cm.faturasua.R
import pt.cm.faturasua.utils.FirebaseUtil


@Composable
fun AuthScreen(
    firebaseUtil: FirebaseUtil,
    onChangeAdminMode: () -> Unit
) {
    val openAlertDialogErrorEmptyFields = remember { mutableStateOf(false) }
    val openAlertDialogErrorInvalidFields = remember { mutableStateOf(false) }
    val openAlertDialogErrorWrongCredentials = remember { mutableStateOf(false) }

    when {
        openAlertDialogErrorEmptyFields.value -> {
            AlertDialogError(
                onDismissRequest = { openAlertDialogErrorEmptyFields.value = false },
                dialogTitle = stringResource(R.string.auth_error_dialog_title) + "!",
                dialogText = stringResource(R.string.auth_error_dialog_empty_fields),
                icon = Icons.Default.Warning
            )
        }
        openAlertDialogErrorInvalidFields.value -> {
            AlertDialogError(
                onDismissRequest = { openAlertDialogErrorInvalidFields.value = false },
                dialogTitle = stringResource(R.string.auth_error_dialog_title) + "!",
                dialogText = stringResource(R.string.auth_error_dialog_invalid_fields),
                icon = Icons.Default.Warning
            )
        }
        openAlertDialogErrorWrongCredentials.value -> {
            AlertDialogError(
                onDismissRequest = { openAlertDialogErrorWrongCredentials.value = false },
                dialogTitle = stringResource(R.string.auth_error_dialog_title) + "!",
                dialogText = stringResource(R.string.auth_error_dialog_wrong_credentials),
                icon = Icons.Default.Warning
            )
        }
    }

    var adminSignIn by remember{
        mutableStateOf(false)
    }
    var alreadySignedIn by remember{
        mutableStateOf(true)
    }

    var expanded by remember{
        mutableStateOf(false)
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly

    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(150.dp)
        )

        // ADMIN LOGIN
        if(adminSignIn){
            AdminMenu(onClick = { token, password ->
                if(token == "" || password == "")
                    openAlertDialogErrorEmptyFields.value = true
                else if(token != "admin" && password != "admin")    // Hardcoded admin credentials
                    openAlertDialogErrorWrongCredentials.value = true
                else
                    onChangeAdminMode()
            })

            FilledTonalButton(
                content = { Text("Go back") },
                onClick = { adminSignIn = !adminSignIn }
            )
        }
        // USER LOGIN
        else{
            if(alreadySignedIn){
                if(expanded){
                    ExpandedMenu(
                        buttonText = "Log in",
                        onClick = { email, password, username, phoneNumber ->
                            if(email == "" || password == "")
                                openAlertDialogErrorEmptyFields.value = true
                            else if (!email.contains("@"))
                                openAlertDialogErrorInvalidFields.value = true
                            else {
                                firebaseUtil.signIn(email = email, password = password){
                                    if(!it){
                                        openAlertDialogErrorWrongCredentials.value = true
                                    }
                                }
                            }
                        },
                        signUp = false
                    )

                    FilledTonalButton(
                        content = { Text("Go back") },
                        onClick = { expanded = !expanded }
                    )
                }
                else{
                    Button(onClick = { expanded = !expanded }) {
                        Text(stringResource(R.string.auth_user_login_button))
                    }

                    Button(
                        onClick = { adminSignIn = !adminSignIn }
                    ){
                        Text(stringResource(R.string.auth_admin_login_button))
                    }

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "New to FaturasUA?", style = MaterialTheme.typography.bodyMedium)
                        FilledTonalButton(
                            content = { Text("Create an account first") },
                            modifier = Modifier.padding(5.dp),
                            onClick = {
                                alreadySignedIn = !alreadySignedIn
                            }
                        )
                    }
                }
            }

            // USER SIGN UP
            else{
                ExpandedMenu(
                    buttonText = "Create account",
                    onClick = { email, password, username, phoneNumber ->
                        if(email == "" || password == "" || username == "" || phoneNumber == "")
                            openAlertDialogErrorEmptyFields.value = true
                        else if (!email.contains("@") || password.length < 8 || !username.contains(" ") || phoneNumber.length != 9)
                            openAlertDialogErrorInvalidFields.value = true
                        else
                            firebaseUtil.createAccount(
                                email = email,
                                password = password,
                                displayName = username,
                                phoneNumber = phoneNumber
                            )
                    },
                    signUp = true
                )
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 25.dp)
                ) {
                    Text(text = "Already have an account?", style = MaterialTheme.typography.bodyMedium)
                    FilledTonalButton(
                        content = { Text("Log in instead") },
                        modifier = Modifier.padding(5.dp),
                        onClick = {
                            alreadySignedIn = !alreadySignedIn
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AlertDialogError(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        icon = { Icon(icon, contentDescription = stringResource(R.string.auth_error_dialog_title), tint = MaterialTheme.colorScheme.error) },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = { TextButton(onClick = { onDismissRequest() }) { Text("OK") } }
    )
}

@Composable
fun AdminMenu(
    onClick: (tokenId: String, password: String) -> Unit
){
    var token by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }

    OutlinedTextField(
        value = token,
        label = { Text(text = "Token") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onValueChange = { token = it }
    )
    Spacer(modifier = Modifier.size(10.dp))
    OutlinedTextField(
        value = password,
        label = { Text(text = "Password") },
        onValueChange = { password = it },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password)
    )
    Spacer(modifier = Modifier.size(10.dp))
    Button(onClick = { onClick(token, password)}) {
        Text("Enter admin panel")
    }
}

@Composable
fun ExpandedMenu(
    buttonText: String,
    onClick:(email: String, password: String, username: String, phoneNumber:String) -> Unit,
    signUp: Boolean
){
    var username by remember{ mutableStateOf("") }
    var phoneNumber by remember{ mutableStateOf("") }
    var email by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    if(signUp){
        OutlinedTextField(
            value = username,
            label = { Text(text = "Name (first and last)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
            onValueChange = { username = it }
        )
        Spacer(modifier = Modifier.size(10.dp))
        OutlinedTextField(
            value = phoneNumber,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Phone),
            label = { Text(text = "Phone number") },
            onValueChange = { value ->
                if (value.length <= 9) {
                    phoneNumber = value.filter { it.isDigit() }
                }
            }
        )
        Spacer(modifier = Modifier.size(10.dp))
    }
    OutlinedTextField(
        value = email,
        label = { Text(text = "Email") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Email),
        onValueChange = { email = it }
    )
    Spacer(modifier = Modifier.size(10.dp))
    OutlinedTextField(
        value = password,
        label = { Text(text = "Password (min. 8 chars)") },
        onValueChange = { password = it },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password)

    )
    Spacer(modifier = Modifier.size(50.dp))
    Button(onClick = { onClick(email, password, username, phoneNumber)}) {
        Text(buttonText)
    }
}
