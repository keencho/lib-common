package com.keencho.lib.common.utils

import java.util.stream.Collectors

class KcListUtils {
    companion object {

        @JvmStatic
        fun <T, V> findByValue(list: List<T>, key: V): List<T> {
            return list.stream().filter { it == key } .collect(Collectors.toList())
        }

        @JvmStatic
        fun <T, V> findOneByValue(list: List<T>, key: V): T? {
            val valueList = findByValue(list, key)

            if (valueList.isEmpty()) {
                return null
            }

            return findByValue(list, key).first()
        }

    }
}