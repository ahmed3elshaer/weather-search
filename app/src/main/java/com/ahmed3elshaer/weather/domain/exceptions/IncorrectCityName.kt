package com.ahmed3elshaer.weather.domain.exceptions

class IncorrectCityName(private val reason: String) : Exception() {
    override val message: String
        get() = reason
}