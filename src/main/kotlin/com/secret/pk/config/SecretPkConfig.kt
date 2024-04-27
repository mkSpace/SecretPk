package com.secret.pk.config

import com.secret.pk.secret.SecretEncryptor
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SecretPkProperty::class)
class SecretPkConfig {

    @Bean
    fun secretEncryptor(property: SecretPkProperty): SecretEncryptor {
        return SecretEncryptor(property)
    }

}

@ConfigurationProperties(prefix = "secret.pk")
data class SecretPkProperty(val algorithm: String, val secretKey: String)