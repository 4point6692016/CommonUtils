@file:Suppress("unused")

package com.example.a4point6692016utils

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.forEach
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
//import com.example.utils4point6692016.R
//import com.example.utils4point6692016.databinding.DetailItemViewBinding
import com.google.android.material.snackbar.Snackbar
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

lateinit var appContext: Context

// String Resources
const val DATE_FORMAT = "MMM dd, yyyy"
const val DATE_TIME_FORMAT_24H = "$DATE_FORMAT, HH:mm:ss"
const val DATE_TIME_FORMAT_12H = "$DATE_FORMAT, hh:mm:ss a"
const val TODAY = "Today"
const val TOMORROW = "Tomorrow"
const val YESTERDAY = "YESTERDAY"
const val YEARS = "years"
const val MONTHS = "months"
const val HOURS = "hours"
const val MILLISECONDS = "milliseconds"
const val WEEKS = "weeks"
const val MINUTES = "minutes"
const val SECONDS = "seconds"
const val DAYS = "days"
const val AGO = "ago"
const val YEARS_AGO = " $YEARS $AGO"
const val DAYS_AGO = " $DAYS $AGO"
const val HOURS_AGO = " $HOURS $AGO"
const val MINUTES_AGO = " $MINUTES $AGO"
const val MONTHS_AGO = " $MONTHS $AGO"
const val SECONDS_AGO = " $SECONDS $AGO"
const val MILLISECONDS_IN_A_YEAR = 31536000000 // 1000 * 60 * 60 * 24 * 365

const val SOMETHING_WENT_WRONG = "Something went wrong!"
const val ERROR_OCCURRED = "Error occurred"
const val CONTACT_DEV = "Contact Developer"

const val OK = "OK"
const val YES = "Yes"
const val NO = "No"
const val DISMISS = "Dismiss"
const val CANCEL = "Cancel"
const val SAVE = "Save"
const val EDIT = "Edit"
const val JUST_NOW = "Just Now"
const val NO_VALUE = "No Value"

const val EMPTY = ""
const val EMPTY_SPACE = " "
const val COMMA = ","
const val UNDERSCORE = "_"
const val HYPHEN = "-"
const val PERIOD = "."
const val ZERO_STRING = "0"
const val PERCENTAGE = "%"
const val NULL = "null"
const val NEW_LINE = "\n"

const val TRUE = "true"
const val FALSE = "false"

const val CLOSE = "CLOSE"
const val FINISH = "FINISH"

const val NONE_SYMBOL = " - "

const val FILE_TIMESTAMP_FORMAT = "yyyy.MM.dd.HH.mm.ss"

const val RUPEE_SYMBOL = "₹"
const val UP_ARROW = "↑"
const val DOWN_ARROW = "↓"

const val OR = "OR"
const val AND = "AND"

const val RECORD_PASSING_KEY = "recordPassingKey"

const val RECORD_SAVED = "Record Saved Successfully!"

// Others
val locale: Locale = Locale.US
val sdf_24h = SimpleDateFormat(DATE_TIME_FORMAT_24H, locale)
val sdf_12h = SimpleDateFormat(DATE_TIME_FORMAT_12H, locale)
val dateSDF = SimpleDateFormat(DATE_FORMAT, locale)
fun mCalendar(): Calendar = Calendar.getInstance()

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

val alertDialogTheme: Int? = null

fun Context.showAlert(message: String, positiveText: String, negativeText: String,
                      positiveAction: () -> Unit, negativeAction: () -> Unit,
                      title: String = "") {
    val alertDialogBuilder = if (alertDialogTheme == null) {
        AlertDialog.Builder(this)
    } else {
        AlertDialog.Builder(this, alertDialogTheme)
    }

    alertDialogBuilder.apply {
        setMessage(message)
        if (title.isNotEmpty()) {
            setTitle(title)
        }
        setPositiveButton(positiveText) { _, _ -> positiveAction.invoke() }
        setNegativeButton(negativeText) { _, _ -> negativeAction.invoke() }
        this.create().show()
    }
}

