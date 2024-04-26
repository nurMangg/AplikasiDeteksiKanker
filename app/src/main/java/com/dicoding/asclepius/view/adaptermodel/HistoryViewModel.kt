package com.dicoding.asclepius.view.adaptermodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.repo.HistoryRepository

class HistoryViewModel(application: Application) : ViewModel() {
    private val mAnalysisRepository: HistoryRepository = HistoryRepository(application)
    fun getAll(): LiveData<List<History>> = mAnalysisRepository.getAllNotes()
}