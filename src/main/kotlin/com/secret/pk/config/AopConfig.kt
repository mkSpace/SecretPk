package com.secret.pk.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.secret.pk.aop.SecretPkAspect
import com.secret.pk.secret.SecretEncryptor
import net.bytebuddy.ByteBuddy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AopConfig {

    @Bean
    fun secretPkAspect(objectMapper: ObjectMapper, secretEncryptor: SecretEncryptor): SecretPkAspect {
        return SecretPkAspect(objectMapper, byteBuddy(), secretEncryptor)
    }

    @Bean
    fun byteBuddy(): ByteBuddy {
        return ByteBuddy()
    }
}