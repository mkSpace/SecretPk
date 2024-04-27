package com.secret.pk.controller

import com.secret.pk.aop.SecretPk

data class SampleResponse(
    @field:SecretPk val id: Long,
    val email: String,
    val nickname: String
)

data class SampleRequest(
    val email: String,
    val nickname: String
)