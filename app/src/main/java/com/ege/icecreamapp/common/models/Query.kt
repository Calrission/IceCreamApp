package com.ege.icecreamapp.common.models

data class Query(
    val coordinates: List<List<Double>>,
    val format: String,
    val profile: String
)