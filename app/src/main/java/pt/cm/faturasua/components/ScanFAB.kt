package pt.cm.faturasua.components

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import kotlinx.coroutines.launch
import pt.cm.faturasua.classes.ScanFABItemClass
import pt.cm.faturasua.classes.ScanFABState
import pt.cm.faturasua.data.Invoice
import pt.cm.faturasua.screens.AlertDialogScan
import pt.cm.faturasua.utils.FirebaseUtil
import pt.cm.faturasua.utils.QrCodeUtil
import pt.cm.faturasua.utils.ReceiptNotificationService
import pt.cm.faturasua.viewmodel.UserViewModel


@Composable
fun ScanFAB(
    modifier: Modifier = Modifier,
    context: Context,
    firebaseUtil : FirebaseUtil,
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    scanFABState: ScanFABState,
    onScanFabStateChange: (ScanFABState) -> Unit
){
    val scope = rememberCoroutineScope()

    val items = listOf(
        ScanFABItemClass.Scan,
        ScanFABItemClass.AddImage
    )

    val receiptNotificationService = remember{
        ReceiptNotificationService(context = context)
    }

    var qrCode by remember{
        mutableStateOf(Invoice())
    }

    val openAlertDialogScan = remember { mutableStateOf(false) }
    when {
        openAlertDialogScan.value -> {
            AlertDialogScan(
                onDismissRequest = { openAlertDialogScan.value = false },
                onConfirmation = {
                    firebaseUtil.addReceiptToDB(qrCode)
                    openAlertDialogScan.value = false
                },
                dialogTitle = "Invoice uploaded!",
                dialogContent = qrCode,
            )
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {

                val qrCodeUtil = QrCodeUtil { result ->
                    result.userId = userViewModel.profile.value.uid
                    result.title = "Invoice by ${userViewModel.profile.value.name}"
                    result.category = "No category provided yet"
                    qrCode = result
                    scope.launch {
                        openAlertDialogScan.value = true
                    }
                    if (userViewModel.notifsOn.value){
                        receiptNotificationService.sendReceiptAddedNotification()
                    }
                }
                qrCodeUtil.analyze(uri, context.applicationContext)

                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    )

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
                                navController.navigate(ScanFABItemClass.Scan.route)
                                Log.d("Navigation", "Navigate to Scan Screen")
                            }
                            ScanFABItemClass.AddImage.route -> {
                                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
                scope.launch {
                    onScanFabStateChange (
                        if (transition.currentState == ScanFABState.Expanded) {
                            ScanFABState.Collapsed
                        } else {
                            ScanFABState.Expanded
                        }
                    )
                }

            },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimary,
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
    Row {
       if(showLabel){
           Text(
               text = item.label,
               fontWeight = FontWeight.Bold,
               fontSize = 13.sp,
               modifier = Modifier
                   .alpha(
                       animateFloatAsState(
                           targetValue = alpha,
                           animationSpec = tween(50)
                       ).value
                   )
                   .shadow(textShadow)
                   .background(MaterialTheme.colorScheme.background)
                   .padding(10.dp)
           )

           Spacer(modifier = Modifier.size(16.dp))
       }

        Box(
            modifier = Modifier
                .size(30.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple(
                        bounded = true,
                        radius = 20.dp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = { onScanFabItemClick.invoke(item) }
                )
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    color = Color.White,
                    radius = size.minDimension - 20,
                    center = center
                )
            }

            Image(
                painter = painterResource(id = item.iconId),
                contentDescription = item.label,
                modifier = Modifier
                    .size(30.dp)
            )
        }
    }
}