package com.duongnh.shapetest.domain.use_case

import com.duongnh.shapetest.domain.MyResult
import com.duongnh.shapetest.domain.model.ColourItem
import com.duongnh.shapetest.domain.repository.ColourRepository
import kotlinx.coroutines.flow.Flow

class GetRandomColorUseCase(private val colourRepository: ColourRepository) {

    suspend fun invoke(format: String): Flow<MyResult<ColourItem, String>> =
        colourRepository.getRandomColor(format)

}