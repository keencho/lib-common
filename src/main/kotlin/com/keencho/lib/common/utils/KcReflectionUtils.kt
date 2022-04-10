package com.keencho.lib.common.utils

import java.lang.reflect.Field
import java.util.*
import java.util.stream.Collectors

class KcReflectionUtils{
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
    }
}