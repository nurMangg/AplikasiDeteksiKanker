package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.adaptermodel.ViewModelFactory
import com.dicoding.asclepius.view.adaptermodel.viewModel
import org.tensorflow.lite.task.vision.classifier.Classifications

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var historyResult : History? = null
    private lateinit var nviewModel: viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nviewModel = obtainViewModel(this@ResultActivity)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val imageString = intent.getStringExtra(IMAGE_URI)
        if (imageString != null) {
            val imageUri = Uri.parse(imageString)
            displayImage(imageUri)

            val imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        Log.d(TAG, "Error: $error")
                    }

                    override fun onResults(result: List<Classifications>?, interenceTime: Long) {
                        result?.let { showResult(it, imageString)}
                        nviewModel.insert(historyResult!!)
                    }
                }
            )
            imageClassifierHelper.classifyStaticImage(imageUri)

        } else {
            Log.e(TAG, "No Image Provided")
            finish()
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): viewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(viewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    private fun showResult(result: List<Classifications>, uri: String) {
        val topResult = result[0]
        val label = topResult.categories[0].label
        val score = topResult.categories[0].score

        fun Float.formatToString(): String {
            return String.format("%.2f%%", this * 100)
        }

        historyResult = History(
            uri = uri,
            label = label,
            confidence = score)

        binding.resultText.text = "$label ${score.formatToString()}"
    }

    private fun displayImage(uri: Uri) {
        Log.d(TAG, "Display Image: $uri")
        binding.resultImage.setImageURI(uri)
    }

    companion object {
        const val IMAGE_URI = "img_uri"
        const val TAG = "imagepPicker"
        const val RESULT_TEXT = "result_text"
        const val REQUEST_HISTORY_UPDATE = 1
    }
}