package com.secret.pk.aop

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class SecretPkPathVariable(val name: String = "", val value: String = "", val required: Boolean = true)