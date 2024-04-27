package com.secret.pk.secret

import com.secret.pk.config.SecretPkProperty
import java.nio.ByteBuffer
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class SecretEncryptor(private val property: SecretPkProperty) {

    private val secretKey: SecretKey

    init {
        val keyBytes = property.secretKey.toByteArray()
        secretKey = SecretKeySpec(keyBytes, property.algorithm)
    }

    fun encryptLongToString(value: Long): String {
        val cipher = Cipher.getInstance(property.algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val byteBuffer = ByteBuffer.allocate(8)
        byteBuffer.putLong(value)
        val longBytes = byteBuffer.array()
        val encryptedBytes = cipher.doFinal(longBytes)
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun decryptStringToLong(encryptedString: String): Long {
        val cipher = Cipher.getInstance(property.algorithm)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)

        val encryptedBytes = Base64.getDecoder().decode(encryptedString)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return ByteBuffer.wrap(decryptedBytes).getLong()
    }
}