fun Activity.informAndFinish(message: String, title: String = "") {
    message.ifNotEmpty {
        showAlert(it, OK, "", positiveAction = { finish() }, {}, title = title)
    }
}

fun Context.showInfoAlert(message: String, title: String = "") {
    showAlert(message, OK, "", {}, {}, title)
}

fun String.ifNotEmpty(execute: (String) -> Unit){
    if (isNotEmpty()) execute(this)
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
    params.height = if (isInDp) height.toFloat().convertDpToPixels() else height
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

fun String.replaceNewLineWithSpace(): String {
    return replace(NEW_LINE, EMPTY_SPACE)
}

fun <T> T?.ifNotNull(perform: (T) -> Unit) {
    if (this != null) {
        perform(this)
    }
}

fun <T> List<T>?.ifNotEmpty(perform: (List<T>) -> Unit) {
    if (!isNullOrEmpty()) {
        perform(this)
    }
}

fun <T> getObjectPropertiesMap(obj: T) = buildMap<String, String> {
    var clazz: Class<*> = obj!!::class.java

    while (clazz != Any::class.java) {
        val fields = clazz.declaredFields

        for (field in fields) {
            field.isAccessible = true
            val value = field.get(obj)
            this[field.name] = value?.toString() ?: ""
        }

        clazz = clazz.superclass
    }
}

fun StringBuilder.appendNewLine(): StringBuilder = append(NEW_LINE)

fun StringBuilder.appendWithNewLine(strToAppend: String) {
    append(strToAppend, NEW_LINE)
    appendNewLine()
}

fun String.toSpannableString(): SpannableString = SpannableString(this)

fun String.toBoldSpannableString(): SpannableString = SpannableString(this.makeEntireStringBold())

fun Context.getScreenWidthInPx(): Int {
    return resources.displayMetrics.widthPixels
}

fun Context.getScreenHeightInPx(): Int {
    return resources.displayMetrics.heightPixels
}

fun Context.isDeviceNotificationDisabled(): Boolean {
    return !isDeviceNotificationEnabled()
}

fun Context.isDeviceNotificationEnabled(): Boolean {
    return NotificationManagerCompat.from(this).areNotificationsEnabled()
}

fun getScreenDimensions(windowManager: WindowManager): Pair<Int, Int> {
    val displayMetrics = getDisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
}

fun String.isStringNumeric(): Boolean {
    val regex = Regex("^[0-9]+\$")
    return regex.matches(this)
}

fun RecyclerView.configureRVFastScroller(thumbRes: Int? = null) {
    FastScrollerBuilder(this).apply {
        thumbRes?.let { res ->
            setThumbDrawable(context.getDrawableRes(res)!!)
        }
        build()
    }
}

fun View.isFocussedOrHasAChildInFocus(): Boolean {
    return if (isFocused) {
        true
    } else {
        if (this is ViewGroup) {
            this.forEach {
                if (it.isFocussedOrHasAChildInFocus()) {
                    return true
                }
            }
        }
        false
    }
}

fun View?.resolveAvailableWidth(): Int {
    this ?: return 0
    if (width >= 0) return width
    if (layoutParams.width >= 0) return layoutParams.width
    if (measuredWidth >= 0) return measuredWidth
    return 0
}

fun View?.resolveAvailableHeight(): Int {
    this ?: return 0
    if (height >= 0) return height
    if (layoutParams.height >= 0) return layoutParams.height
    if (measuredHeight >= 0) return measuredHeight
    return 0
}

fun Context.getDrawableRes(drawableResId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableResId)
}

fun Context.getColour(res: Int) = resources.getColor(res, null)

