package com.secret.pk.argumentresolver

import com.secret.pk.aop.SecretPk
import com.secret.pk.secret.SecretEncryptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class SecretPkArgumentResolver(private val encryptor: SecretEncryptor) : HandlerMethodArgumentResolver {

    private val log: Logger = LoggerFactory.getLogger(SecretPkArgumentResolver::class.java)

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(SecretPk::class.java)
                && parameter.parameter.type == Long::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val param = webRequest.parameterMap[parameter.parameterName]?.firstOrNull()
        log.info("parameter.parameter: ${parameter.parameter}, parameter: $parameter, param: $param")
        param ?: throw IllegalStateException("param must not be null")
        return encryptor.decryptStringToLong(param)
    }
}