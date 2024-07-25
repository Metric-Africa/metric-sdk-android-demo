package com.metricsdktest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.metric.sdk.ui.sdklaucher.Reason
import com.metric.sdk.ui.sdklaucher.VerificationOutcome
import com.metric.sdk.ui.sdklaucher.legacy.MetricSdkContract
import com.metricsdktest.ui.theme.MetricSDKTestTheme

/**
 * @author Sam
 * Created 02/08/2023 at 10:56 pm
 */
class HomeFragment: Fragment() {

    private val metricSdkResultLauncher = registerForActivityResult(MetricSdkContract()) { outcome ->
        var resultText = ""
        when (outcome) {
            is VerificationOutcome.Failed -> {
                resultText = when (outcome.reason) {
                    Reason.LIVENESS_FAILED -> "LIVENESS_FAILED"
                    Reason.CANCELLED -> "CANCELLED"
                    Reason.INVALID_TOKEN -> "INVALID_TOKEN"
                    Reason.VERIFICATION_FAILED -> "VERIFICATION_FAILED"
                    Reason.UNAUTHORISED -> "UNAUTHORISED"
                    Reason.UNKNOWN -> "UNKNOWN"
                }
                Log.e("TAG", "verification failed $resultText")
            }

            is VerificationOutcome.Success -> {
                resultText = "success"
                Log.e("TAG", "verification succeeded $outcome")
            }
        }
        Toast.makeText(requireContext(), resultText, Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MetricSDKTestTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Home(
                            onLaunchSdk = this@HomeFragment::launchSdk
                        )
                    }
                }
            }
        }
    }

    private fun launchSdk(token: String) {
        if (token.isBlank()) return
        metricSdkResultLauncher.launch(token)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    onLaunchSdk: (String) -> Unit,
) {
    var token by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center),
        ) {
            TextField(
                value = token,
                onValueChange = { str ->
                    token = str
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onLaunchSdk(token) },
            ) {
                Text(text = "Launch SDK")
            }
        }
    }
}
