package pt.cm.faturasua.data

data class Receipt(
    val seller : String,
    val receiptNumber : String,
    val tax : Double,
    val total : Double
)
