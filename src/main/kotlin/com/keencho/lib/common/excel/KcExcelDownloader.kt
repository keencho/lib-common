package com.keencho.lib.common.excel

import com.keencho.lib.common.excel.annotation.KcExcelColumn
import com.keencho.lib.common.excel.annotation.KcExcelDocument
import com.keencho.lib.common.utils.KcAssert
import com.keencho.lib.common.utils.KcReflectionUtils
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.util.stream.Collectors

class KcExcelDownloader<T> (
    private val data: LinkedHashMap<String, List<T>>,
    private val showSequence: Boolean
) {

    constructor(data: LinkedHashMap<String, List<T>>): this(data, false)

    private val workbook: SXSSFWorkbook = SXSSFWorkbook()

    private fun validateData(data: Map<String, List<T>>) {
        data.forEach { (_, v) ->
            KcAssert.notNull(v, "value must not be null!")

            val clazz = v.stream().findFirst().javaClass;
            KcAssert.notNull(clazz, "excel target class must not be null!")
            KcAssert.isTrue(clazz.isAnnotationPresent(KcExcelDocument::class.java), "KcExcelDocument annotation must not be null!")
        }
    }

    fun write() {
        this.validateData(data)

        var sheetNo = 0
        for (key in this.data.keys) {
            val sheet = workbook.createSheet()

            // 위에서 이미 not null 체크를 했으므로.
            val value = this.data[key]!!
            val fieldList = KcReflectionUtils.listField(value).stream().filter{ f -> f.getAnnotation(KcExcelDocument::class.java) != null }.collect(Collectors.toList())
            this.workbook.setSheetName(sheetNo++, key)

            var rowCount = 0
            var columnCount = 0
            var row = sheet.createRow(rowCount++)
            var count = 1

            ////////////////////////// '번호' 컬럼 세팅 //////////////////////////
            // TODO: 이거 필요할까? 그냥 클래스에서 바로 꽂는게 더 깔끔하지 않을까?
            if (this.showSequence) {
                val cell = row.createCell(0)
                cell.setCellValue("번호")
                columnCount = 1
            }
            ////////////////////////// '번호' 컬럼 세팅 끝 //////////////////////////

            ////////////////////////// 헤더 세팅 //////////////////////////
            for (headerField in fieldList) {
                val cell = row.createCell(columnCount++)
                cell.setCellValue(headerField.getAnnotation(KcExcelColumn::class.java).headerName)
            }
            ////////////////////////// 헤더 세팅 끝 //////////////////////////

            ////////////////////////// 필드 값 세팅 //////////////////////////
            for(genericData in value) {
                row = sheet.createRow(rowCount ++)
                columnCount = 0

                if (showSequence) {
                    val cell = row.createCell(0)
                    cell.setCellValue(count++.toDouble())
                    columnCount = 1
                }

                for(rowField in fieldList) {
                    val cell = row.createCell(columnCount)
                    val obj = KcReflectionUtils.getValueByGetter(genericData, rowField) ?: continue

                    if (obj is String) {
                        cell.setCellValue(obj.toString())
                    } else if (obj is Double) {
                        cell.setCellValue(obj.toDouble())
                    }

                    columnCount++
                }
            }
            ////////////////////////// 필드 값 세팅 끝 //////////////////////////
        }

    }

}
