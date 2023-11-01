package pt.cm.faturasua.utils

import pt.cm.faturasua.data.Invoice

class ParsingUtil {
    fun parseQR(qrCode : String) : Boolean {
        val fields : List<String> = qrCode.split("*")

        println(fields)
        println("Number of fields: " + fields.size)

        val number : String? = parseField(fields, "G:")
        val type : String? = parseField(fields, "D:")
        val businessNIF : String? = parseField(fields, "A:")
        val customerNIF : String? = parseField(fields, "B:")
        val date : String? = parseField(fields, "F:")
        val iva : String? = parseField(fields, "N:")
        val amount : String? = parseField(fields, "O:")

        if (listOf(number, type, businessNIF, customerNIF, date, iva, amount).any { it == null }) {
            println("ERRO: O código QR lido não é uma fatura válida. Por favor tente com outro documento.")
            return false
        }

        // TODO: Get customer NIF dynamically from DB (hardcoded for now)
        if (customerNIF != "509441130") {
            println("ERRO: A fatura lida não foi emitida com o seu número de contribuinte (NIF) e, portanto, não pode ser adicionada à sua conta.")
            return false
        }

        // TODO: Ask user to given a little description for invoice, after scanning/uploading when confirming if invoice details are all OK
        val title = "NAME GIVEN BY THE USERNAME TO BETTER IDENTIFY THE INVOICE"
        val invoice = Invoice(id = number!!, title = title!!, type = type!!, businessNIF = businessNIF!!, customerNIF = customerNIF!!, date = date!!, iva = iva!!, amount = amount!!)

        println()
        println("number:\t\t$number")
        println("type:\t\t$type")
        println("businessNIF:\t$businessNIF")
        println("customerNIF:\t$customerNIF")
        println("date:\t\t$date")
        println("iva:\t\t$iva€")
        println("amount:\t\t$amount€")

        return true
    }

    private fun parseField(fields : List<String>, header : String) : String? {
        val fieldValue : String? = fields.firstOrNull { it.startsWith(header) }
        if (fieldValue.isNullOrEmpty()) {
            return null
        }
        return fieldValue.removePrefix(header)
    }
}