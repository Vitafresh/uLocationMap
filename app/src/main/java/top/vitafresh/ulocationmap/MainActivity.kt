package top.vitafresh.ulocationmap

import android.content.Context
import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import top.vitafresh.ulocationmap.ui.theme.ULocationMapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: LocationViewModel = viewModel()
            ULocationMapTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp()
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }
}

@Composable
fun MyApp(viewModel: LocationViewModel) {
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    LocationDisplay(
        locationUtils = locationUtils,
        viewModel = viewModel,
        context = context
    )
}

@Composable
fun LocationDisplay(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    context: Context
) {
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                // Update Location
            } else {
                // Ask for permission
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if (rationaleRequired) {
                    Toast.makeText(
                        context,
                        "Location permission required",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Location permission denied. Enable it in Android settings",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Location not available")
        Button(
            onClick = {
                if (locationUtils.hasLocationPermission(context)) {
                    // Update Location
                } else {
                    //Request Location permission
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
        ) {
            Text(text = "Get Location")

        }
    }
}