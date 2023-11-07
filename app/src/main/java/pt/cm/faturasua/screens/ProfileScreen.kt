package pt.cm.faturasua.screens

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pt.cm.faturasua.R
import pt.cm.faturasua.data.Profile
import pt.cm.faturasua.utils.FirebaseUtil
import pt.cm.faturasua.viewmodel.UserViewModel


@Composable
fun ProfileScreen(
    firebaseUtil: FirebaseUtil,
    userViewModel: UserViewModel = viewModel()
){
    var passwordScreen by remember{
        mutableStateOf(false)
    }

    Column (

        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ){
        if(passwordScreen){
            var oldPassword by remember{ mutableStateOf("") }
            var newPassword by remember{ mutableStateOf("") }
            var newPasswordConfirm by remember{ mutableStateOf("") }

            OutlinedTextField(
                value = oldPassword,
                label = { Text("Current password") },
                onValueChange = { oldPassword = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.size(50.dp))
            OutlinedTextField(
                value = newPassword,
                label = { Text("New password") },
                onValueChange = { newPassword = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.size(50.dp))
            OutlinedTextField(
                value = newPasswordConfirm,
                label = { Text("Confirm new password") },
                onValueChange = { newPasswordConfirm = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.size(50.dp))
            OutlinedButton(
                content = { Text("Go back") },
                onClick = {
                    passwordScreen = !passwordScreen
                }
            )
            Spacer(modifier = Modifier.size(30.dp))
            Button(
                onClick = {
                    if(newPassword.equals(newPasswordConfirm))
                        firebaseUtil.updatePassword(newPassword)
                },
                content = { Text(text = "Change password") }
            )
        }
        else{
            val profile = userViewModel.profile.collectAsState().value
            var name by remember{ mutableStateOf(profile.name) }
            var email by remember{ mutableStateOf(profile.email) }
            var phoneNumber by remember{ mutableStateOf(profile.phoneNumber) }
            val nif = userViewModel.nif.value.toString()


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://creazilla-store.fra1.digitaloceanspaces.com/icons/7912642/avatar-icon-md.png")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "User avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.size(30.dp))
            TextField(
                value = name,
                label = { Text(text = "Name") },
                onValueChange = { name = it},
                readOnly = true
            )
            Spacer(modifier = Modifier.size(10.dp))
            TextField(
                value = email,
                label = { Text(text = "E-mail") },
                onValueChange = { email = it},
                readOnly = false
            )
            Spacer(modifier = Modifier.size(10.dp))
            TextField(
                value = nif,
                label = { Text(text = "NIF") },
                onValueChange = {},
                readOnly = true
            )
            Spacer(modifier = Modifier.size(10.dp))
            TextField(
                value = phoneNumber,
                label = { Text(text = "Phone number") },
                onValueChange = { phoneNumber = it },
                readOnly = false
            )
            Spacer(modifier = Modifier.size(30.dp))
            FilledTonalButton(
                content = { Text("Change password") },
                onClick = {
                    passwordScreen = !passwordScreen
                }
            )
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                onClick = {
                    val profile = Profile(
                        name = name,
                        email = email,
                        photo = Uri.EMPTY,
                        phoneNumber = phoneNumber
                    )
                    firebaseUtil.updateUserProfile(profile)
                },
                content = { Text(text = "Save changes") }
            )
        }

    }
}
