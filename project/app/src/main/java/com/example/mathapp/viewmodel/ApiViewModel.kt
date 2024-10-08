package com.example.mathapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.model.ApiService
import com.example.mathapp.model.NumberFact
import com.example.mathapp.ui.theme.CustomButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiViewModel : ViewModel() {
    private val _numberFact = MutableStateFlow<NumberFact>(NumberFact("Loading...", 0, false, ""))
    val numberFact: StateFlow<NumberFact> get() = _numberFact

    private val apiService = ApiService.create()
    private val _hasError = MutableStateFlow(false)
    val hasError: StateFlow<Boolean> get() = _hasError

    fun fetchRandomTrivia() {
        viewModelScope.launch {
            apiService.getRandomTrivia().enqueue(object : Callback<NumberFact> {
                override fun onResponse(call: Call<NumberFact>, response: Response<NumberFact>) {
                    if (response.isSuccessful) {
                        _numberFact.value = response.body() ?: NumberFact("No fact available.", 0, false, "")
                    } else {
                        Log.e("ApiViewModel", "Error: ${response.code()} - ${response.message()}")
                        _numberFact.value = NumberFact("Failed to retrieve fact.", 0, false, "")
                        _hasError.value = false
                    }
                }

                override fun onFailure(call: Call<NumberFact>, t: Throwable) {
                    Log.e("ApiViewModel", "Failure: ${t.message}", t)
                    _numberFact.value = NumberFact("Hups, failed to retrive trivia", 0, false, "")
                    _hasError.value = true
                }
            })
        }
    }
}
