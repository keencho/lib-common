package com.keencho.lib.common.utils

class KcStringUtils {
    companion object {
        private fun containsTxt(charSequence: CharSequence): Boolean {
            for (char in charSequence) {
                if (!Character.isWhitespace(char)) {
                    return true
                }
            }
            return false
        }

        @JvmStatic
        fun hasText(string: String?): Boolean {
            return string != null && string.isNotEmpty() && containsTxt(string)
        }

        @JvmStatic
        fun hasLength(string: String?): Boolean {
            return string != null && string.isNotEmpty()
        }

        @JvmStatic
        fun capitalize(str: String): String {
            if (str.isEmpty()) {
                return str
            }

            return str.substring(0, 1).uppercase() + str.substring(1)
        }
    }
}