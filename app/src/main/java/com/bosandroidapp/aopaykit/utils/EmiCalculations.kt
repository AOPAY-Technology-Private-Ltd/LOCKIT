package com.bosandroidapp.aopaykit.utils

import android.accessibilityservice.AccessibilityService.MODE_PRIVATE
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.CheckCompleteEmiStatus
import com.bosandroidapp.aopaykit.constant.ConstantClass.convertDate
import com.bosandroidapp.aopaykit.constant.ConstantClass.uploadDataOnFirebaseConsole
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.bosandroidapp.aopaykit.kioskmode.setEMICompleted
import com.bosandroidapp.aopaykit.kioskmode.setEMINotCompleted
import com.bosandroidapp.aopaykit.kioskmode.startLockSituation
import com.bosandroidapp.aopaykit.kioskmode.stopLockSituation
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Date
import java.util.Locale


private var currentDate: String? = null
const val ACCESSIBILITYTAG = "Accessibility Service"


suspend fun String.getCurrentLastPaidDueDate(context: Context?, paidMonths: Long, payableMonths: Long, AllgrossPeriod: Int, customergrossPeriod: Int, servercurrentDate:String): ArrayList<MonthsAndPayables> = withContext(Dispatchers.IO) {
    val AllgrossPeriod = AllgrossPeriod
    val customergrossPeriod = customergrossPeriod
    val grossPeriod = AllgrossPeriod + customergrossPeriod
    // generate your list
    // val paidTill = this@getCurrentLastPaidDueDate.getJumpedDate(paidMonths)
    val paidTill = this@getCurrentLastPaidDueDate
    val list = paidTill.furtherDueDates(payableMonths, grossPeriod)
    currentDate = servercurrentDate.convertDate()
    val currentMonthDue = this@getCurrentLastPaidDueDate.getCurrentMonthDue(24, grossPeriod)
    val currentMonthGrossDue = this@getCurrentLastPaidDueDate.getCurrentMonthGross(24, grossPeriod)
    // log for debugging
    Log.d("firstDueDate", paidTill)
    Log.d("EmiList", list.toString())
    Log.d("Current Due", currentMonthDue)
    Log.d("Current Gross Due", currentMonthGrossDue)

    // switch back automatically since return value will flow to caller
    list // is showing all duedate list
}


private suspend fun getCustomerLoanEmiDetailsReq(req: GetCustomerLoanDetailsReq) =
    RetrofitClient.apiInterface.getCustomerLoanDetailsList(req)


suspend fun Context.syncEmis() = withContext(Dispatchers.IO) {
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

   /* if (hasDateChanged()) {*/
        Logger.d(ACCESSIBILITYTAG, "Date Changed")
        val preference = SharedPreference(this@syncEmis)

        var loanemireq = GetCustomerLoanDetailsReq(
            loancode = "",
            customercode = preference.getStringValue(ConstantClass.CustomerCode, "")
        )
        try {
            Logger.d(ACCESSIBILITYTAG, "Syncing EMIs")
            Log.d("Loanreq",Gson().toJson(loanemireq))
            val loanDetails = getCustomerLoanEmiDetailsReq(loanemireq)
            currentDate = loanDetails?.body()?.indiaTimeIST!!.convertDate()
            val list = loanDetails?.body()?.data?.toList()

            list?.let { loans ->
                val obj = loans.getLoansStringObject()
                Logger.d(ACCESSIBILITYTAG, obj)
                sharedPref.edit().putString("LoanData", obj).apply()
            }

        }
        catch (e: Exception) {
            Logger.d(ACCESSIBILITYTAG, e.localizedMessage ?: "")
        }

  /*  }*/

    isEMIDue(sharedPref)

   /* if(latitude > 0.0 && longitude > 0.0){
        scheduleLocationWorker(latitude, longitude)
    }*/

}



