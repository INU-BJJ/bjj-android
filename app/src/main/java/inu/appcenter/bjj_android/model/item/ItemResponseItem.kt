package inu.appcenter.bjj_android.model.item

data class ItemResponseItem(
    val imageName: String,
    val isOwned: Boolean,
    val isWearing: Boolean,
    val itemId: Long,
    val itemLevel: String,
    val itemName: String,
    val itemType: String,
    val validPeriod: String
)