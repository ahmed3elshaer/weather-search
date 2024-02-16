package com.ahmed3elshaer.weather.domain.exceptions

class CityNotFound : Exception() {
    override val message: String
        get() = "City not found"
}