@RequiresApi(Build.VERSION_CODES.Q)
fun Context.spanStringWithIcon(
    actualText: SpannableString,
    replaceText: String,
    @DrawableRes iconRes: Int,
    shouldIconThemeBased: Boolean = false,
    iconWidth: Int = -1,
    iconHeight: Int = -1,
    iconAlignment: Int = ImageSpan.ALIGN_CENTER
): SpannableString {
    return this.getDrawableRes(iconRes)?.also {
        if (shouldIconThemeBased) {
            it.setTint(0)
        }
        if (iconHeight != -1 && iconWidth != -1) {
            it.setBounds(0, 0, iconWidth, iconHeight)
        } else {
            it.setBounds(0, 0, it.intrinsicHeight, it.intrinsicWidth)
        }

        val imageSpan = ImageSpan(it, iconAlignment)
        actualText.setSpan(
            imageSpan,
            actualText.indexOf(replaceText),
            actualText.indexOf(replaceText) + replaceText.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }?.let {
        actualText
    } ?: actualText
}

fun String.getStringWithBackgroundColor(colorCode: String = "#9CDAFF"): String {
    return "<span style='background-color:$colorCode'>$this</span>"
}

fun String.makeTextBold(startIndex: Int, lastIndex: Int, mode: Int = Spannable.SPAN_INCLUSIVE_INCLUSIVE): SpannableStringBuilder {
    val spannableStringBuilder = SpannableStringBuilder(this)
    val spanStyle = StyleSpan(Typeface.BOLD)
    spannableStringBuilder.setSpan(spanStyle, startIndex, lastIndex, mode)
    return spannableStringBuilder
}

fun String.makeEntireStringBold(): SpannableStringBuilder {
    return makeTextBold(0, length)
}

fun String.makeBoldForHtml() = "<b>$this</b>"

fun String.italicizeForHtml() = "<i>$this</i>"

fun String.underlineForHtml() = "<u>$this</u>"

fun String.replaceNewLinesForHtml() = replace("\n", "<br>")

fun String.getFullColouredString(color: String): SpannableString {
    return getColouredString(0, length, color)
}

fun String.getColouredString(startIndex: Int, endIndex: Int, color: String): SpannableString {
    val spannable = SpannableString(this)
    spannable.setSpan(ForegroundColorSpan(Color.parseColor(color)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannable
}

fun getSpannedStringForMandatoryField(value: String): SpannableString {
    val spannableString = SpannableString("* $value ") // No I18N
    spannableString.setSpan(ForegroundColorSpan(-0x12b7b8), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return spannableString
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

// todo to confirm if this worked
//fun getDetailItemView(activity: Activity,
//                      dividerColor: Int = R.color.primary,
//                      labelColor: Int = R.color.grey,
//                      detailValueColor: Int = R.color.black,
//                      label: String, value: String): View {
//    return DetailItemViewBinding.inflate(activity.layoutInflater).apply {
//        labelView.apply {
//            text = label
//            setTextColor(appContext.getColor(labelColor))
//        }
//        valueView.apply {
//            text = value
//            setTextColor(appContext.getColor(detailValueColor))
//        }
//        verticalDiv.root.setBackgroundColor(appContext.getColor(dividerColor))
//    }.root
//}

fun getViewFromUtil(layoutRes: Int): View = LayoutInflater.from(appContext).inflate(layoutRes, null)

fun runOnUiThreadWithDelay(delay: Long = 200, runnable: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(runnable, delay)
}

object DateTimeUtils {

    fun getDisplayableDateAndTime(timeInMS: Long = System.currentTimeMillis(), shouldFetchIn12HourFormat: Boolean = false): String {
        val resultDate = Date(timeInMS)
        return (if (shouldFetchIn12HourFormat) sdf_12h else sdf_24h).format(resultDate)
    }

    fun getDisplayableDate(timeInMS: Long = System.currentTimeMillis()): String {
        val date = Date(timeInMS)
        return dateSDF.format(date)
    }

    fun month() = mCalendar().get(Calendar.MONTH)

    fun today() = mCalendar().get(Calendar.DAY_OF_MONTH)

    fun thisYear() = mCalendar().get(Calendar.YEAR)

    fun currentTimeStampForFiles() : String {
        return SimpleDateFormat(FILE_TIMESTAMP_FORMAT, locale).format(System.currentTimeMillis())
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

fun Boolean.ifTrue(perform: () -> Unit) {
    if (this) {
        perform()
    }
}

fun Boolean.ifFalse(perform: () -> Unit) {
    if (!this) {
        perform()
    }
}

fun EditText.value() = text.toString()

fun TextView.value() = text.toString()

fun EditText.trimmedValue() = value().trim()

fun TextView.trimmedValue() = value().trim()

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

fun <T> ArrayList<T>.addIfNotAvailable(element: T) {
    if (!contains(element)) {
        add(element)
    }
}

/**
 * This is hack for viewpager2's children with different height to be expanded for their measured height
 * not the previous child's measurement
 * https://issuetracker.google.com/u/0/issues/143095219?pli=1
 */
fun ViewPager2.expandScreenItemHack(recyclerView: RecyclerView?): ViewPager2.OnPageChangeCallback {
    fun measureAndUpdate(view: View) {
        view.post {
            val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)
            if (layoutParams.height != view.measuredHeight) {
                layoutParams = (layoutParams as LinearLayout.LayoutParams).also { param ->
                    param.height = view.measuredHeight
                }
            }
        }
    }
    val listener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            (recyclerView?.layoutManager as? LinearLayoutManager)?.findViewByPosition(position)?.let {
                measureAndUpdate(it)
            }
        }
    }
    registerOnPageChangeCallback(listener)
    return listener
}

fun FileOutputStream.flushAndClose() {
    flush()
    close()
}

fun SearchView.onQueryChange(action: (query: String) -> Boolean) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return action(query.toString())
        }
    })
}

