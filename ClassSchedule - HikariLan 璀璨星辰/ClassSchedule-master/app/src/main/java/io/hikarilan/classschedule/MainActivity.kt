package io.hikarilan.classschedule

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import com.chargemap.compose.numberpicker.FullHours
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.hikarilan.classschedule.data.*
import io.hikarilan.classschedule.data.DateEntity.Companion.toDateEntity
import io.hikarilan.classschedule.ui.theme.ClassScheduleTheme
import java.util.*

val classList = mutableStateListOf<ClassEntity>()

val currentWeekInThisSemesterShowed = mutableStateOf(currentWeekInThisSemester.value)

val dialogEditingClass: MutableState<ClassEntity?> = mutableStateOf(null)

val menuClass: MutableState<ClassEntity?> = mutableStateOf(null)
val copiedClass: MutableState<ClassEntity?> = mutableStateOf(null)

class MainActivity : ComponentActivity() {

    @ExperimentalUnitApi
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContent {
            ClassScheduleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Main(this)
                }
            }
        }
    }

    private fun init() {
        Database.deploy(this)

        Database.getAppDatabase().preferenceDao().findByKey("generic.maxClassNumberDay").let {
            if (it == null) Database.getAppDatabase().preferenceDao()
                .insertAll(PreferenceEntity("generic.maxClassNumberDay", "4"))
        }
        Database.getAppDatabase().preferenceDao().findByKey("generic.maxClassNumberAfternoon").let {
            if (it == null) Database.getAppDatabase().preferenceDao()
                .insertAll(PreferenceEntity("generic.maxClassNumberAfternoon", "4"))
        }
        Database.getAppDatabase().preferenceDao().findByKey("generic.maxClassNumberNight").let {
            if (it == null) Database.getAppDatabase().preferenceDao()
                .insertAll(PreferenceEntity("generic.maxClassNumberNight", "2"))
        }
        Database.getAppDatabase().preferenceDao().findByKey("generic.maxWeek").let {
            if (it == null) Database.getAppDatabase().preferenceDao()
                .insertAll(PreferenceEntity("generic.maxWeek", "5"))
        }
        Database.getAppDatabase().preferenceDao().findByKey("generic.maxWeekInThisSemester").let {
            if (it == null) Database.getAppDatabase().preferenceDao()
                .insertAll(PreferenceEntity("generic.maxWeekInThisSemester", "16"))
        }
        Database.getAppDatabase().preferenceDao().findByKey("generic.shouldShowNonThisWeekClass")
            .let {
                if (it == null) Database.getAppDatabase().preferenceDao()
                    .insertAll(PreferenceEntity("generic.shouldShowNonThisWeekClass", "false"))
            }
        Database.getAppDatabase().preferenceDao().findByKey("generic.defaultTimePerClassMinutes")
            .let {
                if (it == null) Database.getAppDatabase().preferenceDao()
                    .insertAll(PreferenceEntity("generic.defaultTimePerClassMinutes", "50"))
            }
        Database.getAppDatabase().preferenceDao().findByKey("generic.defaultTimePerRestMinutes")
            .let {
                if (it == null) Database.getAppDatabase().preferenceDao()
                    .insertAll(PreferenceEntity("generic.defaultTimePerRestMinutes", "10"))
            }
        Database.getAppDatabase().preferenceDao().findByKey("generic.commencementTime")
            .let {
                if (it == null) {
                    Database.getAppDatabase().preferenceDao()
                        .insertAll(
                            PreferenceEntity(
                                "generic.commencementTime", Gson().toJson(
                                    Calendar.getInstance().apply {
                                        time = Date()
                                    }.toDateEntity(),
                                    DateEntity::class.java
                                )
                            )
                        )
                }
            }
        commencementTime.value = Gson().fromJson(
            getPreferenceByKey("generic.commencementTime").value,
            DateEntity::class.java
        )
        maxClassNumberDay.value = getPreferenceByKey("generic.maxClassNumberDay").value.toInt()
        maxClassNumberAfternoon.value =
            getPreferenceByKey("generic.maxClassNumberAfternoon").value.toInt()
        maxClassNumberNight.value = getPreferenceByKey("generic.maxClassNumberNight").value.toInt()
        maxWeek.value = getPreferenceByKey("generic.maxWeek").value.toInt()
        currentWeekInThisSemester.value = getCurrentWeek(commencementTime.value)
        currentWeekInThisSemesterShowed.value = currentWeekInThisSemester.value
        maxWeekInThisSemester.value =
            getPreferenceByKey("generic.maxWeekInThisSemester").value.toInt()
        shouldShowNonThisWeekClass.value =
            getPreferenceByKey("generic.shouldShowNonThisWeekClass").value.toBooleanStrict()
        classList.clear()
        classList.addAll(Database.getAppDatabase().classDao().getAll())
        dialogEditingClass.value = null
        defaultTimePerClassMinutes.value =
            getPreferenceByKey("generic.defaultTimePerClassMinutes").value.toInt()
        defaultTimePerRestMinutes.value =
            getPreferenceByKey("generic.defaultTimePerRestMinutes").value.toInt()

        Database.getAppDatabase().preferenceDao().findByKey("generic.classTimes")
            .let {
                if (it == null) {
                    classTimes.putAll(
                        generateTimes(
                            1,
                            maxClassNumberDay.value,
                            FullHours(8, 0)
                        )
                    )
                    classTimes.putAll(
                        generateTimes(
                            maxClassNumberDay.value + 1,
                            maxClassNumberDay.value + maxClassNumberAfternoon.value,
                            FullHours(14, 0)
                        )
                    )
                    classTimes.putAll(
                        generateTimes(
                            maxClassNumberDay.value + maxClassNumberAfternoon.value + 1,
                            maxClassNumberDay.value + maxClassNumberAfternoon.value + maxClassNumberNight.value,
                            FullHours(18, 0)
                        )
                    )
                    Database.getAppDatabase().preferenceDao().insertAll(
                        PreferenceEntity(
                            "generic.classTimes",
                            Gson().toJson(
                                classTimes.toMap(),
                                object :
                                    TypeToken<HashMap<Int, Pair<FullHours, FullHours>>>() {}.type
                            )
                        )
                    )
                } else {
                    classTimes.clear()
                    classTimes.putAll(
                        Gson().fromJson(
                            getPreferenceByKey("generic.classTimes").value,
                            object : TypeToken<HashMap<Int, Pair<FullHours, FullHours>>>() {}.type
                        )
                    )
                }

            }
    }
}