@RequiresApi(Build.VERSION_CODES.R)
private suspend fun Context.isEMIDue(sharedPref: SharedPreferences) = withContext(Dispatchers.IO) {
    var malfunctionedDates = 0
    var emiDues: Int? = null
    var lateEMIs = 0

    try {
        val loanDetails = sharedPref.getString("LoanData", "")
        val list = loanDetails?.toFormattedList()

        uploadDataOnFirebaseConsole(Gson().toJson(list), "LoanList")

        list?.let { loans ->
            loans.forEach { loan ->
                loan.let {
                    val leftMonths = it.duesEmi.toLong()
                    val paidMonths = it.paidEmi.toLong()
                    val allGrossPeriod = if (it.grossPeriod.isEmpty()) 0 else it.grossPeriod.toInt()
                    val customerGrossPeriod = if (it.customergrossPeriod.isEmpty()) 0 else it.customergrossPeriod.toInt()
                    val grossPeriod = allGrossPeriod + customerGrossPeriod

                    Log.d(ACCESSIBILITYTAG, "Loan Details: ${it.startDate.toFormattedDate()}-->$leftMonths == $paidMonths")

                    if (leftMonths != (0.toLong())) {

                        CheckCompleteEmiStatus = true

                        Log.d("currentDate", currentDate!!)
                        uploadDataOnFirebaseConsole("$currentDate", "currentDate")
                        val grossDue = it.startDate.toFormattedDate().getGrossDate(grossPeriod)
                        Log.d("grossDueDate", grossDue)
                        if (grossDue.isLateFeesApplicable(currentDate)) {
                            lateEMIs++
                        }
                        Log.d("printlateemiGrossDue", "$lateEMIs $grossDue")
                        uploadDataOnFirebaseConsole("$lateEMIs $grossDue", "LateEmi'sGrossDue")
                        if (emiDues == null) {
                            emiDues = 0
                        }
                        emiDues=emiDues!! + 1

                    }

                    else{
                        emiDues = 0
                        lateEMIs = 0
                        CheckCompleteEmiStatus = false
                    }

                }
            }
        }


        Logger.d(ACCESSIBILITYTAG,"EMIDUES: $emiDues")


        if (emiDues != null) {
            if (emiDues!! > 0) {
                this@isEMIDue.setEMINotCompleted()
            } else {
                this@isEMIDue.setEMICompleted()
            }
        }


        if (lateEMIs > 0) {
            Logger.d(ACCESSIBILITYTAG, "Late EMIs Count: $lateEMIs")
            this@isEMIDue.startLockSituation()
        }
        else {
             this@isEMIDue.stopLockSituation()
        }


    } catch (e: Exception) {
        Logger.d(ACCESSIBILITYTAG, e.localizedMessage ?: "")
    }

}


/*private suspend fun Context.defaultedLoansList(sharedPref: SharedPreferences): List<LocalLoanData> = withContext(Dispatchers.IO) {
    val defaultedEmis= arrayListOf<LocalLoanData>()
    try {
        val loanDetails = sharedPref.getString("LoanData", "")
        val list = loanDetails?.toFormattedList()
        list?.let { loans ->
            loans.forEach { loan ->
                loan.let {
                    val leftMonths = it.duesEmi.toLong()
                    val paidMonths = it.paidEmi.toLong()
                    val grossPeriod = if(it.grossPeriod.isEmpty()) 0 else it.grossPeriod.toInt()
                    Log.d(
                        "Loan Details",
                        "${it.startDate.toFormattedDate()}-->$leftMonths == $paidMonths"
                    )
                    if (leftMonths != (0.toLong())) {
                        currentDate = getNetworkTime(this@defaultedLoansList).first?.formatDate()
                        val grossDue = "30/7/2025".getJumpedDate((paidMonths + 1))
                            .getValidOrLastDate().getGrossDate(grossPeriod)
                        if(grossDue.isLateFeesApplicable()) {
                            defaultedEmis.add(it)
                        }
                    }
                }
            }
        }
    } catch (e: Exception) {
        Log.d("isEMIDue", e.localizedMessage ?: "")
    }
    defaultedEmis

}*/


fun String.toFormattedDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: this
    } catch (e: Exception) {
        this // return original if parsing fails
    }
}


private fun String.getJumpedDate(paidMonths: Long): String {
    val splitDate = this.trim().split("/")
    val monthIncreased = splitDate[1].toLong() + paidMonths
    if (monthIncreased > 12) {
        val years = monthIncreased / 12
        val month = monthIncreased % 12
        return "${splitDate[0]}/$month/${splitDate[2].toLong() + years}"
    } else {
        return "${splitDate[0]}/${splitDate[1].toLong() + paidMonths}/${splitDate[2]}"
    }
}


