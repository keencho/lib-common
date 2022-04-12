package com.keencho.lib.common.utils

import java.lang.reflect.Field
import java.util.*
import java.util.stream.Collectors
import kotlin.reflect.KClass

class KcReflectionUtils {
    companion object {

        @JvmStatic
        fun <T> listField(data: List<T>): List<Field> {
            return Arrays.stream(data
                .stream()
                .findFirst()
                .javaClass
                .declaredFields)
                .collect(Collectors.toList())
        }

        @JvmStatic
        fun <T> getValueByGetter(instance: T, field: Field): Any? {
            val clazz = field::class.java
            val method = clazz.getMethod("get" + KcStringUtils.capitalize(field.name))

            return method.invoke(instance, null)
        }

        @JvmStatic
        fun <T> newInstance(clazz: Class<out T>): T {
            return clazz.kotlin.java.getDeclaredConstructor().newInstance()
        }
    }
}