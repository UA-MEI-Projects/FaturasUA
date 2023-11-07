package pt.cm.faturasua.screens

import android.Manifest
import android.bluetooth.BluetoothCsipSetCoordinator
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract.SearchSnippets
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.compose.FaturasUATheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlin.coroutines.coroutineContext

@Composable
fun MapsScreen(
    context:Context,
    title: String? = null,
    snippet: String? = null
){
    var pos by remember{ mutableStateOf(LatLng(0.0, 0.0)) }

    var hasLocationPermission by remember{
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasLocationPermission = granted
        }
    )

    LaunchedEffect(key1 = true){
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val location = LocationServices.getFusedLocationProviderClient(context)
    location.lastLocation.addOnCompleteListener {
        if(it.result != null){
            pos = LatLng(it.result.latitude, it.result.longitude)
        }
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(pos, 20f)
    }

    val drinksMarkerState = rememberMarkerState(
        key = title,
        position = pos
    )


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(500.dp)
    ){
        if(hasLocationPermission){
            GoogleMap(
                modifier = Modifier
                    .padding(20.dp),
                cameraPositionState = cameraPositionState
            ){
                Marker(state = drinksMarkerState,
                    title =  title,
                    snippet = snippet
                )
            }
        }
        else{
            Text(
                text = "Please turn on your location",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun MapsPreview() {
    FaturasUATheme {
        MapsScreen()
    }
}*/
