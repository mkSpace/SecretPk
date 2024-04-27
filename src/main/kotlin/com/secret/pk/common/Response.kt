package com.secret.pk.common

data class Response<T>(
    val isSuccess: Boolean,
    val data: T? = null,
    val errorResponse: ErrorResponse? = null
) {
    companion object {
        fun <T> success(data: T? = null): Response<T> {
            return Response(
                isSuccess = true,
                data = data
            )
        }

        fun success(): Response<Unit> {
            return Response(isSuccess = true)
        }
    }
}

data class ErrorResponse(
    val code: String,
    val detailMessage: String?
)