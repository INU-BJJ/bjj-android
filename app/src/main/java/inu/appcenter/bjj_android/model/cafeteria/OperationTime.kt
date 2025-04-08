package inu.appcenter.bjj_android.model.cafeteria


data class OperationTime(
    val operation: String,
    val weekdays: List<String>,
    val weekends: List<String>
)