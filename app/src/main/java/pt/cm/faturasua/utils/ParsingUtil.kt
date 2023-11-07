package pt.cm.faturasua.utils

import pt.cm.faturasua.data.Invoice

class ParsingUtil {
    fun parseQR(qrCode: String): Invoice? {
        val fields: List<String> = qrCode.split("*")

        val number: String? = parseField(fields, "G:")?.replace("/", " ")
        val type: String? = parseField(fields, "D:")
        val businessNIF: String? = parseField(fields, "A:")
        val customerNIF: String? = parseField(fields, "B:")
        val date: String? = parseField(fields, "F:")
        val iva: String? = parseField(fields, "N:")
        val amount: String? = parseField(fields, "O:")


        if (listOf(number, type, businessNIF, customerNIF, date, iva, amount).any { it == null }) {
            System.out.println("ERRO: O código QR lido não é uma fatura válida. Por favor tente com outro documento.")
            return null
        }

        // TODO: Get customer NIF dynamically from DB (hardcoded for now)
        /*        if (customerNIF != "509441130") {
            System.out.println("ERRO: A fatura lida não foi emitida com o seu número de contribuinte (NIF) e, portanto, não pode ser adicionada à sua conta.")
            return null
        }*/

        return Invoice(
            id = number!!,
            title = "",
            category = "",
            type = type!!,
            businessNIF = businessNIF!!,
            customerNIF = customerNIF!!,
            date = date!!,
            iva = iva!!,
            amount = amount!!
        )
    }

    private fun parseField(fields : List<String>, header : String) : String? {
        val fieldValue : String? = fields.firstOrNull { it.startsWith(header) }
        if (fieldValue.isNullOrEmpty()) {
            return null
        }
        return fieldValue.removePrefix(header)
    }
}