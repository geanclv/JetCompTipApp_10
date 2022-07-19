package com.geancarloleiva.jetcomptipapp_10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geancarloleiva.jetcomptipapp_10.component.InputField
import com.geancarloleiva.jetcomptipapp_10.ui.theme.JetCompTipApp_10Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetCompTipApp_10Theme {
                CreateMainScreen()
            }
        }
    }
}

@Composable
fun CreateMainScreen() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            CreateHeader()
            CreateBody()
        }
    }
}

@Composable
fun CreateHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color.LightGray
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%,.2f".format(totalPerPerson)
            Text(
                text = "Total per person",
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "$${total}",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold
            )
        }

    }
}

@Composable
fun CreateBody() {
    CreateBillForm(){ billAmount ->
        //TODO billAmount will be divided between number of persons and then showed in header
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun CreateBillForm(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
) {
    val totalBillAmountState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillAmountState) {
        totalBillAmountState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(2.dp, Color.LightGray)
    ) {
        Column() {
            InputField(modifier = Modifier.fillMaxWidth(),
                valueState = totalBillAmountState,
                labelId = "Bill amount",
                isEnabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) {
                        return@KeyboardActions
                    }
                    onValueChange(totalBillAmountState.value.trim())
                    keyboardController?.hide()
                }
            )
            Text(text = "first")
            Text(text = "second")
            Text(text = "3")
        }
    }
}