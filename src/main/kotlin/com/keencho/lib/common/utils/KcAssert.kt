package com.keencho.lib.common.utils

class KcAssert {
    companion object {
        private val DEFAULT_EXCEPTION = RuntimeException()
        private val DEFAULT_EXCEPTION_CLASS = DEFAULT_EXCEPTION::class.java

        private fun throwException(message: String, exceptionClass: Class<out Exception /* = java.lang.Exception */>) {
            throw exceptionClass.getDeclaredConstructor(String::class.java).newInstance(message)
        }

        @JvmStatic
        fun isTrue(expression: Boolean, message: String, exceptionClass: Class<out Exception>) {
            if (!expression) {
                throwException(message, exceptionClass)
            }
        }

        @JvmStatic
        fun isTrue(expression: Boolean, message: String) {
            isTrue(expression, message, DEFAULT_EXCEPTION_CLASS)
        }

        @JvmStatic
        fun isFalse(expression: Boolean, message: String, exceptionClass: Class<out Exception>) {
            if (expression) {
                throwException(message, exceptionClass)
            }
        }

        @JvmStatic
        fun isFalse(expression: Boolean, message: String) {
            isFalse(expression, message, DEFAULT_EXCEPTION_CLASS)
        }

        @JvmStatic
        fun isNull(obj: Any?, message: String, exceptionClass: Class<out Exception>) {
            if (obj != null) {
                throwException(message, exceptionClass)
            }
        }

        @JvmStatic
        fun isNull(expression: Boolean, message: String) {
            isNull(expression, message, DEFAULT_EXCEPTION_CLASS)
        }

        @JvmStatic
        fun notNull(obj: Any?, message: String, exceptionClass: Class<out Exception>) {
            if (obj == null) {
                throwException(message, exceptionClass)
            }
        }

        @JvmStatic
        fun notNull(expression: Boolean, message: String) {
            isNull(expression, message, DEFAULT_EXCEPTION_CLASS)
        }

        @JvmStatic
        fun isEmpty(map: Map<Any, Any>?, message: String, exceptionClass: Class<out Exception>) {
            if (map != null && map.isEmpty()) {
                throwException(message, exceptionClass)
            }
        }

        @JvmStatic
        fun isEmpty(map: Map<Any, Any>?, message: String) {
            isEmpty(map, message, DEFAULT_EXCEPTION_CLASS)
        }

        @JvmStatic
        fun notEmpty(map: Map<Any, Any>?, message: String, exceptionClass: Class<out Exception>) {
            if (map == null || map.isEmpty()) {
                throwException(message, exceptionClass)
            }
        }

        @JvmStatic
        fun notEmpty(map: Map<Any, Any>?, message: String) {
            notEmpty(map, message, DEFAULT_EXCEPTION_CLASS)
        }

        @JvmStatic
        fun hasLength(text: String?, message: String, exceptionClass: Class<out Exception>) {
            if (!KcStringUtils.hasLength(text)) {
                throwException(message, exceptionClass)
            }
        }

        @JvmStatic
        fun hasLength(text: String?, message: String) {
            hasLength(text, message, DEFAULT_EXCEPTION_CLASS)
        }

        @JvmStatic
        fun hasText(text: String?, message: String, exceptionClass: Class<out Exception>) {
            if (!KcStringUtils.hasText(text)) {
                throwException(message, exceptionClass)
            }
        }

        @JvmStatic
        fun hasText(text: String?, message: String) {
            hasText(text, message, DEFAULT_EXCEPTION_CLASS)
        }
    }
}