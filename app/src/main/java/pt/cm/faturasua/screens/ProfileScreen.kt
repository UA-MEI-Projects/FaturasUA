package pt.cm.faturasua.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pt.cm.faturasua.R


@Composable
fun ProfileScreen(){
    Column (

        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://www.alleycat.org/wp-content/uploads/2019/03/FELV-cat.jpg")
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "@usename",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape),
        )

        Text("@jhonitodoe_1")
        Spacer(modifier = Modifier.size(100.dp))
        TextField(value = "Jhon Doe.", label = {Text(text = "Name" )} ,onValueChange = {} ,readOnly = true)
        Spacer(modifier = Modifier.size(10.dp))
        TextField(value = "jhondoe@clix.pt", label = {Text(text = "E-Mail" )} ,onValueChange = {} ,readOnly = true)
        Spacer(modifier = Modifier.size(10.dp))
        TextField(value = "999199212", label = {Text(text = "NIF" )} ,onValueChange = { /*TODO*/ } ,readOnly = false)
        Spacer(modifier = Modifier.size(10.dp))
        TextField(value = "919212313", label = {Text(text = "Num. Telemóvel" )} ,onValueChange = { /*TODO*/ } ,readOnly = true)
        Spacer(modifier = Modifier.size(50.dp))
        Button(onClick = { /*TODO*/ }, content = { Text(text = "Salvar alterações") })

    }
}