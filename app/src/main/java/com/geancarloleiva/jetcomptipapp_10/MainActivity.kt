package com.geancarloleiva.jetcomptipapp_10

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.geancarloleiva.jetcomptipapp_10.component.InputField
import com.geancarloleiva.jetcomptipapp_10.ui.theme.JetCompTipApp_10Theme
import com.geancarloleiva.jetcomptipapp_10.widget.RoundIconButton

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
            CreateBody()
        }
    }
}

@Composable
fun CreateBillPerPersonSpace(totalPerPerson: Double = 0.0) {
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
    val stateTotalPerPerson = remember {
        mutableStateOf(0.0)
    }
    val stateTotalBillAmount = remember {
        mutableStateOf("")
    }
    val stateTotalPerson = remember {
        mutableStateOf(1)
    }
    val stateTotalTip = remember {
        mutableStateOf(0f)
    }

    CreateBillForm(stateTotalPerPerson = stateTotalPerPerson,
        stateTotalBillAmount = stateTotalBillAmount,
        stateTotalPerson = stateTotalPerson,
        stateTotalTip = stateTotalTip) { billAmount ->
        Log.d("Test", "Bill amount: $billAmount")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateBillForm(
    modifier: Modifier = Modifier,
    stateTotalPerPerson: MutableState<Double>,
    stateTotalBillAmount: MutableState<String>,
    stateTotalPerson: MutableState<Int>,
    stateTotalTip: MutableState<Float>,
    onValueChange: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val stateValid = remember(stateTotalBillAmount.value) {
        stateTotalBillAmount.value.trim().isNotEmpty()
    }
    val stateSliderValue = remember {
        mutableStateOf(0f)
    }

    CreateBillPerPersonSpace(stateTotalPerPerson.value)

    Surface(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(2.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            //Bill amount
            InputField(modifier = Modifier.fillMaxWidth(),
                valueState = stateTotalBillAmount,
                labelId = "Bill amount",
                isEnabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!stateValid) {
                        return@KeyboardActions
                    }
                    onValueChange(stateTotalBillAmount.value.trim())
                    keyboardController?.hide()
                }
            )

            if (stateValid) {
                //Persons Row
                Row(
                    modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Nr. of persons:",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(70.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                if (stateTotalPerson.value == 1) {
                                    Toast.makeText(
                                        context, "You can remove more person",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    stateTotalPerson.value--

                                    stateTotalPerPerson.value = calculateTotalPerPerson(
                                        stateTotalBillAmount.value.toFloat(),
                                        stateTotalTip.value, stateTotalPerson.value
                                    )
                                }
                            },
                            buttonDescription = "Remove"
                        )
                        Text(
                            text = stateTotalPerson.value.toString(),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )
                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                stateTotalPerson.value++

                                stateTotalPerPerson.value = calculateTotalPerPerson(
                                    stateTotalBillAmount.value.toFloat(),
                                    stateTotalTip.value, stateTotalPerson.value
                                )
                            },
                            buttonDescription = "Add"
                        )
                    }
                }

                //Tip Row
                Row(
                    modifier = Modifier.padding(
                        horizontal = 3.dp,
                        vertical = 12.dp
                    ),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Total tip:",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(140.dp))
                    Text(
                        text = "$ ${stateTotalTip.value}",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }

                //Tip % Row
                Row(
                    modifier = Modifier.padding(
                        horizontal = 3.dp,
                        vertical = 12.dp
                    ),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "% of tip:",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(170.dp))
                    Text(
                        text = "${stateSliderValue.value.toInt()} %",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }

                //Slider
                Slider(
                    value = stateSliderValue.value,
                    onValueChange = { newSliderValue ->
                        stateSliderValue.value = newSliderValue
                        stateTotalTip.value = calculateTotalTip(
                            stateTotalBillAmount.value.toFloat(), stateSliderValue.value.toInt()
                        )

                        stateTotalPerPerson.value = calculateTotalPerPerson(
                            stateTotalBillAmount.value.toFloat(),
                            stateTotalTip.value, stateTotalPerson.value
                        )
                    },
                    valueRange = 0f..100f,
                    onValueChangeFinished = {

                    }
                )
            } else {

            }
        }
    }
}

fun calculateTotalTip(
    billAmount: Float,
    tipPercentage: Int
): Float {
    var totalTip = 0f
    if (billAmount > 1 && billAmount.toString().isNotEmpty()) {
        totalTip = (billAmount * tipPercentage) / 100
    }
    return totalTip
}

fun calculateTotalPerPerson(
    billAmount: Float,
    totalTip: Float,
    nbrPersons: Int
): Double {
    var totalPerPerson = 0.0
    if (billAmount > 1 && billAmount.toString().isNotEmpty()
        && totalTip > 0 && totalTip.toString().isNotEmpty()
    ) {
        totalPerPerson = ((billAmount + totalTip) / nbrPersons).toDouble()
    }
    return totalPerPerson
}