package com.duongnh.shapetest.domain.repository

import com.duongnh.shapetest.domain.MyResult
import com.duongnh.shapetest.domain.model.ColourItem
import kotlinx.coroutines.flow.Flow

interface ColourRepository {

    suspend fun getRandomColor(format: String): Flow<MyResult<ColourItem, String>>

    suspend fun getRandomImageUrl(format: String): Flow<MyResult<ColourItem, String>>

}