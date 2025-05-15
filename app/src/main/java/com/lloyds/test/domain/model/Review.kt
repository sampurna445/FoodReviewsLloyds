package com.lloyds.test.domain.model

data class Review(
    val id: String,
    val product: String,
    val manufacturer: String,
    val category: String,
    val rating: Double,
    val dateReleased: String,
    val videoCode: String,
    val videoTitle: String
) 