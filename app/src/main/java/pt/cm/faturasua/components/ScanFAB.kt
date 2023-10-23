package pt.cm.faturasua.components

import android.graphics.fonts.FontStyle
import android.util.Log
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.cm.faturasua.classes.ScanFABItemClass
import pt.cm.faturasua.classes.ScanFABState


@Composable
fun ScanFAB(
    modifier: Modifier = Modifier,
    navController: NavController,
    scanFABState: ScanFABState,
    onScanFabStateChange: (ScanFABState) -> Unit,
    items : List<ScanFABItemClass>){

    // put this in main screen
    var floatingActionButtonState by remember{
        mutableStateOf(ScanFABState.Collapsed)
    }

    val transition = updateTransition(targetState = scanFABState, label = "transition")

    val rotation by transition.animateFloat(label = "rotate") {
        if(it == ScanFABState.Expanded){
            315f
        }
        else{
            0f
        }
    }

    val itemsScale by transition.animateFloat {
        if(it == ScanFABState.Expanded) 36f else 0f
    }

    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = {tween(durationMillis = 50)}
    ) {
        if(it == ScanFABState.Expanded) 1f else 0f
    }

    val textShadow by transition.animateDp(
        label = "text shadow",
        transitionSpec = {tween(durationMillis = 50)}
    ) {
        if(it == ScanFABState.Expanded) 2.dp else 0.dp
    }
    Column(
        horizontalAlignment = Alignment.End
    ) {
        if(transition.currentState == ScanFABState.Expanded){
            items.forEach {
                ScanFABItem(
                    item = it,
                    onScanFabItemClick = {scanFABItem ->
                        when(scanFABItem.route){
                            ScanFABItemClass.Scan.route ->{
                                navController.navigate(scanFABItem.route)
                                Log.d("Navigation", "Navigate to Scan Screen")
                            }
                            ScanFABItemClass.AddImage.route -> {
                                Log.d("Navigation", "Navigate to Scan-Add Image Screen")
                            }
                        }
                    },
                    alpha = alpha,
                    textShadow = textShadow,
                    itemScale = itemsScale
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
        FloatingActionButton(
            onClick = {
                onScanFabStateChange (
                    if (transition.currentState == ScanFABState.Expanded) {
                        ScanFABState.Collapsed
                    } else {
                        ScanFABState.Expanded
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        ) {
            Icon(
                Icons.Filled.Add,
                "Floating action button",
                Modifier.rotate(rotation)
            )

        }
    }

}

@Composable
fun ScanFABItem(
    item : ScanFABItemClass,
    alpha: Float,
    textShadow: Dp,
    itemScale: Float,
    showLabel : Boolean = true,
    onScanFabItemClick : (ScanFABItemClass) -> Unit
){
    val buttonColors = MaterialTheme.colorScheme.secondary
    val shadow = Color.Black.copy(0.5f)
    Row {
       if(showLabel){
           Text(
               text = item.label,
               fontWeight = FontWeight.Bold,
               fontSize = 12.sp,
               modifier = Modifier
                   .alpha(
                       animateFloatAsState(
                           targetValue = alpha,
                           animationSpec = tween(50)
                       ).value
                   )
                   .shadow(textShadow)
                   .background(MaterialTheme.colorScheme.background)
                   .padding(start = 6.dp, top = 4.dp, end = 6.dp)
           )

           Spacer(modifier = Modifier.size(16.dp))
       }
        
        Canvas(
            modifier = Modifier.size(32.dp).clickable(
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(
                    bounded = true,
                    radius = 20.dp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                onClick = onScanFabItemClick.invoke(item)
            )
        ) {
            drawCircle(
                color = shadow,
                radius = itemScale,
                center = Offset(
                    center.x + 2f,
                    center.y + 2f
                )
            )

            drawCircle(
                color = buttonColors,
                radius = itemScale
            )

            drawImage(
                image = item.icon,
                topLeft = Offset(
                    center.x - (item.icon.width / 2),
                    center.y - (item.icon.width / 2)
                ),
                alpha = alpha
            )
        }
    }
}