fun <T> List<T>.contains(predicate: (T) -> Boolean): Boolean {
    return find(predicate) != null
}

fun Cursor.getStringFromColumn(columnName: String): String = getString(getColumnIndexOrThrow(columnName))

fun getLocationOnScreen(view: View): Pair<Int, Int> {
    val outArray = IntArray(2)
    view.getLocationOnScreen(outArray)
    return Pair(outArray.first(), outArray.last())
}

fun Context.getMemoryInfo(): ActivityManager.MemoryInfo {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return ActivityManager.MemoryInfo().also { memoryInfo ->
        activityManager.getMemoryInfo(memoryInfo)
    }
}

fun getRelativeTermFromMilliseconds(givenTime: Long): String {
    val now = currentTimeInMS()
    val diff = (now - givenTime).toDouble()
    val year = MILLISECONDS_IN_A_YEAR
    if ((diff / (1000 * 60)) < 1) {
        return JUST_NOW
    }
    if ((diff / (1000 * 60)) >= 1 && (diff / (1000 * 60)) < 60) {
        return "${(diff / (1000 * 60)).toInt()}" + MINUTES_AGO
    }
    if ((diff / (1000 * 60 * 60)) >= 1 && (diff / (1000 * 60 * 60)) < 24) {
        return "${(diff / (1000 * 60 * 60)).toInt()}" + HOURS_AGO
    }
    if ((diff / (1000 * 60 * 60 * 24)) >= 1 && (diff / (1000 * 60 * 60 * 24)) < 365) {
        return "${(diff / (1000 * 60 * 60 * 24)).toInt() + 1}" + DAYS_AGO
    }
    if ((diff / year) > 1) {
        return "${(diff / year).toInt()}" + YEARS_AGO
    }
    return EMPTY
}

fun Long.getWeekOfDay(): String {
    val calendar = mCalendar()
    calendar.timeInMillis = this
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        1 -> "SUNDAY"
        2 -> "MONDAY"
        3 -> "TUESDAY"
        4 -> "WEDNESDAY"
        5 -> "THURSDAY"
        6 -> "FRIDAY"
        7 -> "SATURDAY"
        else -> EMPTY
    }
}

fun Int.isLeapYear(): Boolean {
    return (this % 4 == 0) && ((this % 100 != 0) || (this % 400 == 0))
}

inline val Int.dp: Int
    get() {
        return this.toFloat().convertDpToPixels()
    }

inline val Int.sp: Int
    get() {
        return this.toFloat().convertSpToPixels()
    }

fun Float.convertSpToPixels(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        getDisplayMetrics()
    ).toInt()
}

fun Float.convertDpToPixels(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        getDisplayMetrics()
    ).toInt()
}

fun getDisplayMetrics(): DisplayMetrics = appContext.resources.displayMetrics