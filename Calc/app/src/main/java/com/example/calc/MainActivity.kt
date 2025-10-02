package com.example.calc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp()
        }
    }
}

@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("") }
    var showScientific by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEDED))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.White),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = if (input.isEmpty()) "0" else input,
                fontSize = 36.sp,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.End
            )
        }

        if (showScientific) {
            ScientificButtons(onButtonClick = { label ->
                input = handleInput(input, label, onToggleScientific = { showScientific = !showScientific })
            })
        }

        // Basic Buttons selalu tampil
        BasicButtons(onButtonClick = { label ->
            input = handleInput(input, label, onToggleScientific = { showScientific = !showScientific })
        })
    }
}

fun handleInput(input: String, label: String, onToggleScientific: () -> Unit): String {
    return when(label) {
        "C" -> ""
        "=" -> {
            try {
                val result = ExpressionBuilder(input).build().evaluate()
                result.toString()
            } catch (e: Exception) {
                "Error"
            }
        }
        "Sc" -> { // toggle scientific
            onToggleScientific()
            input
        }
        else -> input + label
    }
}

@Composable
fun ScientificButtons(onButtonClick: (String) -> Unit) {
    val sciButtons = listOf(
        listOf("INV","Sin","Cos","Tan"),
        listOf("Ln","Log","Src","x^y")
    )

    sciButtons.forEach { row ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            row.forEach { label ->
                CalculatorButton(label = label) {
                    onButtonClick(label)
                }
            }
        }
    }
}

@Composable
fun BasicButtons(onButtonClick: (String) -> Unit) {
    val basicButtons = listOf(
        listOf("7","8","9","/"),
        listOf("4","5","6","*"),
        listOf("1","2","3","-"),
        listOf("0","(",")","+"),
        listOf("Sc", "C", ".", "=")
    )

    basicButtons.forEach { row ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            row.forEach { label ->
                CalculatorButton(label = label) {
                    onButtonClick(label)
                }
            }
        }
    }
}

@Composable
fun RowScope.CalculatorButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .weight(1f) // weight benar karena RowScope
            .height(60.dp)
    ) {
        Text(text = label, fontSize = 20.sp)
    }
}
