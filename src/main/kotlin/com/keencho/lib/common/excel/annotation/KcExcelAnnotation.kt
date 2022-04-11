package com.keencho.lib.common.excel.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class KcExcelDocument

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class KcExcelColumn (
    val headerName: String = "Unknown"
)