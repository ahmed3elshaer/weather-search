package com.ahmed3elshaer.weather.domain

import androidx.core.text.isDigitsOnly
import com.ahmed3elshaer.weather.domain.exceptions.IncorrectCityName
import javax.inject.Inject

class ValidateSearchInputUseCase @Inject constructor(
) {
    operator fun invoke(input: String): Result<Boolean> {
        when {
            input.isBlank() -> return Result.failure(
                IncorrectCityName("Please enter a city name, e.g. London, Cairo, etc.")
            )

            input.isDigitsOnly() -> return Result.failure(
                IncorrectCityName("City name should not be a number")
            )

            input.length < 3 -> return Result.failure(
                IncorrectCityName("City name should be at least 3 characters")
            )

            input.length > 50 -> return Result.failure(
                IncorrectCityName("City name should be at most 50 characters")
            )

            input.contains(Regex("[^a-zA-Z]")) -> return Result.failure(
                IncorrectCityName("City name should contain only letters")
            )

            else -> return Result.success(true)
        }
    }
}