fun reInit() {
    Database.getAppDatabase().preferenceDao().findByKey("generic.maxClassNumberDay").let {
        if (it == null) Database.getAppDatabase().preferenceDao()
            .insertAll(PreferenceEntity("generic.maxClassNumberDay", "4"))
    }
    Database.getAppDatabase().preferenceDao().findByKey("generic.maxClassNumberAfternoon").let {
        if (it == null) Database.getAppDatabase().preferenceDao()
            .insertAll(PreferenceEntity("generic.maxClassNumberAfternoon", "4"))
    }
    Database.getAppDatabase().preferenceDao().findByKey("generic.maxClassNumberNight").let {
        if (it == null) Database.getAppDatabase().preferenceDao()
            .insertAll(PreferenceEntity("generic.maxClassNumberNight", "2"))
    }
    Database.getAppDatabase().preferenceDao().findByKey("generic.maxWeek").let {
        if (it == null) Database.getAppDatabase().preferenceDao()
            .insertAll(PreferenceEntity("generic.maxWeek", "5"))
    }
    Database.getAppDatabase().preferenceDao().findByKey("generic.maxWeekInThisSemester").let {
        if (it == null) Database.getAppDatabase().preferenceDao()
            .insertAll(PreferenceEntity("generic.maxWeekInThisSemester", "16"))
    }
    Database.getAppDatabase().preferenceDao().findByKey("generic.shouldShowNonThisWeekClass")
        .let {
            if (it == null) Database.getAppDatabase().preferenceDao()
                .insertAll(PreferenceEntity("generic.shouldShowNonThisWeekClass", "false"))
        }
    Database.getAppDatabase().preferenceDao().findByKey("generic.defaultTimePerClassMinutes")
        .let {
            if (it == null) Database.getAppDatabase().preferenceDao()
                .insertAll(PreferenceEntity("generic.defaultTimePerClassMinutes", "50"))
        }
    Database.getAppDatabase().preferenceDao().findByKey("generic.defaultTimePerRestMinutes")
        .let {
            if (it == null) Database.getAppDatabase().preferenceDao()
                .insertAll(PreferenceEntity("generic.defaultTimePerRestMinutes", "10"))
        }
    Database.getAppDatabase().preferenceDao().findByKey("generic.commencementTime")
        .let {
            if (it == null) {
                Database.getAppDatabase().preferenceDao()
                    .insertAll(
                        PreferenceEntity(
                            "generic.commencementTime", Gson().toJson(
                                Calendar.getInstance().apply {
                                    time = Date()
                                }.toDateEntity(),
                                DateEntity::class.java
                            )
                        )
                    )
            }
        }
    commencementTime.value = Gson().fromJson(
        getPreferenceByKey("generic.commencementTime").value,
        DateEntity::class.java
    )
    maxClassNumberDay.value = getPreferenceByKey("generic.maxClassNumberDay").value.toInt()
    maxClassNumberAfternoon.value =
        getPreferenceByKey("generic.maxClassNumberAfternoon").value.toInt()
    maxClassNumberNight.value = getPreferenceByKey("generic.maxClassNumberNight").value.toInt()
    maxWeek.value = getPreferenceByKey("generic.maxWeek").value.toInt()
    currentWeekInThisSemester.value = getCurrentWeek(commencementTime.value)
    currentWeekInThisSemesterShowed.value = currentWeekInThisSemester.value
    maxWeekInThisSemester.value =
        getPreferenceByKey("generic.maxWeekInThisSemester").value.toInt()
    shouldShowNonThisWeekClass.value =
        getPreferenceByKey("generic.shouldShowNonThisWeekClass").value.toBooleanStrict()
    classList.clear()
    classList.addAll(Database.getAppDatabase().classDao().getAll())
    dialogEditingClass.value = null
    defaultTimePerClassMinutes.value =
        getPreferenceByKey("generic.defaultTimePerClassMinutes").value.toInt()
    defaultTimePerRestMinutes.value =
        getPreferenceByKey("generic.defaultTimePerRestMinutes").value.toInt()

    Database.getAppDatabase().preferenceDao().findByKey("generic.classTimes")
        .let {
            if (it == null) {
                classTimes.putAll(
                    generateTimes(
                        1,
                        maxClassNumberDay.value,
                        FullHours(8, 0)
                    )
                )
                classTimes.putAll(
                    generateTimes(
                        maxClassNumberDay.value + 1,
                        maxClassNumberDay.value + maxClassNumberAfternoon.value,
                        FullHours(14, 0)
                    )
                )
                classTimes.putAll(
                    generateTimes(
                        maxClassNumberDay.value + maxClassNumberAfternoon.value + 1,
                        maxClassNumberDay.value + maxClassNumberAfternoon.value + maxClassNumberNight.value,
                        FullHours(18, 0)
                    )
                )
                Database.getAppDatabase().preferenceDao().insertAll(
                    PreferenceEntity(
                        "generic.classTimes",
                        Gson().toJson(
                            classTimes.toMap(),
                            object :
                                TypeToken<HashMap<Int, Pair<FullHours, FullHours>>>() {}.type
                        )
                    )
                )
            } else {
                classTimes.clear()
                classTimes.putAll(
                    Gson().fromJson(
                        getPreferenceByKey("generic.classTimes").value,
                        object : TypeToken<HashMap<Int, Pair<FullHours, FullHours>>>() {}.type
                    )
                )
            }

        }
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun Main(activity: ComponentActivity) {
    ShowClassEditDialog()
    ShowMenu()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = stringResource(id = R.string.app_name) + " - " + stringResource(id = R.string.currentWeek).replace(
                    "{0}",
                    currentWeekInThisSemesterShowed.value.toString()
                )
            )
        }, navigationIcon = {
            IconButton(onClick = {
                activity.startActivity(Intent(activity, SettingsActivity::class.java))
            }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
            }
        }, actions = {
            IconButton(
                onClick = {
                    currentWeekInThisSemesterShowed.value--
                },
                enabled = currentWeekInThisSemesterShowed.value > 1
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Previous")
            }
            IconButton(
                onClick = {
                    currentWeekInThisSemesterShowed.value++
                },
                enabled = currentWeekInThisSemesterShowed.value < maxWeekInThisSemester.value
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Next")
            }
        })
    }) {
        ShowSchedule(currentWeekInThisSemesterShowed.value)
    }
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ShowSchedule(currentWeek: Int) {
    LazyColumn {
        item {
            Row {
                Box(
                    modifier = Modifier.size(
                        width = LocalConfiguration.current.screenWidthDp.dp * 0.07f,
                        height = LocalConfiguration.current.screenHeightDp.dp * 0.025f
                    )
                )
                for (index in 1..maxWeek.value) {
                    ShowWeek(week = index)
                }
            }
        }
        items(maxClassNumberDay.value) { index ->
            val index = index
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ShowTime(classNumber = index + 1)
                ClassEntity.fillEntities(
                    classList,
                    maxWeek.value,
                    index + 1
                ).map {
                    when {
                        it.availableWeeks == "" -> return@map it
                        currentWeek.toString() !in it.availableWeeks.split(',') -> {
                            return@map if (shouldShowNonThisWeekClass.value) {
                                it.copy(
                                    color = 0x25000000
                                )
                            } else {
                                it.copy(
                                    className = "",
                                    teacher = "",
                                    location = "",
                                    color = 0x00FFFFFFF,
                                )
                            }
                        }
                        else -> return@map it
                    }
                }.forEach {
                    ShowClassCard(it)
                }
            }
        }
        item {
            Surface(modifier = Modifier.fillMaxWidth(), color = Color.Gray) {
                Text(text = "午休", textAlign = TextAlign.Center)
            }
        }
        items(maxClassNumberAfternoon.value) { index ->
            val index = index + maxClassNumberDay.value
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ShowTime(classNumber = index + 1)
                ClassEntity.fillEntities(
                    classList,
                    maxWeek.value,
                    index + 1
                ).map {
                    when {
                        it.availableWeeks == "" -> return@map it
                        currentWeek.toString() !in it.availableWeeks.split(',') -> {
                            return@map if (shouldShowNonThisWeekClass.value) {
                                it.copy(
                                    color = 0x25000000
                                )
                            } else {
                                it.copy(
                                    className = "",
                                    teacher = "",
                                    location = "",
                                    color = 0x00FFFFFFF,
                                )
                            }
                        }
                        else -> return@map it
                    }
                }.forEach {
                    ShowClassCard(it)
                }
            }
        }
        item {
            Surface(modifier = Modifier.fillMaxWidth(), color = Color.Gray) {
                Text(text = "晚休", textAlign = TextAlign.Center)
            }
        }
        items(maxClassNumberNight.value) { index ->
            val index = index + maxClassNumberDay.value + maxClassNumberAfternoon.value
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ShowTime(classNumber = index + 1)
                ClassEntity.fillEntities(
                    classList,
                    maxWeek.value,
                    index + 1
                ).map {
                    when {
                        it.availableWeeks == "" -> return@map it
                        currentWeek.toString() !in it.availableWeeks.split(',') -> {
                            return@map if (shouldShowNonThisWeekClass.value) {
                                it.copy(
                                    color = 0x25000000
                                )
                            } else {
                                it.copy(
                                    className = "",
                                    teacher = "",
                                    location = "",
                                    color = 0x00FFFFFFF,
                                )
                            }
                        }
                        else -> return@map it
                    }
                }.forEach {
                    ShowClassCard(it)
                }
            }
        }
    }

    /*
    LazyVerticalGrid(
        cells = GridCells.Fixed(maxWeek.value + 1),
        contentPadding = PaddingValues(1.dp),
        modifier = Modifier.draggable(
            rememberDraggableState(onDelta = {
                if (it > 5.0f && currentWeekInThisSemesterShowed.value > 1)
                    currentWeekInThisSemesterShowed.value--
                else if (it < 5.0f && currentWeekInThisSemesterShowed.value < maxWeekInThisSemester.value)
                    currentWeekInThisSemesterShowed.value++
            }),
            orientation = Orientation.Horizontal
        )
    ) {
        items(maxWeek.value + 1) { index ->
            ShowWeek(week = index)
        }
        items(maxClassNumber.value) { index ->
            ShowTime(classNumber = index + 1)
            items(
                items = ClassEntity.fillEntities(
                    classList,
                    maxWeek.value,
                    maxClassNumber.value
                ).map {
                    if (currentWeek.toString() !in it.availableWeeks.split(',')) {
                        return@map it.copy(className = "", teacher = "", location = "")
                    }
                    return@map it
                }
            ) { item ->
                ShowClassCard(item)
            }
        }
    }
    */


}

