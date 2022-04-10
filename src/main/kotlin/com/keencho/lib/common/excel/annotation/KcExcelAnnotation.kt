package com.keencho.lib.common.excel.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class KcExcelDocument(
    val headerHeight: Int = 25,
    val bodyHeight: Int = 20
)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class KcExcelColumn (
    val headerName: String = "Unknown"
)