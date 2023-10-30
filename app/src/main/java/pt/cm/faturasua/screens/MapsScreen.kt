package pt.cm.faturasua.screens

import android.bluetooth.BluetoothCsipSetCoordinator
import android.provider.ContactsContract.SearchSnippets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.FaturasUATheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapsScreen(
    pos : LatLng,
    title: String? = null,
    snippet: String? = null
){
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(pos, 20f)
    }

    val drinksMarkerState = rememberMarkerState(
        key = title,
        position = pos
    )


    GoogleMap(
        modifier = Modifier.fillMaxWidth()
            .padding(20.dp),
        cameraPositionState = cameraPositionState
    ){
        Marker(state = drinksMarkerState,
            title =  title,
            snippet = snippet
            )
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
