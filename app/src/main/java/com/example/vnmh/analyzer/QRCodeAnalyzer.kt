package com.example.vnmh.analyzer

import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.flow.MutableStateFlow


class QrCodeAnalyzer(
    appContext: Context
) {
    private val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC)
        .enableAutoZoom()
        .build()

    private val scanner = GmsBarcodeScanning.getClient(appContext, options)
    val barCodeResults = MutableStateFlow<String?>(null)


    suspend fun startScan() {
        try {
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    barCodeResults.value = barcode.displayValue
                }

                .addOnCanceledListener {
                    barCodeResults.value = "Canceled"
                }

                .addOnFailureListener() {
                    barCodeResults.value = "Failed"
                }
        } catch (e: Exception) {

        }
    }
}



//import android.graphics.ImageFormat
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.ImageProxy
//import com.google.zxing.BarcodeFormat
//import com.google.zxing.BinaryBitmap
//import com.google.zxing.DecodeHintType
//import com.google.zxing.MultiFormatReader
//import com.google.zxing.PlanarYUVLuminanceSource
//import com.google.zxing.common.HybridBinarizer
//import java.nio.ByteBuffer
//
//
//class QrCodeAnalyzer(
//    private val onQrCodeScanned: (String) -> Unit
//): ImageAnalysis.Analyzer {
//
//    private val supportedImageFormats = listOf(
//        ImageFormat.YUV_420_888,
//        ImageFormat.YUV_422_888,
//        ImageFormat.YUV_444_888,
//    )
//
//    override fun analyze(image: ImageProxy) {
//        if(image.format in supportedImageFormats) {
//            val bytes = image.planes.first().buffer.toByteArray()
//            val source = PlanarYUVLuminanceSource(
//                bytes,
//                image.width,
//                image.height,
//                0,
//                0,
//                image.width,
//                image.height,
//                false
//            )
//            val binaryBmp = BinaryBitmap(HybridBinarizer(source))
//            try {
//                val result = MultiFormatReader().apply {
//                    setHints(
//                        mapOf(
//                            DecodeHintType.POSSIBLE_FORMATS to arrayListOf(
//                                BarcodeFormat.QR_CODE
//                            )
//                        )
//                    )
//                }.decode(binaryBmp)
//                onQrCodeScanned(result.text)
//            } catch(e: Exception) {
//                e.printStackTrace()
//            } finally {
//                image.close()
//            }
//        }
//    }
//
//    private fun ByteBuffer.toByteArray(): ByteArray {
//        rewind()
//        return ByteArray(remaining()).also {
//            get(it)
//        }
//    }
//}