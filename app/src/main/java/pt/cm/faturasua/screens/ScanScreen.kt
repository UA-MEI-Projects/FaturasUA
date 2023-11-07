@file:OptIn(ExperimentalMaterial3Api::class)
package pt.cm.faturasua.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.core.resolutionselector.ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import pt.cm.faturasua.R
import pt.cm.faturasua.components.InvoiceCardDetailInfo
import pt.cm.faturasua.data.Invoice
import pt.cm.faturasua.utils.FirebaseUtil
import pt.cm.faturasua.utils.ParsingUtil
import pt.cm.faturasua.utils.QrCodeUtil
import pt.cm.faturasua.utils.ReceiptNotificationService
import pt.cm.faturasua.viewmodel.UserViewModel

@ExperimentalMaterial3Api
@Composable
fun ScanScreen(
    userViewModel: UserViewModel = viewModel(),
    firebaseUtil: FirebaseUtil
){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val notifsPermission = userViewModel.notifsOn.collectAsState().value

    val cameraProviderFuture = remember{
        ProcessCameraProvider.getInstance(context)
    }
    var showBottomSheet by remember{ // TODO: To delete
        mutableStateOf(false)
    }

    var qrCode by remember{
        mutableStateOf(Invoice())
    }
    val sheetState = rememberModalBottomSheetState()
    var hasCameraPermission by remember{
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val openAlertDialogScan = remember { mutableStateOf(true) }
    when {
        openAlertDialogScan.value -> {
            AlertDialogScan(
                onDismissRequest = { openAlertDialogScan.value = false },
                onConfirmation = { firebaseUtil.addReceiptToDB(qrCode) },
                dialogTitle = "Invoice scanned!",
                dialogContent = qrCode,
            )
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )
    val receiptNotificationService = remember{
        ReceiptNotificationService(context = context)
    }

    LaunchedEffect(key1 = true){
        launcher.launch(Manifest.permission.CAMERA)
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ){
        if(hasCameraPermission){
            AndroidView(factory = {context ->
                val previewView = PreviewView(context)
                val preview = Preview.Builder().build()
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                preview.setSurfaceProvider(previewView.surfaceProvider)

                val imageAnalysis = ImageAnalysis.Builder()
                    .setResolutionSelector(
                        ResolutionSelector.Builder()
                            .setResolutionStrategy(
                                ResolutionStrategy(Size(previewView.width, previewView.height), FALLBACK_RULE_CLOSEST_HIGHER)
                            ).build()
                    )
                    .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    QrCodeUtil{result ->
                            result.userId = userViewModel.profile.value.uid
                            result.title = userViewModel.profile.value.name
                            qrCode = result
                            scope.launch {
                                //sheetState.expand()
                                //showBottomSheet = true
                                openAlertDialogScan.value = true
                            }
                            imageAnalysis.clearAnalyzer()
                        }
                )
                try {
                    cameraProviderFuture.get().bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalysis
                    )
                }catch (e: Exception){
                    e.printStackTrace()
                }
                previewView
            },
                    modifier = Modifier.weight(1f)
            )
            if(showBottomSheet){
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                    modifier = Modifier.padding(20.dp)
                ){

                    
                }

            }

        }
    }
}

@Composable
fun AlertDialogScanContent(
    qrCode : Invoice
){
    var invoiceTitle by remember { mutableStateOf("") }
    var invoiceCategory by remember { mutableStateOf("") }

    Column {
        TextField(
            value = invoiceTitle,
            singleLine = true,
            //trailingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
            onValueChange = { newValue -> invoiceTitle = newValue },
            label = { Text("Invoice title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        )

        /*DropdownMenu(expanded = false, onDismissRequest = { /*TODO*/ }) {
            DropdownMenuItem(text = { "Invoice category" }, onClick = { /*TODO*/ })
        }*/

        Text(text = "Invoice details:")
        InvoiceCardDetailInfo(
            type = qrCode.type,
            category = qrCode.category,
            timestamp = qrCode.timestamp,
            number = qrCode.id,
            title = qrCode.title,
            amount = qrCode.amount.toDouble(),
            date = qrCode.date,
            nif = qrCode.businessNIF.toInt(),
            iva = qrCode.iva.toDouble(),
            status = qrCode.status
        )
    }
}

@Composable
fun AlertDialogScan(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogContent: Invoice,
    //icon: ImageVector,
) {
    AlertDialog(
        //containerColor = MaterialTheme.colorScheme.primaryContainer,
        //icon = { Icon(icon, contentDescription = "", tint = MaterialTheme.colorScheme.error) },
        title = { Text(text = dialogTitle) },
        text = { AlertDialogScanContent(qrCode = dialogContent) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = { TextButton(onClick = { onConfirmation() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)) { Text("Add invoice") } },
        dismissButton = { TextButton(onClick = { onDismissRequest() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer)) { Text("Discard invoice") } }
    )
}

