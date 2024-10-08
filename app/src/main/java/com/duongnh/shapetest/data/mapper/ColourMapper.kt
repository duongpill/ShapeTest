package com.duongnh.shapetest.data.mapper

import com.duongnh.shapetest.data.data_source.remote.dto.ColourResponse
import com.duongnh.shapetest.domain.model.ColourItem

fun ColourResponse.toModel() = ColourItem(id, title, hex, imageUrl)