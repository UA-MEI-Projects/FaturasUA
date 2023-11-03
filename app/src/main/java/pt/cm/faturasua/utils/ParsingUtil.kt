package pt.cm.faturasua.utils

import pt.cm.faturasua.data.Invoice

class ParsingUtil {
    fun parseQR(qrCode : String) : Invoice? {
        val fields : List<String> = qrCode.split("*")

        val number : String? = parseField(fields, "G:")
        val type : String? = parseField(fields, "D:")
        val businessNIF : String? = parseField(fields, "A:")
        val customerNIF : String? = parseField(fields, "B:")
        val date : String? = parseField(fields, "F:")
        val iva : String? = parseField(fields, "N:")
        val amount : String? = parseField(fields, "O:")


        if (listOf(number, type, businessNIF, customerNIF, date, iva, amount).any { it == null }) {
            System.out.println("ERRO: O código QR lido não é uma fatura válida. Por favor tente com outro documento.")
            return null
        }

        // TODO: Get customer NIF dynamically from DB (hardcoded for now)
/*        if (customerNIF != "509441130") {
            System.out.println("ERRO: A fatura lida não foi emitida com o seu número de contribuinte (NIF) e, portanto, não pode ser adicionada à sua conta.")
            return null
        }*/

        // TODO: Ask user to given a little description for invoice, after scanning/uploading when confirming if invoice details are all OK
        val title = "NAME GIVEN BY THE USERNAME TO BETTER IDENTIFY THE INVOICE"
        var invoice = Invoice(id = number!!, title = title!!, type = type!!, businessNIF = businessNIF!!, customerNIF = customerNIF!!, date = date!!, iva = iva!!, amount = amount!!)

        System.out.println()
        System.out.println("number:\t\t$number")
        System.out.println("type:\t\t$type")
        System.out.println("businessNIF:\t$businessNIF")
        System.out.println("customerNIF:\t$customerNIF")
        System.out.println("date:\t\t$date")
        System.out.println("iva:\t\t$iva€")
        System.out.println("amount:\t\t$amount€")

        return invoice
    }

    private fun parseField(fields : List<String>, header : String) : String? {
        val fieldValue : String? = fields.firstOrNull { it.startsWith(header) }
        if (fieldValue.isNullOrEmpty()) {
            return null
        }
        return fieldValue.removePrefix(header)
    }
}