package com.keencho.lib.common.excel

import com.keencho.lib.common.excel.annotation.KcExcelDocument
import com.keencho.lib.common.utils.KcAssert

class KcExcelDownloaderUtil<T> {

    enum class ExcelCellPosition { HEADER, BODY }

    fun validateData(data: Map<String, List<T>>) {
        data.forEach { (k, v) ->
            KcAssert.notNull(v, "value must not be null!")

            val clazz = v.stream().findFirst().javaClass;
            KcAssert.notNull(clazz, "excel target class must not be null!")
            KcAssert.isTrue(clazz.isAnnotationPresent(KcExcelDocument::class.java), "KcExcelDocument annotation must not be null!")
        }
    }
}

