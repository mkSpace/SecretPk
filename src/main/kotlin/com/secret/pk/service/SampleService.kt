package com.secret.pk.service

import com.secret.pk.controller.SampleResponse
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class SampleService {

    private val userMap = ConcurrentHashMap<Long, SampleResponse>()

    fun create(email: String, nickname: String): SampleResponse {
        val nextId = (userMap.map { it.key }.maxOrNull() ?: -1) + 1
        userMap[nextId] = SampleResponse(nextId, email, nickname)
        return userMap[nextId]!!
    }

    fun getById(id: Long): SampleResponse {
        return userMap[id] ?: throw IllegalArgumentException("cannot find user, userId: $id")
    }
}