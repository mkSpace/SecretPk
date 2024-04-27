package com.secret.pk.argumentresolver

import com.secret.pk.aop.SecretPkPathVariable
import com.secret.pk.secret.SecretEncryptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.HandlerMapping

class SecretPkPathVariableArgumentResolver(private val encryptor: SecretEncryptor) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(SecretPkPathVariable::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val pathVariables = webRequest.getAttribute(
            HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,
            NativeWebRequest.SCOPE_REQUEST
        ) as Map<*, *>

        val paramName = parameter.parameterName ?: throw IllegalArgumentException("Parameter name is null")
        val param = pathVariables[paramName] as? String ?: throw IllegalArgumentException("Parameter name is null")
        return encryptor.decryptStringToLong(param)
    }
}