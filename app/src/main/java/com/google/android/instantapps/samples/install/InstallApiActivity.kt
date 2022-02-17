/*
 * Copyright (C) 2019 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.instantapps.samples.install

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.instantapps.InstantApps
import java.util.*

/**
 * An Activity that shows usage of the Install API, [documented here](https://developer.android.com/topic/instant-apps/reference.html#showinstallprompt).
 *
 * This sample does not have an installed app counterpart from the Play Store.
 * To see how this sample works, follow the instructions in the accompanying README.md
 */
class InstallApiActivity : AppCompatActivity() {

    /**
     * Intent to launch after the app has been installed.
     */
    private val postInstallIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://install-api.instantappsample.com/")
    ).addCategory(Intent.CATEGORY_BROWSABLE).putExtras(Bundle().apply {
        putString("The key to", "sending data via intent")
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_install)
        findViewById<Button>(R.id.start_nfc_scan).apply {
                setOnClickListener {
                    val intent = Intent(applicationContext, NfcScannerActivity::class.java)
                    startActivity(intent)
                }
        }
    }
}
