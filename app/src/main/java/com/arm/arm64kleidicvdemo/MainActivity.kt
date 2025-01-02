package com.arm.arm64kleidicvdemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.arm.arm64kleidicvdemo.databinding.ActivityMainBinding
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var imageProcessor: ImageProcessor

    private var originalMat: Mat? = null
    private var currentBitmap: Bitmap? = null

    companion object {
        private const val REPETITIONS = 500
        private const val TEST_IMAGE = "img.png"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        initializeOpenCV()
        setupListeners()
    }

    private fun setupUI() {
        enableEdgeToEdge()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setupSpinner()
    }

    private fun setupSpinner() {
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ImageOperation.values().map { it.displayName }
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            viewBinding.spinnerOperation.adapter = adapter
        }
    }

    private fun initializeOpenCV() {
        if (!OpenCVLoader.initLocal()) {
            showToast("Unable to load OpenCV")
        }
        imageProcessor = ImageProcessor()
    }

    private fun setupListeners() {
        with(viewBinding) {
            buttonLoadImage.setOnClickListener { loadImage() }
            buttonProcess.setOnClickListener { processImage() }
        }
    }

    private fun loadImage() {
        try {
            assets.open(TEST_IMAGE).use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                displayAndStoreBitmap(bitmap)
                convertBitmapToMat(bitmap)
            }
        } catch (e: Exception) {
            showToast("Error loading image: ${e.message}")
        }
    }

    private fun displayAndStoreBitmap(bitmap: Bitmap) {
        currentBitmap = bitmap
        viewBinding.imageView.setImageBitmap(bitmap)
    }

    private fun convertBitmapToMat(bitmap: Bitmap) {
        originalMat = Mat(bitmap.height, bitmap.width, CvType.CV_8UC3).also { mat ->
            bitmap.copy(Bitmap.Config.ARGB_8888, true).let { tempBitmap ->
                Utils.bitmapToMat(tempBitmap, mat)
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2BGR)
            }
        }
    }

    private fun processImage() {
        if (originalMat == null) {
            showToast("Load an image first!")
            return
        }

        val selectedOperationName = viewBinding.spinnerOperation.selectedItem.toString()
        val operation = ImageOperation.fromDisplayName(selectedOperationName)
            ?: run {
                showToast("Invalid operation selected")
                return
            }

        val processedMat = Mat()
        val durations = mutableListOf<Long>()

        repeat(REPETITIONS) {
            originalMat?.copyTo(processedMat)
            val duration = measureOperationTime {
                imageProcessor.applyOperation(processedMat, operation)
            }
            durations.add(duration)
        }

        val metrics = PerformanceMetrics(durations)
        viewBinding.textViewTime.text = metrics.toString()
        displayProcessedImage(processedMat)
    }

    private fun measureOperationTime(block: () -> Unit): Long {
        val start = System.nanoTime()
        block()
        return System.nanoTime() - start
    }

    private fun displayProcessedImage(processedMat: Mat) {
        Imgproc.cvtColor(processedMat, processedMat, Imgproc.COLOR_BGR2RGBA)
        val resultBitmap = Bitmap.createBitmap(
            processedMat.cols(),
            processedMat.rows(),
            Bitmap.Config.ARGB_8888
        )
        Utils.matToBitmap(processedMat, resultBitmap)
        viewBinding.imageView.setImageBitmap(resultBitmap)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}