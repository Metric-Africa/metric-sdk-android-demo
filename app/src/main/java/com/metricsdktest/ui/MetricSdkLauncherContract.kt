package com.metricsdktest.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.metric.sdk.init.Metric
import com.metric.sdk.ui.sdklaucher.VerificationOutcome

class MetricSdkLauncherContract: ActivityResultContract<String, VerificationOutcome?>() {
    override fun createIntent(context: Context, input: String): Intent {
        return Metric.createStartIntent(input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): VerificationOutcome? {
        return Metric.getResultsFromIntent(intent)
    }
}