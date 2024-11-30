package com.example.a4point6692016utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun String.makeToast(context: Context, isLongToast: Boolean = false) {
    Toast.makeText(context, this, if (isLongToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

fun Context.showAlert(message: String, positiveText: String, negativeText: String,
                      positiveAction: () -> Unit, negativeAction: () -> Unit,
                      title: String = "", themeResId: Int) {
    AlertDialog.Builder(this, themeResId).apply {
        setMessage(message)
        if (title.isNotEmpty()) {
            setTitle(title)
        }
        setPositiveButton(positiveText) { _, _ -> positiveAction.invoke() }
        setNegativeButton(negativeText) { _, _ -> negativeAction.invoke() }
        this.create().show()
    }
}

fun Int.getMonth(): String {
    return when(this) {
        0  -> "Jan"
        1  -> "Feb"
        2  -> "Mar"
        3  -> "Apr"
        4  -> "May"
        5  -> "Jun"
        6  -> "Jul"
        7  -> "Aug"
        8  -> "Sep"
        9  -> "Oct"
        10 -> "Nov"
        11 -> "Dec"
        else -> "Invalid"
    }
}

fun showKeyBoard(window: Window) {
    val controller = WindowInsetsControllerCompat(window, window.decorView)
    controller.show(WindowInsetsCompat.Type.ime())
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun makeSnackBar(snackBarView: View, snackBarMessage: String, snackBarActionText: String, snackBarAction: () -> Unit, snackBarActionColorCode: String = "#2A88E3"){
    Snackbar.make(snackBarView, snackBarMessage, Snackbar.LENGTH_LONG).apply {
        setAction(snackBarActionText){ snackBarAction.invoke() }
        setActionTextColor(Color.parseColor(snackBarActionColorCode))
        show()
    }
}

fun makeViewsGone(vararg views: View?) {
    views.forEach { it?.makeGone() }
}

fun makeViewsVisible(vararg views: View?) {
    views.forEach { it?.makeVisible() }
}

fun expandView(view: View, maxHeight: Int, fromHeight: Int = 1, makeVisible: Boolean = true, delay: Long = 10, incrementalStep: Int = 1, endAction: () -> Unit = {}) {
    if (fromHeight >= maxHeight) {
        view.setHeight(maxHeight)
        endAction()
        return
    }
    runOnUiThreadWithDelay(delay) {
        view.setHeight(fromHeight)
        if (makeVisible) {
            view.makeVisible()
        }
        expandView(view, maxHeight, fromHeight + incrementalStep, false, delay, incrementalStep, endAction)
    }
}

fun convertDpToPixel(dp: Int): Int {
    return (dp * (Resources.getSystem().displayMetrics.densityDpi / 160f)).toInt()
}

fun reduceViewAlpha(view: View, alpha: Float = 0.5f, duration: Long = 300, endAction: () -> Unit = { }) {
    view.animate().alpha(alpha).setDuration(duration).withEndAction(endAction).start()
}

fun increaseViewAlpha(view: View, alpha: Float = 1f, duration: Long = 300, endAction: () -> Unit = { }) {
    view.animate().alpha(alpha).setDuration(duration).withEndAction(endAction).start()
}

fun View.compressToDeath(decrementalSteps: Int = 2, hideAndCompress: Boolean = false) {
    if (hideAndCompress) {
        makeInvisible()
    }
    if (height <= decrementalSteps) {
        makeGone()
        return
    }
    post {
        setHeight(height - decrementalSteps, false)
        compressToDeath(decrementalSteps)
    }
}

fun View.setHeight(height: Int, isInDp: Boolean = true) {
    val params = layoutParams
    params.height = if (isInDp) convertDpToPixel(height) else height
    layoutParams = params
}

fun AppCompatActivity.disableUpButton() {
    supportActionBar?.setDisplayHomeAsUpEnabled(false)
}

fun AppCompatActivity.enableUpButton() {
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

fun View.setSmoothClickListener(onClick: () -> Unit) {
    setOnClickListener {
        runOnUiThreadWithDelay {
            onClick()
        }
    }
}

fun View.doOnClickOrOnFocusGain(task: () -> Unit) {
    setOnClickListener {
        task()
    }
    doOnFocusGained(task)
}

fun View.doOnFocusGained(task: () -> Unit) {
    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            task()
        }
    }
}

fun View.showOrHide(shouldShow: Boolean) {
    if (shouldShow) {
        makeVisible()
    } else {
        makeGone()
    }
}

fun String.removeSpaces(): String {
    return replace("\\s".toRegex(), "")
}

fun Context.getScreenWidthInPx(): Int {
    return resources.displayMetrics.widthPixels
}

fun String.isStringNumeric(): Boolean {
    val regex = Regex("^[0-9]+\$")
    return regex.matches(this)
}

fun RecyclerView.configureRVFastScroller(thumbRes: Int? = null) {
    FastScrollerBuilder(this).apply {
        thumbRes?.let { res ->
            setThumbDrawable(ContextCompat.getDrawable(context, res)!!)
        }
        build()
    }
}

fun Context.getColour(res: Int) = resources.getColor(res, null)

fun String.getStringWithBackgroundColor(colorCode: String = "#9CDAFF"): String {
    return "<span style='background-color:$colorCode'>$this</span>"
}

fun String.getTextToHighlight(searchQuery: String): String {
    val indexOfSearchQuery = indexOf(searchQuery, ignoreCase = true)
    return if (indexOfSearchQuery > -1) {
        val matchingSegment = substring(indexOfSearchQuery, indexOfSearchQuery + searchQuery.length)
        val replaceWith = matchingSegment.getStringWithBackgroundColor()
        return replaceFirst(matchingSegment, replaceWith)
    } else {
        ""
    }
}

fun AutoCompleteTextView.configureDropDown(context: Context, arrayRes: Int, eachDropDownItemRes: Int, onTextChanged: ((String) -> Unit)? = null) {
    val array = context.resources.getStringArray(arrayRes)
    val arrayAdapter = ArrayAdapter(context, eachDropDownItemRes, array)
    apply {
        setText(arrayAdapter.getItem(0))
        setAdapter(arrayAdapter)
        onTextChanged?.let { onTxtChanged ->
            val textChangedListenerAddedTag = "textChangedListenerAddedTag"
            if ((tag as String?) != textChangedListenerAddedTag) { // to only add the listener once
                addTextChangedListener { newText ->
                    onTxtChanged(newText?.toString() ?: "")
                }
                tag = textChangedListenerAddedTag
            }
        }
    }
}

fun runOnUiThreadWithDelay(delay: Long = 200, runnable: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(runnable, delay)
}

object StringResources {
    const val DATE_TIME_FORMAT = "MMM dd, yyyy, HH:mm:ss"
    const val DATE_FORMAT = "MMM dd, yyyy"

    const val SOMETHING_WENT_WRONG = "Something went wrong!"

    const val OK = "OK"
    const val YES = "Yes"
    const val NO = "No"
    const val CANCEL = "Cancel"
    const val SAVE = "Save"

    const val CLOSE = "CLOSE"
    const val FINISH = "FINISH"

    const val FILE_TIMESTAMP_FORMAT = "yyyy.MM.dd.HH.mm.ss"

    const val RUPEE_SYMBOL = "₹"

    const val OR = "OR"
    const val AND = "AND"

    const val RECORD_PASSING_KEY = "recordPassingKey"

    const val RECORD_SAVED = "Record Saved Successfully!"
}

object DateTimeUtils {
    val locale: Locale = Locale.US

    val sdf = SimpleDateFormat(StringResources.DATE_TIME_FORMAT, locale)

    val dateSDF = SimpleDateFormat(StringResources.DATE_FORMAT, locale)

    fun currentDateAndTime(now: Long = System.currentTimeMillis()): String {
        val resultDate = Date(now)
        return sdf.format(resultDate)
    }

    fun currentDate(now: Long = System.currentTimeMillis()): String {
        val date = Date(now)
        return dateSDF.format(date)
    }

    fun mCalendar(): Calendar = Calendar.getInstance()

    fun month() = mCalendar().get(Calendar.MONTH)

    fun today() = mCalendar().get(Calendar.DAY_OF_MONTH)

    fun thisYear() = mCalendar().get(Calendar.YEAR)

    fun currentTimeStampForFiles() : String {
        return SimpleDateFormat(StringResources.FILE_TIMESTAMP_FORMAT, locale).format(System.currentTimeMillis())
    }
}

fun String.areParenthesesBalanced(): Boolean {
    var count = 0
    for (char in this) {
        if (char == '(') {
            count++
        } else if (char == ')') {
            if (count == 0) {
                return false // Opening parenthesis before a closing one
            }
            count--
        }
    }
    return count == 0 // All opening parentheses have a closing pair
}

fun String.containsInvalidCharacters(): Boolean {
    val regex = Regex("^[0-9+\\-*/().]*\$")
    return !regex.matches(this)
}

fun String.containsInvalidAddSubCharacters(): Boolean {
    val regex = Regex("^[0-9+\\-.]*\$")
    return !regex.matches(this)
}

fun String.containsNumberFollowedByOpeningParenthesis(): Boolean {
    val regex = Regex("\\d\\(")
    return regex.containsMatchIn(this)
}

fun String.containsBigNumber(): Boolean {
    val regex = "\\d{10,}".toRegex()
    return regex.containsMatchIn(this)
}


fun String.containsPeriodBeforeOrAfterParenthesis(): Boolean {
    return contains("(.") || contains(".)")
}

fun String.containsOperatorFollowedByClosingParenthesis(): Boolean {
    val regex = Regex("[+\\-*/]\\)")
    return regex.containsMatchIn(this)
}

fun String.containsOpeningParenthesisFollowedByOperator(): Boolean {
    val regex = Regex("\\([+\\-*/]")
    return regex.containsMatchIn(this)
}

fun String.containsOperatorInsideParentheses(): Boolean {
    val regex = Regex("\\([+\\-*/]\\)")
    return regex.containsMatchIn(this)
}

fun String.containsConsecutiveOperators(): Boolean {
    val regex = Regex("[+\\-*/][+\\-*/]")
    return regex.containsMatchIn(this)
}

fun String.containsConsecutiveAddSubOperators(): Boolean {
    val regex = Regex("[+\\-][+\\-]")
    return regex.containsMatchIn(this)
}

fun String.containsConsecutivePeriods(): Boolean {
    val regex = "\\.\\.".toRegex()
    return regex.containsMatchIn(this)
}

fun String.containsHugeNumber(): Boolean {
    val regex = Regex("\\b(?:0*([1-9]\\d{9,})|([1-9]\\d{9,}))\\b") // 9 significant digits
    return regex.containsMatchIn(this)
}

fun String.startsOrEndsWithOperator(): Boolean {
    val regex = Regex("^[+\\-*/].*|[+\\-*/]$")
    return regex.containsMatchIn(this)
}

fun String.endsWithOperator(): Boolean {
    return endsWith("+") || endsWith("-")
}

fun String.startsOrEndsWithPeriod(): Boolean {
    val regex = "^\\.|\\.$".toRegex()
    return regex.containsMatchIn(this)
}

fun String.containsMoreThan2DecimalPoints(): Boolean {
    val regex = "\\d+\\.\\d{3,}".toRegex()
    return regex.containsMatchIn(this)
}

fun String.hasNoNumericCharacters(): Boolean {
    val regex = "\\d".toRegex()
    return !regex.containsMatchIn(this)
}

fun String.isInvalidAddSubExpression(): Boolean {
    val regex = buildString { append("^[-+]?\\d{1,9}(?:\\.\\d{1,2})?(?:(?:\\+|-)\\d{1,9}(?:\\.\\d{1,2})?)*$") }.toRegex()
    return !regex.containsMatchIn(this)
}

fun EditText.value() = text.toString()

fun currentTimeInMSAsStr() = System.currentTimeMillis().toString()

fun currentTimeInMS() = System.currentTimeMillis()

fun String.isSingleValidOperand(): Boolean {
    return try {
        toBigDecimal()
        true
    } catch (_: Exception) {
        false
    }
}