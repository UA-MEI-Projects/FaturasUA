@file:OptIn(ExperimentalMaterial3Api::class)
package pt.cm.faturasua.screens

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import pt.cm.faturasua.utils.PreferencesManager
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

    val openAlertDialogScan = remember { mutableStateOf(false) }
    when {
        openAlertDialogScan.value -> {
            AlertDialogScan(
                onDismissRequest = { openAlertDialogScan.value = false },
                onConfirmation = {
                    firebaseUtil.addReceiptToDB(qrCode)
                    openAlertDialogScan.value = false
                },
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
                            result.title = "Invoice by ${userViewModel.profile.value.name}"
                            result.category = "No category provided yet"
                            qrCode = result
                            scope.launch {
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
        }
    }
}

@Composable
fun AlertDialogScanContent(
    qrCode : Invoice
){
    var invoiceTitle by remember { mutableStateOf("") }

    Column {
        TextField(
            value = invoiceTitle,
            singleLine = true,
            onValueChange = { newValue -> invoiceTitle = newValue ; qrCode.title = newValue },
            label = { Text("Invoice title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        val optionsList : List<String> = listOf(stringResource(R.string.dashboard_category_general_expenses), stringResource(R.string.dashboard_category_meals), stringResource(R.string.dashboard_category_education), stringResource(R.string.dashboard_category_health), stringResource(R.string.dashboard_category_property))
        DropdownMenuCategories(
            options = optionsList,
            selected = "",
            label = "Invoice category",
            onOptionChange = { selectedOption: String ->
                qrCode.category = when (selectedOption) {
                    optionsList[0] -> "GE"
                    optionsList[1] -> "M"
                    optionsList[2] -> "E"
                    optionsList[3] -> "H"
                    optionsList[4] -> "P"
                    else -> selectedOption
                }
            }
        )

        Text(
            text = "Invoice details:",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
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
) {
    AlertDialog(
        title = { Text(text = dialogTitle) },
        text = { AlertDialogScanContent(qrCode = dialogContent) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = { TextButton(onClick = { onConfirmation() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)) { Text("Add invoice") } },
        dismissButton = { TextButton(onClick = { onDismissRequest() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer)) { Text("Discard invoice") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuCategories(
    options : List<String>,
    selected : String,
    label: String,
    onOptionChange : (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(selected) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedLabelColor = Color.DarkGray,
                    focusedLabelColor = Color.DarkGray,
                ),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                            onOptionChange(selectionOption)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

