package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.adaptermodel.ViewModelFactory
import com.dicoding.asclepius.view.adaptermodel.HisViewModel
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.SimpleDateFormat
import java.util.Date

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var historyResult: History? = null
    private lateinit var nviewModelHis: HisViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)



        nviewModelHis = obtainViewModel(this@ResultActivity)

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
                        result?.let { showResult(it, imageString) }
                        nviewModelHis.insert(historyResult!!)
                        showToast("Success Save To History")
                    }
                }
            )
            imageClassifierHelper.classifyStaticImage(imageUri)

        } else {
            Log.e(TAG, "No Image Provided")
            finish()
        }

    }

    private fun showToast(mess: String) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): HisViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[HisViewModel::class.java]
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun showResult(result: List<Classifications>, uri: String) {
        val topResult = result[0]
        val label = topResult.categories[0].label
        val score = topResult.categories[0].score

        fun Float.formatToString(): String {
            return String.format("%.2f%%", this * 100)
        }

        val formatedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val formatedTime = SimpleDateFormat("HH:mm:ss").format(Date())
        val dateNow = "$formatedDate  $formatedTime"

        historyResult = History(
            uri = uri,
            label = label,
            confidence = score,
            dateGenerate = dateNow
        )

        binding.resultText.text = "$label ${score.formatToString()}"
    }


    private fun displayImage(uri: Uri) {
        Log.d(TAG, "Display Image: $uri")
        binding.resultImage.setImageURI(uri)
    }

    companion object {
        const val IMAGE_URI = "img_uri"
        const val TAG = "imagepPicker"

    }
}