@ExperimentalUnitApi
@Composable
fun ShowTime(classNumber: Int) {
    Column(
        modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.07f)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = classNumber.toString(),
            fontSize = TextUnit(5F, TextUnitType.Em),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = kotlin.run {
                val time = classTimes[classNumber]!!.first
                Calendar.getInstance().run {
                    set(Calendar.HOUR_OF_DAY, time.hours)
                    set(Calendar.MINUTE, time.minutes)
                    SimpleDateFormat("HH:mm", Locale.US).format(getTime())
                }
            },
            fontSize = TextUnit(2F, TextUnitType.Em),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = kotlin.run {
                val time = classTimes[classNumber]!!.second
                Calendar.getInstance().run {
                    set(Calendar.HOUR_OF_DAY, time.hours)
                    set(Calendar.MINUTE, time.minutes)
                    SimpleDateFormat("HH:mm", Locale.US).format(getTime())
                }
            },
            fontSize = TextUnit(2F, TextUnitType.Em),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ShowWeek(week: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(
            width = LocalConfiguration.current.screenWidthDp.dp * 0.93f / maxWeek.value,
            height = LocalConfiguration.current.screenHeightDp.dp * 0.025f
        )
    ) {
        when (week) {
            1 -> Text(
                text = stringResource(id = R.string.week_1),
            )
            2 -> Text(
                text = stringResource(id = R.string.week_2),
            )
            3 -> Text(
                text = stringResource(id = R.string.week_3),
            )
            4 -> Text(
                text = stringResource(id = R.string.week_4),
            )
            5 -> Text(
                text = stringResource(id = R.string.week_5),
            )
            6 -> Text(
                text = stringResource(id = R.string.week_6),
            )
            7 -> Text(
                text = stringResource(id = R.string.week_7),
            )
        }
    }
}


