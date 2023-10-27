package pt.cm.faturasua.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.FaturasUATheme

@Composable
fun MapsScreen(){
    Text("Maps Screen")
}

@Preview(showBackground = true)
@Composable
fun MapsPreview() {
    FaturasUATheme {
        MapsScreen()
    }
}