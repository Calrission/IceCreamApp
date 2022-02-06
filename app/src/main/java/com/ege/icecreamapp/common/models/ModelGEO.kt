package com.ege.icecreamapp.common.models

data class ModelGEO(
    val bbox: List<Double>,
    val features: List<Feature>,
    val metadata: Metadata,
    val type: String
)