fun String.getCurrentMonthDue(totalEmis: Long, grossPeriod: Int): String {

    val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
    sdf.isLenient = false

    val fixedDate = sdf.parse(this)

    val cal1 = Calendar.getInstance()
    cal1.time = sdf.parse(currentDate ?: "") ?: Date()

    val cal2 = Calendar.getInstance()
    cal2.time = fixedDate ?: Date()
    val lastDueDate =
        this.getJumpedDate(totalEmis + 1).getValidOrLastDate().getGrossDate(grossPeriod)
    val cal3 = Calendar.getInstance()
    cal3.time = sdf.parse(lastDueDate) ?: Date()

    if (cal3.before(cal1)) {
        return lastDueDate
    } else {
        val yearDiff = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)
        val monthDiff = cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH)

        val difference = (yearDiff * 12) + monthDiff
        val dd = this.getJumpedDate(difference.toLong()).getValidOrLastDate()
        val cal4 = Calendar.getInstance()
        cal4.time = sdf.parse(dd) ?: Date()
        if (cal1.before(cal4)) {
            return dd
        } else {
            val ddd = this.getJumpedDate((difference + 1).toLong()).getValidOrLastDate()
            return ddd
        }
    }

}


fun String.getCurrentMonthGross(totalEmis: Long, grossPeriod: Int): String {
    val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
    sdf.isLenient = false

    val fixedDate = sdf.parse(this)

    val cal1 = Calendar.getInstance()
    cal1.time = sdf.parse(currentDate ?: "") ?: Date()

    val cal2 = Calendar.getInstance()
    cal2.time = fixedDate ?: Date()
    val lastDueDate =
        this.getJumpedDate(totalEmis + 1).getValidOrLastDate().getGrossDate(grossPeriod)
    val cal3 = Calendar.getInstance()
    cal3.time = sdf.parse(lastDueDate) ?: Date()

    if (cal3.before(cal1)) {
        return lastDueDate
    } else {
        val yearDiff = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)
        val monthDiff = cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH)

        val difference = (yearDiff * 12) + monthDiff
        val dd =
            this.getJumpedDate(difference.toLong()).getValidOrLastDate().getGrossDate(grossPeriod)
        val cal4 = Calendar.getInstance()
        cal4.time = sdf.parse(dd) ?: Date()
        if (cal1.before(cal4)) {
            return dd
        } else {
            val ddd = this.getJumpedDate((difference + 1).toLong()).getValidOrLastDate().getGrossDate(grossPeriod)
            return ddd
        }
    }

}


data class MonthsAndPayables(
    val dueDate: String,
    val dueDateWithGross: String,
    val lateFeesApplied: Boolean
)


private fun String.furtherDueDates(months: Long, grossPeriod: Int): ArrayList<MonthsAndPayables> {
    val list = arrayListOf<MonthsAndPayables>()
    //var nextDue = this.getJumpedDate(1)
    var nextDue = this
    for (i in 1..months) {
        //val validDue = nextDue.getValidOrLastDate()
        val validDue = nextDue
        val grossDue = validDue.getGrossDate(grossPeriod)
        list.add(MonthsAndPayables(validDue, grossDue, grossDue.isLateFeesApplicable(currentDate)))
        nextDue = nextDue.getJumpedDate(1)
    }
    return list
}


private fun String.getValidOrLastDate(): String {
    val format = "d/M/yyyy"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val formatter = DateTimeFormatter.ofPattern(format, Locale.getDefault())
        return try {
            LocalDate.parse(this, formatter).format(formatter)
        } catch (e: DateTimeParseException) {
            // Extract year & month from input
            try {
                val parts = this.split("-", " ", "/")
                val day = parts[0].toIntOrNull() ?: 1
                val month =
                    parts[1].toIntOrNull()?.coerceIn(1, 12) ?: 1   // ensure month between 1–12
                val year = parts.getOrNull(2)?.toIntOrNull() ?: LocalDate.now().year

                val lastDayOfMonth = YearMonth.of(year, month).atEndOfMonth().dayOfMonth
                val safeDay = day.coerceIn(1, lastDayOfMonth)

                val safeDate = LocalDate.of(year, month, safeDay)
                safeDate.format(formatter)

            } catch (ex: Exception) {
                "" // return blank instead of crash
            }
        }
    } else {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.isLenient = false  // strict parsing (rejects Feb 30, etc.)

        return try {
            sdf.parse(this)
            this
        } catch (e: Exception) {
            val parts = this.split("-", " ", "/")
            if (parts.size < 2) return this // fallback if format totally wrong

            val year = parts[2].toIntOrNull() ?: return this
            val month = parts[1].toIntOrNull()?.minus(1) ?: return this  // Calendar is 0-based

            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            val lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

            // Return corrected last valid date
            "%04d-%02d-%02d".format(year, month + 1, lastDay)
        }
    }
}


