package com.ege.icecreamapp

data class ModelIceCream(
    val title: String,
    val desc: String,
    val edition: String,
    val price: Float,
    val imgRes: Int,
    var countBuy: Int = 1
)
