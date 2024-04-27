package com.secret.pk.config

import com.secret.pk.argumentresolver.SecretPkArgumentResolver
import com.secret.pk.argumentresolver.SecretPkPathVariableArgumentResolver
import com.secret.pk.secret.SecretEncryptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configuration
class WebConfigurer(private val secretEncryptor: SecretEncryptor) : WebMvcConfigurer{

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.addAll(listOf(
            SecretPkArgumentResolver(secretEncryptor),
            SecretPkPathVariableArgumentResolver(secretEncryptor)
        ))
    }
}