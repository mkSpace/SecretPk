package com.secret.pk.controller

import com.secret.pk.aop.SecretPk
import com.secret.pk.aop.SecretPkPathVariable
import com.secret.pk.aop.SecretTarget
import com.secret.pk.common.Response
import com.secret.pk.service.SampleService
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/sample")
@RestController
@SecretTarget
class SampleController(private val sampleService: SampleService) {

    @PostMapping
    fun create(@RequestBody request: SampleRequest): Response<Any> {
        return Response.success(sampleService.create(request.email, request.nickname))
    }

    @GetMapping("/get")
    fun get1(@SecretPk id: Long): Response<Any> {
        return Response.success(data = sampleService.getById(id))
    }

    @GetMapping("/get/{id}")
    fun get2(@SecretPkPathVariable("id") id: Long): Response<Any> {
        return Response.success(data = sampleService.getById(id))
    }
}