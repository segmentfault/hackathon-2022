package io.hikarilan.classschedule

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import io.hikarilan.classschedule.ui.theme.ClassScheduleTheme

class InfoActivity : ComponentActivity() {

    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClassScheduleTheme {
                Surface(color = MaterialTheme.colors.background) {
                    InfoMain(this)
                }
            }
        }
    }
}

@ExperimentalUnitApi
@Composable
fun InfoMain(activity: ComponentActivity) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "关于")
        }, navigationIcon = {
            IconButton(onClick = {
                activity.finish()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "BackToSettings")
            }
        })
    }) {
        Column {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = TextUnit(7.0f, TextUnitType.Em),
            )
            Text(text = "开发者： HikariLan 贺兰星辰",
                modifier = Modifier.clickable {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse("https://www.github.com/shaokeyibb")
                    activity.startActivity(intent)
                })
            Text(text = "(好像没什么想说的了，就这样吧，哈哈）")
        }
    }
}