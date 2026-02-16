package com.example.bluetoothfiletransfer

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    private lateinit var statusTextView: TextView
    private lateinit var devicesTextView: TextView

    private val enableBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            updateBluetoothStatus()
        }

    private val filePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { sendFile(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val enableButton = findViewById<Button>(R.id.enableButton)
        val scanButton = findViewById<Button>(R.id.scanButton)
        val shareButton = findViewById<Button>(R.id.shareButton)

        statusTextView = findViewById(R.id.statusTextView)
        devicesTextView = findViewById(R.id.devicesTextView)

        // Auto request Bluetooth ON
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBluetoothLauncher.launch(enableIntent)
        }

        updateBluetoothStatus()

        enableButton.setOnClickListener {
            if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enableBluetoothLauncher.launch(enableIntent)
            } else {
                Toast.makeText(this, "Bluetooth already enabled", Toast.LENGTH_SHORT).show()
            }
        }

        scanButton.setOnClickListener {
            if (bluetoothAdapter?.isEnabled == true) {
                startScanningSimulation()
            } else {
                Toast.makeText(this, "Enable Bluetooth first", Toast.LENGTH_SHORT).show()
            }
        }

        shareButton.setOnClickListener {
            if (bluetoothAdapter?.isEnabled == true) {
                filePicker.launch("*/*")
            } else {
                Toast.makeText(this, "Enable Bluetooth first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateBluetoothStatus()
    }

    private fun updateBluetoothStatus() {
        if (bluetoothAdapter == null) {
            statusTextView.text = "Bluetooth not supported"
        } else if (bluetoothAdapter.isEnabled) {
            statusTextView.text = "Bluetooth Status: ON"
        } else {
            statusTextView.text = "Bluetooth Status: OFF"
        }
    }

    private fun startScanningSimulation() {
        devicesTextView.text = "Scanning for devices..."

        Handler(Looper.getMainLooper()).postDelayed({
            showPairedDevices()
        }, 2000)
    }

    private fun showPairedDevices() {
        val devices = bluetoothAdapter?.bondedDevices
        val list = StringBuilder()
        var count = 0

        devices?.forEach {
            list.append("• ").append(it.name).append("\n")
            count++
        }

        devicesTextView.text =
            if (count > 0) "Found $count Device(s):\n\n$list"
            else "No Paired Devices Found"
    }

    private fun sendFile(uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, "Select Device to Share"))
    }
}
