package io.hikarilan.classschedule

import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.FullHoursNumberPicker
import com.chargemap.compose.numberpicker.NumberPicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.hikarilan.classschedule.data.Database
import io.hikarilan.classschedule.data.PreferenceEntity
import io.hikarilan.classschedule.data.getPreferenceByKey
import io.hikarilan.classschedule.data.updatePreference
import io.hikarilan.classschedule.ui.theme.ClassScheduleTheme
import java.util.*

val maxClassNumberDay =
    mutableStateOf(getPreferenceByKey("generic.maxClassNumberDay").value.toInt())
val maxClassNumberAfternoon =
    mutableStateOf(getPreferenceByKey("generic.maxClassNumberAfternoon").value.toInt())
val maxClassNumberNight =
    mutableStateOf(getPreferenceByKey("generic.maxClassNumberNight").value.toInt())

val defaultTimePerClassMinutes =
    mutableStateOf(getPreferenceByKey("generic.defaultTimePerClassMinutes").value.toInt())
val defaultTimePerRestMinutes =
    mutableStateOf(getPreferenceByKey("generic.defaultTimePerRestMinutes").value.toInt())
val classTimes = mutableStateMapOf<Int, Pair<FullHours, FullHours>>()

class ClassTimeSettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClassScheduleTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ClassTimeSettingsMain(activity = this)
                }
            }
        }
    }
}

fun FullHours.pass(hours: Int = 0, minutes: Int = 0): FullHours {
    val calender = Calendar.getInstance().also {
        it.set(Calendar.HOUR_OF_DAY, this.hours)
        it.set(Calendar.MINUTE, this.minutes)
        it.add(Calendar.HOUR_OF_DAY, hours)
        it.add(Calendar.MINUTE, minutes)
    }

    return FullHours(calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE))
}

fun generateTimes(
    from: Int,
    to: Int,
    fromTime: FullHours = classTimes[from]!!.first,
    timePerClassMinutes: Int = defaultTimePerClassMinutes.value,
    timePerRestMinutes: Int = defaultTimePerRestMinutes.value
): Map<Int, Pair<FullHours, FullHours>> {
    var nowTime = fromTime
    val map = mutableMapOf<Int, Pair<FullHours, FullHours>>()
    for (now in from..to) {
        map[now] = Pair(nowTime, nowTime.pass(minutes = timePerClassMinutes))
        nowTime = nowTime.pass(minutes = timePerClassMinutes).pass(minutes = timePerRestMinutes)
    }
    return map
}

fun showEditingClassNumberDialog(index: Int, activity: ComponentActivity) {

    val to = TimePickerDialog(activity, { _, hourOfDay, minute ->
        classTimes[index + 1] =
            classTimes[index + 1]!!.copy(second = FullHours(hourOfDay, minute))

        Database.getAppDatabase().preferenceDao().insertAll(
            PreferenceEntity(
                "generic.classTimes",
                Gson().toJson(
                    classTimes.toMap(),
                    object :
                        TypeToken<HashMap<Int, Pair<FullHours, FullHours>>>() {}.type
                ).toString()
            )
        )

    }, classTimes[index + 1]!!.first.hours, classTimes[index + 1]!!.first.minutes, true)

    val from = TimePickerDialog(activity, { _, hourOfDay, minute ->
        classTimes[index + 1] =
            classTimes[index + 1]!!.copy(first = FullHours(hourOfDay, minute))

        Database.getAppDatabase().preferenceDao().insertAll(
            PreferenceEntity(
                "generic.classTimes",
                Gson().toJson(
                    classTimes.toMap(),
                    object :
                        TypeToken<HashMap<Int, Pair<FullHours, FullHours>>>() {}.type
                ).toString()
            )
        )

        to.show()

    }, classTimes[index + 1]!!.first.hours, classTimes[index + 1]!!.first.minutes, true)

    from.show()

    /*
    Dialog(onDismissRequest = { dialogEditingClassNumber.value = 0 }) {
        Surface(
            elevation = 8.dp,
            modifier = Modifier
                .padding(4.dp)
                .requiredHeight(LocalConfiguration.current.screenHeightDp.dp * 0.30f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 25.dp),
                verticalArrangement = Arrangement.Center
            ) {
                FullHoursNumberPicker(
                    modifier = Modifier.padding(end = 5.dp),
                    hoursDivider = {
                        Text(text = "时")
                    },
                    minutesDivider = {
                        Text(text = "分")
                    },
                    value = classTimes[index + 1]!!.first,
                    hoursRange = 0..23,
                    onValueChange = {

                        classTimes[index + 1] =
                            classTimes[index + 1]!!.copy(first = it as FullHours)

                        Database.getAppDatabase().preferenceDao().insertAll(
                            PreferenceEntity(
                                "generic.classTimes",
                                Gson().toJson(
                                    classTimes.toMap(),
                                    object :
                                        TypeToken<HashMap<Int, Pair<FullHours, FullHours>>>() {}.type
                                ).toString()
                            )
                        )
                    }
                )
                FullHoursNumberPicker(
                    modifier = Modifier.padding(start = 5.dp),
                    hoursDivider = {
                        Text(text = "时")
                    },
                    minutesDivider = {
                        Text(text = "分")
                    },
                    value = classTimes[index + 1]!!.second,
                    hoursRange = 0..23,
                    onValueChange = {

                        classTimes[index + 1] =
                            classTimes[index + 1]!!.copy(second = it as FullHours)

                        Database.getAppDatabase().preferenceDao().insertAll(
                            PreferenceEntity(
                                "generic.classTimes",
                                Gson().toJson(
                                    classTimes.toMap(),
                                    object :
                                        TypeToken<HashMap<Int, Pair<FullHours, FullHours>>>() {}.type
                                ).toString()
                            )
                        )
                    }
                )
            }
        }
    }
     */
}

