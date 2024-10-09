package com.example.mathapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.model.ApiService
import com.example.mathapp.model.NumberFact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ViewModel for handling API requests and storing trivia facts.
class ApiViewModel : ViewModel() {
    // Mutable state flow to hold the current NumberFact data.
    private val _numberFact = MutableStateFlow<NumberFact>(NumberFact("Loading...", 0, false, ""))
    // Exposed state flow for external access to number facts.
    val numberFact: StateFlow<NumberFact> get() = _numberFact

    // Create an instance of the API service.
    private val apiService = ApiService.create()

    // Mutable state flow to indicate whether there was an error during the API call.
    private val _hasError = MutableStateFlow(false)
    // Exposed state flow for external access to error state.
    val hasError: StateFlow<Boolean> get() = _hasError

    // Function to fetch random trivia from the API.
    fun fetchRandomTrivia() {
        // Reset error state before making the API call.
        _hasError.value = false

        // Launch a coroutine to perform the network operation.
        viewModelScope.launch {
            apiService.getRandomTrivia().enqueue(object : Callback<NumberFact> {
                // Handle successful API response.
                override fun onResponse(call: Call<NumberFact>, response: Response<NumberFact>) {
                    if (response.isSuccessful) {
                        // Update the numberFact with the response data or a default message.
                        _numberFact.value = response.body() ?: NumberFact("No fact available.", 0, false, "")
                    } else {
                        // Log the error and update the state with a failure message.
                        Log.e("ApiViewModel", "Error: ${response.code()} - ${response.message()}")
                        _numberFact.value = NumberFact("Failed to retrieve fact.", 0, false, "")
                        _hasError.value = false // No error in this case as the call was made successfully.
                    }
                }

                // Handle failure of the API call.
                override fun onFailure(call: Call<NumberFact>, t: Throwable) {
                    // Log the failure and update the numberFact with a failure message.
                    Log.e("ApiViewModel", "Failure: ${t.message}", t)
                    _numberFact.value = NumberFact("Oops, failed to retrieve trivia", 0, false, "")
                    _hasError.value = true // Set error state to true on failure.
                }
            })
        }
    }
}
