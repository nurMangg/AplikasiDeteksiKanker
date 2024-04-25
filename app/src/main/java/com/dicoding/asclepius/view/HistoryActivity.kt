package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.view.adaptermodel.HistoryViewModel
import com.dicoding.asclepius.view.adaptermodel.ViewModelFactory

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        historyViewModel = obtainViewModel(this@HistoryActivity)

        historyViewModel.getAll().observe(this){
            showRecyclerView(it as ArrayList<History>)
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }

    private fun showRecyclerView(historyList : ArrayList<History>){
        val layoutManager = LinearLayoutManager(this)
        binding.historyRv.layoutManager = layoutManager
        binding.historyRv.setHasFixedSize(true)
        val adapter = HistoryAdapter(historyList)
        binding.historyRv.adapter = adapter
    }
}