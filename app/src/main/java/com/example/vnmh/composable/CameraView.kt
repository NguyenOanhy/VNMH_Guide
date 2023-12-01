package com.example.vnmh.composable

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.vnmh.analyzer.QrCodeAnalyzer
import kotlinx.coroutines.launch
import java.net.URLEncoder


@Composable
fun CameraView(navController: NavController) {
    val context = LocalContext.current
    val barcodeScanner: QrCodeAnalyzer = QrCodeAnalyzer(context)
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            hasCamPermission = isGranted
        }
    )

    DisposableEffect(key1 = context) {
        if (!hasCamPermission) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        onDispose {}
    }

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        val barcodeResults = barcodeScanner.barCodeResults.collectAsStateWithLifecycle()

        ScanBarcode(barcodeScanner::startScan, barcodeResults.value, hasCamPermission, navController)
    }
}

@Composable
private fun ScanBarcode(
    onScanBarcode: suspend () -> Unit,
    barcodeValue: String?,
    hasCamPermission: Boolean,
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (hasCamPermission) {
            if (!barcodeValue.isNullOrBlank()) {
                if (barcodeValue.contains("fmp.")) {
                    val scope = rememberCoroutineScope()
                    var hasNavigated by remember { mutableStateOf(false) }

                    DisposableEffect(barcodeValue) {
                        if (!hasNavigated) {
                            val encodedItemId = URLEncoder.encode(barcodeValue, "UTF-8")
                            navController.navigate("collectionDetailView/${encodedItemId}") {
                                popUpTo("camera") {
                                    inclusive = true
                                }
                            }
                            hasNavigated = true
                        }
                        onDispose {}
                    }
                }
                if (barcodeValue.contains(".jpg") || barcodeValue.contains(".png") || barcodeValue.contains(".jpeg")) {
                    Image(
                        painter = rememberImagePainter(data = barcodeValue),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                    )
                } else {
                    Text(
                        text = "$barcodeValue",
                        modifier = Modifier
                            .padding(top = 270.dp)
                    )
                }
            } else {
                Text(
                    text = "Chưa quét mã QR",
                    modifier = Modifier
                        .padding(top = 270.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                onClick = {
                    scope.launch {
                        onScanBarcode()
                    }
                }
            ) {
                Text(
                    text = "Scan",
                    textAlign = TextAlign.Center,
                )
            }

        }
    }
}



//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.util.Size
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.selection.selectable
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.Text
//import androidx.compose.material3.rememberModalBottomSheetState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import com.example.vnmh.analyzer.QrCodeAnalyzer
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CameraView(){
//    var code by remember {
//        mutableStateOf("")
//    }
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val cameraProviderFuture = remember {
//        ProcessCameraProvider.getInstance(context)
//    }
//    var hasCamPermission by remember {
//        mutableStateOf(
//            ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED
//        )
//    }
//    val sheetState = rememberModalBottomSheetState()
//    var isSheetOpen by rememberSaveable {
//        mutableStateOf(false)
//    }
//    var showBottomSheet by remember { mutableStateOf(false) }
//    val scope = rememberCoroutineScope()
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { granted ->
//            hasCamPermission = granted
//        }
//    )
//    LaunchedEffect(key1 = true) {
//        launcher.launch(Manifest.permission.CAMERA)
//    }
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        if (hasCamPermission) {
//            AndroidView(
//                factory = { context ->
//                    val previewView = PreviewView(context)
//                    val preview = Preview.Builder().build()
//                    val selector = CameraSelector.Builder()
//                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                        .build()
//                    preview.setSurfaceProvider(previewView.surfaceProvider)
//                    val imageAnalysis = ImageAnalysis.Builder()
//                        .setTargetResolution(
//                            Size(
//                                previewView.width,
//                                previewView.height
//                            )
//                        )
//                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                        .build()
//                    imageAnalysis.setAnalyzer(
//                        ContextCompat.getMainExecutor(context),
//                        QrCodeAnalyzer { result ->
//                            code = result
//                            scope.launch {
//                                showBottomSheet = true
//                            }
//                        }
//                    )
//                    try {
//                        cameraProviderFuture.get().bindToLifecycle(
//                            lifecycleOwner,
//                            selector,
//                            preview,
//                            imageAnalysis
//                        )
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                    previewView
//                },
//                modifier = Modifier.fillMaxSize()
//            )
//            if (showBottomSheet) {
//                ModalBottomSheet(
//                    sheetState = sheetState,
//                    onDismissRequest = {
//                        isSheetOpen = false
//                    },
//                    content = {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp, 0.dp, 16.dp, 64.dp)
//                        ) {
//                            Text(
//                                text = code,
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.SemiBold,
//                                textDecoration = TextDecoration.Underline,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp)
//                                    .selectable(
//                                        selected = true,
//                                        onClick = {
//                                            val uri = Uri.parse(code)
//                                            val intent = Intent(Intent.ACTION_VIEW, uri)
//                                            context.startActivity(intent)
//                                        }
//                                    )
//                            )
//                        }
//                    }
//                )
//            }
//        }
//    }
//}