package com.example.drawingapp

import android.content.ContentValues
import android.graphics.*
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawingView)

        // Color buttons
        findViewById<Button>(R.id.redBtn).setOnClickListener { drawingView.setColor(Color.RED) }
        findViewById<Button>(R.id.blueBtn).setOnClickListener { drawingView.setColor(Color.BLUE) }
        findViewById<Button>(R.id.greenBtn).setOnClickListener { drawingView.setColor(Color.GREEN) }
        findViewById<Button>(R.id.blackBtn).setOnClickListener { drawingView.setColor(Color.BLACK) }

        // Brush sizes
        findViewById<Button>(R.id.smallBtn).setOnClickListener { drawingView.setBrushSize(5f) }
        findViewById<Button>(R.id.mediumBtn).setOnClickListener { drawingView.setBrushSize(15f) }
        findViewById<Button>(R.id.largeBtn).setOnClickListener { drawingView.setBrushSize(30f) }

        // Eraser
        findViewById<Button>(R.id.eraserBtn).setOnClickListener { drawingView.setColor(Color.WHITE) }

        // Clear
        findViewById<Button>(R.id.clearBtn).setOnClickListener { drawingView.clearCanvas() }

        // Save
        findViewById<Button>(R.id.saveBtn).setOnClickListener { saveDrawing() }
    }

    private fun saveDrawing() {
        val bitmap = drawingView.getBitmap()

        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "Drawing_${System.currentTimeMillis()}.png")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/DrawingApp")

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        uri?.let {
            val stream: OutputStream? = contentResolver.openOutputStream(it)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream?.close()
            Toast.makeText(this, "Saved to Gallery!", Toast.LENGTH_SHORT).show()
        }
    }
}
