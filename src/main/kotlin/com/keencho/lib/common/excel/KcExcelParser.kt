package com.keencho.lib.common.excel

import com.keencho.lib.common.excel.annotation.KcExcelColumn
import com.keencho.lib.common.excel.annotation.KcExcelDocument
import com.keencho.lib.common.utils.KcAssert
import com.keencho.lib.common.utils.KcReflectionUtils
import com.keencho.lib.common.utils.KcStringUtils
import org.apache.commons.io.FilenameUtils
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.util.stream.Collectors
import java.util.stream.IntStream

class KcExcelParser<T> (
    private val file: File,
    private val clazz: Class<T>,
    private var startRowNum: Int
) {

    private var workbook: Workbook? = null
    private lateinit var sheet: Sheet
    private val columnNameIndex: HashMap<String, Short> = HashMap()
    private var lastCellNum: Short = -1
    private var isLinkedHashMap: Boolean = false

    init {
        init()
    }

    private fun init() {
        // Map 타입 객체는 LinkedHashMap만 지원함.
        if (clazz.isAssignableFrom(LinkedHashMap::class.java)) {
            isLinkedHashMap = true
            KcAssert.isTrue(startRowNum == 0, "start row must be 0 when return type is LinkedHashMap")
        } else {
            if (startRowNum == 0) startRowNum = 1
            isLinkedHashMap = false
            KcAssert.isTrue(clazz.getAnnotation(KcExcelDocument::class.java) != null, "KcExcelDocument annotation must not be null!")
        }

        val extension = FilenameUtils.getExtension(file.name)
        val inputStream = FileInputStream(file)

        if ("xls" == extension.lowercase()) {
            workbook = HSSFWorkbook(inputStream)
        } else if ("xlsx" == extension.lowercase()) {
            workbook = XSSFWorkbook(inputStream)
        }

        KcAssert.isTrue(workbook != null, "Unsupported excel file extension!")

        sheet = workbook!!.getSheetAt(0)
    }

    fun parse(): List<T> {
        val result = ArrayList<T>()

        lateinit var tempLinkedHashMapHeader: List<String>
        while(true) {
            val row = sheet.getRow(startRowNum) ?: break

            if (IntStream.range(row.firstCellNum.toInt(), row.lastCellNum.toInt()).noneMatch { idx ->
                    row.getCell(idx) != null &&
                    KcStringUtils.hasText(row.getCell(idx).toString()) &&
                    KcStringUtils.hasText(row.getCell(idx).toString().trim())
            }) {
                break
            }

            if (isLinkedHashMap) {
                if (startRowNum == 0) {
                    tempLinkedHashMapHeader = IntStream.range(0, getColumnCount().toInt()).mapToObj{ row.getCell(it).stringCellValue }.collect(Collectors.toList())
                    startRowNum = 1
                    continue
                }
                result.add(parseToMapData(row, tempLinkedHashMapHeader))
            } else {
                result.add(parseToData())
            }
            startRowNum ++
        }

        return result
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    private fun parseToData(): T {
        val result: T = KcReflectionUtils.newInstance(clazz)
        val fieldList = clazz.declaredFields

        for (field in fieldList) {
            if (field.getAnnotation(KcExcelColumn::class.java) == null) {
                continue
            }

            val headerName = field.getAnnotation(KcExcelColumn::class.java).headerName
            val value = getStringValue(startRowNum, headerName)

            field.trySetAccessible()
            field.set(result, value)
        }

        return result
    }

    private fun parseToMapData(row: Row, header: List<String>): T {
        val result: T = KcReflectionUtils.newInstance(clazz)

        for (idx in 1..getColumnCount()) {
            val i = idx - 1
            (result as LinkedHashMap<String, String>)[header[i]] = row.getCell(i).toString()
        }

        return result
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    private fun getStringValue(rowIndex: Int, columnName: String): String {
        return this.getStringValue(rowIndex, columnName, "")
    }

    private fun getStringValue(rowIndex: Int, columnName: String, defaultValue: String): String {
        val colIndex = getIndexByColumnName(columnName).toInt()
        val ret = getStringValueByIndex(rowIndex, colIndex)

        return if (!KcStringUtils.hasText(ret) && KcStringUtils.hasText(defaultValue)) {
            defaultValue
        } else ret!!

    }

    private fun getIndexByColumnName(columnName: String): Short {
        val parsedColumnName = columnName.trim { it <= ' ' }.replace("\\s".toRegex(), "")

        if (columnNameIndex.containsKey(parsedColumnName)) {
            return columnNameIndex[parsedColumnName]!!
        }

        for (i in 0..getColumnCount()) {
            val columnNameByIdx = this.getStringValueByIndex(0, i)?.replace("\\s".toRegex(), "")

            if (parsedColumnName == columnNameByIdx) {
                val intToShort = i.toShort()
                columnNameIndex[parsedColumnName] = intToShort
                return intToShort
            }
        }

        return -1
    }

    private fun getColumnCount(): Short {
        if (lastCellNum < 0) {
            lastCellNum = sheet.getRow(0).lastCellNum
        }
        return lastCellNum
    }

    private fun getStringValueByIndex(rowIndex: Int, colIndex: Int): String? {
        if (rowIndex < 0 || colIndex < 0) {
            return null
        }
        val cell = sheet.getRow(rowIndex).getCell(colIndex) ?: return null
        return cell.toString().trim { it <= ' ' }
    }
}