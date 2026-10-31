package uz.example.hamyon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

private val Ink = Color(0xFF18243A)
private val Blue = Color(0xFF315BE8)
private val Pale = Color(0xFFF3F6FC)
private val Green = Color(0xFF16865B)

data class Transaction(val title: String, val detail: String, val amount: Long)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HamyonApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HamyonApp() {
    var balance by remember { mutableLongStateOf(1250000L) }
    var recipient by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val transactions = remember {
        mutableStateListOf(
            Transaction("Do‘kondan xarid", "Bugun • 14:20", -85000),
            Transaction("Hisob to‘ldirish", "Kecha • 18:05", 300000),
            Transaction("Kafe", "Kecha • 12:41", -42000)
        )
    }
    val fmt = NumberFormat.getIntegerInstance(Locale("uz", "UZ"))

    MaterialTheme(colorScheme = lightColorScheme(primary = Blue, background = Pale)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Hamyon", fontWeight = FontWeight.Bold, color = Ink) },
                    actions = { IconButton(onClick = { message = "Bu ilova sinov uchun. Haqiqiy pul o'tkazilmaydi." }) {
                        Icon(Icons.Default.Info, contentDescription = "Ma'lumot")
                    } }
                )
            },
            containerColor = Pale
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Ink)
                    ) {
                        Column(Modifier.padding(22.dp)) {
                            Text("MAVJUD BALANS", color = Color(0xFFBFCBE0), fontSize = 12.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("${fmt.format(balance)} so‘m", color = Color.White, fontSize = 29.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(18.dp))
                            Text("HAMYON • SINOV HISOBI", color = Color(0xFFBFCBE0), fontSize = 11.sp)
                            Spacer(Modifier.height(10.dp))
                            Text("Sinov rejimi — haqiqiy to‘lov amalga oshirilmaydi",
                                color = Color(0xFFFFD78A), fontSize = 11.sp)
                        }
                    }
                }
                item {
                    Text("Pul o‘tkazish", color = Ink, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = recipient, onValueChange = { recipient = it },
                                label = { Text("Qabul qiluvchi ismi") },
                                modifier = Modifier.fillMaxWidth(), singleLine = true
                            )
                            OutlinedTextField(
                                value = amountText, onValueChange = { amountText = it.filter(Char::isDigit) },
                                label = { Text("Summa (so‘m)") },
                                modifier = Modifier.fillMaxWidth(), singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Button(
                                onClick = {
                                    val amount = amountText.toLongOrNull()
                                    message = when {
                                        recipient.isBlank() -> "Qabul qiluvchi ismini kiriting."
                                        amount == null || amount <= 0 -> "Summani to‘g‘ri kiriting."
                                        amount > balance -> "Sinov balansida mablag‘ yetarli emas."
                                        else -> {
                                            balance -= amount
                                            transactions.add(0, Transaction("Sinov o‘tkazmasi", recipient, -amount))
                                            recipient = ""; amountText = ""
                                            "Sinov o‘tkazmasi qayd etildi. Haqiqiy pul yuborilmadi."
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Sinov o‘tkazmasini bajarish")
                            }
                        }
                    }
                }
                item {
                    Text("Operatsiyalar tarixi", color = Ink, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                items(transactions) { tx ->
                    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Row(
                            Modifier.fillMaxWidth().padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (tx.amount > 0) Icons.Default.AddCircle else Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = if (tx.amount > 0) Green else Blue
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(tx.title, color = Ink, fontWeight = FontWeight.SemiBold)
                                Text(tx.detail, color = Color.Gray, fontSize = 12.sp)
                            }
                            Text(
                                (if (tx.amount > 0) "+" else "") + fmt.format(tx.amount),
                                color = if (tx.amount > 0) Green else Ink,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            if (message.isNotBlank()) {
                AlertDialog(
                    onDismissRequest = { message = "" },
                    confirmButton = { TextButton(onClick = { message = "" }) { Text("Yopish") } },
                    text = { Text(message) }
                )
            }
        }
    }
}
