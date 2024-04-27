package com.secret.pk.aop

import com.fasterxml.jackson.databind.ObjectMapper
import com.secret.pk.common.Response
import com.secret.pk.secret.SecretEncryptor
import net.bytebuddy.ByteBuddy
import net.bytebuddy.description.modifier.Visibility
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

@Aspect
class SecretPkAspect(
    private val objectMapper: ObjectMapper,
    private val byteBuddy: ByteBuddy,
    private val encryptor: SecretEncryptor
) {

    private val log: Logger = LoggerFactory.getLogger(SecretPkAspect::class.java)

    @Around("execution(* com.secret.pk.controller..*(..)) && @target(com.secret.pk.aop.SecretTarget)")
    fun convert(joinPoint: ProceedingJoinPoint): Response<*> {
        log.info("=================Before Convert==============")
        val response = joinPoint.proceed() as Response<*>
        val data = response.data
        if (data == null || !response.isSuccess) return response
        val resultDataClassName = data.javaClass.name
        var dynamicClassBuilder = byteBuddy
            .subclass(Any::class.java)
            .name(resultDataClassName + SUFFIX_DYNAMIC_CLASS_NAME)
        val memberProperties = data::class.memberProperties
        if (!memberProperties.any { it.javaField?.annotations?.firstOrNull()?.annotationClass == SecretPk::class }) return response
        memberProperties.forEach { property ->
            val hasSecretPkAnnotation =
                property.javaField?.annotations?.firstOrNull()?.annotationClass == SecretPk::class
            dynamicClassBuilder = if (hasSecretPkAnnotation) {
                dynamicClassBuilder.defineField(property.name, String::class.java, Visibility.PUBLIC)
            } else {
                dynamicClassBuilder.defineField(
                    property.name,
                    property.returnType.jvmErasure.java,
                    Visibility.PUBLIC
                )
            }
        }
        val newClass = dynamicClassBuilder
            .make()
            .load(SecretPkAspect::class.java.classLoader)
            .loaded
        newClass.fields.forEach { field ->
            log.info("new class field: $field")
        }
        val newClassInstance = newClass.getDeclaredConstructor().newInstance()
        memberProperties.forEach { property ->
            val hasSecretPkAnnotation =
                property.javaField?.annotations?.firstOrNull()?.annotationClass == SecretPk::class
            if (hasSecretPkAnnotation) {
                val encrypted = (getPropertyValue(data, property.name) as? Long)
                    ?.let { encryptor.encryptLongToString(it) }
                    ?: throw IllegalStateException("@SecretPk fields must be Long")
                newClass.getField(property.name).set(newClassInstance, encrypted)
            } else {
                val value = getPropertyValue(data, property.name)
                newClass.getField(property.name).set(newClassInstance, value)
            }
        }
        log.info("Write newClass ${newClassInstance}")
        log.info("Write newClass with objectMApper ${objectMapper.writeValueAsString(newClassInstance)}")
        return Response(isSuccess = response.isSuccess, errorResponse = response.errorResponse, data = newClassInstance)
    }

    private fun getPropertyValue(instance: Any, propertyName: String): Any? {
        val kClass = instance::class
        val property = kClass.memberProperties.find { it.name == propertyName }
        return (property as? KProperty1<Any, *>)?.get(instance)
    }

    companion object {
        private const val SUFFIX_DYNAMIC_CLASS_NAME = "_S"
    }

}