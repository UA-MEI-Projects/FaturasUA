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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
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
            var newPassword by remember{ mutableStateOf("") }
            var newPasswordConfirm by remember{ mutableStateOf("") }

            OutlinedTextField(
                value = newPassword,
                label = { Text("New Password") },
                onValueChange = { newPassword = it }
            )
            Spacer(modifier = Modifier.size(100.dp))
            OutlinedTextField(
                value = newPasswordConfirm,
                label = { Text("Confirm New Password") },
                onValueChange = { newPasswordConfirm = it }
            )
            Spacer(modifier = Modifier.size(30.dp))
            ClickableText(
                text = AnnotatedString("Edit Profile"),
                onClick = {
                    passwordScreen = !passwordScreen
                }
            )
            Spacer(modifier = Modifier.size(100.dp))
            Button(
                onClick = {
                    if(newPassword.equals(newPasswordConfirm))
                        firebaseUtil.updatePassword(newPassword)
                },
                content = { Text(text = "Salvar alterações") }
            )
        }
        else{
            var name by remember{ mutableStateOf(userViewModel.profile.value!!.name) }
            var email by remember{ mutableStateOf(userViewModel.profile.value!!.email) }
            var phoneNmber by remember{ mutableStateOf(userViewModel.profile.value!!.phoneNumber) }


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://www.alleycat.org/wp-content/uploads/2019/03/FELV-cat.jpg")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "@username",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape),
            )

            Text("@jhonitodoe_1")
            Spacer(modifier = Modifier.size(100.dp))
            TextField(
                value = name,
                label = { Text(text = "Name") },
                onValueChange = { name = it},
                readOnly = true
            )
            Spacer(modifier = Modifier.size(10.dp))
            TextField(
                value = email,
                label = { Text(text = "E-Mail") },
                onValueChange = { email = it},
                readOnly = false
            )
            Spacer(modifier = Modifier.size(10.dp))
            TextField(
                value = "999199212",
                label = { Text(text = "NIF") },
                onValueChange = { /*TODO*/ },
                readOnly = true
            )
            Spacer(modifier = Modifier.size(10.dp))
            TextField(
                value = phoneNmber,
                label = { Text(text = "Num. Telemóvel") },
                onValueChange = { phoneNmber = it },
                readOnly = true
            )
            Spacer(modifier = Modifier.size(30.dp))
            ClickableText(
                text = AnnotatedString("Change Password"),
                onClick = {
                    passwordScreen = !passwordScreen
                }
            )
            Spacer(modifier = Modifier.size(50.dp))
            Button(
                onClick = {
                    val profile = Profile(
                        name = name,
                        email = email,
                        photo = Uri.EMPTY,
                        phoneNumber = phoneNmber
                    )
                    firebaseUtil.updateUserProfile(profile)
                },
                content = { Text(text = "Salvar alterações") }
            )
        }

    }
}
