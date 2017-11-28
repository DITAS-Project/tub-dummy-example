package de.tub.api

class APIException(override val message: String?,val code:Int) : Exception(message) {
    override fun toString(): String {
        return "APIException".plus(" code: ").plus(code).plus(" ").plus(message)
    }
}