private fun String.getGrossDate(grossPeriod: Int): String {
    val format = "d/M/yyyy"
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.isLenient = false  // strict parsing

    val validDate: Date? = try {
        sdf.parse(this)
    } catch (e: Exception) {
        val parts = this.split("-", " ", "/")
        if (parts.size < 2) return ""

        val year = parts[2].toIntOrNull() ?: return ""
        val month = parts[1].toIntOrNull()?.minus(1) ?: return ""

        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        val lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        sdf.parse("%02d/%02d/%04d".format(lastDay, month + 1, year))
    }


    return validDate?.let {
        val cal = Calendar.getInstance()
        cal.time = it
        cal.add(Calendar.DAY_OF_MONTH, grossPeriod)
        sdf.format(cal.time)
    } ?: this

}


/*private fun String.isLateFeesApplicable(): Boolean {

    val format = "d/M/yyyy"
    currentDate?.let {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.isLenient = false  // strict parsing

        return try {
            val date1 = sdf.parse(currentDate)
            val date2 = sdf.parse(this)
            !(date1 != null && date2 != null && date1.before(date2))
        } catch (e: Exception) {
            true
        }
    }
    return true
}*/


private fun String.isLateFeesApplicable(currentDateStr: String?): Boolean {

    if (currentDateStr.isNullOrEmpty()) return false

    return try {
        val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        sdf.isLenient = false

        val current = sdf.parse(currentDateStr)
        val due = sdf.parse(this)

        Log.d("current dueDate", "$current $due")

        uploadDataOnFirebaseConsole("$current $due", "current dueDate")

        // LATE only if current date is AFTER due date
        current != null && due != null && current.after(due)


    } catch (e: Exception) {
        false
    }
}


fun Context.hasDateChanged(): Boolean {
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val lastSync = sharedPref.getString("LoanSyncDate", "")
    if (lastSync != currentDate) {
        sharedPref.edit().putString("LoanSyncDate", currentDate).apply()
        Logger.d(ACCESSIBILITYTAG, currentDate.toString())
        return true
    }
    return false
}


fun List<com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerDataItem?>.getLoansStringObject(): String {
    val arr = JSONArray()
    this.forEach {
        it?.let {
            arr.put(JSONObject().apply {
                this.put("startDate", it.startDate)
                this.put("paidEMIs", it.paidEMI)
                this.put("dueEMIs", it.duesEMI)
                this.put("grossPeriod", it.gracePeriod)
                this.put("customergrossPeriod", it.customerGracePeriod)
            })

        }
    }
    Log.d("Loans Array", arr.toString())
    return arr.toString()
}


data class LocalLoanData(
    val startDate: String,
    val paidEmi: String,
    val duesEmi: String,
    val grossPeriod: String,
    val customergrossPeriod: String
)


fun String.toFormattedList(): List<LocalLoanData>? {
    try {
        val array = JSONArray(this)
        return arrayListOf<LocalLoanData>().apply {
            for (i in 0..(array.length() - 1)) {
                this.add(
                    LocalLoanData(
                        array.getJSONObject(i).getString("startDate"),
                        array.getJSONObject(i).getString("paidEMIs"),
                        array.getJSONObject(i).getString("dueEMIs"),
                        array.getJSONObject(i).getString("grossPeriod"),
                        array.getJSONObject(i).getString("customergrossPeriod"),
                    )
                )
            }

        }
    } catch (e: Exception) {
        return null
    }

}



