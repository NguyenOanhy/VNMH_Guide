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
import coil.compose.rememberImagePainter
import com.example.vnmh.analyzer.QrCodeAnalyzer
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraView() {
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

        ScanBarcode(barcodeScanner::startScan, barcodeResults.value, hasCamPermission)
    }
}

@Composable
private fun ScanBarcode(
    onScanBarcode: suspend () -> Unit,
    barcodeValue: String?,
    hasCamPermission: Boolean
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