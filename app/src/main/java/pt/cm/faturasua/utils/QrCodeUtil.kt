package pt.cm.faturasua.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.net.Uri
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import pt.cm.faturasua.data.Invoice
import java.io.IOException
import java.nio.ByteBuffer
import java.util.Collections.addAll

class QrCodeUtil(
    private val onQrCodeScanned: (Invoice) -> Unit
): ImageAnalysis.Analyzer{

    private val supportedImageFormats = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888
    )

    val scanner = BarcodeScanning.getClient()

    fun analyze(binaryBitmap: BinaryBitmap){
        try {
            val result = MultiFormatReader().apply {
                setHints(
                    mapOf(
                        DecodeHintType.POSSIBLE_FORMATS to arrayListOf(
                            BarcodeFormat.QR_CODE
                        )
                    )
                )
            }.decode(binaryBitmap)
        }catch (e :Exception){
            e.printStackTrace()
        }
    }

    fun scan(inputImage: InputImage){
        scanner.process(inputImage)
            .addOnCompleteListener { barcodes ->
                for (barcode in barcodes.result) {
                    val rawValue = barcode.rawValue
                    val invoice  = ParsingUtil().parseQR(rawValue.toString())
                    if(invoice != null){
                        onQrCodeScanned(invoice)
                    }

                }
            }

    }

    fun analyze(bitmap : Bitmap){
        val image = InputImage.fromBitmap(bitmap, 0)

        scan(image)
    }

    fun analyze(image : Uri, context : Context){
        val inputImage: InputImage
        try {
            inputImage = InputImage.fromFilePath(context, image)
            scan(inputImage)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }



    override fun analyze(image: ImageProxy) {
        if(image.format in supportedImageFormats){
            /*val bytes = image.planes.first().buffer.toByteArray()

            val source = PlanarYUVLuminanceSource(
                bytes,
                image.width,
                image.height,
                0,
                0,
                image.width,
                image.height,
                false
            )

            val binaryBmp = BinaryBitmap(HybridBinarizer(source))
            analyze(binaryBmp)*/
            analyze(image.toBitmap())
            image.close()
        }
    }


    private fun ByteBuffer.toByteArray(): ByteArray{
        rewind()
        return ByteArray(remaining()).also {
            get(it)
        }
    }
}