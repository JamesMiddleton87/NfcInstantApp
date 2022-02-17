package com.google.android.instantapps.samples.install

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.instantapps.samples.install.databinding.ActivityNfcScannerBinding
import java.util.*

class NfcScannerActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNfcScannerBinding

    private var nfcAdapter: NfcAdapter? = null
    private var mv: NfcViewModel = NfcViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNfcScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        Log.e("TEST", "IMAGE HERE ")
        val navController = findNavController(R.id.nav_host_fragment_content_nfc_scanner)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        findViewById<TextView>(R.id.textview_first).text = mv.getText()

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_nfc_scanner)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        var tagFromIntent: Tag? = intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        val nfc = NfcA.get(tagFromIntent)
        nfc.connect()
        val isConnected = nfc.isConnected()
        mv.setConnected(isConnected)

        if (isConnected) {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
                parseNdefMessage(intent)
            }

        } else {
            Log.e("ans", "Not connected")
        }

    }

    private fun parseNdefMessage(intent: Intent) {
        val ndefMessageArray = intent.getParcelableArrayExtra(
            NfcAdapter.EXTRA_NDEF_MESSAGES
        )
        // Test if there is actually a NDef message passed via the Intent
        if (ndefMessageArray != null) {
            val ndefMessage = ndefMessageArray[0] as NdefMessage
            //Get Bytes of payload
            val payloads = ndefMessage.records
            // Read First Byte and then trim off the right length
            var text = ""
            payloads.forEach { record ->
                text += "\n" + parseMultiMessages(record.payload)
            }
            Log.e("TEST", "IMAGE text: " + text)

            mv.setText(text)
            findViewById<TextView>(R.id.textview_first).text = mv.getText()
            Log.e("ans", "IS Connected data:" + mv.getText())
        }
    }

    private fun parseMultiMessages(payload: ByteArray): String {
        val textArray: ByteArray =
            Arrays.copyOfRange(payload, payload[0].toInt() + 1, payload.size)
        // Convert to Text
        val text = String(textArray, Charsets.UTF_8)

        Log.e("ans", "IS Connected data:" + text)

        return text
    }

    private fun enableForegroundDispatch(activity: ComponentActivity, adapter: NfcAdapter?) {
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()
        filters[0] = IntentFilter()
        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType("text/plain")
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException(ex)
            }
        }
        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    override fun onResume() {
        super.onResume()
        enableForegroundDispatch(this, this.nfcAdapter)
    }

    public override fun onPause() {
        super.onPause()
        enableForegroundDispatch(this, this.nfcAdapter)
    }




}