package com.duongnh.shapetest.presentation

import com.duongnh.shapetest.domain.model.Shape

data class MainUIState(
    val squares: List<Shape> = emptyList(),
    val circles: List<Shape> = emptyList(),
    val triangles: List<Shape> = emptyList(),
    val allShapes: List<Shape> = emptyList(),
    val shape: Shape = Shape(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)