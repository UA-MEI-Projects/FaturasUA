package pt.cm.faturasua.utils



import pt.cm.faturasua.data.Invoice
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.LinkedList

class StatsUtil {
    companion object{

        fun scanChartData(invoices : ArrayList<Invoice>): HashMap<String, Int> {
//            val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyymmdd")
//            val data = HashMap<LocalDate, Int>()

            val data = HashMap<String, Int>()
            invoices.forEach { invoice ->
               // val key = LocalDate.parse(invoice.date, dateTimeFormatter)
                val key = invoice.date
                var value = data.get(key)
                value = if(value != null) value+1 else 1
                data.set(key, value)
            }

            return data
        }

        fun scanTotalValue(invoices: ArrayList<Invoice>): Double{
            var total: Double = 0.0

            invoices.forEach { invoice ->
                total += invoice.amount.toDouble()
            }

            return total
        }
    }

}