@Composable
fun ClassTimeSettingsMain(activity: ComponentActivity) {

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.classTimeSettings))
        }, navigationIcon = {
            IconButton(onClick = {
                activity.finish()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "BackToSettings")
            }
        })
    }) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "一般单节课时长")
                NumberPicker(
                    value = defaultTimePerClassMinutes.value,
                    onValueChange = {
                        defaultTimePerClassMinutes.value = it
                        updatePreference("generic.defaultTimePerClassMinutes", it.toString())
                        classTimes.putAll(
                            generateTimes(
                                1,
                                maxClassNumberDay.value
                            )
                        )
                        classTimes.putAll(
                            generateTimes(
                                maxClassNumberDay.value + 1,
                                maxClassNumberAfternoon.value
                            )
                        )
                        classTimes.putAll(
                            generateTimes(
                                maxClassNumberDay.value + maxClassNumberAfternoon.value + 1,
                                maxClassNumberDay.value + maxClassNumberAfternoon.value + maxClassNumberNight.value
                            )
                        )
                    },
                    // do not create step,bug detected
                    range = 5..120
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
                Text(text = "一般课间休息时长")
                NumberPicker(
                    value = defaultTimePerRestMinutes.value,
                    onValueChange = {
                        defaultTimePerRestMinutes.value = it
                        updatePreference("generic.defaultTimePerRestMinutes", it.toString())
                        classTimes.putAll(
                            generateTimes(
                                1,
                                maxClassNumberDay.value
                            )
                        )
                        classTimes.putAll(
                            generateTimes(
                                maxClassNumberDay.value + 1,
                                maxClassNumberAfternoon.value
                            )
                        )
                        classTimes.putAll(
                            generateTimes(
                                maxClassNumberDay.value + maxClassNumberAfternoon.value + 1,
                                maxClassNumberDay.value + maxClassNumberAfternoon.value + maxClassNumberNight.value
                            )
                        )
                    },
                    // do not create step,bug detected
                    range = 5..120
                )
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
                Text(text = stringResource(id = R.string.settings_setMaxClassNumber))
                NumberPicker(
                    value = maxClassNumberDay.value,
                    onValueChange = {
                        maxClassNumberDay.value = it
                        updatePreference("generic.maxClassNumberDay", it.toString())
                        classTimes.putAll(
                            generateTimes(
                                1,
                                maxClassNumberDay.value
                            )
                        )
                    },
                    range = 1..10
                )
                NumberPicker(
                    value = maxClassNumberAfternoon.value,
                    onValueChange = {
                        classTimes.putAll(
                            generateTimes(
                                maxClassNumberDay.value + 1,
                                maxClassNumberDay.value + maxClassNumberAfternoon.value
                            )
                        )
                        maxClassNumberAfternoon.value = it
                        updatePreference("generic.maxClassNumberAfternoon", it.toString())
                    },
                    range = 1..10
                )
                NumberPicker(
                    value = maxClassNumberNight.value,
                    onValueChange = {
                        classTimes.putAll(
                            generateTimes(
                                maxClassNumberDay.value + maxClassNumberAfternoon.value + 1,
                                maxClassNumberDay.value + maxClassNumberAfternoon.value + maxClassNumberNight.value
                            )
                        )
                        maxClassNumberNight.value = it
                        updatePreference("generic.maxClassNumberNight", it.toString())
                    },
                    range = 1..10
                )
            }
            Divider()
            Text(text = "课程时间设置", modifier = Modifier.padding(10.dp))
            LazyColumn {
                items(maxClassNumberDay.value) { index ->
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 25.dp)
                            .fillMaxWidth()
                            .height(70.dp)
                            .clickable { showEditingClassNumberDialog(index,activity) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "第 ${index + 1} 节")

                        Text(
                            text = "${
                                kotlin.run {
                                    val time = classTimes[index + 1]!!.first
                                    Calendar.getInstance().run {
                                        set(Calendar.HOUR_OF_DAY, time.hours)
                                        set(Calendar.MINUTE, time.minutes)
                                        SimpleDateFormat("HH:mm", Locale.US).format(getTime())
                                    }
                                }
                            } - ${
                                kotlin.run {
                                    val time = classTimes[index + 1]!!.second
                                    Calendar.getInstance().run {
                                        set(Calendar.HOUR_OF_DAY, time.hours)
                                        set(Calendar.MINUTE, time.minutes)
                                        SimpleDateFormat("HH:mm", Locale.US).format(getTime())
                                    }
                                }
                            }"
                        )
                    }
                }
                item {
                    Divider()
                }
                items(maxClassNumberAfternoon.value) { index ->

                    val index = maxClassNumberDay.value + index

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 25.dp)
                            .fillMaxWidth()
                            .height(70.dp)
                            .clickable { showEditingClassNumberDialog(index,activity) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "第 ${index + 1} 节")

                        Text(
                            text = "${
                                kotlin.run {
                                    val time = classTimes[index + 1]!!.first
                                    Calendar.getInstance().run {
                                        set(Calendar.HOUR_OF_DAY, time.hours)
                                        set(Calendar.MINUTE, time.minutes)
                                        SimpleDateFormat("HH:mm", Locale.US).format(getTime())
                                    }
                                }
                            } - ${
                                kotlin.run {
                                    val time = classTimes[index + 1]!!.second
                                    Calendar.getInstance().run {
                                        set(Calendar.HOUR_OF_DAY, time.hours)
                                        set(Calendar.MINUTE, time.minutes)
                                        SimpleDateFormat("HH:mm", Locale.US).format(getTime())
                                    }
                                }
                            }"
                        )
                    }
                }
                item {
                    Divider()
                }
                items(maxClassNumberNight.value) { index ->

                    val index = maxClassNumberDay.value + maxClassNumberAfternoon.value + index

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 25.dp)
                            .fillMaxWidth()
                            .height(70.dp)
                            .clickable { showEditingClassNumberDialog(index,activity) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "第 ${index + 1} 节")

                        Text(
                            text = "${
                                kotlin.run {
                                    val time = classTimes[index + 1]!!.first
                                    Calendar.getInstance().run {
                                        set(Calendar.HOUR_OF_DAY, time.hours)
                                        set(Calendar.MINUTE, time.minutes)
                                        SimpleDateFormat("HH:mm", Locale.US).format(getTime())
                                    }
                                }
                            } - ${
                                kotlin.run {
                                    val time = classTimes[index + 1]!!.second
                                    Calendar.getInstance().run {
                                        set(Calendar.HOUR_OF_DAY, time.hours)
                                        set(Calendar.MINUTE, time.minutes)
                                        SimpleDateFormat("HH:mm", Locale.US).format(getTime())
                                    }
                                }
                            }"
                        )
                    }
                }
            }
        }
    }
}