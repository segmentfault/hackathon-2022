package io.hikarilan.classschedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import io.hikarilan.classschedule.data.*
import io.hikarilan.classschedule.data.DateEntity.Companion.toCalender
import io.hikarilan.classschedule.data.DateEntity.Companion.toDateEntity
import io.hikarilan.classschedule.ui.theme.ClassScheduleTheme
import java.util.*
import kotlin.properties.Delegates


val maxWeek = mutableStateOf(getPreferenceByKey("generic.maxWeek").value.toInt())

val commencementTime = mutableStateOf(DateEntity(1970, 1, 1))

val currentWeekInThisSemester = mutableStateOf(getCurrentWeek())
val maxWeekInThisSemester =
    mutableStateOf(getPreferenceByKey("generic.maxWeekInThisSemester").value.toInt())
val shouldShowNonThisWeekClass =
    mutableStateOf(getPreferenceByKey("generic.shouldShowNonThisWeekClass").value.toBooleanStrict())

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClassScheduleTheme {
                Surface(color = MaterialTheme.colors.background) {
                    SettingsMain(activity = this)
                }
            }
        }
    }
}

fun getCurrentWeek(time: DateEntity = commencementTime.value): Int {
    return Calendar.getInstance().apply { this.time = Date() }
        .get(Calendar.WEEK_OF_YEAR) - time.toCalender().get(Calendar.WEEK_OF_YEAR) + 1
}

@Composable
fun SettingsMain(activity: ComponentActivity) {

    currentWeekInThisSemester.value = getCurrentWeek(commencementTime.value)
    currentWeekInThisSemesterShowed.value = getCurrentWeek(commencementTime.value)

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.settings))
        }, navigationIcon = {
            IconButton(onClick = {
                activity.finish()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "BackToMain")
            }
        })
    }) {
        LazyColumn {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 25.dp)
                        .clickable {
                            DatePickerDialog(
                                activity,
                                { _, year, month, dayOfMonth ->
                                    commencementTime.value =
                                        commencementTime.value.copy(
                                            year = year,
                                            month = month + 1,
                                            dayOfMonth = dayOfMonth
                                        )
                                    updatePreference(
                                        "generic.commencementTime", Gson().toJson(
                                            DateEntity(year, month + 1, dayOfMonth),
                                            DateEntity::class.java
                                        )
                                    )
                                },
                                commencementTime.value.year,
                                commencementTime.value.month - 1,
                                commencementTime.value.dayOfMonth
                            ).show()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "设置开学时间")
                    Text(
                        text = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.US
                        ).format(commencementTime.value.toCalender().time)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(id = R.string.settings_setEnableWeekend))
                    Switch(checked = maxWeek.value == 7, onCheckedChange = {
                        if (it) maxWeek.value = 7 else maxWeek.value = 5
                        updatePreference("generic.maxWeek", maxWeek.value.toString())
                    })
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "设置是否显示非本周课程")
                    Switch(checked = shouldShowNonThisWeekClass.value, onCheckedChange = {
                        shouldShowNonThisWeekClass.value = it
                        updatePreference(
                            "generic.shouldShowNonThisWeekClass",
                            shouldShowNonThisWeekClass.value.toString()
                        )
                    })
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .clickable {
                            activity.startActivity(
                                Intent(
                                    activity,
                                    ClassTimeSettingsActivity::class.java
                                )
                            )
                        }
                        .padding(horizontal = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "课表时间设置")
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "课表时间设置")
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(horizontal = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(id = R.string.settings_setCurrentWeekInThisSemester))
                    NumberPicker(
                        value = currentWeekInThisSemester.value,
                        onValueChange = {
                            commencementTime.value =
                                commencementTime.value.toCalender().also { cal ->
                                    cal.set(
                                        Calendar.WEEK_OF_YEAR,
                                        cal.get(Calendar.WEEK_OF_YEAR) + it - currentWeekInThisSemester.value
                                    )
                                }.toDateEntity()
                            currentWeekInThisSemester.value = it
                            currentWeekInThisSemesterShowed.value = it
                        },
                        range = 1..maxWeekInThisSemester.value
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(horizontal = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(id = R.string.settings_setMaxWeekInThisSemester))
                    NumberPicker(
                        value = maxWeekInThisSemester.value,
                        onValueChange = {
                            maxWeekInThisSemester.value = it
                            if (currentWeekInThisSemesterShowed.value > it)
                                currentWeekInThisSemesterShowed.value = it
                            updatePreference("generic.maxWeekInThisSemester", it.toString())
                        },
                        range = 1..30
                    )
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .clickable {
                            val clipboardManager =
                                activity.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            clipboardManager.setPrimaryClip(
                                ClipData.newPlainText(
                                    "ClassScheduler_ExportJSON", Gson()
                                        .toJson(
                                            TransformData(
                                                classList.toList(),
                                                Database
                                                    .getAppDatabase()
                                                    .preferenceDao()
                                                    .getAll()
                                            ),
                                            object : TypeToken<TransformData>() {}.type
                                        )
                                        .toString()
                                )
                            )
                            Toast
                                .makeText(
                                    activity,
                                    R.string.toast_successfulllyExport,
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                        .padding(horizontal = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = stringResource(id = R.string.settings_exportDataToClipboard))
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = stringResource(id = R.string.settings_exportDataToClipboard)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .clickable {
                            val clipboardManager =
                                activity.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            try {
                                val gson = Gson()
                                val transformData =
                                    JsonParser.parseString(clipboardManager.primaryClip?.getItemAt(0)?.text.toString()).asJsonObject
                                val mappedPart1 =
                                    transformData
                                        .getAsJsonArray("part1")
                                        .map { gson.fromJson(it, ClassEntity::class.java) }
                                val mappedPart2 =
                                    transformData
                                        .getAsJsonArray("part2")
                                        .map { gson.fromJson(it, PreferenceEntity::class.java) }
                                Database
                                    .getAppDatabase()
                                    .let {
                                        it
                                            .classDao()
                                            .insertAll(*mappedPart1.toTypedArray())
                                        it
                                            .preferenceDao()
                                            .insertAll(*mappedPart2.toTypedArray())
                                    }
                            } catch (e: Exception) {
                                Toast
                                    .makeText(
                                        activity,
                                        R.string.toast_failedToImport,
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                return@clickable
                            }
                            reInit()
                            activity.finish()
                            Toast
                                .makeText(
                                    activity,
                                    R.string.toast_successfulllyImport,
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                        .padding(horizontal = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(id = R.string.settings_importDataFromClipboard))
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = stringResource(id = R.string.settings_importDataFromClipboard)
                    )
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .clickable {
                            Database
                                .getAppDatabase()
                                .preferenceDao()
                                .let {
                                    it
                                        .getAll()
                                        .forEach { dao -> it.delete(dao) }
                                }
                            Database
                                .getAppDatabase()
                                .classDao()
                                .let {
                                    it
                                        .getAll()
                                        .forEach { dao -> it.delete(dao) }
                                }
                            reInit()
                            activity.finish()
                            Toast
                                .makeText(
                                    activity,
                                    R.string.toast_successfullyClear,
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                        .padding(horizontal = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(id = R.string.settings_resetAllData))
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(id = R.string.settings_resetAllData)
                    )
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 25.dp)
                        .clickable {
                            activity.startActivity(Intent(activity, InfoActivity::class.java))
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "关于本软件"
                    )
                }
            }

        }
    }
}