@ExperimentalFoundationApi
@ExperimentalUnitApi
@ExperimentalMaterialApi
@Composable
fun ShowClassCard(item: ClassEntity) {
    Surface(
        modifier = Modifier
            .size(
                width = LocalConfiguration.current.screenWidthDp.dp * 0.93f / maxWeek.value,
                height = 100.dp
            )
            .combinedClickable(
                onClick = {
                    dialogEditingClass.value = Database
                        .getAppDatabase()
                        .classDao()
                        .getAll()
                        .find { it == item }
                        ?: item
                },
                onLongClick = {
                    menuClass.value = Database
                        .getAppDatabase()
                        .classDao()
                        .getAll()
                        .find { it == item }
                        ?: item
                }
            ), color = Color(item.color), border = BorderStroke(1.dp, Color(0x35000000))
    ) {
        Column(
            Modifier
                .padding(5.dp)
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = item.className,
                fontSize = TextUnit(5F, TextUnitType.Em),
                maxLines = 2,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (item.location.isNotBlank()) "@${item.location}" else item.location,
                fontSize = TextUnit(3F, TextUnitType.Em),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = item.teacher,
                fontSize = TextUnit(3F, TextUnitType.Em),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ShowMenu() {
    if (menuClass.value == null) return

    DropdownMenu(
        expanded = menuClass.value != null,
        onDismissRequest = {
            menuClass.value = null
        }
    ) {
        DropdownMenuItem(
            onClick = {
                copiedClass.value = menuClass.value
                menuClass.value = null
            },
            contentPadding = PaddingValues(20.dp),
            enabled = menuClass.value?.className?.isNotBlank() == true

        ) {
            Icon(imageVector = Icons.Default.Info, contentDescription = "复制")
            Text(text = "复制")
        }
        DropdownMenuItem(
            onClick = {
                val menu = menuClass.value!!
                val copied = copiedClass.value!!
                val changed = copied.copy(week = menu.week, classNumber = menu.classNumber)
                classList.remove(menu)
                classList.add(changed)
                Database.getAppDatabase().classDao().insertAll(changed)
                menuClass.value = null
                copiedClass.value = null
            }, contentPadding = PaddingValues(20.dp), enabled = copiedClass.value != null
        ) {
            Icon(imageVector = Icons.Default.Info, contentDescription = "粘贴")
            Text(text = "粘贴")
        }
        DropdownMenuItem(
            onClick = {
                Database.getAppDatabase().classDao().delete(menuClass.value!!)
                classList.remove(menuClass.value!!)
                menuClass.value = null
            },
            contentPadding = PaddingValues(20.dp), enabled = menuClass.value
                    in Database.getAppDatabase().classDao().getAll()
        ) {
            Icon(imageVector = Icons.Default.Info, contentDescription = "删除")
            Text(text = "删除")
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ShowClassEditDialog() {
    if (dialogEditingClass.value == null) return

    val className = remember { mutableStateOf(dialogEditingClass.value!!.className) }
    val teacher = remember { mutableStateOf(dialogEditingClass.value!!.teacher) }
    val location = remember { mutableStateOf(dialogEditingClass.value!!.location) }
    val availableClass: SnapshotStateList<Int> = remember {
        val list: SnapshotStateList<Int> = mutableStateListOf()
        if (dialogEditingClass.value!!.availableWeeks.isNotBlank())
            list.addAll(
                dialogEditingClass.value!!.availableWeeks.split(',').map { it.toInt() })
        list
    }
    val color = remember { mutableStateOf(Color(dialogEditingClass.value!!.color)) }

    Dialog(onDismissRequest = { dialogEditingClass.value = null }) {
        Surface(
            elevation = 8.dp,
            modifier = Modifier
                .requiredWidth(LocalConfiguration.current.screenWidthDp.dp * 0.90f)
                .requiredHeight(LocalConfiguration.current.screenHeightDp.dp * 0.85f)
                .padding(4.dp)
        ) {
            Scaffold(topBar = {
                TopAppBar(title = {
                    Text(text = stringResource(id = R.string.dialogEditingClass_title))
                }, actions = {
                    IconButton(
                        onClick = {
                            Database.getAppDatabase().classDao().delete(dialogEditingClass.value!!)
                            classList.remove(dialogEditingClass.value!!)
                            dialogEditingClass.value = null
                        },
                        enabled = dialogEditingClass.value
                                in Database.getAppDatabase().classDao().getAll()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove"
                        )
                    }
                    IconButton(
                        onClick = {
                            dialogEditingClass.value = null
                        }, enabled = true
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                    IconButton(
                        onClick = {
                            Database.getAppDatabase().classDao()
                                .insertAll(dialogEditingClass.value!!)

                            classList.clear()
                            classList.addAll(Database.getAppDatabase().classDao().getAll())

                            dialogEditingClass.value = null
                        },
                        enabled = className.value.isNotBlank()
                                && availableClass.isNotEmpty()
                                && color.value.toArgb() != 0x00FFFFFFF
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Save"
                        )
                    }
                })
            }) {
                if (dialogEditingClass.value == null) return@Scaffold
                val availableClassType = remember { mutableStateOf(3) }

                fun updateAvailableClassType() {
                    availableClassType.value =
                        if (dialogEditingClass.value!!.availableWeeks.isNotBlank())
                            dialogEditingClass.value!!.availableWeeks.split(",")
                                .map { it.toInt() }
                                .let {
                                    val list = mutableListOf<Int>()
                                    for (i in 1..maxWeekInThisSemester.value) {
                                        list.add(i)
                                    }
                                    when {
                                        it.containsAll(list) -> 0
                                        it.all { i -> i % 2 == 1 } && it.containsAll(
                                            list.filter { i -> i % 2 == 1 }) -> 1
                                        it.all { i -> i % 2 == 0 } && it.containsAll(
                                            list.filter { i -> i % 2 == 0 }) -> 2
                                        else -> 3
                                    }
                                }
                        else 3
                }

                updateAvailableClassType()

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.size(10.dp))
                    TextField(
                        value = className.value,
                        onValueChange = { str ->
                            className.value = str
                            dialogEditingClass.value!!.className = className.value
                        },
                        label = { Text(text = stringResource(id = R.string.dialogEditingClass_setClassName)) },
                        isError = className.value.isBlank(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Divider()

                    Spacer(modifier = Modifier.size(10.dp))
                    TextField(
                        value = teacher.value,
                        onValueChange = { str ->
                            teacher.value = str
                            dialogEditingClass.value!!.teacher = teacher.value
                        },
                        label = { Text(text = stringResource(id = R.string.dialogEditingClass_setTeacher)) },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Divider()

                    Spacer(modifier = Modifier.size(10.dp))
                    TextField(
                        value = location.value,
                        onValueChange = { str ->
                            location.value = str
                            dialogEditingClass.value!!.location = location.value
                        },
                        label = { Text(text = stringResource(id = R.string.dialogEditingClass_setLocation)) },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Divider()

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.dialogEditingClass_setAvailableWeeks),
                            modifier = Modifier.weight(0.25f)
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(0.25f)
                        ) {
                            RadioButton(selected = availableClassType.value == 1, onClick = {
                                val list = mutableListOf<Int>()
                                for (i in 1..maxWeekInThisSemester.value) {
                                    list.add(i)
                                }
                                availableClass.clear()
                                availableClass.addAll(list.filter { i -> i % 2 == 1 })
                                dialogEditingClass.value!!.availableWeeks =
                                    availableClass.joinToString(separator = ",")
                                updateAvailableClassType()
                            })
                            Text(text = stringResource(id = R.string.dialogEditingClass_setAvailableWeeks_oddNumberOnly))
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(0.25f)
                        ) {
                            RadioButton(selected = availableClassType.value == 2, onClick = {
                                val list = mutableListOf<Int>()
                                for (i in 1..maxWeekInThisSemester.value) {
                                    list.add(i)
                                }
                                availableClass.clear()
                                availableClass.addAll(list.filter { i -> i % 2 == 0 })
                                dialogEditingClass.value!!.availableWeeks =
                                    availableClass.joinToString(separator = ",")
                                updateAvailableClassType()
                            })
                            Text(text = stringResource(id = R.string.dialogEditingClass_setAvailableWeeks_evenNumberOnly))
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(0.25f)
                        ) {
                            RadioButton(selected = availableClassType.value == 0, onClick = {
                                val list = mutableListOf<Int>()
                                for (i in 1..maxWeekInThisSemester.value) {
                                    list.add(i)
                                }
                                availableClass.clear()
                                availableClass.addAll(list)
                                dialogEditingClass.value!!.availableWeeks =
                                    availableClass.joinToString(separator = ",")
                                updateAvailableClassType()
                            })
                            Text(text = stringResource(id = R.string.dialogEditingClass_setAvailableWeeks_all))
                        }
                    }
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(6)
                    ) {
                        items(maxWeekInThisSemester.value) { index ->
                            Row(Modifier.padding(5.dp)) {
                                val index = index + 1
                                Checkbox(
                                    checked = availableClass.contains(index),
                                    onCheckedChange = { checked ->
                                        if (checked) availableClass.add(index)
                                        else availableClass.remove(index)
                                        dialogEditingClass.value!!.availableWeeks =
                                            availableClass.joinToString(separator = ",")
                                        updateAvailableClassType()
                                    })
                                Text(text = index.toString())
                            }
                        }
                    }

                    Spacer(modifier = Modifier.size(10.dp))
                    Divider()
                    Column {
                        Text(text = "背景色", modifier = Modifier.padding(5.dp))
                        LazyVerticalGrid(
                            cells = GridCells.Adaptive(40.dp),
                            contentPadding = PaddingValues(horizontal = 5.dp)
                        ) {
                            items(
                                listOf(
                                    Color(0x3566CCFF),
                                )
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(100),
                                    border = BorderStroke(
                                        2.dp,
                                        if (color.value == it) Color(0x7F000000)
                                        else Color(0xFFFFFFFF)
                                    ),
                                    color = it,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable {
                                            color.value = it
                                            dialogEditingClass.value!!.color =
                                                color.value
                                                    .toArgb()
                                                    .toLong()
                                        }
                                ) {
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}