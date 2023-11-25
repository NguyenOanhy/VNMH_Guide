package com.example.vnmh.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vnmh.data.remote.MuseumService
import com.example.vnmh.data.remote.dto.ARItem
import com.example.vnmh.data.remote.dto.MuseumItem
import kotlinx.coroutines.launch

class MuseumViewModel : ViewModel() {

    private val service = MuseumService.create()

    // LiveData to hold the museum data
    private val _museumData = MutableLiveData<List<MuseumItem>>()
    val museumData: LiveData<List<MuseumItem>> = _museumData

    private val _arUrl = MutableLiveData<List<ARItem>>()
    val arData: LiveData<List<ARItem>> = _arUrl


    // Function to fetch agriculture photographs data
    fun fetchAfterPhotographs() {
        viewModelScope.launch {
            try {
                val agricultureData = service.getAfterPhotography()
                _museumData.value = agricultureData
            } catch (e: Exception) {
                Log.e("DBG", "Error fetching data: ${e.message}")
            }
        }
    }

    // Function to fetch cities photographs data
    fun fetchBeforePhotograhs() {
        viewModelScope.launch {
            try {
                val citiesData = service.getBeforePhotography()
                _museumData.value = citiesData
            } catch (e: Exception) {
                Log.e("DBG", "Error fetching data: ${e.message}")
            }
        }
    }

    fun fetchUrlAR() {
        viewModelScope.launch {
            try {
                val urlData = service.getUrlAR()
                _arUrl.value = urlData
            } catch (e: Exception) {
                Log.e("DBG", "Error fetching data: ${e.message}")
            }
        }
    }
}
