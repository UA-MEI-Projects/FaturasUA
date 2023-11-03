package pt.cm.faturasua.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import pt.cm.faturasua.R
import pt.cm.faturasua.utils.FirebaseUtil


@Composable
fun AuthScreen(
    firebaseUtil: FirebaseUtil
) {
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
        Image(painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(
                150.dp
            ))
        if(alreadySignedIn){
            if(expanded){
                ExpandedMenu(
                    buttonText = "Sign In",
                    onClick = { email, password, username, phoneNumber ->
                        if(email != "" && password != "")
                            firebaseUtil.signIn(email = email, password = password)
                    },
                    signUp = false
                )
            }
            else{
                Button(onClick = { expanded = !expanded }) {
                    Text("Sign in")
                }
            }
            ClickableText(
                text = AnnotatedString("Not signed in?"),
                onClick = {
                    alreadySignedIn = !alreadySignedIn
                }
            )
        }
        else{
            if(expanded){
                ExpandedMenu(
                    buttonText = "Sign Up",
                    onClick = { email, password, username, phoneNumber ->
                        if(email != "" && password != "")
                            firebaseUtil.createAccount(
                                email = email,
                                password = password,
                                displayName = username,
                                phoneNumber = phoneNumber
                            )
                    },
                    signUp = true
                )
            }
            else{
                Button(onClick = { expanded = !expanded }) {
                    Text("Sign up")
                }
            }
            ClickableText(
                text = AnnotatedString("Already signed in?"),
                onClick = {
                    alreadySignedIn = !alreadySignedIn
                }
            )
        }
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
            label = { Text(text = "Email") },
            onValueChange = { username = it }
        )
        Spacer(modifier = Modifier.size(10.dp))
        OutlinedTextField(
            value = phoneNumber,
            label = { Text(text = "Phone Number") },
            onValueChange = { phoneNumber = it }
        )
        Spacer(modifier = Modifier.size(10.dp))
    }
    OutlinedTextField(
        value = email,
        label = { Text(text = "Email") },
        onValueChange = { email = it }
    )
    Spacer(modifier = Modifier.size(10.dp))
    OutlinedTextField(
        value = password,
        label = { Text(text = "Password") },
        onValueChange = { password = it },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)

    )
    Spacer(modifier = Modifier.size(50.dp))
    Button(onClick = { onClick(email, password, username, phoneNumber)}) {
        Text(buttonText)
    }
}