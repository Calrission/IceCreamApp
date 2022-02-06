package com.ege.icecreamapp.common.models

data class Properties(
    val segments: List<Segment>,
    val summary: Summary,
    val way_points: List<Int>
)