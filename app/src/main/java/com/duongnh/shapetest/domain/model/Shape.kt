package com.duongnh.shapetest.domain.model

data class Shape(
    val id: String,
    val type: ShapeType,
    val x: Float,
    val y: Float,
    val size: Float,
    var color: String?,
    var imageUrl: String?
) {
    constructor() : this("", ShapeType.SQUARE, 0f, 0f, 0f, null, null)
}

enum class ShapeType { SQUARE, CIRCLE, TRIANGLE }