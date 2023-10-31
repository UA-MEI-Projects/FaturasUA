package pt.cm.faturasua.data

import java.time.LocalDateTime

data class Invoice(
    val id : String,                                        // Invoice number and id
    val timestamp: String = LocalDateTime.now().toString(), // Scan datetime
    val type : String,                                      // Invoice type
    val businessNIF : String,                               // Business tax number
    val customerNIF : String,                               // Customer/user tax number
    val date : String,                                      // Invoice date
    val iva : String,                                       // Tax costs
    val amount : String,                                    // Total amount
    val status : Boolean? = null                            // Invoice status
                                                                // (null -> pending / false -> rejected / true -> approved)
)
