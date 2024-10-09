package com.example.mathapp.model

// Data class representing a fact about a number.
data class NumberFact(
    val text: String,
    val number: Int,
    val found: Boolean,
    val type: String
)
