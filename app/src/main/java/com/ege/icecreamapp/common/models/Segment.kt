package com.ege.icecreamapp.common.models

data class Segment(
    val distance: Double,
    val duration: Double,
    val steps: List<Step>
)